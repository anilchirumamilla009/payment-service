package com.techwave.paymentservice.exception;

/**
 * Signals an unauthorized request.
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}

