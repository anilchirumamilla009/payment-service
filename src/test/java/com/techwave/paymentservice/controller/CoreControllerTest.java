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
class CoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ── Countries ───────────────────────────────────────────────────────

    @Test
    @WithMockUser
    void getCountries_returnsOkWithList() throws Exception {
        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].resourceType", is("countries")));
    }

    @Test
    @WithMockUser
    void getCountry_existingId_returnsOk() throws Exception {
        mockMvc.perform(get("/countries/GB"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("GB")))
                .andExpect(jsonPath("$.name", is("United Kingdom")))
                .andExpect(jsonPath("$.resourceType", is("countries")));
    }

    @Test
    @WithMockUser
    void getCountry_nonExistingId_returns404() throws Exception {
        mockMvc.perform(get("/countries/ZZ"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    void getCountry_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/countries/GB"))
                .andExpect(status().isUnauthorized());
    }

    // ── Currencies ──────────────────────────────────────────────────────

    @Test
    @WithMockUser
    void getCurrencies_returnsOkWithList() throws Exception {
        mockMvc.perform(get("/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    @WithMockUser
    void getCurrency_existingId_returnsOk() throws Exception {
        mockMvc.perform(get("/currencies/EUR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("EUR")))
                .andExpect(jsonPath("$.name", is("Euro")));
    }

    @Test
    @WithMockUser
    void getCurrency_nonExistingId_returns404() throws Exception {
        mockMvc.perform(get("/currencies/XYZ"))
                .andExpect(status().isNotFound());
    }

    // ── Silos ───────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    void getSilos_returnsOkWithList() throws Exception {
        mockMvc.perform(get("/silos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @WithMockUser
    void getSilo_existingId_returnsOk() throws Exception {
        mockMvc.perform(get("/silos/treasury-core"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("treasury-core")))
                .andExpect(jsonPath("$.resourceType", is("silos")));
    }

    @Test
    @WithMockUser
    void getSilo_nonExistingId_returns404() throws Exception {
        mockMvc.perform(get("/silos/nonexistent"))
                .andExpect(status().isNotFound());
    }
}

