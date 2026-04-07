package com.techwave.paymentservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CorrelationIdFilter Unit Tests")
class CorrelationIdFilterTest {

    private CorrelationIdFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        filter = new CorrelationIdFilter();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("Uses provided X-Correlation-ID header")
    void usesProvidedCorrelationId() throws ServletException, IOException {
        String providedId = "test-correlation-id-123";
        request.addHeader("X-Correlation-ID", providedId);

        AtomicReference<String> capturedMdc = new AtomicReference<>();
        FilterChain chain = (req, res) ->
                capturedMdc.set(MDC.get("correlationId"));

        filter.doFilterInternal(request, response, chain);

        assertEquals(providedId, capturedMdc.get());
        assertEquals(providedId,
                response.getHeader("X-Correlation-ID"));
        // MDC should be cleared after filter
        assertNull(MDC.get("correlationId"));
    }

    @Test
    @DisplayName("Generates new UUID when no header provided")
    void generatesNewCorrelationId() throws ServletException, IOException {
        AtomicReference<String> capturedMdc = new AtomicReference<>();
        FilterChain chain = (req, res) ->
                capturedMdc.set(MDC.get("correlationId"));

        filter.doFilterInternal(request, response, chain);

        assertNotNull(capturedMdc.get());
        assertFalse(capturedMdc.get().isEmpty());
        assertEquals(capturedMdc.get(),
                response.getHeader("X-Correlation-ID"));
        assertNull(MDC.get("correlationId"));
    }

    @Test
    @DisplayName("Generates new UUID when header is blank")
    void generatesNewCorrelationIdWhenBlank()
            throws ServletException, IOException {
        request.addHeader("X-Correlation-ID", "   ");

        AtomicReference<String> capturedMdc = new AtomicReference<>();
        FilterChain chain = (req, res) ->
                capturedMdc.set(MDC.get("correlationId"));

        filter.doFilterInternal(request, response, chain);

        assertNotNull(capturedMdc.get());
        assertFalse(capturedMdc.get().isBlank());
        assertNull(MDC.get("correlationId"));
    }

    @Test
    @DisplayName("MDC is cleared even when filter chain throws")
    void mdcClearedOnException() {
        FilterChain chain = (req, res) -> {
            throw new ServletException("Test exception");
        };

        assertThrows(ServletException.class, () ->
                filter.doFilterInternal(request, response, chain));

        assertNull(MDC.get("correlationId"));
    }
}


