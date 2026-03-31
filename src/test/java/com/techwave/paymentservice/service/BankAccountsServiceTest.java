package com.techwave.paymentservice.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.techwave.paymentservice.exception.BadRequestException;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.model.BankAccount;
import com.techwave.paymentservice.model.BankAccountAudit;
import com.techwave.paymentservice.model.LegalEntity;

@SpringBootTest
class BankAccountsServiceTest {

    private static final UUID EXISTING_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID NON_EXISTING_ID = UUID.fromString("99999999-9999-9999-9999-999999999999");

    @Autowired
    private BankAccountsService bankAccountsService;

    @Test
    void getBankAccount_existingId_returnsBankAccount() {
        BankAccount ba = bankAccountsService.getBankAccount(EXISTING_ID);
        assertNotNull(ba);
        assertEquals(EXISTING_ID, ba.getId());
        assertEquals("bank-accounts", ba.getResourceType());
        assertEquals("Cornerstone FX Ltd", ba.getBeneficiary());
    }

    @Test
    void getBankAccount_nonExistingId_throwsNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> bankAccountsService.getBankAccount(NON_EXISTING_ID));
    }

    @Test
    void createBankAccount_validPayload_createsSuccessfully() {
        BankAccount ba = new BankAccount();
        ba.setBeneficiary("New Beneficiary");
        ba.setCurrency("USD");
        ba.setCountry("US");

        BankAccount result = bankAccountsService.createBankAccount(ba);
        assertNotNull(result.getId());
        assertEquals("bank-accounts", result.getResourceType());
        assertEquals("New Beneficiary", result.getBeneficiary());
    }

    @Test
    void createBankAccount_missingBeneficiary_throwsBadRequest() {
        BankAccount ba = new BankAccount();
        ba.setCurrency("USD");
        ba.setCountry("US");
        assertThrows(BadRequestException.class, () -> bankAccountsService.createBankAccount(ba));
    }

    @Test
    void createBankAccount_missingCurrency_throwsBadRequest() {
        BankAccount ba = new BankAccount();
        ba.setBeneficiary("Test");
        ba.setCountry("US");
        assertThrows(BadRequestException.class, () -> bankAccountsService.createBankAccount(ba));
    }

    @Test
    void createBankAccount_missingCountry_throwsBadRequest() {
        BankAccount ba = new BankAccount();
        ba.setBeneficiary("Test");
        ba.setCurrency("USD");
        assertThrows(BadRequestException.class, () -> bankAccountsService.createBankAccount(ba));
    }

    @Test
    void createBankAccount_nullPayload_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> bankAccountsService.createBankAccount(null));
    }

    @Test
    void getBankAccountAuditTrail_existingId_returnsAudits() {
        List<BankAccountAudit> audits = bankAccountsService.getBankAccountAuditTrail(EXISTING_ID);
        assertNotNull(audits);
        assertFalse(audits.isEmpty());
        assertEquals(EXISTING_ID, audits.get(0).getResource());
    }

    @Test
    void getBankAccountAuditTrail_nonExistingId_throwsNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> bankAccountsService.getBankAccountAuditTrail(NON_EXISTING_ID));
    }

    @Test
    void getBankAccountBeneficialOwners_existingId_returnsList() {
        List<LegalEntity> owners = bankAccountsService.getBankAccountBeneficialOwners(EXISTING_ID);
        assertNotNull(owners);
        assertFalse(owners.isEmpty());
    }

    @Test
    void getBankAccountBeneficialOwners_nonExistingId_throwsNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> bankAccountsService.getBankAccountBeneficialOwners(NON_EXISTING_ID));
    }
}

