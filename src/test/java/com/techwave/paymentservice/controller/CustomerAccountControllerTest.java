package com.techwave.paymentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techwave.paymentservice.dto.response.CustomerAccountResponse;
import com.techwave.paymentservice.dto.response.LegalEntityResponse;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.service.CustomerAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerAccountController.class)
@DisplayName("CustomerAccountController Tests")
class CustomerAccountControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private CustomerAccountService customerAccountService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private UUID customerAccountId;
    private UUID personId;
    private UUID corporationId;
    private CustomerAccountResponse customerAccountResponse;
    
    @BeforeEach
    void setUp() {
        customerAccountId = UUID.randomUUID();
        personId = UUID.randomUUID();
        corporationId = UUID.randomUUID();
        
        customerAccountResponse = new CustomerAccountResponse();
        customerAccountResponse.setId(customerAccountId);
        customerAccountResponse.setAccountNumber("ACC001");
    }
    
    @Nested
    @DisplayName("getCustomerAccount() Tests")
    class GetCustomerAccountTests {
        
        @Test
        @DisplayName("GET /api/v1/customer-accounts/{uuid} - Found")
        void getCustomerAccount_Found() throws Exception {
            // Given
            when(customerAccountService.getCustomerAccount(customerAccountId))
                    .thenReturn(customerAccountResponse);
            
            // When & Then
            mockMvc.perform(get("/api/v1/customer-accounts/{uuid}", customerAccountId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(customerAccountId.toString()))
                    .andExpect(jsonPath("$.accountNumber").value("ACC001"));
            
            verify(customerAccountService).getCustomerAccount(customerAccountId);
        }
        
        @Test
        @DisplayName("GET /api/v1/customer-accounts/{uuid} - Not Found")
        void getCustomerAccount_NotFound() throws Exception {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(customerAccountService.getCustomerAccount(nonExistentId))
                    .thenThrow(new ResourceNotFoundException("Customer account not found: " + nonExistentId));
            
            // When & Then
            mockMvc.perform(get("/api/v1/customer-accounts/{uuid}", nonExistentId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("NOT_FOUND"))
                    .andExpect(jsonPath("$.message").containsString("Customer account not found"));
            
            verify(customerAccountService).getCustomerAccount(nonExistentId);
        }
        
        @Test
        @DisplayName("GET /api/v1/customer-accounts/{uuid} - Invalid UUID Format")
        void getCustomerAccount_InvalidUUID() throws Exception {
            // When & Then
            mockMvc.perform(get("/api/v1/customer-accounts/not-a-uuid"))
                    .andExpect(status().isBadRequest());
        }
    }
    
    @Nested
    @DisplayName("getCustomerAccountBeneficialOwners() Tests")
    class GetCustomerAccountBeneficialOwnersTests {
        
        @Test
        @DisplayName("GET /api/v1/customer-accounts/{uuid}/beneficial-owners - Success with persons")
        void getCustomerAccountBeneficialOwners_SuccessWithPersons() throws Exception {
            // Given
            LegalEntityResponse owner1 = new LegalEntityResponse(personId, "people");
            LegalEntityResponse owner2 = new LegalEntityResponse(UUID.randomUUID(), "people");
            List<LegalEntityResponse> owners = Arrays.asList(owner1, owner2);
            
            when(customerAccountService.getCustomerAccountBeneficialOwners(customerAccountId))
                    .thenReturn(owners);
            
            // When & Then
            mockMvc.perform(get("/api/v1/customer-accounts/{uuid}/beneficial-owners", customerAccountId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id").value(personId.toString()))
                    .andExpect(jsonPath("$[0].resourceType").value("people"))
                    .andExpect(jsonPath("$[1].resourceType").value("people"));
            
            verify(customerAccountService).getCustomerAccountBeneficialOwners(customerAccountId);
        }
        
        @Test
        @DisplayName("GET /api/v1/customer-accounts/{uuid}/beneficial-owners - Success with corporations")
        void getCustomerAccountBeneficialOwners_SuccessWithCorporations() throws Exception {
            // Given
            LegalEntityResponse owner1 = new LegalEntityResponse(corporationId, "corporations");
            LegalEntityResponse owner2 = new LegalEntityResponse(UUID.randomUUID(), "corporations");
            List<LegalEntityResponse> owners = Arrays.asList(owner1, owner2);
            
            when(customerAccountService.getCustomerAccountBeneficialOwners(customerAccountId))
                    .thenReturn(owners);
            
            // When & Then
            mockMvc.perform(get("/api/v1/customer-accounts/{uuid}/beneficial-owners", customerAccountId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].resourceType").value("corporations"))
                    .andExpect(jsonPath("$[1].resourceType").value("corporations"));
            
            verify(customerAccountService).getCustomerAccountBeneficialOwners(customerAccountId);
        }
        
        @Test
        @DisplayName("GET /api/v1/customer-accounts/{uuid}/beneficial-owners - Success with mixed types")
        void getCustomerAccountBeneficialOwners_SuccessMixed() throws Exception {
            // Given
            LegalEntityResponse owner1 = new LegalEntityResponse(personId, "people");
            LegalEntityResponse owner2 = new LegalEntityResponse(corporationId, "corporations");
            List<LegalEntityResponse> owners = Arrays.asList(owner1, owner2);
            
            when(customerAccountService.getCustomerAccountBeneficialOwners(customerAccountId))
                    .thenReturn(owners);
            
            // When & Then
            mockMvc.perform(get("/api/v1/customer-accounts/{uuid}/beneficial-owners", customerAccountId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].resourceType").value("people"))
                    .andExpect(jsonPath("$[1].resourceType").value("corporations"));
            
            verify(customerAccountService).getCustomerAccountBeneficialOwners(customerAccountId);
        }
        
        @Test
        @DisplayName("GET /api/v1/customer-accounts/{uuid}/beneficial-owners - Empty List")
        void getCustomerAccountBeneficialOwners_Empty() throws Exception {
            // Given
            when(customerAccountService.getCustomerAccountBeneficialOwners(customerAccountId))
                    .thenReturn(Collections.emptyList());
            
            // When & Then
            mockMvc.perform(get("/api/v1/customer-accounts/{uuid}/beneficial-owners", customerAccountId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)));
            
            verify(customerAccountService).getCustomerAccountBeneficialOwners(customerAccountId);
        }
        
        @Test
        @DisplayName("GET /api/v1/customer-accounts/{uuid}/beneficial-owners - Not Found")
        void getCustomerAccountBeneficialOwners_NotFound() throws Exception {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(customerAccountService.getCustomerAccountBeneficialOwners(nonExistentId))
                    .thenThrow(new ResourceNotFoundException("Customer account not found: " + nonExistentId));
            
            // When & Then
            mockMvc.perform(get("/api/v1/customer-accounts/{uuid}/beneficial-owners", nonExistentId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("NOT_FOUND"));
            
            verify(customerAccountService).getCustomerAccountBeneficialOwners(nonExistentId);
        }
    }
}
