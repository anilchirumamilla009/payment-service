package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.CustomerAccountEntity;
import com.techwave.paymentservice.entity.CustomerAccountState;
import com.techwave.paymentservice.entity.CustomerAccountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("CustomerAccountRepository Integration Tests")
class CustomerAccountRepositoryTest {
    
    @Autowired
    private CustomerAccountRepository customerAccountRepository;
    
    private CustomerAccountEntity customerAccount1;
    private CustomerAccountEntity customerAccount2;
    
    @BeforeEach
    void setUp() {
        customerAccount1 = new CustomerAccountEntity();
        customerAccount1.setName("Premium Account");
        customerAccount1.setDescription("Premium customer account");
        customerAccount1.setAccountType(CustomerAccountType.PREMIUM);
        customerAccount1.setAccountState(CustomerAccountState.ACTIVE);
        customerAccount1.setSilo("silo1");
        
        customerAccount2 = new CustomerAccountEntity();
        customerAccount2.setName("Standard Account");
        customerAccount2.setDescription("Standard customer account");
        customerAccount2.setAccountType(CustomerAccountType.STANDARD);
        customerAccount2.setAccountState(CustomerAccountState.ACTIVE);
        customerAccount2.setSilo("silo2");
    }
    
    @Nested
    @DisplayName("save() Tests")
    class SaveTests {
        
        @Test
        @DisplayName("Should save customer account successfully")
        void shouldSaveCustomerAccount() {
            // When
            CustomerAccountEntity saved = customerAccountRepository.save(customerAccount1);
            
            // Then
            assertThat(saved).isNotNull();
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getName()).isEqualTo("Premium Account");
            assertThat(saved.getAccountType()).isEqualTo(CustomerAccountType.PREMIUM);
            assertThat(saved.getAccountState()).isEqualTo(CustomerAccountState.ACTIVE);
            assertThat(saved.getAccountCreationTime()).isNotNull();
        }
    }
    
    @Nested
    @DisplayName("findById() Tests")
    class FindByIdTests {
        
        @Test
        @DisplayName("Should find customer account by ID")
        void shouldFindCustomerAccount_ById() {
            // Given
            CustomerAccountEntity saved = customerAccountRepository.save(customerAccount1);
            
            // When
            Optional<CustomerAccountEntity> found = customerAccountRepository.findById(saved.getId());
            
            // Then
            assertThat(found).isPresent();
            assertThat(found.get().getName()).isEqualTo("Premium Account");
            assertThat(found.get().getSilo()).isEqualTo("silo1");
        }
        
        @Test
        @DisplayName("Should return empty when account not found")
        void shouldReturnEmpty_NotFound() {
            // When
            Optional<CustomerAccountEntity> found = customerAccountRepository.findById(UUID.randomUUID());
            
            // Then
            assertThat(found).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("findAll() Tests")
    class FindAllTests {
        
        @Test
        @DisplayName("Should return all customer accounts")
        void shouldReturnAllAccounts() {
            // Given
            customerAccountRepository.save(customerAccount1);
            customerAccountRepository.save(customerAccount2);
            
            // When
            long count = customerAccountRepository.count();
            
            // Then
            assertThat(count).isEqualTo(2);
        }
    }
    
    @Nested
    @DisplayName("exists() Tests")
    class ExistsTests {
        
        @Test
        @DisplayName("Should return true when account exists")
        void shouldReturnTrue_AccountExists() {
            // Given
            CustomerAccountEntity saved = customerAccountRepository.save(customerAccount1);
            
            // When
            boolean exists = customerAccountRepository.existsById(saved.getId());
            
            // Then
            assertThat(exists).isTrue();
        }
        
        @Test
        @DisplayName("Should return false when account not exists")
        void shouldReturnFalse_AccountNotExists() {
            // When
            boolean exists = customerAccountRepository.existsById(UUID.randomUUID());
            
            // Then
            assertThat(exists).isFalse();
        }
    }
    
    @Nested
    @DisplayName("update() Tests")
    class UpdateTests {
        
        @Test
        @DisplayName("Should update customer account successfully")
        void shouldUpdateCustomerAccount() {
            // Given
            CustomerAccountEntity saved = customerAccountRepository.save(customerAccount1);
            UUID id = saved.getId();
            
            // When
            saved.setName("Premium Plus Account");
            saved.setAccountState(CustomerAccountState.SUSPENDED);
            customerAccountRepository.save(saved);
            
            // Then
            Optional<CustomerAccountEntity> updated = customerAccountRepository.findById(id);
            assertThat(updated).isPresent();
            assertThat(updated.get().getName()).isEqualTo("Premium Plus Account");
            assertThat(updated.get().getAccountState()).isEqualTo(CustomerAccountState.SUSPENDED);
        }
    }
    
    @Nested
    @DisplayName("delete() Tests")
    class DeleteTests {
        
        @Test
        @DisplayName("Should delete customer account successfully")
        void shouldDeleteCustomerAccount() {
            // Given
            CustomerAccountEntity saved = customerAccountRepository.save(customerAccount1);
            UUID id = saved.getId();
            
            // When
            customerAccountRepository.deleteById(id);
            
            // Then
            assertThat(customerAccountRepository.findById(id)).isEmpty();
        }
    }
}
