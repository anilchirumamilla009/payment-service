package com.techwave.paymentservice.exception;

/**
 * Signals that the current caller is not allowed to perform an operation.
 */
public class ForbiddenOperationException extends RuntimeException {

    public ForbiddenOperationException(String message) {
        super(message);
    }
}

