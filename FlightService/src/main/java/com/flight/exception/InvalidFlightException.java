package com.flight.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to handle invalid flight-related operations.
 * Automatically maps to a 400 Bad Request status when thrown.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidFlightException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidFlightException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public InvalidFlightException(String message) {
        super(message);
    }
}