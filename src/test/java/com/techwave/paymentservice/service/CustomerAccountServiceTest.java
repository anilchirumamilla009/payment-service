package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.response.CustomerAccountResponse;
import com.techwave.paymentservice.dto.response.LegalEntityResponse;
import com.techwave.paymentservice.entity.CorporationEntity;
import com.techwave.paymentservice.entity.CustomerAccountBeneficialOwner;
import com.techwave.paymentservice.entity.CustomerAccountBeneficialOwnerId;
import com.techwave.paymentservice.entity.CustomerAccountEntity;
import com.techwave.paymentservice.entity.PersonEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.CustomerAccountMapper;
import com.techwave.paymentservice.repository.CustomerAccountBeneficialOwnerRepository;
import com.techwave.paymentservice.repository.CustomerAccountRepository;
import com.techwave.paymentservice.service.impl.CustomerAccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomerAccountService Tests")
class CustomerAccountServiceTest {
    
    @Mock
    private CustomerAccountRepository customerAccountRepository;
    
    @Mock
    private CustomerAccountBeneficialOwnerRepository beneficialOwnerRepository;
    
    @Mock
    private CustomerAccountMapper customerAccountMapper;
    
    @InjectMocks
    private CustomerAccountServiceImpl customerAccountService;
    
    private UUID customerAccountId;
    private UUID personId;
    private UUID corporationId;
    private CustomerAccountEntity customerAccountEntity;
    private CustomerAccountResponse customerAccountResponse;
    
    @BeforeEach
    void setUp() {
        customerAccountId = UUID.randomUUID();
        personId = UUID.randomUUID();
        corporationId = UUID.randomUUID();
        
        customerAccountEntity = new CustomerAccountEntity();
        customerAccountEntity.setId(customerAccountId);
        customerAccountEntity.setAccountNumber("ACC001");
        
        customerAccountResponse = new CustomerAccountResponse();
        customerAccountResponse.setId(customerAccountId);
        customerAccountResponse.setAccountNumber("ACC001");
    }
    
    @Nested
    @DisplayName("getCustomerAccount() Tests")
    class GetCustomerAccountTests {
        
        @Test
        @DisplayName("Should return customer account when found")
        void shouldGetCustomerAccount_Found() {
            // Given
            when(customerAccountRepository.findById(customerAccountId))
                    .thenReturn(Optional.of(customerAccountEntity));
            when(customerAccountMapper.toResponse(customerAccountEntity))
                    .thenReturn(customerAccountResponse);
            
            // When
            CustomerAccountResponse result = customerAccountService.getCustomerAccount(customerAccountId);
            
            // Then
            assertThat(result).isEqualTo(customerAccountResponse);
            verify(customerAccountRepository).findById(customerAccountId);
            verify(customerAccountMapper).toResponse(customerAccountEntity);
        }
        
        @Test
        @DisplayName("Should throw ResourceNotFoundException when account not found")
        void shouldThrowException_AccountNotFound() {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(customerAccountRepository.findById(nonExistentId)).thenReturn(Optional.empty());
            
            // When & Then
            assertThatThrownBy(() -> customerAccountService.getCustomerAccount(nonExistentId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Customer account not found");
            
            verify(customerAccountRepository).findById(nonExistentId);
            verify(customerAccountMapper, never()).toResponse(any());
        }
    }
    
    @Nested
    @DisplayName("getCustomerAccountBeneficialOwners() Tests")
    class GetCustomerAccountBeneficialOwnersTests {
        
        @Test
        @DisplayName("Should return beneficial owners with person resource type")
        void shouldReturnBeneficialOwners_WithPersons() {
            // Given
            PersonEntity owner1 = new PersonEntity();
            owner1.setId(personId);
            owner1.setFirstName("John");
            owner1.setLastName("Doe");
            
            PersonEntity owner2 = new PersonEntity();
            owner2.setId(UUID.randomUUID());
            owner2.setFirstName("Jane");
            owner2.setLastName("Smith");
            
            CustomerAccountBeneficialOwner beneficialOwner1 = new CustomerAccountBeneficialOwner();
            CustomerAccountBeneficialOwnerId id1 = new CustomerAccountBeneficialOwnerId();
            id1.setCustomerAccountId(customerAccountId);
            beneficialOwner1.setId(id1);
            beneficialOwner1.setLegalEntity(owner1);
            
            CustomerAccountBeneficialOwner beneficialOwner2 = new CustomerAccountBeneficialOwner();
            CustomerAccountBeneficialOwnerId id2 = new CustomerAccountBeneficialOwnerId();
            id2.setCustomerAccountId(customerAccountId);
            beneficialOwner2.setId(id2);
            beneficialOwner2.setLegalEntity(owner2);
            
            List<CustomerAccountBeneficialOwner> owners = Arrays.asList(beneficialOwner1, beneficialOwner2);
            
            when(customerAccountRepository.existsById(customerAccountId)).thenReturn(true);
            when(beneficialOwnerRepository.findByIdCustomerAccountId(customerAccountId)).thenReturn(owners);
            
            // When
            List<LegalEntityResponse> result = customerAccountService.getCustomerAccountBeneficialOwners(customerAccountId);
            
            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(response -> "people".equals(response.getResourceType()));
            assertThat(result.get(0).getId()).isEqualTo(personId);
            assertThat(result.get(0).getResourceType()).isEqualTo("people");
            verify(customerAccountRepository).existsById(customerAccountId);
            verify(beneficialOwnerRepository).findByIdCustomerAccountId(customerAccountId);
        }
        
        @Test
        @DisplayName("Should return beneficial owners with corporation resource type")
        void shouldReturnBeneficialOwners_WithCorporations() {
            // Given
            CorporationEntity corp1 = new CorporationEntity();
            corp1.setId(corporationId);
            corp1.setName("ACME Corp");
            
            CorporationEntity corp2 = new CorporationEntity();
            corp2.setId(UUID.randomUUID());
            corp2.setName("Tech Corp");
            
            CustomerAccountBeneficialOwner beneficialOwner1 = new CustomerAccountBeneficialOwner();
            CustomerAccountBeneficialOwnerId id1 = new CustomerAccountBeneficialOwnerId();
            id1.setCustomerAccountId(customerAccountId);
            beneficialOwner1.setId(id1);
            beneficialOwner1.setLegalEntity(corp1);
            
            CustomerAccountBeneficialOwner beneficialOwner2 = new CustomerAccountBeneficialOwner();
            CustomerAccountBeneficialOwnerId id2 = new CustomerAccountBeneficialOwnerId();
            id2.setCustomerAccountId(customerAccountId);
            beneficialOwner2.setId(id2);
            beneficialOwner2.setLegalEntity(corp2);
            
            List<CustomerAccountBeneficialOwner> owners = Arrays.asList(beneficialOwner1, beneficialOwner2);
            
            when(customerAccountRepository.existsById(customerAccountId)).thenReturn(true);
            when(beneficialOwnerRepository.findByIdCustomerAccountId(customerAccountId)).thenReturn(owners);
            
            // When
            List<LegalEntityResponse> result = customerAccountService.getCustomerAccountBeneficialOwners(customerAccountId);
            
            // Then
            assertThat(result).hasSize(2);
            assertThat(result).allMatch(response -> "corporations".equals(response.getResourceType()));
            assertThat(result.get(0).getId()).isEqualTo(corporationId);
            assertThat(result.get(0).getResourceType()).isEqualTo("corporations");
            verify(customerAccountRepository).existsById(customerAccountId);
            verify(beneficialOwnerRepository).findByIdCustomerAccountId(customerAccountId);
        }
        
        @Test
        @DisplayName("Should return mixed beneficial owners (persons and corporations)")
        void shouldReturnBeneficialOwners_Mixed() {
            // Given
            PersonEntity person = new PersonEntity();
            person.setId(personId);
            
            CorporationEntity corp = new CorporationEntity();
            corp.setId(corporationId);
            
            CustomerAccountBeneficialOwner beneficialOwner1 = new CustomerAccountBeneficialOwner();
            CustomerAccountBeneficialOwnerId id1 = new CustomerAccountBeneficialOwnerId();
            id1.setCustomerAccountId(customerAccountId);
            beneficialOwner1.setId(id1);
            beneficialOwner1.setLegalEntity(person);
            
            CustomerAccountBeneficialOwner beneficialOwner2 = new CustomerAccountBeneficialOwner();
            CustomerAccountBeneficialOwnerId id2 = new CustomerAccountBeneficialOwnerId();
            id2.setCustomerAccountId(customerAccountId);
            beneficialOwner2.setId(id2);
            beneficialOwner2.setLegalEntity(corp);
            
            List<CustomerAccountBeneficialOwner> owners = Arrays.asList(beneficialOwner1, beneficialOwner2);
            
            when(customerAccountRepository.existsById(customerAccountId)).thenReturn(true);
            when(beneficialOwnerRepository.findByIdCustomerAccountId(customerAccountId)).thenReturn(owners);
            
            // When
            List<LegalEntityResponse> result = customerAccountService.getCustomerAccountBeneficialOwners(customerAccountId);
            
            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getResourceType()).isEqualTo("people");
            assertThat(result.get(1).getResourceType()).isEqualTo("corporations");
            verify(beneficialOwnerRepository).findByIdCustomerAccountId(customerAccountId);
        }
        
        @Test
        @DisplayName("Should return empty list when no beneficial owners exist")
        void shouldReturnEmpty_NoBeneficialOwners() {
            // Given
            when(customerAccountRepository.existsById(customerAccountId)).thenReturn(true);
            when(beneficialOwnerRepository.findByIdCustomerAccountId(customerAccountId))
                    .thenReturn(Collections.emptyList());
            
            // When
            List<LegalEntityResponse> result = customerAccountService.getCustomerAccountBeneficialOwners(customerAccountId);
            
            // Then
            assertThat(result).isEmpty();
            verify(beneficialOwnerRepository).findByIdCustomerAccountId(customerAccountId);
        }
        
        @Test
        @DisplayName("Should throw ResourceNotFoundException when account doesn't exist")
        void shouldThrowException_AccountNotFound() {
            // Given
            UUID nonExistentId = UUID.randomUUID();
            when(customerAccountRepository.existsById(nonExistentId)).thenReturn(false);
            
            // When & Then
            assertThatThrownBy(() -> customerAccountService.getCustomerAccountBeneficialOwners(nonExistentId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Customer account not found");
            
            verify(beneficialOwnerRepository, never()).findByIdCustomerAccountId(any());
        }
    }
}
