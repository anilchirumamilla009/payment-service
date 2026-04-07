package com.techwave.paymentservice.controller;

import com.techwave.paymentservice.dto.BankAccountAuditDto;
import com.techwave.paymentservice.dto.BankAccountDto;
import com.techwave.paymentservice.dto.LegalEntityDto;
import com.techwave.paymentservice.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BankAccountsController Unit Tests")
class BankAccountsControllerTest {

    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private BankAccountsController controller;

    private UUID accountId;
    private BankAccountDto dto;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        dto = new BankAccountDto();
        dto.setId(accountId);
        dto.setIban("DE89370400440532013000");
        dto.setBic("COBADEFFXXX");
        dto.setBeneficiary("Test Beneficiary");
        dto.setCurrency("EUR");
        dto.setCountry("DE");
    }

    @Test
    @DisplayName("createBankAccount returns 201 CREATED")
    void createBankAccount_returns201() {
        when(bankAccountService.createOrLocateBankAccount(dto))
                .thenReturn(dto);

        ResponseEntity<BankAccountDto> response =
                controller.createBankAccount(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(accountId, response.getBody().getId());
        verify(bankAccountService).createOrLocateBankAccount(dto);
    }

    @Test
    @DisplayName("getBankAccount returns 200 OK")
    void getBankAccount_returns200() {
        when(bankAccountService.getBankAccountById(accountId))
                .thenReturn(dto);

        ResponseEntity<BankAccountDto> response =
                controller.getBankAccount(accountId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(accountId, response.getBody().getId());
    }

    @Test
    @DisplayName("getBankAccountAuditTrail returns 200 OK")
    void getBankAccountAuditTrail_returns200() {
        BankAccountAuditDto auditDto = new BankAccountAuditDto();
        auditDto.setResource(accountId);
        auditDto.setVersion(1);

        when(bankAccountService.getAuditTrail(accountId))
                .thenReturn(List.of(auditDto));

        ResponseEntity<List<BankAccountAuditDto>> response =
                controller.getBankAccountAuditTrail(accountId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("getBankAccountBeneficialOwners returns 200 OK")
    void getBankAccountBeneficialOwners_returns200() {
        UUID ownerId = UUID.randomUUID();
        LegalEntityDto ownerDto = new LegalEntityDto(ownerId, "people");

        when(bankAccountService.getBeneficialOwners(accountId))
                .thenReturn(List.of(ownerDto));

        ResponseEntity<List<LegalEntityDto>> response =
                controller.getBankAccountBeneficialOwners(accountId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(ownerId, response.getBody().get(0).getId());
    }
}

