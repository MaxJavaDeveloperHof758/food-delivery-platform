package com.fooddelivery.users.controller;

import com.fooddelivery.users.dto.RoleRequestDto;
import com.fooddelivery.users.dto.UserResponseDto;
import com.fooddelivery.users.dto.UserUpdateDto;
import com.fooddelivery.users.security.UserPrincipal;
import com.fooddelivery.users.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "API for managing users")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get current user profile", description = "Returns profile of authenticated user")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> getCurrentUser(Authentication authentication){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserResponseDto user=userService.getUserById(userPrincipal.getId());
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Update current user profile", description = "Updates profile of authenticated user")
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDto> updateCurrentUser(Authentication authentication,
                                                             @RequestBody @Valid UserUpdateDto userUpdateDto){
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        UserResponseDto updatedUser = userService.updateUser(userPrincipal.getId(), userUpdateDto);
        return ResponseEntity.ok(updatedUser);
    }

    //this endpoint is used by other microservices and is public
    @Operation(summary = "Get user by ID (Admin only)",
            description = "Returns user by id - Admin only",
            parameters = {
                    @Parameter(name = "id", description = "User ID", example = "1")
            })
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    //this endpoint is used by other microservices and is public
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable("id") Long id){
                Boolean ifExists=userService.ifUserExistsById(id);
                return ResponseEntity.status(HttpStatus.OK).body(ifExists);
    }

    @Operation(summary = "Get all users", description = "Returns all users registered in service")
    @ApiResponse(responseCode = "200", description = "Users found")
    @ApiResponse(responseCode = "404", description = "Users not found")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> usersList = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @Operation(summary = "Get one user by fullName", description = "Returns one user by his fullName")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search/email")
    public ResponseEntity<UserResponseDto> getUserByEmail(@RequestParam String email) {
        UserResponseDto user = userService.getUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @Operation(summary = "Get one user by any part of his name",
            description = "Returns one user by any part of his name (name/firstname/lastname)")
    @ApiResponse(responseCode = "200", description = "Users found")
    @ApiResponse(responseCode = "404", description = "Users not found")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("search/containing")
    public ResponseEntity<List<UserResponseDto>> getAllUsersByFullNameContaining(@RequestParam String name) {
        List<UserResponseDto> usersList = userService.getAllUsersByFullNameContaining(name);
        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @Operation(summary = "Create role for existing user", description = "Creates role for existing user")
    @ApiResponse(responseCode = "201", description = "Role for user was created")
    @ApiResponse(responseCode = "404", description = "User/role not found")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{userId}/roles")
    public ResponseEntity<UserResponseDto> addRoleToUser(
            @PathVariable Long userId,
            @RequestBody RoleRequestDto roleRequestDto) {
        UserResponseDto updatedUser = userService.addRoleToUser(userId, roleRequestDto);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete existing user", description = "Deletes existing user by his ID")
    @ApiResponse(responseCode = "204", description = "User deleted successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Delete role from existing user", description = "Deletes role from existing user")
    @ApiResponse(responseCode = "204", description = "Role from User was deleted successfully")
    @ApiResponse(responseCode = "404", description = "User/role not found")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}/roles")
    public ResponseEntity<UserResponseDto> removeRoleFromUser(
            @PathVariable Long userId,
            @RequestBody RoleRequestDto roleRequestDto) {
        UserResponseDto updatedUser = userService.removeRoleFromUser(userId, roleRequestDto);
        return ResponseEntity.ok(updatedUser);
    }
}
