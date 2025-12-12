package com.fooddelivery.users.service;

import com.fooddelivery.users.dto.UserRequestDto;
import com.fooddelivery.users.dto.UserResponseDto;
import com.fooddelivery.users.entity.Role;
import com.fooddelivery.users.entity.User;
import com.fooddelivery.users.exception.ResourceAlreadyExistsException;
import com.fooddelivery.users.exception.UserNotFoundException;
import com.fooddelivery.users.mapper.UserMapper;
import com.fooddelivery.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

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
        user.setPasswordHash(passwordEncoder.encode(userRequestDto.getPassword()));
        User savedUser=userRepository.save(user);
        return userMapper.userToUserResponseDto(savedUser);
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
        userRepository.deleteById(id);
    }
}
