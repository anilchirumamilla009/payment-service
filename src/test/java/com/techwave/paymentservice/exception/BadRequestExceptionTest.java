package com.techwave.paymentservice.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BadRequestException Unit Tests")
class BadRequestExceptionTest {

    @Test
    @DisplayName("Constructor with message sets message correctly")
    void constructor_withMessage() {
        BadRequestException ex =
                new BadRequestException("Invalid input");

        assertEquals("Invalid input", ex.getMessage());
        assertNull(ex.getCause());
    }

    @Test
    @DisplayName("Constructor with message and cause sets both")
    void constructor_withMessageAndCause() {
        IllegalArgumentException cause =
                new IllegalArgumentException("root cause");
        BadRequestException ex =
                new BadRequestException("Invalid input", cause);

        assertEquals("Invalid input", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}

