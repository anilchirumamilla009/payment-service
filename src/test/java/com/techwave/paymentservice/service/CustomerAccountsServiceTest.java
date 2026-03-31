package com.techwave.paymentservice.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.model.CustomerAccount;
import com.techwave.paymentservice.model.LegalEntity;

@SpringBootTest
class CustomerAccountsServiceTest {

    private static final UUID EXISTING_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");
    private static final UUID NON_EXISTING_ID = UUID.fromString("99999999-9999-9999-9999-999999999999");

    @Autowired
    private CustomerAccountsService customerAccountsService;

    @Test
    void getCustomerAccount_existingId_returnsAccount() {
        CustomerAccount ca = customerAccountsService.getCustomerAccount(EXISTING_ID);
        assertNotNull(ca);
        assertEquals(EXISTING_ID, ca.getId());
        assertEquals("customer-accounts", ca.getResourceType());
        assertEquals("Cornerstone Principal Account", ca.getName());
    }

    @Test
    void getCustomerAccount_nonExistingId_throwsNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> customerAccountsService.getCustomerAccount(NON_EXISTING_ID));
    }

    @Test
    void getCustomerAccountBeneficialOwners_existingId_returnsOwners() {
        List<LegalEntity> owners = customerAccountsService.getCustomerAccountBeneficialOwners(EXISTING_ID);
        assertNotNull(owners);
        assertFalse(owners.isEmpty());
    }

    @Test
    void getCustomerAccountBeneficialOwners_nonExistingId_throwsNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> customerAccountsService.getCustomerAccountBeneficialOwners(NON_EXISTING_ID));
    }
}

