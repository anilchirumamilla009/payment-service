package com.techwave.paymentservice.exception;

/**
 * Signals that a requested resource does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

