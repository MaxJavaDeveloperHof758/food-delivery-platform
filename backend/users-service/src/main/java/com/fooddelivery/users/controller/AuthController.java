package com.fooddelivery.users.controller;

import com.fooddelivery.users.dto.UserRequestDto;
import com.fooddelivery.users.dto.UserResponseDto;
import com.fooddelivery.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid UserRequestDto userRequestDto){
        UserResponseDto createdUser= userService.createUser(userRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}
