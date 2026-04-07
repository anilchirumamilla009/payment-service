package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.CustomerAccountDto;
import com.techwave.paymentservice.dto.LegalEntityDto;
import com.techwave.paymentservice.entity.CustomerAccountEntity;
import com.techwave.paymentservice.entity.PersonEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.CustomerAccountMapper;
import com.techwave.paymentservice.repository.CustomerAccountRepository;
import com.techwave.paymentservice.service.impl.CustomerAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerAccountService Unit Tests")
class CustomerAccountServiceTest {

    @Mock
    private CustomerAccountRepository customerAccountRepository;

    @Mock
    private CustomerAccountMapper customerAccountMapper;

    @InjectMocks
    private CustomerAccountServiceImpl customerAccountService;

    private UUID accountId;
    private CustomerAccountEntity entity;
    private CustomerAccountDto dto;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();

        entity = new CustomerAccountEntity();
        entity.setId(accountId);
        entity.setName("Test Account");
        entity.setDescription("Test description");
        entity.setAccountType(
                CustomerAccountEntity.CustomerAccountType.PERSONAL);
        entity.setAccountState(
                CustomerAccountEntity.CustomerAccountState.ACCEPTED);

        dto = new CustomerAccountDto();
        dto.setId(accountId);
        dto.setName("Test Account");
        dto.setDescription("Test description");
        dto.setAccountType("PERSONAL");
        dto.setAccountState("ACCEPTED");
    }

    @Test
    @DisplayName("getCustomerAccountById returns DTO when found")
    void getCustomerAccountById_found() {
        when(customerAccountRepository.findById(accountId))
                .thenReturn(Optional.of(entity));
        when(customerAccountMapper.toDto(entity)).thenReturn(dto);

        CustomerAccountDto result =
                customerAccountService.getCustomerAccountById(accountId);

        assertNotNull(result);
        assertEquals(accountId, result.getId());
        assertEquals("Test Account", result.getName());
        verify(customerAccountRepository).findById(accountId);
    }

    @Test
    @DisplayName("getCustomerAccountById throws when not found")
    void getCustomerAccountById_notFound() {
        UUID id = UUID.randomUUID();
        when(customerAccountRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> customerAccountService.getCustomerAccountById(id));
    }

    @Test
    @DisplayName("getBeneficialOwners returns list of owners")
    void getBeneficialOwners_success() {
        PersonEntity owner = new PersonEntity();
        UUID ownerId = UUID.randomUUID();
        owner.setId(ownerId);
        entity.setBeneficialOwners(Set.of(owner));

        LegalEntityDto ownerDto = new LegalEntityDto(ownerId, "people");

        when(customerAccountRepository.findById(accountId))
                .thenReturn(Optional.of(entity));
        when(customerAccountMapper.toLegalEntityDtoList(Set.of(owner)))
                .thenReturn(List.of(ownerDto));

        List<LegalEntityDto> result =
                customerAccountService.getBeneficialOwners(accountId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ownerId, result.get(0).getId());
    }

    @Test
    @DisplayName("getBeneficialOwners throws when account not found")
    void getBeneficialOwners_accountNotFound() {
        UUID id = UUID.randomUUID();
        when(customerAccountRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> customerAccountService.getBeneficialOwners(id));
    }

    @Test
    @DisplayName("getBeneficialOwners returns empty list when no owners")
    void getBeneficialOwners_emptyOwners() {
        entity.setBeneficialOwners(Set.of());
        when(customerAccountRepository.findById(accountId))
                .thenReturn(Optional.of(entity));
        when(customerAccountMapper.toLegalEntityDtoList(Set.of()))
                .thenReturn(List.of());

        List<LegalEntityDto> result =
                customerAccountService.getBeneficialOwners(accountId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}

