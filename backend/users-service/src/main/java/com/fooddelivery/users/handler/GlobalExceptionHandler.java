package com.fooddelivery.users.handler;

import com.fooddelivery.users.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validation Failed");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", "Authentication failed",
                        "message", "Invalid email or password"
                ));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabledException(DisabledException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", "Authentication failed",
                        "message", "Account is disabled"
                ));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<?> handleLockedException(LockedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", "Authentication failed",
                        "message", "Account is locked"
                ));
    }

    @ExceptionHandler(AccountExpiredException.class)
    public ResponseEntity<?> handleAccountExpiredException(AccountExpiredException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", "Authentication failed",
                        "message", "Account has expired"
                ));
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<?> handleCredentialsExpiredException(CredentialsExpiredException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", "Authentication failed",
                        "message", "Credentials have expired"
                ));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", "Authentication failed",
                        "message", ex.getMessage()
                ));
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "error", "Authentication failed",
                        "message", "Invalid email or password"
                ));
    }
    @ExceptionHandler(UnexpectedRollbackException.class)
    public ResponseEntity<?> handleUnexpectedRollbackException(UnexpectedRollbackException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "Internal server error",
                        "message", "Transaction error occurred"
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidationException(MethodArgumentNotValidException ex){
        Map<String, Object> response = new HashMap<>();

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .sorted(Comparator
                        .comparing(FieldError::getField) // Сначала сортируем по полю
                        .thenComparing(FieldError::getDefaultMessage))
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        response.put("error", "Validation Failed");
        response.put("messages", errors);
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserNotFoundException.class,AddressNotFoundException.class, RoleNotFoundException.class})
    public ResponseEntity<Map<String,Object>> handleNotFoundException(RuntimeException ex){
        Map<String,Object> response=new HashMap<>();
        response.put("error","Not Found");
        response.put("message",ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CustomAccessDeniedException.class)
    public ResponseEntity<Map<String,Object>> handleAccessDeniedException(CustomAccessDeniedException ex){
        Map<String,Object> response=new HashMap<>();
        response.put("error","Access denied");
        response.put("message",ex.getMessage());
        response.put("status", HttpStatus.FORBIDDEN.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response,HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String,Object>> handleAlreadyExistsException(ResourceAlreadyExistsException ex){
        Map<String,Object> response=new HashMap<>();
        response.put("error","Already exists");
        response.put("message",ex.getMessage());
        response.put("status",HttpStatus.CONFLICT.value());
        response.put("timestamp",LocalDateTime.now());
        return new ResponseEntity<>(response,HttpStatus.CONFLICT);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleGeneral(Exception ex){
        Map<String,Object> response=new HashMap<>();
        response.put("error","Internal Server Error");
        response.put("message",ex.getMessage());
        response.put("status",HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("timestamp",LocalDateTime.now());
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
