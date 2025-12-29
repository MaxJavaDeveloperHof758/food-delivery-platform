package com.fooddelivery.users.service;

import com.fooddelivery.users.dto.*;
import com.fooddelivery.users.entity.Role;
import com.fooddelivery.users.entity.User;
import com.fooddelivery.users.exception.ResourceAlreadyExistsException;
import com.fooddelivery.users.exception.RoleNotFoundException;
import com.fooddelivery.users.exception.UserNotFoundException;
import com.fooddelivery.users.mapper.UserMapper;
import com.fooddelivery.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        if(userRepository.existsByEmail(userRequestDto.getEmail())){
            throw new ResourceAlreadyExistsException("User with such email already exists");
        }
        Role userRole=roleService.getOrCreateRole("ROLE_USER");
        User user=userMapper.userRequestDtoToUser(userRequestDto);
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(userRole);
        if (user.getAddresses() == null) {
            user.setAddresses(new HashSet<>());
        }
        user.setPasswordHash(passwordEncoder.encode(userRequestDto.getPassword()));
        User savedUser=userRepository.save(user);
        User userWithDetails = userRepository.findByIdWithDetails(savedUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found after creation"));
        return userMapper.userToUserResponseDto(userWithDetails);
    }
    public Boolean ifUserExistsById(Long id){
        return userRepository.existsById(id);
    }
    @Transactional
    public UserResponseDto addRoleToUser(Long userId,RoleRequestDto roleRequestDto){
        User user=userRepository.findByIdWithDetails(userId)
                .orElseThrow(()->new UserNotFoundException("User not found with id "+userId));
        Role role=roleService.getOrCreateRole(roleRequestDto.getName());
        if(user.getRoles()==null){
            user.setRoles(new HashSet<>());
        }
        boolean roleAlreadyExists = user.getRoles().stream()
                .anyMatch(r -> r.getName().equals(roleRequestDto.getName()));
       if(roleAlreadyExists){
           throw new ResourceAlreadyExistsException("User already has role "+role);
       }
       user.getRoles().add(role);
       User updatedUser=userRepository.save(user);
       return userMapper.userToUserResponseDto(updatedUser);
    }
    @Transactional
    public UserResponseDto removeRoleFromUser(Long userId,RoleRequestDto roleRequestDto){
        User user=userRepository.findByIdWithDetails(userId)
                .orElseThrow(()->new UserNotFoundException("User not found with id "+userId));
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new RoleNotFoundException("User has no roles assigned");
        }
        Role roleToRemove = user.getRoles().stream()
                .filter(r -> r.getName().equals(roleRequestDto.getName()))
                .findFirst()
                .orElseThrow(() -> new RoleNotFoundException(
                        "User does not have role: " + roleRequestDto.getName()
                ));
        if(user.getRoles().size()==1){
            throw new IllegalStateException("You cannot remove the last role existing. User must have at least one role");
        }
        user.getRoles().remove(roleToRemove);
        User updatedUser=userRepository.save(user);
        return userMapper.userToUserResponseDto(updatedUser);
    }

    public List<UserResponseDto> getAllUsers() {                           //поиск всех пользователей
        return userMapper.userListToUserResponseDtoList(userRepository.findAll());
    }

    public UserResponseDto getUserById(Long id) {                //поиск пользователя по id
        return userMapper.userToUserResponseDto(userRepository.findByIdWithDetails(id)
                .orElseThrow(()->new UserNotFoundException("User not found with id "+id)));
    }

    public UserResponseDto getUserByFullName(String fullName) {            //поиск пользователя по его fullName
        return userMapper.userToUserResponseDto(userRepository.findByFullName(fullName)
                .orElseThrow(()->new UserNotFoundException("User not found with fullName: "+fullName)));
    }

    public UserResponseDto getUserByEmail(String email) {                  //поиск пользователя по email
        return userMapper.userToUserResponseDto(userRepository.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User not found with email: "+email)));
    }
    public List<UserResponseDto> getAllUsersByFullNameContaining(String name){     //поиск пользователей по шаблону
        return userMapper.userListToUserResponseDtoList(userRepository.findUsersByFullNameContaining(name));
    }

    public UserResponseDto updateUser(Long userId, UserRequestDto userRequestDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));
        if (!existingUser.getEmail().equals(userRequestDto.getEmail()) &&
                userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already taken");
        }
        userMapper.updateUserFromDto(userRequestDto, existingUser);
        if (userRequestDto.getPassword() != null &&
                !userRequestDto.getPassword().isEmpty()) {
            existingUser.setPasswordHash(passwordEncoder.encode(userRequestDto.getPassword()));
        }
        User savedUser = userRepository.save(existingUser);
        return userMapper.userToUserResponseDto(savedUser);
    }

    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException("User not found with id "+id);
        }
        userRepository.deleteById(id);
    }
}
