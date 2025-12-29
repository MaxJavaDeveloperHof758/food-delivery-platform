package com.fooddelivery.users.handler;

import com.fooddelivery.users.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String,Object>> handleAccessDeniedException(AccessDeniedException ex){
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
        response.put("status",HttpStatus.BAD_REQUEST.value());
        response.put("timestamp",LocalDateTime.now());
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
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
