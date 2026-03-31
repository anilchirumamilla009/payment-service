package com.techwave.paymentservice.exception;

/**
 * Signals that the request payload or input is invalid.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}

