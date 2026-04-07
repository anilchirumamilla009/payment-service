package com.techwave.paymentservice.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("LegalEntityDto Unit Tests")
class LegalEntityDtoTest {

    @Test
    @DisplayName("No-args constructor creates empty object")
    void noArgsConstructor() {
        LegalEntityDto dto = new LegalEntityDto();
        assertNull(dto.getId());
        assertNull(dto.getResourceType());
    }

    @Test
    @DisplayName("All-args constructor sets fields")
    void allArgsConstructor() {
        UUID id = UUID.randomUUID();
        LegalEntityDto dto = new LegalEntityDto(id, "people");

        assertEquals(id, dto.getId());
        assertEquals("people", dto.getResourceType());
    }

    @Test
    @DisplayName("Setters and getters work correctly")
    void settersAndGetters() {
        LegalEntityDto dto = new LegalEntityDto();
        UUID id = UUID.randomUUID();
        dto.setId(id);
        dto.setResourceType("corporations");

        assertEquals(id, dto.getId());
        assertEquals("corporations", dto.getResourceType());
    }
}

