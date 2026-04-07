package com.techwave.paymentservice.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ExceptionDetailDto Unit Tests")
class ExceptionDetailDtoTest {

    @Test
    @DisplayName("No-args constructor creates empty object")
    void noArgsConstructor() {
        ExceptionDetailDto dto = new ExceptionDetailDto();
        assertNull(dto.getStatus());
        assertNull(dto.getError());
        assertNull(dto.getMessage());
        assertNull(dto.getMessages());
    }

    @Test
    @DisplayName("All-args constructor sets all fields")
    void allArgsConstructor() {
        List<String> messages = List.of("error1", "error2");
        ExceptionDetailDto dto =
                new ExceptionDetailDto(400, "Bad Request",
                        "Validation failed", messages);

        assertEquals(400, dto.getStatus());
        assertEquals("Bad Request", dto.getError());
        assertEquals("Validation failed", dto.getMessage());
        assertEquals(2, dto.getMessages().size());
    }

    @Test
    @DisplayName("Setters and getters work correctly")
    void settersAndGetters() {
        ExceptionDetailDto dto = new ExceptionDetailDto();
        dto.setStatus(500);
        dto.setError("Internal Server Error");
        dto.setMessage("Unexpected");
        dto.setMessages(List.of("msg1"));

        assertEquals(500, dto.getStatus());
        assertEquals("Internal Server Error", dto.getError());
        assertEquals("Unexpected", dto.getMessage());
        assertEquals(1, dto.getMessages().size());
    }
}


