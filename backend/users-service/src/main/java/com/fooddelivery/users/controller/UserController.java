package com.fooddelivery.users.controller;

import com.fooddelivery.users.entity.User;
import com.fooddelivery.users.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user){
        User savedUser=userService.createUser(user.getEmail(),user.getPasswordHash(),user.getFullName());
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
    @GetMapping
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
    @PostMapping
    public ResponseEntity<User> updateUser(@PathVariable Long id,@RequestBody User user){
        User updateUser=userService.updateUser(id,user.getEmail(),user.getPasswordHash(),user.getFullName());
        return new ResponseEntity<>(updateUser,HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
