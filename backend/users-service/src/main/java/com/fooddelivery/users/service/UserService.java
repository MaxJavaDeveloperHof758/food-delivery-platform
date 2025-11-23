package com.fooddelivery.users.service;

import com.fooddelivery.users.entity.User;
import com.fooddelivery.users.repository.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String email, String passwordHash,
                           String fullName) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setFullName(fullName);
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllUsersSortedByFullName() {           //возвращает пользователей, отсортированных по имени
        return userRepository.findAllUsers(Sort.by("full_name"));
    }

    public Optional<User> getUserById(Long id) {                //поиск пользователя по Id
        return userRepository.findById(id);
    }

    public User getUserByFullName(String fullName) {            //поиск пользователя по его fullName
        return userRepository.findUserByFullName(fullName);
    }

    public User getUserByEmail(String email) {                  //поиск пользователя по email
        return userRepository.findUserByEmail(email);
    }

    public User updateUser(Long id, String email, String passwordHash, String fullName) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setFullName(fullName);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
