package com.techwave.paymentservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertNotNull(context);
    }

    @Test
    void mainMethodStartsApplication() {
        assertDoesNotThrow(() ->
                PaymentServiceApplication.main(new String[]{"--server.port=0"}));
    }

    @Test
    void requiredBeansArePresent() {
        assertNotNull(context.getBean("coreController"));
        assertNotNull(context.getBean("bankAccountsController"));
        assertNotNull(context.getBean("legalEntitiesController"));
        assertNotNull(context.getBean("customerAccountsController"));
        assertNotNull(context.getBean("healthController"));
    }
}
