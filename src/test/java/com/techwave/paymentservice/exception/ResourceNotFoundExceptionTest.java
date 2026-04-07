package com.techwave.paymentservice.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ResourceNotFoundException Unit Tests")
class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("Constructor sets message correctly")
    void constructor_setsMessage() {
        ResourceNotFoundException ex =
                new ResourceNotFoundException("Person", "abc-123");

        assertEquals("Person not found with id: abc-123",
                ex.getMessage());
        assertEquals("Person", ex.getResourceType());
        assertEquals("abc-123", ex.getResourceId());
    }

    @Test
    @DisplayName("getResourceType returns correct type")
    void getResourceType_returnsType() {
        ResourceNotFoundException ex =
                new ResourceNotFoundException("BankAccount", "uuid-456");

        assertEquals("BankAccount", ex.getResourceType());
    }

    @Test
    @DisplayName("getResourceId returns correct id")
    void getResourceId_returnsId() {
        ResourceNotFoundException ex =
                new ResourceNotFoundException("Corporation", "id-789");

        assertEquals("id-789", ex.getResourceId());
    }
}

