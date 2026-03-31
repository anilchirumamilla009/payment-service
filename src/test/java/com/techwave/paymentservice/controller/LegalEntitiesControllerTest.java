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
class LegalEntitiesControllerTest {

    private static final String CORPORATION_UUID = "11111111-1111-1111-1111-111111111111";
    private static final String PERSON_UUID = "22222222-2222-2222-2222-222222222222";
    private static final String NON_EXISTING_UUID = "99999999-9999-9999-9999-999999999999";

    @Autowired
    private MockMvc mockMvc;

    // ── Corporations ────────────────────────────────────────────────────

    @Test
    @WithMockUser
    void getCorporation_existingId_returnsOk() throws Exception {
        mockMvc.perform(get("/corporations/{uuid}", CORPORATION_UUID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(CORPORATION_UUID)))
                .andExpect(jsonPath("$.resourceType", is("corporations")))
                .andExpect(jsonPath("$.name", is("Cornerstone FX Ltd")));
    }

    @Test
    @WithMockUser
    void getCorporation_nonExistingId_returns404() throws Exception {
        mockMvc.perform(get("/corporations/{uuid}", NON_EXISTING_UUID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void createCorporation_validPayload_returnsOk() throws Exception {
        String json = """
                {
                    "name": "Acme Corp",
                    "code": "ACME-US",
                    "incorporationCountry": "US",
                    "type": "FINTECH"
                }
                """;
        mockMvc.perform(post("/corporations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name", is("Acme Corp")));
    }

    @Test
    @WithMockUser
    void createCorporation_missingName_returns400() throws Exception {
        String json = """
                {
                    "code": "ACME-US"
                }
                """;
        mockMvc.perform(post("/corporations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void updateCorporation_existingId_returnsOk() throws Exception {
        String json = """
                {
                    "name": "Cornerstone FX Ltd Updated"
                }
                """;
        mockMvc.perform(patch("/corporations/{uuid}", CORPORATION_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Cornerstone FX Ltd Updated")));
    }

    @Test
    @WithMockUser
    void updateCorporation_nonExistingId_returns404() throws Exception {
        String json = """
                { "name": "X" }
                """;
        mockMvc.perform(patch("/corporations/{uuid}", NON_EXISTING_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void getCorporationAuditTrail_existingId_returnsOk() throws Exception {
        mockMvc.perform(get("/corporations/{uuid}/audit-trail", CORPORATION_UUID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @WithMockUser
    void getCorporationByCode_existing_returnsOk() throws Exception {
        mockMvc.perform(get("/corporations/{country}/{code}", "GB", "CFS-UK"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("CFS-UK")));
    }

    @Test
    @WithMockUser
    void getCorporationByCode_nonExisting_returns404() throws Exception {
        mockMvc.perform(get("/corporations/{country}/{code}", "XX", "NOPE"))
                .andExpect(status().isNotFound());
    }

    // ── People ──────────────────────────────────────────────────────────

    @Test
    @WithMockUser
    void getPerson_existingId_returnsOk() throws Exception {
        mockMvc.perform(get("/people/{uuid}", PERSON_UUID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(PERSON_UUID)))
                .andExpect(jsonPath("$.resourceType", is("people")))
                .andExpect(jsonPath("$.firstName", is("Stephen")));
    }

    @Test
    @WithMockUser
    void getPerson_nonExistingId_returns404() throws Exception {
        mockMvc.perform(get("/people/{uuid}", NON_EXISTING_UUID))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void createPerson_validPayload_returnsOk() throws Exception {
        String json = """
                {
                    "firstName": "Jane",
                    "lastName": "Doe"
                }
                """;
        mockMvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName", is("Jane")));
    }

    @Test
    @WithMockUser
    void createPerson_missingFirstName_returns400() throws Exception {
        String json = """
                { "lastName": "Doe" }
                """;
        mockMvc.perform(post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void updatePerson_existingId_returnsOk() throws Exception {
        String json = """
                { "lastName": "Updated" }
                """;
        mockMvc.perform(patch("/people/{uuid}", PERSON_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", is("Updated")));
    }

    @Test
    @WithMockUser
    void getPersonAuditTrail_existingId_returnsOk() throws Exception {
        mockMvc.perform(get("/people/{uuid}/audit-trail", PERSON_UUID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    void getCorporation_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/corporations/{uuid}", CORPORATION_UUID))
                .andExpect(status().isUnauthorized());
    }
}

