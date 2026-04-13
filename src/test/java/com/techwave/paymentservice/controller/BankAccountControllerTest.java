package com.techwave.paymentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techwave.paymentservice.dto.request.BankAccountRequest;
import com.techwave.paymentservice.dto.response.BankAccountAuditResponse;
import com.techwave.paymentservice.dto.response.BankAccountResponse;
import com.techwave.paymentservice.dto.response.LegalEntityResponse;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.service.BankAccountService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankAccountController.class)
@DisplayName("BankAccountController Tests")
class BankAccountControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private BankAccountService bankAccountService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private UUID bankAccountId;
    private UUID personId;
    private BankAccountRequest bankAccountRequest;
    private BankAccountResponse bankAccountResponse;
    
    @BeforeEach
    void setUp() {
        bankAccountId = UUID.randomUUID();
        personId = UUID.randomUUID();
        
        bankAccountRequest = new BankAccountRequest();
        bankAccountRequest.setIban("DE89370400440532013000");
        bankAccountRequest.setBeneficiary("John Doe");
        bankAccountRequest.setCountry("DE");
        bankAccountRequest.setCurrency("EUR");
        bankAccountRequest.setBic("COBADEHHXXX");
        
        bankAccountResponse = new BankAccountResponse();
        bankAccountResponse.setId(bankAccountId);
        bankAccountResponse.setIban("DE89370400440532013000");
        bankAccountResponse.setBeneficiary("John Doe");
    }
    
    @Nested
    @DisplayName("createBankAccount() Tests")
    class CreateBankAccountTests {
        
        @Test
        @DisplayName("PUT /api/v1/bank-accounts - Success (Create)")
        void createBankAccount_Success_Create() throws Exception {
            // Given
            when(bankAccountService.createOrLocateBankAccount(any(BankAccountRequest.class)))
                    .thenReturn(bankAccountResponse);
            
            // When & Then
            mockMvc.perform(put("/api/v1/bank-accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(bankAccountRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(bankAccountId.toString()))
                    .andExpect(jsonPath("$.iban").value("DE89370400440532013000"))
                    .andExpect(jsonPath("$.beneficiary").value("John Doe"));
            
            verify(bankAccountService, times(1)).createOrLocateBankAccount(any(BankAccountRequest.class));
        }
        
        @Test
        @DisplayName("PUT /api/v1/bank-accounts - Idempotent (Locate Existing)")
        void createBankAccount_Idempotent_LocateExisting() throws Exception {
            // Given - calling twice with same IBAN should return same response
            when(bankAccountService.createOrLocateBankAccount(any(BankAccountRequest.class)))
                    .thenReturn(bankAccountResponse);
            
            // When & Then
            mockMvc.perform(put("/api/v1/bank-accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(bankAccountRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(bankAccountId.toString()));
            
            verify(bankAccountService, times(1)).createOrLocateBankAccount(any(BankAccountRequest.class));
        }
        
        @Test
        @DisplayName("PUT /api/v1/bank-accounts - Validation Error (Missing Country)")
        void createBankAccount_ValidationError_MissingCountry() throws Exception {
            // Given
            BankAccountRequest invalidRequest = new BankAccountRequest();
            invalidRequest.setIban("DE89370400440532013000");
            invalidRequest.setBeneficiary("John Doe");
            
            // When & Then
            mockMvc.perform(put("/api/v1/bank-accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
            
            verify(bankAccountService, never()).createOrLocateBankAccount(any());
        }
        
        @Test
        @DisplayName("PUT /api/v1/bank-accounts - Validation Error (Invalid IBAN)")
        void createBankAccount_ValidationError_InvalidIBAN() throws Exception {
            // Given
            BankAccountRequest invalidRequest = new BankAccountRequest();
            invalidRequest.setIban("INVALID");
            invalidRequest.setCountry("DE");
            
            // When & Then
            mockMvc.perform(put("/api/v1/bank-accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
            
            verify(bankAccountService, never()).createOrLocateBankAccount(any());
        }
        
        @Test
        @DisplayName("PUT /api/v1/bank-accounts - Validation Error (Invalid BIC)")
        void createBankAccount_ValidationError_InvalidBIC() throws Exception {
            // Given
            BankAccountRequest invalidRequest = new BankAccountRequest();
            invalidRequest.setIban("DE89370400440532013000");
            invalidRequest.setCountry("DE");
            invalidRequest.setBic("INVALID");
            
            // When & Then
            mockMvc.perform(put("/api/v1/bank-accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }
        
        @Test
        @DisplayName("PUT /api/v1/bank-accounts - Validation Error (Invalid Currency Code)")
        void createBankAccount_ValidationError_InvalidCurrency() throws Exception {
            // Given
            BankAccountRequest invalidRequest = new BankAccountRequest();
            invalidRequest.setIban("DE89370400440532013000");
            invalidRequest.setCountry("DE");
            invalidRequest.setCurrency("INVALID");
            
            // When & Then
            mockMvc.perform(put("/api/v1/bank-accounts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }
    }
    
    @Nested
    @DisplayName("getBankAccount() Tests")
    class GetBankAccountTests {
        
        @Test
        @DisplayName("GET /api/v1/bank-accounts/{uuid} - Found")
        void getBankAccount_Found() throws Exception {
            // Given
            when(bankAccountService.getBankAccount(bankAccountId))
                    .thenReturn(bankAccountResponse);
            
            // When & Then
            mockMvc.perform(get("/api/v1/bank-accounts/{uuid}", bankAccountId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(bankAccountId.toString()))
                    .andExpect(jsonPath("$.iban").value("DE89370400440532013000"));
            
            verify(bankAccountService).getBankAccount(bankAccountId);
        }
        
        @Test
        @DisplayName("GET /api/v1/bank-accounts/{uuid} - Not Found")
        void getBankAccount_NotFound() throws Exception {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(bankAccountService.getBankAccount(nonExistentId))
                    .thenThrow(new ResourceNotFoundException("Bank account not found: " + nonExistentId));
            
            // When & Then
            mockMvc.perform(get("/api/v1/bank-accounts/{uuid}", nonExistentId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("NOT_FOUND"));
        }
    }
    
    @Nested
    @DisplayName("getBankAccountAuditTrail() Tests")
    class GetBankAccountAuditTrailTests {
        
        @Test
        @DisplayName("GET /api/v1/bank-accounts/{uuid}/audit-trail - Success")
        void getBankAccountAuditTrail_Success() throws Exception {
            // Given
            BankAccountAuditResponse audit1 = new BankAccountAuditResponse();
            BankAccountAuditResponse audit2 = new BankAccountAuditResponse();
            List<BankAccountAuditResponse> audits = Arrays.asList(audit1, audit2);
            
            when(bankAccountService.getBankAccountAuditTrail(bankAccountId))
                    .thenReturn(audits);
            
            // When & Then
            mockMvc.perform(get("/api/v1/bank-accounts/{uuid}/audit-trail", bankAccountId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));
            
            verify(bankAccountService).getBankAccountAuditTrail(bankAccountId);
        }
        
        @Test
        @DisplayName("GET /api/v1/bank-accounts/{uuid}/audit-trail - Empty")
        void getBankAccountAuditTrail_Empty() throws Exception {
            // Given
            when(bankAccountService.getBankAccountAuditTrail(bankAccountId))
                    .thenReturn(Collections.emptyList());
            
            // When & Then
            mockMvc.perform(get("/api/v1/bank-accounts/{uuid}/audit-trail", bankAccountId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
        
        @Test
        @DisplayName("GET /api/v1/bank-accounts/{uuid}/audit-trail - Not Found")
        void getBankAccountAuditTrail_NotFound() throws Exception {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(bankAccountService.getBankAccountAuditTrail(nonExistentId))
                    .thenThrow(new ResourceNotFoundException("Bank account not found: " + nonExistentId));
            
            // When & Then
            mockMvc.perform(get("/api/v1/bank-accounts/{uuid}/audit-trail", nonExistentId))
                    .andExpect(status().isNotFound());
        }
    }
    
    @Nested
    @DisplayName("getBankAccountBeneficialOwners() Tests")
    class GetBankAccountBeneficialOwnersTests {
        
        @Test
        @DisplayName("GET /api/v1/bank-accounts/{uuid}/beneficial-owners - Success")
        void getBankAccountBeneficialOwners_Success() throws Exception {
            // Given
            LegalEntityResponse owner1 = new LegalEntityResponse(personId, "people");
            LegalEntityResponse owner2 = new LegalEntityResponse(UUID.randomUUID(), "corporations");
            List<LegalEntityResponse> owners = Arrays.asList(owner1, owner2);
            
            when(bankAccountService.getBankAccountBeneficialOwners(bankAccountId))
                    .thenReturn(owners);
            
            // When & Then
            mockMvc.perform(get("/api/v1/bank-accounts/{uuid}/beneficial-owners", bankAccountId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].resourceType").value("people"))
                    .andExpect(jsonPath("$[1].resourceType").value("corporations"));
            
            verify(bankAccountService).getBankAccountBeneficialOwners(bankAccountId);
        }
        
        @Test
        @DisplayName("GET /api/v1/bank-accounts/{uuid}/beneficial-owners - Empty")
        void getBankAccountBeneficialOwners_Empty() throws Exception {
            // Given
            when(bankAccountService.getBankAccountBeneficialOwners(bankAccountId))
                    .thenReturn(Collections.emptyList());
            
            // When & Then
            mockMvc.perform(get("/api/v1/bank-accounts/{uuid}/beneficial-owners", bankAccountId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
        
        @Test
        @DisplayName("GET /api/v1/bank-accounts/{uuid}/beneficial-owners - Not Found")
        void getBankAccountBeneficialOwners_NotFound() throws Exception {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(bankAccountService.getBankAccountBeneficialOwners(nonExistentId))
                    .thenThrow(new ResourceNotFoundException("Bank account not found: " + nonExistentId));
            
            // When & Then
            mockMvc.perform(get("/api/v1/bank-accounts/{uuid}/beneficial-owners", nonExistentId))
                    .andExpect(status().isNotFound());
        }
    }
}
