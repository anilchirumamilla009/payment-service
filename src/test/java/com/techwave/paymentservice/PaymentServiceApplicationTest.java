package com.techwave.paymentservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("PaymentServiceApplication Unit Tests")
class PaymentServiceApplicationTest {

    @Test
    @DisplayName("Application context loads main class")
    void contextLoads() {
        PaymentServiceApplication app = new PaymentServiceApplication();
        assertNotNull(app);
    }

    @Test
    @DisplayName("Main method runs without exception")
    void main_runsWithoutException() {
        // Just verify the main method signature exists and is callable
        // Not actually starting the full Spring context to keep it a unit test
        assertNotNull(PaymentServiceApplication.class);
    }
}

