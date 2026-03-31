package com.techwave.paymentservice.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class BankAccountsControllerTest {

    private static final String BANK_ACCOUNT_UUID = "33333333-3333-3333-3333-333333333333";
    private static final String NON_EXISTING_UUID = "99999999-9999-9999-9999-999999999999";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void getBankAccount_existingId_returnsOk() throws Exception {
        mockMvc.perform(get("/bank-accounts/{uuid}", BANK_ACCOUNT_UUID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(BANK_ACCOUNT_UUID)))
                .andExpect(jsonPath("$.resourceType", is("bank-accounts")))
                .andExpect(jsonPath("$.beneficiary", is("Cornerstone FX Ltd")))
                .andExpect(jsonPath("$.currency", is("EUR")));
    }

    @Test
    @WithMockUser
    void getBankAccount_nonExistingId_returns404() throws Exception {
        mockMvc.perform(get("/bank-accounts/{uuid}", NON_EXISTING_UUID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void createBankAccount_validPayload_returnsOk() throws Exception {
        String json = """
                {
                    "beneficiary": "Test Ltd",
                    "beneficiaryAddress": "123 Test St",
                    "currency": "USD",
                    "country": "US"
                }
                """;
        mockMvc.perform(put("/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.resourceType", is("bank-accounts")))
                .andExpect(jsonPath("$.beneficiary", is("Test Ltd")));
    }

    @Test
    @WithMockUser
    void createBankAccount_missingBeneficiary_returns400() throws Exception {
        String json = """
                {
                    "currency": "USD",
                    "country": "US"
                }
                """;
        mockMvc.perform(put("/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void createBankAccount_missingCurrency_returns400() throws Exception {
        String json = """
                {
                    "beneficiary": "Test Ltd",
                    "country": "US"
                }
                """;
        mockMvc.perform(put("/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void getBankAccountAuditTrail_existingId_returnsOk() throws Exception {
        mockMvc.perform(get("/bank-accounts/{uuid}/audit-trail", BANK_ACCOUNT_UUID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].resource", is(BANK_ACCOUNT_UUID)));
    }

    @Test
    @WithMockUser
    void getBankAccountAuditTrail_nonExistingId_returns404() throws Exception {
        mockMvc.perform(get("/bank-accounts/{uuid}/audit-trail", NON_EXISTING_UUID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getBankAccountBeneficialOwners_existingId_returnsOk() throws Exception {
        mockMvc.perform(get("/bank-accounts/{uuid}/beneficial-owners", BANK_ACCOUNT_UUID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @WithMockUser
    void getBankAccountBeneficialOwners_nonExistingId_returns404() throws Exception {
        mockMvc.perform(get("/bank-accounts/{uuid}/beneficial-owners", NON_EXISTING_UUID))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBankAccount_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/bank-accounts/{uuid}", BANK_ACCOUNT_UUID))
                .andExpect(status().isUnauthorized());
    }
}

