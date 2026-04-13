package com.techwave.paymentservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techwave.paymentservice.controller.PersonController;
import com.techwave.paymentservice.dto.response.PersonResponse;
import com.techwave.paymentservice.service.LegalEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(PersonController.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private LegalEntityService legalEntityService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Nested
    @DisplayName("ResourceNotFoundException Tests")
    class ResourceNotFoundExceptionTests {
        
        @Test
        @DisplayName("Should return 404 with NOT_FOUND code")
        void handleResourceNotFoundException_Returns404() throws Exception {
            // Given
            UUID personId = UUID.randomUUID();
            when(legalEntityService.getPerson(personId))
                    .thenThrow(new ResourceNotFoundException("Person not found: " + personId));
            
            // When & Then
            mockMvc.perform(get("/api/v1/people/{uuid}", personId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").value("Person not found: " + personId))
                    .andExpect(jsonPath("$.statusCode").value(404));
        }
        
        @Test
        @DisplayName("Should include error message in response")
        void handleResourceNotFoundException_IncludeMessage() throws Exception {
            // Given
            String errorMessage = "Custom not found error message";
            when(legalEntityService.getPerson(any(UUID.class)))
                    .thenThrow(new ResourceNotFoundException(errorMessage));
            
            // When & Then
            mockMvc.perform(get("/api/v1/people/{uuid}", UUID.randomUUID()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(errorMessage));
        }
    }
    
    @Nested
    @DisplayName("BadRequestException Tests")
    class BadRequestExceptionTests {
        
        @Test
        @DisplayName("Should return 400 with BAD_REQUEST code")
        void handleBadRequestException_Returns400() throws Exception {
            // Given
            String errorMessage = "Invalid request parameters";
            when(legalEntityService.getPerson(any(UUID.class)))
                    .thenThrow(new BadRequestException(errorMessage));
            
            // When & Then
            mockMvc.perform(get("/api/v1/people/{uuid}", UUID.randomUUID()))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
                    .andExpect(jsonPath("$.message").value(errorMessage))
                    .andExpect(jsonPath("$.statusCode").value(400));
        }
    }
    
    @Nested
    @DisplayName("MethodArgumentNotValidException Tests")
    class MethodArgumentNotValidExceptionTests {
        
        @Test
        @DisplayName("Should return 400 with VALIDATION_ERROR code for invalid request body")
        void handleValidationException_Returns400() throws Exception {
            // Given - sending invalid person request (missing firstName)
            String invalidPayload = "{\"lastName\": \"Doe\"}";
            
            // When & Then
            mockMvc.perform(post("/api/v1/people")
                    .contentType("application/json")
                    .content(invalidPayload))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.statusCode").value(400))
                    .andExpect(jsonPath("$.message").isString());
        }
        
        @Test
        @DisplayName("Should include field-level validation errors")
        void handleValidationException_IncludesFieldErrors() throws Exception {
            // Given - sending empty strings which violate @NotBlank
            String invalidPayload = "{\"firstName\": \"\", \"lastName\": \"\"}";
            
            // When & Then
            mockMvc.perform(post("/api/v1/people")
                    .contentType("application/json")
                    .content(invalidPayload))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                    .andExpect(jsonPath("$.message").isString());
        }
        
        @Test
        @DisplayName("Should handle multiple validation errors")
        void handleValidationException_MultipleErrors() throws Exception {
            // Given - sending request with null and empty fields
            String invalidPayload = "{\"firstName\": null}";
            
            // When & Then
            mockMvc.perform(post("/api/v1/people")
                    .contentType("application/json")
                    .content(invalidPayload))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
        }
    }
    
    @Nested
    @DisplayName("Generic Exception Tests")
    class GenericExceptionTests {
        
        @Test
        @DisplayName("Should return 500 for unexpected exceptions")
        void handleGenericException_Returns500() throws Exception {
            // Given
            UUID personId = UUID.randomUUID();
            when(legalEntityService.getPerson(personId))
                    .thenThrow(new RuntimeException("Unexpected error"));
            
            // When & Then
            mockMvc.perform(get("/api/v1/people/{uuid}", personId))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.statusCode").value(500));
        }
        
        @Test
        @DisplayName("Should include error message for generic exceptions")
        void handleGenericException_IncludeMessage() throws Exception {
            // Given
            String errorMessage = "Database connection failed";
            when(legalEntityService.getPerson(any(UUID.class)))
                    .thenThrow(new RuntimeException(errorMessage));
            
            // When & Then
            mockMvc.perform(get("/api/v1/people/{uuid}", UUID.randomUUID()))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.message").isString());
        }
    }
    
    @Nested
    @DisplayName("ExceptionDetailResponse Format Tests")
    class ExceptionDetailResponseFormatTests {
        
        @Test
        @DisplayName("Response should have correct JSON structure")
        void exceptionResponse_CorrectStructure() throws Exception {
            // Given
            when(legalEntityService.getPerson(any(UUID.class)))
                    .thenThrow(new ResourceNotFoundException("Test error"));
            
            // When & Then
            mockMvc.perform(get("/api/v1/people/{uuid}", UUID.randomUUID()))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").isString())
                    .andExpect(jsonPath("$.message").isString())
                    .andExpect(jsonPath("$.statusCode").isNumber());
        }
        
        @Test
        @DisplayName("Response Content-Type should be application/json")
        void exceptionResponse_CorrectContentType() throws Exception {
            // Given
            when(legalEntityService.getPerson(any(UUID.class)))
                    .thenThrow(new ResourceNotFoundException("Test error"));
            
            // When & Then
            mockMvc.perform(get("/api/v1/people/{uuid}", UUID.randomUUID()))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType("application/json"));
        }
    }
}
