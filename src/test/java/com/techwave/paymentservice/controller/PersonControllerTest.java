package com.techwave.paymentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techwave.paymentservice.dto.request.PersonRequest;
import com.techwave.paymentservice.dto.response.PersonAuditResponse;
import com.techwave.paymentservice.dto.response.PersonResponse;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.service.LegalEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
@DisplayName("PersonController Tests")
class PersonControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private LegalEntityService legalEntityService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private UUID personId;
    private PersonRequest personRequest;
    private PersonResponse personResponse;
    
    @BeforeEach
    void setUp() {
        personId = UUID.randomUUID();
        
        personRequest = new PersonRequest("John", "Doe");
        
        personResponse = new PersonResponse();
        personResponse.setId(personId);
        personResponse.setFirstName("John");
        personResponse.setLastName("Doe");
    }
    
    @Nested
    @DisplayName("createPerson() Tests")
    class CreatePersonTests {
        
        @Test
        @DisplayName("POST /api/v1/people - Success")
        void createPerson_Success() throws Exception {
            // Given
            when(legalEntityService.createPerson(any(PersonRequest.class)))
                    .thenReturn(personResponse);
            
            // When & Then
            mockMvc.perform(post("/api/v1/people")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(personRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(personId.toString()))
                    .andExpect(jsonPath("$.firstName").value("John"))
                    .andExpect(jsonPath("$.lastName").value("Doe"));
            
            verify(legalEntityService, times(1)).createPerson(any(PersonRequest.class));
        }
        
        @Test
        @DisplayName("POST /api/v1/people - Validation Error (Missing FirstName)")
        void createPerson_ValidationError_MissingFirstName() throws Exception {
            // Given
            PersonRequest invalidRequest = new PersonRequest("", "Doe");
            
            // When & Then
            mockMvc.perform(post("/api/v1/people")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
            
            verify(legalEntityService, never()).createPerson(any());
        }
        
        @Test
        @DisplayName("POST /api/v1/people - Validation Error (Null LastName)")
        void createPerson_ValidationError_NullLastName() throws Exception {
            // Given
            PersonRequest invalidRequest = new PersonRequest("John", null);
            
            // When & Then
            mockMvc.perform(post("/api/v1/people")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
            
            verify(legalEntityService, never()).createPerson(any());
        }
        
        @Test
        @DisplayName("POST /api/v1/people - Validation Error (Invalid JSON)")
        void createPerson_InvalidJSON() throws Exception {
            // When & Then
            mockMvc.perform(post("/api/v1/people")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{invalid json}"))
                    .andExpect(status().isBadRequest());
            
            verify(legalEntityService, never()).createPerson(any());
        }
    }
    
    @Nested
    @DisplayName("getPerson() Tests")
    class GetPersonTests {
        
        @Test
        @DisplayName("GET /api/v1/people/{uuid} - Found")
        void getPerson_Found() throws Exception {
            // Given
            when(legalEntityService.getPerson(personId)).thenReturn(personResponse);
            
            // When & Then
            mockMvc.perform(get("/api/v1/people/{uuid}", personId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(personId.toString()))
                    .andExpect(jsonPath("$.firstName").value("John"))
                    .andExpect(jsonPath("$.lastName").value("Doe"));
            
            verify(legalEntityService).getPerson(personId);
        }
        
        @Test
        @DisplayName("GET /api/v1/people/{uuid} - Not Found")
        void getPerson_NotFound() throws Exception {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(legalEntityService.getPerson(nonExistentId))
                    .thenThrow(new ResourceNotFoundException("Person not found: " + nonExistentId));
            
            // When & Then
            mockMvc.perform(get("/api/v1/people/{uuid}", nonExistentId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").containsString("Person not found"));
            
            verify(legalEntityService).getPerson(nonExistentId);
        }
        
        @Test
        @DisplayName("GET /api/v1/people/{uuid} - Invalid UUID Format")
        void getPerson_InvalidUUID() throws Exception {
            // When & Then
            mockMvc.perform(get("/api/v1/people/not-a-uuid"))
                    .andExpect(status().isBadRequest());
        }
    }
    
    @Nested
    @DisplayName("updatePerson() Tests")
    class UpdatePersonTests {
        
        @Test
        @DisplayName("PATCH /api/v1/people/{uuid} - Success")
        void updatePerson_Success() throws Exception {
            // Given
            PersonRequest updateRequest = new PersonRequest("Jane", "Smith");
            PersonResponse updatedResponse = new PersonResponse();
            updatedResponse.setId(personId);
            updatedResponse.setFirstName("Jane");
            updatedResponse.setLastName("Smith");
            
            when(legalEntityService.updatePerson(eq(personId), any(PersonRequest.class)))
                    .thenReturn(updatedResponse);
            
            // When & Then
            mockMvc.perform(patch("/api/v1/people/{uuid}", personId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.firstName").value("Jane"))
                    .andExpect(jsonPath("$.lastName").value("Smith"));
            
            verify(legalEntityService).updatePerson(eq(personId), any(PersonRequest.class));
        }
        
        @Test
        @DisplayName("PATCH /api/v1/people/{uuid} - Not Found")
        void updatePerson_NotFound() throws Exception {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            PersonRequest updateRequest = new PersonRequest("Jane", "Smith");
            
            when(legalEntityService.updatePerson(eq(nonExistentId), any(PersonRequest.class)))
                    .thenThrow(new ResourceNotFoundException("Person not found: " + nonExistentId));
            
            // When & Then
            mockMvc.perform(patch("/api/v1/people/{uuid}", nonExistentId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isNotFound());
            
            verify(legalEntityService).updatePerson(eq(nonExistentId), any(PersonRequest.class));
        }
        
        @Test
        @DisplayName("PATCH /api/v1/people/{uuid} - Validation Error")
        void updatePerson_ValidationError() throws Exception {
            // Given
            PersonRequest invalidRequest = new PersonRequest("", "Doe");
            
            // When & Then
            mockMvc.perform(patch("/api/v1/people/{uuid}", personId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
            
            verify(legalEntityService, never()).updatePerson(any(), any());
        }
    }
    
    @Nested
    @DisplayName("getPersonAuditTrail() Tests")
    class GetPersonAuditTrailTests {
        
        @Test
        @DisplayName("GET /api/v1/people/{uuid}/audit-trail - Success")
        void getPersonAuditTrail_Success() throws Exception {
            // Given
            PersonAuditResponse audit1 = new PersonAuditResponse();
            PersonAuditResponse audit2 = new PersonAuditResponse();
            List<PersonAuditResponse> audits = Arrays.asList(audit1, audit2);
            
            when(legalEntityService.getPersonAuditTrail(personId)).thenReturn(audits);
            
            // When & Then
            mockMvc.perform(get("/api/v1/people/{uuid}/audit-trail", personId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));
            
            verify(legalEntityService).getPersonAuditTrail(personId);
        }
        
        @Test
        @DisplayName("GET /api/v1/people/{uuid}/audit-trail - Empty Audit Trail")
        void getPersonAuditTrail_Empty() throws Exception {
            // Given
            when(legalEntityService.getPersonAuditTrail(personId))
                    .thenReturn(Collections.emptyList());
            
            // When & Then
            mockMvc.perform(get("/api/v1/people/{uuid}/audit-trail", personId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
        
        @Test
        @DisplayName("GET /api/v1/people/{uuid}/audit-trail - Not Found")
        void getPersonAuditTrail_NotFound() throws Exception {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(legalEntityService.getPersonAuditTrail(nonExistentId))
                    .thenThrow(new ResourceNotFoundException("Person not found: " + nonExistentId));
            
            // When & Then
            mockMvc.perform(get("/api/v1/people/{uuid}/audit-trail", nonExistentId))
                    .andExpect(status().isNotFound());
        }
    }
}
