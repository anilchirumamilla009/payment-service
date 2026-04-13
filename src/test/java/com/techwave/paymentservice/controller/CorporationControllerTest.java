package com.techwave.paymentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techwave.paymentservice.dto.request.CorporationRequest;
import com.techwave.paymentservice.dto.response.CorporationAuditResponse;
import com.techwave.paymentservice.dto.response.CorporationResponse;
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

import java.time.LocalDate;
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

@WebMvcTest(CorporationController.class)
@DisplayName("CorporationController Tests")
class CorporationControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private LegalEntityService legalEntityService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private UUID corporationId;
    private CorporationRequest corporationRequest;
    private CorporationResponse corporationResponse;
    
    @BeforeEach
    void setUp() {
        corporationId = UUID.randomUUID();
        
        corporationRequest = new CorporationRequest("ACME Corp", "ACME001");
        corporationRequest.setIncorporationCountry("US");
        corporationRequest.setIncorporationDate(LocalDate.of(2020, 1, 1));
        
        corporationResponse = new CorporationResponse();
        corporationResponse.setId(corporationId);
        corporationResponse.setName("ACME Corp");
        corporationResponse.setCode("ACME001");
    }
    
    @Nested
    @DisplayName("createCorporation() Tests")
    class CreateCorporationTests {
        
        @Test
        @DisplayName("POST /api/v1/corporations - Success")
        void createCorporation_Success() throws Exception {
            // Given
            when(legalEntityService.createCorporation(any(CorporationRequest.class)))
                    .thenReturn(corporationResponse);
            
            // When & Then
            mockMvc.perform(post("/api/v1/corporations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(corporationRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(corporationId.toString()))
                    .andExpect(jsonPath("$.name").value("ACME Corp"))
                    .andExpect(jsonPath("$.code").value("ACME001"));
            
            verify(legalEntityService, times(1)).createCorporation(any(CorporationRequest.class));
        }
        
        @Test
        @DisplayName("POST /api/v1/corporations - Validation Error (Missing Name)")
        void createCorporation_ValidationError_MissingName() throws Exception {
            // Given
            CorporationRequest invalidRequest = new CorporationRequest("", "CODE001");
            
            // When & Then
            mockMvc.perform(post("/api/v1/corporations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
            
            verify(legalEntityService, never()).createCorporation(any());
        }
        
        @Test
        @DisplayName("POST /api/v1/corporations - Validation Error (Missing Code)")
        void createCorporation_ValidationError_MissingCode() throws Exception {
            // Given
            CorporationRequest invalidRequest = new CorporationRequest("Test Corp", "");
            
            // When & Then
            mockMvc.perform(post("/api/v1/corporations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
            
            verify(legalEntityService, never()).createCorporation(any());
        }
        
        @Test
        @DisplayName("POST /api/v1/corporations - Validation Error (Invalid Country Code)")
        void createCorporation_ValidationError_InvalidCountryCode() throws Exception {
            // Given
            CorporationRequest invalidRequest = new CorporationRequest("Test Corp", "CODE001");
            invalidRequest.setIncorporationCountry("INVALID");
            
            // When & Then
            mockMvc.perform(post("/api/v1/corporations")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
            
            verify(legalEntityService, never()).createCorporation(any());
        }
    }
    
    @Nested
    @DisplayName("getCorporation() Tests")
    class GetCorporationTests {
        
        @Test
        @DisplayName("GET /api/v1/corporations/{uuid} - Found")
        void getCorporation_Found() throws Exception {
            // Given
            when(legalEntityService.getCorporation(corporationId)).thenReturn(corporationResponse);
            
            // When & Then
            mockMvc.perform(get("/api/v1/corporations/{uuid}", corporationId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(corporationId.toString()))
                    .andExpect(jsonPath("$.name").value("ACME Corp"));
            
            verify(legalEntityService).getCorporation(corporationId);
        }
        
        @Test
        @DisplayName("GET /api/v1/corporations/{uuid} - Not Found")
        void getCorporation_NotFound() throws Exception {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(legalEntityService.getCorporation(nonExistentId))
                    .thenThrow(new ResourceNotFoundException("Corporation not found: " + nonExistentId));
            
            // When & Then
            mockMvc.perform(get("/api/v1/corporations/{uuid}", nonExistentId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("NOT_FOUND"));
        }
    }
    
    @Nested
    @DisplayName("updateCorporation() Tests")
    class UpdateCorporationTests {
        
        @Test
        @DisplayName("PATCH /api/v1/corporations/{uuid} - Success")
        void updateCorporation_Success() throws Exception {
            // Given
            CorporationRequest updateRequest = new CorporationRequest("Updated Corp", "UPD001");
            updateRequest.setIncorporationCountry("GB");
            
            CorporationResponse updatedResponse = new CorporationResponse();
            updatedResponse.setId(corporationId);
            updatedResponse.setName("Updated Corp");
            updatedResponse.setCode("UPD001");
            
            when(legalEntityService.updateCorporation(eq(corporationId), any(CorporationRequest.class)))
                    .thenReturn(updatedResponse);
            
            // When & Then
            mockMvc.perform(patch("/api/v1/corporations/{uuid}", corporationId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Updated Corp"))
                    .andExpect(jsonPath("$.code").value("UPD001"));
            
            verify(legalEntityService).updateCorporation(eq(corporationId), any(CorporationRequest.class));
        }
        
        @Test
        @DisplayName("PATCH /api/v1/corporations/{uuid} - Not Found")
        void updateCorporation_NotFound() throws Exception {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            
            when(legalEntityService.updateCorporation(eq(nonExistentId), any(CorporationRequest.class)))
                    .thenThrow(new ResourceNotFoundException("Corporation not found: " + nonExistentId));
            
            // When & Then
            mockMvc.perform(patch("/api/v1/corporations/{uuid}", nonExistentId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(corporationRequest)))
                    .andExpect(status().isNotFound());
        }
    }
    
    @Nested
    @DisplayName("getCorporationByCode() Tests")
    class GetCorporationByCodeTests {
        
        @Test
        @DisplayName("GET /api/v1/corporations/{country}/{code} - Found")
        void getCorporationByCode_Found() throws Exception {
            // Given
            when(legalEntityService.getCorporationByCode("US", "ACME001"))
                    .thenReturn(corporationResponse);
            
            // When & Then
            mockMvc.perform(get("/api/v1/corporations/US/ACME001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("ACME Corp"))
                    .andExpect(jsonPath("$.code").value("ACME001"));
            
            verify(legalEntityService).getCorporationByCode("US", "ACME001");
        }
        
        @Test
        @DisplayName("GET /api/v1/corporations/{country}/{code} - Not Found")
        void getCorporationByCode_NotFound() throws Exception {
            // Given
            when(legalEntityService.getCorporationByCode("US", "INVALID"))
                    .thenThrow(new ResourceNotFoundException("Corporation not found - country: US, code: INVALID"));
            
            // When & Then
            mockMvc.perform(get("/api/v1/corporations/US/INVALID"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("NOT_FOUND"));
            
            verify(legalEntityService).getCorporationByCode("US", "INVALID");
        }
    }
    
    @Nested
    @DisplayName("getCorporationAuditTrail() Tests")
    class GetCorporationAuditTrailTests {
        
        @Test
        @DisplayName("GET /api/v1/corporations/{uuid}/audit-trail - Success")
        void getCorporationAuditTrail_Success() throws Exception {
            // Given
            CorporationAuditResponse audit1 = new CorporationAuditResponse();
            CorporationAuditResponse audit2 = new CorporationAuditResponse();
            List<CorporationAuditResponse> audits = Arrays.asList(audit1, audit2);
            
            when(legalEntityService.getCorporationAuditTrail(corporationId)).thenReturn(audits);
            
            // When & Then
            mockMvc.perform(get("/api/v1/corporations/{uuid}/audit-trail", corporationId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));
            
            verify(legalEntityService).getCorporationAuditTrail(corporationId);
        }
        
        @Test
        @DisplayName("GET /api/v1/corporations/{uuid}/audit-trail - Empty")
        void getCorporationAuditTrail_Empty() throws Exception {
            // Given
            when(legalEntityService.getCorporationAuditTrail(corporationId))
                    .thenReturn(Collections.emptyList());
            
            // When & Then
            mockMvc.perform(get("/api/v1/corporations/{uuid}/audit-trail", corporationId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
        
        @Test
        @DisplayName("GET /api/v1/corporations/{uuid}/audit-trail - Not Found")
        void getCorporationAuditTrail_NotFound() throws Exception {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(legalEntityService.getCorporationAuditTrail(nonExistentId))
                    .thenThrow(new ResourceNotFoundException("Corporation not found: " + nonExistentId));
            
            // When & Then
            mockMvc.perform(get("/api/v1/corporations/{uuid}/audit-trail", nonExistentId))
                    .andExpect(status().isNotFound());
        }
    }
}
