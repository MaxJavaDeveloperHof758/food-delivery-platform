package com.fooddelivery.orders.handler;

import com.fooddelivery.orders.exception.*;
import feign.FeignException;
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
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
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
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(FeignException ex) {
        Map<String, Object> response = new HashMap<>();

        HttpStatus status;
        String errorMessage;

        switch (ex.status()) {
            case 404 -> {
                status = HttpStatus.NOT_FOUND;
                errorMessage = "Resource not found in external service";
            }
            case 503, 502 -> {
                status = HttpStatus.SERVICE_UNAVAILABLE;
                errorMessage = "External service unavailable";
            }
            case 400 -> {
                status = HttpStatus.BAD_REQUEST;
                errorMessage = "Bad request to external service";
            }
            default -> {
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                errorMessage = "Error communicating with external service";
            }
        }

        response.put("error", "External Service Error");
        response.put("message", errorMessage);
        response.put("details", ex.getMessage());
        response.put("status", status.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, status);
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleArgumentValidationException(ValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Validation failed");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyExistsException(ResourceAlreadyExistsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Already exists");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler({StatusModificationException.class, UpdateStatusException.class})
    public ResponseEntity<Map<String, Object>> handleStatusModificationException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Cannot modify status");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(RefundPaymentException.class)
    public ResponseEntity<Map<String, Object>> handleRefundPaymentException(RefundPaymentException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Payment refund failure");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({DishNotBelongsToRestaurantException.class,AddressNotBelongsToUserException.class})
    public ResponseEntity<Map<String, Object>> handleResourceMismatchException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Entities mismatch");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Not found");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Map<String, Object>> handleServiceUnavailableException(ServiceUnavailableException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Service unavailable");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
