package com.techwave.paymentservice.controller;

import com.techwave.paymentservice.dto.CustomerAccountDto;
import com.techwave.paymentservice.dto.LegalEntityDto;
import com.techwave.paymentservice.service.CustomerAccountService;
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
@DisplayName("CustomerAccountsController Unit Tests")
class CustomerAccountsControllerTest {

    @Mock
    private CustomerAccountService customerAccountService;

    @InjectMocks
    private CustomerAccountsController controller;

    private UUID accountId;
    private CustomerAccountDto dto;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        dto = new CustomerAccountDto();
        dto.setId(accountId);
        dto.setName("Test Account");
        dto.setAccountType("PERSONAL");
        dto.setAccountState("ACCEPTED");
    }

    @Test
    @DisplayName("getCustomerAccount returns 200 OK")
    void getCustomerAccount_returns200() {
        when(customerAccountService.getCustomerAccountById(accountId))
                .thenReturn(dto);

        ResponseEntity<CustomerAccountDto> response =
                controller.getCustomerAccount(accountId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(accountId, response.getBody().getId());
    }

    @Test
    @DisplayName("getCustomerAccountBeneficialOwners returns 200 OK")
    void getCustomerAccountBeneficialOwners_returns200() {
        UUID ownerId = UUID.randomUUID();
        LegalEntityDto ownerDto =
                new LegalEntityDto(ownerId, "people");

        when(customerAccountService.getBeneficialOwners(accountId))
                .thenReturn(List.of(ownerDto));

        ResponseEntity<List<LegalEntityDto>> response =
                controller.getCustomerAccountBeneficialOwners(accountId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(ownerId, response.getBody().get(0).getId());
    }
}

