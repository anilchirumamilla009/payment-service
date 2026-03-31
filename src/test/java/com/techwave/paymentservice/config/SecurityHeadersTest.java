package com.techwave.paymentservice.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Validates that OWASP-recommended security headers are present on responses.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityHeadersTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void response_containsXContentTypeOptionsNoSniff() throws Exception {
        mockMvc.perform(get("/countries"))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"));
    }

    @Test
    @WithMockUser
    void response_containsXXssProtection() throws Exception {
        mockMvc.perform(get("/countries"))
                .andExpect(header().exists("X-XSS-Protection"));
    }

    @Test
    @WithMockUser
    void response_containsCacheControlHeaders() throws Exception {
        mockMvc.perform(get("/countries"))
                .andExpect(header().exists("Cache-Control"));
    }

    @Test
    @WithMockUser
    void response_containsContentSecurityPolicy() throws Exception {
        mockMvc.perform(get("/countries"))
                .andExpect(header().exists("Content-Security-Policy"));
    }

    @Test
    @WithMockUser
    void response_containsReferrerPolicy() throws Exception {
        mockMvc.perform(get("/countries"))
                .andExpect(header().exists("Referrer-Policy"));
    }

    @Test
    void publicEndpoint_health_accessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk());
    }

    @Test
    void publicEndpoint_actuatorHealth_accessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    void protectedEndpoint_requiresAuth() throws Exception {
        mockMvc.perform(get("/countries"))
                .andExpect(status().isUnauthorized());
    }
}

