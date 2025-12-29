package com.fooddelivery.restaurants.handler;

import com.fooddelivery.restaurants.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

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

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxImageSizeException(MaxUploadSizeExceededException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "File too large!!!");
        response.put("message", "Maximum file size exceeded. Maximum size is 5MB.");
        response.put("status", HttpStatus.PAYLOAD_TOO_LARGE.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.PAYLOAD_TOO_LARGE);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJsonException(HttpMessageNotReadableException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Invalid JSON");
        response.put("message", "Request body contains invalid JSON format");
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, Object>> handleMissingParameterException(
            MissingServletRequestParameterException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Missing parameter");
        response.put("message", String.format("Required parameter '%s' is missing", ex.getParameterName()));
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyExistsException(ResourceAlreadyExistsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Already exists");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DeleteImageException.class)
    public ResponseEntity<Map<String, Object>> handleDeleteImageException(DeleteImageException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Cannot be deleted");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DishNotBelongsToRestaurantException.class)
    public ResponseEntity<Map<String, Object>> handleDishNotBelongsToRestaurantException(DishNotBelongsToRestaurantException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Dish-restaurant mismatch");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UploadImageFailureException.class)
    public ResponseEntity<Map<String, Object>> handleUploadImageFailureException(UploadImageFailureException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Image upload failure");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFoundException.class, ImageNotFoundException.class, EmptyFileException.class})
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(RuntimeException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Not found");
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
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
