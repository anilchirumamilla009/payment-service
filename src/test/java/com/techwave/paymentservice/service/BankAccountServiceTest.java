package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.BankAccountDto;
import com.techwave.paymentservice.dto.LegalEntityDto;
import com.techwave.paymentservice.entity.BankAccountEntity;
import com.techwave.paymentservice.entity.PersonEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.BankAccountMapper;
import com.techwave.paymentservice.repository.BankAccountAuditRepository;
import com.techwave.paymentservice.repository.BankAccountRepository;
import com.techwave.paymentservice.service.impl.BankAccountServiceImpl;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BankAccountService Unit Tests")
class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankAccountAuditRepository bankAccountAuditRepository;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    private UUID accountId;
    private BankAccountEntity entity;
    private BankAccountDto dto;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();

        entity = new BankAccountEntity();
        entity.setId(accountId);
        entity.setIban("DE89370400440532013000");
        entity.setBic("COBADEFFXXX");
        entity.setCurrency("EUR");
        entity.setCountry("DE");
        entity.setVersion(1);

        dto = new BankAccountDto();
        dto.setId(accountId);
        dto.setIban("DE89370400440532013000");
        dto.setBic("COBADEFFXXX");
        dto.setCurrency("EUR");
        dto.setCountry("DE");
    }

    @Test
    @DisplayName("createOrLocate creates new when IBAN not found")
    void createOrLocate_createsNew() {
        BankAccountEntity newEntity = new BankAccountEntity();
        when(bankAccountRepository.findByIban(dto.getIban()))
                .thenReturn(Optional.empty());
        when(bankAccountMapper.toEntity(dto)).thenReturn(newEntity);
        when(bankAccountRepository.save(any()))
                .thenReturn(entity);
        when(bankAccountMapper.toDto(entity)).thenReturn(dto);

        BankAccountDto result =
                bankAccountService.createOrLocateBankAccount(dto);

        assertNotNull(result);
        assertEquals("DE89370400440532013000", result.getIban());
        verify(bankAccountRepository).save(any());
        verify(bankAccountAuditRepository).save(any());
    }

    @Test
    @DisplayName("createOrLocate returns existing when IBAN found")
    void createOrLocate_returnsExisting() {
        when(bankAccountRepository.findByIban(dto.getIban()))
                .thenReturn(Optional.of(entity));
        when(bankAccountMapper.toDto(entity)).thenReturn(dto);

        BankAccountDto result =
                bankAccountService.createOrLocateBankAccount(dto);

        assertNotNull(result);
        verify(bankAccountRepository, never()).save(any());
    }

    @Test
    @DisplayName("getBankAccountById returns DTO when found")
    void getBankAccountById_found() {
        when(bankAccountRepository.findById(accountId))
                .thenReturn(Optional.of(entity));
        when(bankAccountMapper.toDto(entity)).thenReturn(dto);

        BankAccountDto result =
                bankAccountService.getBankAccountById(accountId);

        assertEquals(accountId, result.getId());
    }

    @Test
    @DisplayName("getBankAccountById throws when not found")
    void getBankAccountById_notFound() {
        UUID id = UUID.randomUUID();
        when(bankAccountRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> bankAccountService.getBankAccountById(id));
    }

    @Test
    @DisplayName("getBeneficialOwners returns list")
    void getBeneficialOwners_success() {
        PersonEntity owner = new PersonEntity();
        UUID ownerId = UUID.randomUUID();
        owner.setId(ownerId);
        entity.setBeneficialOwners(Set.of(owner));

        LegalEntityDto ownerDto =
                new LegalEntityDto(ownerId, "people");

        when(bankAccountRepository.findById(accountId))
                .thenReturn(Optional.of(entity));
        when(bankAccountMapper.toLegalEntityDtoList(Set.of(owner)))
                .thenReturn(List.of(ownerDto));

        List<LegalEntityDto> result =
                bankAccountService.getBeneficialOwners(accountId);

        assertEquals(1, result.size());
        assertEquals(ownerId, result.get(0).getId());
    }
}

