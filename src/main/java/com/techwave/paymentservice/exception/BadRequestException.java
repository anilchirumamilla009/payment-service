package com.techwave.paymentservice.exception;

/**
 * Exception thrown when a client request is malformed or contains
 * invalid data. Results in an HTTP 400 response.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

