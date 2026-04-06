package com.techwave.paymentservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techwave.paymentservice.dto.PersonDto;
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
 * Integration tests for the Core (countries/currencies/silos)
 * and Legal-Entities (people/corporations) endpoints.
 * Uses the H2 in-memory database with Flyway seed data.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Core & Legal-Entities Integration Tests")
class CoreAndLegalEntitiesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // ── Countries ────────────────────────────────────────────

    @Test
    @DisplayName("GET /countries returns seeded data")
    void getCountries_returnsSeededData() throws Exception {
        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(10))))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].resourceType", is("countries")));
    }

    @Test
    @DisplayName("GET /countries/{id} returns single country")
    void getCountry_returnsCountry() throws Exception {
        mockMvc.perform(get("/countries/US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("US")))
                .andExpect(jsonPath("$.name",
                        is("United States of America")))
                .andExpect(jsonPath("$.numericCode", is("840")))
                .andExpect(jsonPath("$.alpha3Code", is("USA")));
    }

    @Test
    @DisplayName("GET /countries/{id} returns 404 for unknown id")
    void getCountry_notFound() throws Exception {
        mockMvc.perform(get("/countries/ZZ"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    // ── Currencies ───────────────────────────────────────────

    @Test
    @DisplayName("GET /currencies returns seeded data")
    void getCurrencies_returnsSeededData() throws Exception {
        mockMvc.perform(get("/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(10))))
                .andExpect(jsonPath("$[0].resourceType",
                        is("currencies")));
    }

    @Test
    @DisplayName("GET /currencies/{id} returns single currency")
    void getCurrency_returnsCurrency() throws Exception {
        mockMvc.perform(get("/currencies/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("USD")))
                .andExpect(jsonPath("$.name",
                        is("United States Dollar")));
    }

    // ── Silos ────────────────────────────────────────────────

    @Test
    @DisplayName("GET /silos returns seeded data")
    void getSilos_returnsSeededData() throws Exception {
        mockMvc.perform(get("/silos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",
                        hasSize(greaterThanOrEqualTo(3))))
                .andExpect(jsonPath("$[0].resourceType",
                        is("silos")));
    }

    @Test
    @DisplayName("GET /silos/{id} returns single silo")
    void getSilo_returnsSilo() throws Exception {
        mockMvc.perform(get("/silos/TREASURY_01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("TREASURY_01")))
                .andExpect(jsonPath("$.name", is("Main Treasury")))
                .andExpect(jsonPath("$.type", is("TREASURY")));
    }

    // ── People ───────────────────────────────────────────────

    @Test
    @DisplayName("POST /people -> GET -> PATCH -> audit-trail")
    void personLifecycle() throws Exception {
        // Create
        PersonDto createDto = new PersonDto();
        createDto.setFirstName("Alice");
        createDto.setLastName("Smith");

        MvcResult createResult = mockMvc.perform(
                post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(createDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName", is("Alice")))
                .andExpect(jsonPath("$.resourceType", is("people")))
                .andReturn();

        String uuid = objectMapper.readTree(
                createResult.getResponse().getContentAsString())
                .get("id").asText();

        // Get
        mockMvc.perform(get("/people/" + uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Alice")));

        // Update
        PersonDto updateDto = new PersonDto();
        updateDto.setFirstName("Alicia");

        mockMvc.perform(
                patch("/people/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Alicia")));

        // Audit trail
        mockMvc.perform(get("/people/" + uuid + "/audit-trail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].version", is(1)))
                .andExpect(jsonPath("$[1].version", is(2)));
    }

    // ── Corporations ─────────────────────────────────────────

    @Test
    @DisplayName("POST /corporations -> GET by uuid -> GET by code")
    void corporationLifecycle() throws Exception {
        String body = """
                {
                  "name": "Test Corp",
                  "code": "TCORP",
                  "incorporationCountry": "IE",
                  "incorporationDate": "2022-06-15"
                }
                """;

        MvcResult createResult = mockMvc.perform(
                post("/corporations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Corp")))
                .andExpect(jsonPath("$.resourceType",
                        is("corporations")))
                .andReturn();

        String uuid = objectMapper.readTree(
                createResult.getResponse().getContentAsString())
                .get("id").asText();

        // GET by uuid
        mockMvc.perform(get("/corporations/" + uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("TCORP")));

        // GET by country/code
        mockMvc.perform(get("/corporations/IE/TCORP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Corp")));

        // Audit trail
        mockMvc.perform(
                get("/corporations/" + uuid + "/audit-trail"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}

