package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.request.BankAccountRequest;
import com.techwave.paymentservice.dto.response.BankAccountAuditResponse;
import com.techwave.paymentservice.dto.response.BankAccountResponse;
import com.techwave.paymentservice.dto.response.LegalEntityResponse;
import com.techwave.paymentservice.entity.BankAccountAudit;
import com.techwave.paymentservice.entity.BankAccountBeneficialOwner;
import com.techwave.paymentservice.entity.BankAccountBeneficialOwnerId;
import com.techwave.paymentservice.entity.BankAccountEntity;
import com.techwave.paymentservice.entity.CorporationEntity;
import com.techwave.paymentservice.entity.PersonEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.BankAccountMapper;
import com.techwave.paymentservice.repository.BankAccountAuditRepository;
import com.techwave.paymentservice.repository.BankAccountBeneficialOwnerRepository;
import com.techwave.paymentservice.repository.BankAccountRepository;
import com.techwave.paymentservice.service.impl.BankAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BankAccountService Tests")
class BankAccountServiceTest {
    
    @Mock
    private BankAccountRepository bankAccountRepository;
    
    @Mock
    private BankAccountAuditRepository bankAccountAuditRepository;
    
    @Mock
    private BankAccountBeneficialOwnerRepository beneficialOwnerRepository;
    
    @Mock
    private BankAccountMapper bankAccountMapper;
    
    @InjectMocks
    private BankAccountServiceImpl bankAccountService;
    
    private UUID bankAccountId;
    private UUID personId;
    private UUID corporationId;
    private BankAccountEntity bankAccountEntity;
    private BankAccountResponse bankAccountResponse;
    private BankAccountRequest bankAccountRequest;
    
    @BeforeEach
    void setUp() {
        bankAccountId = UUID.randomUUID();
        personId = UUID.randomUUID();
        corporationId = UUID.randomUUID();
        
        bankAccountEntity = new BankAccountEntity();
        bankAccountEntity.setId(bankAccountId);
        bankAccountEntity.setIban("DE89370400440532013000");
        bankAccountEntity.setBic("COBADEHHXXX");
        bankAccountEntity.setBeneficiary("John Doe");
        bankAccountEntity.setBeneficiaryAddress("123 Main St");
        bankAccountEntity.setNickname("Primary Account");
        bankAccountEntity.setCountry("DE");
        bankAccountEntity.setCurrency("EUR");
        bankAccountEntity.setVersion(1L);
        
        bankAccountResponse = new BankAccountResponse();
        bankAccountResponse.setId(bankAccountId);
        bankAccountResponse.setIban("DE89370400440532013000");
        bankAccountResponse.setBeneficiary("John Doe");
        bankAccountResponse.setCountry("DE");
        bankAccountResponse.setCurrency("EUR");
        
        bankAccountRequest = new BankAccountRequest();
        bankAccountRequest.setIban("DE89370400440532013000");
        bankAccountRequest.setBeneficiary("John Doe");
        bankAccountRequest.setBeneficiaryAddress("123 Main St");
        bankAccountRequest.setCountry("DE");
        bankAccountRequest.setCurrency("EUR");
    }
    
    @Nested
    @DisplayName("createOrLocateBankAccount() Tests")
    class CreateOrLocateBankAccountTests {
        
        @Test
        @DisplayName("Should return existing account when IBAN exists (idempotent)")
        void shouldReturnExisting_IBANExists() {
            // Given
            when(bankAccountRepository.findByIban("DE89370400440532013000"))
                    .thenReturn(Optional.of(bankAccountEntity));
            when(bankAccountMapper.toResponse(bankAccountEntity)).thenReturn(bankAccountResponse);
            
            // When
            BankAccountResponse result = bankAccountService.createOrLocateBankAccount(bankAccountRequest);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(bankAccountId);
            verify(bankAccountRepository).findByIban("DE89370400440532013000");
            verify(bankAccountRepository, never()).save(any());
            verify(bankAccountAuditRepository, never()).save(any());
        }
        
        @Test
        @DisplayName("Should create new account when IBAN does not exist")
        void shouldCreateNewAccount_IBANNotExists() {
            // Given
            when(bankAccountRepository.findByIban("DE89370400440532013000")).thenReturn(Optional.empty());
            when(bankAccountMapper.toEntity(bankAccountRequest)).thenReturn(bankAccountEntity);
            when(bankAccountRepository.save(bankAccountEntity)).thenReturn(bankAccountEntity);
            when(bankAccountMapper.toResponse(bankAccountEntity)).thenReturn(bankAccountResponse);
            when(bankAccountAuditRepository.save(any(BankAccountAudit.class))).thenReturn(any());
            
            // When
            BankAccountResponse result = bankAccountService.createOrLocateBankAccount(bankAccountRequest);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(bankAccountId);
            verify(bankAccountRepository).findByIban("DE89370400440532013000");
            verify(bankAccountRepository).save(bankAccountEntity);
            verify(bankAccountAuditRepository).save(any(BankAccountAudit.class));
        }
    }
    
    @Nested
    @DisplayName("getBankAccount() Tests")
    class GetBankAccountTests {
        
        @Test
        @DisplayName("Should return bank account when found")
        void shouldGetBankAccount_Found() {
            // Given
            when(bankAccountRepository.findById(bankAccountId)).thenReturn(Optional.of(bankAccountEntity));
            when(bankAccountMapper.toResponse(bankAccountEntity)).thenReturn(bankAccountResponse);
            
            // When
            BankAccountResponse result = bankAccountService.getBankAccount(bankAccountId);
            
            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(bankAccountId);
            verify(bankAccountRepository).findById(bankAccountId);
        }
        
        @Test
        @DisplayName("Should throw ResourceNotFoundException when not found")
        void shouldThrowException_NotFound() {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(bankAccountRepository.findById(nonExistentId)).thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> bankAccountService.getBankAccount(nonExistentId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Bank account not found");
        }
    }
    
    @Nested
    @DisplayName("getBankAccountAuditTrail() Tests")
    class GetBankAccountAuditTrailTests {
        
        @Test
        @DisplayName("Should return audit trail in descending version order")
        void shouldReturnAuditTrail_Success() {
            // Given
            BankAccountAudit audit1 = new BankAccountAudit(bankAccountId, 1L, "John Doe", "123 Main St", 
                    "Primary", "DE89370400440532013000", "COBADEHHXXX", "12345678", 
                    "37040044", "053", "ABC123", "EUR", "DE");
            BankAccountAudit audit2 = new BankAccountAudit(bankAccountId, 2L, "Jane Doe", "456 Oak Ave", 
                    "Primary Updated", "DE89370400440532013000", "COBADEHHXXX", "12345678", 
                    "37040044", "053", "ABC123", "EUR", "DE");
            
            List<BankAccountAudit> audits = Arrays.asList(audit2, audit1);
            
            BankAccountAuditResponse auditResponse1 = new BankAccountAuditResponse();
            BankAccountAuditResponse auditResponse2 = new BankAccountAuditResponse();
            
            when(bankAccountRepository.existsById(bankAccountId)).thenReturn(true);
            when(bankAccountAuditRepository.findByResourceOrderByVersionDesc(bankAccountId)).thenReturn(audits);
            when(bankAccountMapper.toAuditResponses(audits)).thenReturn(Arrays.asList(auditResponse2, auditResponse1));
            
            // When
            List<BankAccountAuditResponse> result = bankAccountService.getBankAccountAuditTrail(bankAccountId);
            
            // Then
            assertThat(result).hasSize(2);
            verify(bankAccountRepository).existsById(bankAccountId);
            verify(bankAccountAuditRepository).findByResourceOrderByVersionDesc(bankAccountId);
        }
        
        @Test
        @DisplayName("Should throw ResourceNotFoundException when account not found")
        void shouldThrowException_AccountNotFound() {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(bankAccountRepository.existsById(nonExistentId)).thenReturn(false);
            
            // When & Then
            assertThatThrownBy(() -> bankAccountService.getBankAccountAuditTrail(nonExistentId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Bank account not found");
            
            verify(bankAccountAuditRepository, never()).findByResourceOrderByVersionDesc(any());
        }
    }
    
    @Nested
    @DisplayName("getBankAccountBeneficialOwners() Tests")
    class GetBankAccountBeneficialOwnersTests {
        
        @Test
        @DisplayName("Should return beneficial owners with correct resource types")
        void shouldReturnBeneficialOwners_Success() {
            // Given
            PersonEntity personEntity = new PersonEntity("John", "Doe");
            personEntity.setId(personId);
            
            CorporationEntity corporationEntity = new CorporationEntity();
            corporationEntity.setId(corporationId);
            corporationEntity.setName("ACME Corp");
            
            BankAccountBeneficialOwnerId ownerId1 = new BankAccountBeneficialOwnerId(bankAccountId, personId);
            BankAccountBeneficialOwnerId ownerId2 = new BankAccountBeneficialOwnerId(bankAccountId, corporationId);
            
            BankAccountBeneficialOwner owner1 = new BankAccountBeneficialOwner();
            owner1.setId(ownerId1);
            owner1.setLegalEntity(personEntity);
            
            BankAccountBeneficialOwner owner2 = new BankAccountBeneficialOwner();
            owner2.setId(ownerId2);
            owner2.setLegalEntity(corporationEntity);
            
            List<BankAccountBeneficialOwner> owners = Arrays.asList(owner1, owner2);
            
            when(bankAccountRepository.existsById(bankAccountId)).thenReturn(true);
            when(beneficialOwnerRepository.findByIdBankAccountId(bankAccountId)).thenReturn(owners);
            
            // When
            List<LegalEntityResponse> result = bankAccountService.getBankAccountBeneficialOwners(bankAccountId);
            
            // Then
            assertThat(result).hasSize(2);
            assertThat(result)
                    .anyMatch(r -> r.getResourceType().equals("people") && r.getId().equals(personId))
                    .anyMatch(r -> r.getResourceType().equals("corporations") && r.getId().equals(corporationId));
            verify(bankAccountRepository).existsById(bankAccountId);
        }
        
        @Test
        @DisplayName("Should return empty list when no beneficial owners")
        void shouldReturnEmptyList_NoBeneficialOwners() {
            // Given
            when(bankAccountRepository.existsById(bankAccountId)).thenReturn(true);
            when(beneficialOwnerRepository.findByIdBankAccountId(bankAccountId)).thenReturn(Collections.emptyList());
            
            // When
            List<LegalEntityResponse> result = bankAccountService.getBankAccountBeneficialOwners(bankAccountId);
            
            // Then
            assertThat(result).isEmpty();
        }
        
        @Test
        @DisplayName("Should throw ResourceNotFoundException when account not found")
        void shouldThrowException_AccountNotFound() {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(bankAccountRepository.existsById(nonExistentId)).thenReturn(false);
            
            // When & Then
            assertThatThrownBy(() -> bankAccountService.getBankAccountBeneficialOwners(nonExistentId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Bank account not found");
            
            verify(beneficialOwnerRepository, never()).findByIdBankAccountId(any());
        }
    }
}
