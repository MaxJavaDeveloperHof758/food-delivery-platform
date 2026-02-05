package com.fooddelivery.users.controller;

import com.fooddelivery.users.dto.LoginRequestDto;
import com.fooddelivery.users.dto.UserRequestDto;
import com.fooddelivery.users.dto.UserResponseDto;
import com.fooddelivery.users.entity.RefreshToken;
import com.fooddelivery.users.entity.User;
import com.fooddelivery.users.exception.UserNotFoundException;
import com.fooddelivery.users.repository.RefreshTokenRepository;
import com.fooddelivery.users.repository.UserRepository;
import com.fooddelivery.users.security.JwtUtil;
import com.fooddelivery.users.security.UserPrincipal;
import com.fooddelivery.users.service.RefreshTokenService;
import com.fooddelivery.users.service.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRequestDto userRequestDto) {
        try{
            if(userRepository.existsByEmail(userRequestDto.getEmail())){
                Map<String, String> response = new HashMap<>();
                response.put("error", "Email is already taken");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            UserResponseDto createdUser = userService.createUser(userRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }catch (Exception e) {
            logger.error("Registration error: {}", e);
            Map<String, String> response = new HashMap<>();
            response.put("error", "Registration failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Transactional
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getEmail(),
                            loginRequestDto.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String jwt = jwtUtils.generateTokenFromAuthentication(authentication);

            User user = userRepository.findById(userPrincipal.getId())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id " + userPrincipal.getId()));

            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("type", "Bearer");
            response.put("refreshToken", refreshToken.getToken());
            response.put("id", userPrincipal.getId());
            response.put("email", userPrincipal.getEmail());
            response.put("fullName", userPrincipal.getFullName());
            List<String> roles = userPrincipal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            response.put("roles", roles);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            logger.warn("Invalid credentials for email {}: {}", loginRequestDto.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication failed",
                            "message", "Invalid email or password"));

        } catch (AuthenticationException e) {
            logger.warn("Authentication failed for email {}: {}", loginRequestDto.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authentication failed",
                            "message", e.getMessage()));

        } catch (UserNotFoundException e) {
            logger.error("User not found after authentication: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found",
                            "message", e.getMessage()));

        } catch (Exception e) {
            logger.error("Login error for email {}: {}", loginRequestDto.getEmail(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal server error",
                            "message", "An unexpected error occurred"));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> payload) {
        String requestToken = payload.get("refreshToken");
        if (requestToken == null || requestToken.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Refresh token is required"));
        }
        return refreshTokenRepository.findByToken(requestToken)
                .map(refreshToken -> {
                    if (refreshTokenService.isTokenExpired(refreshToken)) {
                        refreshTokenRepository.delete(refreshToken);
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of("error", "Refresh token expired. Please login again"));
                    }

                    User user = refreshToken.getUser();
                    String newJwt = jwtUtils.generateToken(user);

                    Map<String, Object> response = new HashMap<>();
                    response.put("token", newJwt);
                    response.put("type", "Bearer");
                    response.put("refreshToken", refreshToken.getToken());

                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Invalid refresh token")));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody Map<String, String> payload) {
        String requestToken = payload.get("refreshToken");
        if (requestToken != null && !requestToken.isBlank()) {
            refreshTokenRepository.findByToken(requestToken)
                    .ifPresent(refreshTokenRepository::delete);
        }
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
