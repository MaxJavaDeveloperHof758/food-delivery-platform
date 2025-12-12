package com.fooddelivery.users.controller;

import com.fooddelivery.users.dto.UserRequestDto;
import com.fooddelivery.users.dto.UserResponseDto;
import com.fooddelivery.users.entity.User;
import com.fooddelivery.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "API for managing users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get one user by ID",
            description = "Returns user object if it exists in the database",
            parameters = {
                    @Parameter(name = "id", description = "User ID", example = "1")
            })
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("id") Long id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary = "Get all users", description = "Returns all users registered in service")
    @ApiResponse(responseCode = "200", description = "Users found")
    @ApiResponse(responseCode = "404", description = "Users not found")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> usersList = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @Operation(summary = "Get one user by fullname", description = "Returns one user by his fullname")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/search/fullName")
    public ResponseEntity<UserResponseDto> getUserByFullName(@RequestParam String fullName) {
        UserResponseDto user = userService.getUserByFullName(fullName);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary = "Get one user by email",
            description = "Searches for a user by email address",
            parameters = @Parameter(
                    name = "email", description = "Email address", example = "user_email@example.com"
            ))
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @GetMapping("/search/email")
    public ResponseEntity<UserResponseDto> getUserByEmail(@RequestParam String email) {
        UserResponseDto user = userService.getUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary = "Get one user by any part of his name",
            description = "Returns one user by any part of his name (name/firstname/lastname)")
    @ApiResponse(responseCode = "200", description = "Users found")
    @ApiResponse(responseCode = "404", description = "Users not found")
    @GetMapping("search/containing")
    public ResponseEntity<List<UserResponseDto>> getAllUsersByFullNameContaining(@RequestParam String name) {
        List<UserResponseDto> usersList = userService.getAllUsersByFullNameContaining(name);
        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @Operation(summary = "Update existing user", description = "Updates existing user by his ID")
    @ApiResponse(responseCode = "200", description = "User updated")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("id") Long id,
                                                      @RequestBody @Valid UserRequestDto userRequestDto) {
        UserResponseDto updatedUser = userService.updateUser(id, userRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
    }

    @Operation(summary = "Delete existing user", description = "Deletes existing user by his ID")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
