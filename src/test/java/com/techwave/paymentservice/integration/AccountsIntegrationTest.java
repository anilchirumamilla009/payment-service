package com.techwave.paymentservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for bank-account and customer-account endpoints.
 * Uses the H2 in-memory database with Flyway seed data.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Bank & Customer Account Integration Tests")
class AccountsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ── Bank Accounts ────────────────────────────────────────

    @Test
    @DisplayName("PUT /bank-accounts creates new account")
    void createBankAccount_createsNew() throws Exception {
        String body = """
                {
                  "beneficiary": "John Doe",
                  "iban": "GB29NWBK60161331926819",
                  "bic": "NWBKGB2L",
                  "currency": "GBP",
                  "country": "GB"
                }
                """;

        MvcResult result = mockMvc.perform(
                put("/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.iban",
                        is("GB29NWBK60161331926819")))
                .andExpect(jsonPath("$.resourceType",
                        is("bank-accounts")))
                .andReturn();

        String uuid = objectMapper.readTree(
                result.getResponse().getContentAsString())
                .get("id").asText();

        // GET by uuid
        mockMvc.perform(get("/bank-accounts/" + uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.beneficiary",
                        is("John Doe")));

        // Audit trail
        mockMvc.perform(
                get("/bank-accounts/" + uuid + "/audit-trail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].version", is(1)));
    }

    @Test
    @DisplayName("PUT /bank-accounts is idempotent for same IBAN")
    void createBankAccount_idempotent() throws Exception {
        String body = """
                {
                  "beneficiary": "Jane Doe",
                  "iban": "FR7630006000011234567890189",
                  "bic": "BNPAFRPP",
                  "currency": "EUR",
                  "country": "FR"
                }
                """;

        // First call – creates
        MvcResult first = mockMvc.perform(
                put("/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        String firstId = objectMapper.readTree(
                first.getResponse().getContentAsString())
                .get("id").asText();

        // Second call – locates existing
        MvcResult second = mockMvc.perform(
                put("/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        String secondId = objectMapper.readTree(
                second.getResponse().getContentAsString())
                .get("id").asText();

        assertEquals(firstId, secondId);
    }

    @Test
    @DisplayName("GET /bank-accounts/{uuid} returns 404 for unknown")
    void getBankAccount_notFound() throws Exception {
        mockMvc.perform(get(
                "/bank-accounts/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    @DisplayName("GET /bank-accounts/{uuid}/beneficial-owners returns list")
    void getBeneficialOwners_emptyByDefault() throws Exception {
        String body = """
                {
                  "beneficiary": "Owner Test",
                  "iban": "CH9300762011623852957",
                  "bic": "UBSWCHZH",
                  "currency": "CHF",
                  "country": "CH"
                }
                """;

        MvcResult result = mockMvc.perform(
                put("/bank-accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();

        String uuid = objectMapper.readTree(
                result.getResponse().getContentAsString())
                .get("id").asText();

        mockMvc.perform(
                get("/bank-accounts/" + uuid + "/beneficial-owners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ── Customer Accounts ────────────────────────────────────

    @Test
    @DisplayName("GET /customer-accounts/{uuid} returns 404 for unknown")
    void getCustomerAccount_notFound() throws Exception {
        mockMvc.perform(get(
                "/customer-accounts/"
                        + "00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    // ── Error Handling ───────────────────────────────────────

    @Test
    @DisplayName("Invalid UUID returns 400")
    void invalidUuid_returns400() throws Exception {
        mockMvc.perform(get("/people/not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)));
    }

    private void assertEquals(String expected, String actual) {
        org.junit.jupiter.api.Assertions.assertEquals(expected, actual);
    }
}

