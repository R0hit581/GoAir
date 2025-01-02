package com.flight.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for handling application-specific exceptions.
 */
@ControllerAdvice
public class CustomExceptionHandler {

    /**
     * Handles InvalidFlightException and returns a structured error response.
     *
     * @param e the InvalidFlightException
     * @return ResponseEntity containing error details
     */
    @ExceptionHandler(InvalidFlightException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidFlightException(InvalidFlightException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("errorType", "Invalid Flight");
        response.put("message", e.getMessage());
        response.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // You can add more exception handlers for other custom exceptions if needed
}