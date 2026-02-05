package com.fooddelivery.users.service;

import com.fooddelivery.users.dto.RoleRequestDto;
import com.fooddelivery.users.dto.UserRequestDto;
import com.fooddelivery.users.dto.UserResponseDto;
import com.fooddelivery.users.dto.UserUpdateDto;
import com.fooddelivery.users.entity.Role;
import com.fooddelivery.users.entity.User;
import com.fooddelivery.users.exception.ResourceAlreadyExistsException;
import com.fooddelivery.users.exception.RoleNotFoundException;
import com.fooddelivery.users.exception.UserNotFoundException;
import com.fooddelivery.users.mapper.RoleMapper;
import com.fooddelivery.users.mapper.UserMapper;
import com.fooddelivery.users.repository.RoleRepository;
import com.fooddelivery.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {

        log.info("=== DIAGNOSTIC START ===");
        log.info("Email: {}", userRequestDto.getEmail());

        if(userRepository.existsByEmail(userRequestDto.getEmail())){
            throw new ResourceAlreadyExistsException("Email is already taken: "+userRequestDto.getEmail());
        }
        User user=userMapper.userRequestDtoToUser(userRequestDto);
        user.setPasswordHash(passwordEncoder.encode(userRequestDto.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Role USER is not found!"));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        user.setAddresses(new HashSet<>());

        log.info("=== Before userRepository.save() ===");
        User savedUser = userRepository.save(user);
        log.info("=== After save, ID: {} ===", savedUser.getId());

        UserResponseDto response = new UserResponseDto();
        response.setId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setFullName(savedUser.getFullName());

        RoleMapper roleMapper = Mappers.getMapper(RoleMapper.class);
        response.setRoles(roleMapper.roleSetToRoleResponseDtoSet(roles));

        response.setAddresses(new HashSet<>());

        log.info("=== Registration SUCCESS ===");
        return response;
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
        List<User> users = userRepository.findAllWithRolesAndAddresses();
        return userMapper.userListToUserResponseDtoList(users);
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

    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + userId));

        if (userUpdateDto.getEmail() != null) {
            if (userUpdateDto.getEmail().isBlank()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }

            if (!userUpdateDto.getEmail().equals(user.getEmail())) {
                if (userRepository.existsByEmail(userUpdateDto.getEmail())) {
                    throw new ResourceAlreadyExistsException("Email is already taken: " + userUpdateDto.getEmail());
                }
                user.setEmail(userUpdateDto.getEmail());
            }
        }

        if (userUpdateDto.getFullName() != null) {
            if (userUpdateDto.getFullName().isBlank()) {
                throw new IllegalArgumentException("Full name cannot be empty");
            }
            user.setFullName(userUpdateDto.getFullName());
        }

        user.setUpdatedAt(LocalDateTime.now());
        User updatedUser = userRepository.save(user);

        log.info("User updated successfully: {}", updatedUser.getEmail());
        return userMapper.userToUserResponseDto(updatedUser);
    }

    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException("User not found with id "+id);
        }
        userRepository.deleteById(id);
    }
}
