package com.login.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    /*
     * This class handles exceptions globally across the entire application.
     * It uses @ControllerAdvice to centralize exception handling.
     */

    // Handles exceptions when a requested user is not found.
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Handles exceptions when a user already exists in the database.
    @ExceptionHandler(UserAlreadyPresentException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyPresentException(UserAlreadyPresentException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.CONFLICT, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // Handles exceptions for invalid login credentials.
    @ExceptionHandler(InvalidCredentialException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialException(InvalidCredentialException e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handles validation errors for method arguments (e.g., invalid request body data).
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleIllegalFieldValueException(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();

        // Extracts validation error details for each invalid field.
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String msg = error.getDefaultMessage();
            errorMap.put(fieldName, msg);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap);
    }

    // Catches and handles any generic exceptions not specifically handled by other methods.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception e, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}