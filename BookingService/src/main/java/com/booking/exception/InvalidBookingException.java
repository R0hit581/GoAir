package com.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidBookingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidBookingException() {
        super();
        // TODO Auto-generated constructor stub
    }

    public InvalidBookingException(String message, Throwable cause, boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        // TODO Auto-generated constructor stub
    }

    public InvalidBookingException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public InvalidBookingException(String message) {
        super(message);
        // TODO Auto-generated constructor stub
    }

    public InvalidBookingException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }
}