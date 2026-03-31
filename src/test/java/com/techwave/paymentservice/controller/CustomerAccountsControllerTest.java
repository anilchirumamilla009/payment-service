package com.techwave.paymentservice.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerAccountsControllerTest {

    private static final String CUSTOMER_ACCOUNT_UUID = "44444444-4444-4444-4444-444444444444";
    private static final String NON_EXISTING_UUID = "99999999-9999-9999-9999-999999999999";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void getCustomerAccount_existingId_returnsOk() throws Exception {
        mockMvc.perform(get("/customer-accounts/{uuid}", CUSTOMER_ACCOUNT_UUID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(CUSTOMER_ACCOUNT_UUID)))
                .andExpect(jsonPath("$.resourceType", is("customer-accounts")))
                .andExpect(jsonPath("$.name", is("Cornerstone Principal Account")));
    }

    @Test
    @WithMockUser
    void getCustomerAccount_nonExistingId_returns404() throws Exception {
        mockMvc.perform(get("/customer-accounts/{uuid}", NON_EXISTING_UUID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getCustomerAccountBeneficialOwners_existingId_returnsOk() throws Exception {
        mockMvc.perform(get("/customer-accounts/{uuid}/beneficial-owners", CUSTOMER_ACCOUNT_UUID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @WithMockUser
    void getCustomerAccountBeneficialOwners_nonExistingId_returns404() throws Exception {
        mockMvc.perform(get("/customer-accounts/{uuid}/beneficial-owners", NON_EXISTING_UUID))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCustomerAccount_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/customer-accounts/{uuid}", CUSTOMER_ACCOUNT_UUID))
                .andExpect(status().isUnauthorized());
    }
}

