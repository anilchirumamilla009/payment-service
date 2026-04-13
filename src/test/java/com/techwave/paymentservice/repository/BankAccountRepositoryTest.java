package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.BankAccountEntity;
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
@DisplayName("BankAccountRepository Integration Tests")
class BankAccountRepositoryTest {
    
    @Autowired
    private BankAccountRepository bankAccountRepository;
    
    private BankAccountEntity bankAccount1;
    private BankAccountEntity bankAccount2;
    
    @BeforeEach
    void setUp() {
        bankAccount1 = new BankAccountEntity();
        bankAccount1.setIban("DE89370400440532013000");
        bankAccount1.setBic("COBADEHHXXX");
        bankAccount1.setBeneficiary("John Doe");
        bankAccount1.setBeneficiaryAddress("123 Main St, Berlin");
        bankAccount1.setNickname("Primary Account");
        bankAccount1.setCountry("DE");
        bankAccount1.setCurrency("EUR");
        
        bankAccount2 = new BankAccountEntity();
        bankAccount2.setIban("GB82WEST12345698765432");
        bankAccount2.setBic("WESTGB2L");
        bankAccount2.setBeneficiary("Jane Smith");
        bankAccount2.setBeneficiaryAddress("456 Oxford St, London");
        bankAccount2.setNickname("UK Account");
        bankAccount2.setCountry("GB");
        bankAccount2.setCurrency("GBP");
    }
    
    @Nested
    @DisplayName("save() Tests")
    class SaveTests {
        
        @Test
        @DisplayName("Should save bank account successfully")
        void shouldSaveBankAccount() {
            // When
            BankAccountEntity saved = bankAccountRepository.save(bankAccount1);
            
            // Then
            assertThat(saved).isNotNull();
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getIban()).isEqualTo("DE89370400440532013000");
            assertThat(saved.getBeneficiary()).isEqualTo("John Doe");
            assertThat(saved.getCreatedAt()).isNotNull();
        }
    }
    
    @Nested
    @DisplayName("findByIban() Tests")
    class FindByIbanTests {
        
        @Test
        @DisplayName("Should find bank account by IBAN")
        void shouldFindBankAccount_ByIban() {
            // Given
            bankAccountRepository.save(bankAccount1);
            
            // When
            Optional<BankAccountEntity> found = bankAccountRepository
                    .findByIban("DE89370400440532013000");
            
            // Then
            assertThat(found).isPresent();
            assertThat(found.get().getBeneficiary()).isEqualTo("John Doe");
            assertThat(found.get().getCountry()).isEqualTo("DE");
        }
        
        @Test
        @DisplayName("Should return empty when IBAN not found")
        void shouldReturnEmpty_IbanNotFound() {
            // When
            Optional<BankAccountEntity> found = bankAccountRepository
                    .findByIban("INVALID_IBAN");
            
            // Then
            assertThat(found).isEmpty();
        }
        
        @Test
        @DisplayName("Should distinguish accounts by IBAN")
        void shouldDistinguishByIban() {
            // Given
            bankAccountRepository.save(bankAccount1);
            bankAccountRepository.save(bankAccount2);
            
            // When
            Optional<BankAccountEntity> found1 = bankAccountRepository
                    .findByIban("DE89370400440532013000");
            Optional<BankAccountEntity> found2 = bankAccountRepository
                    .findByIban("GB82WEST12345698765432");
            
            // Then
            assertThat(found1).isPresent();
            assertThat(found2).isPresent();
            assertThat(found1.get().getCountry()).isEqualTo("DE");
            assertThat(found2.get().getCountry()).isEqualTo("GB");
        }
    }
    
    @Nested
    @DisplayName("findById() Tests")
    class FindByIdTests {
        
        @Test
        @DisplayName("Should find bank account by ID")
        void shouldFindBankAccount_ById() {
            // Given
            BankAccountEntity saved = bankAccountRepository.save(bankAccount1);
            
            // When
            Optional<BankAccountEntity> found = bankAccountRepository.findById(saved.getId());
            
            // Then
            assertThat(found).isPresent();
            assertThat(found.get().getIban()).isEqualTo("DE89370400440532013000");
        }
    }
    
    @Nested
    @DisplayName("exists() Tests")
    class ExistsTests {
        
        @Test
        @DisplayName("Should return true when account exists")
        void shouldReturnTrue_AccountExists() {
            // Given
            BankAccountEntity saved = bankAccountRepository.save(bankAccount1);
            
            // When
            boolean exists = bankAccountRepository.existsById(saved.getId());
            
            // Then
            assertThat(exists).isTrue();
        }
        
        @Test
        @DisplayName("Should return false when account not exists")
        void shouldReturnFalse_AccountNotExists() {
            // When
            boolean exists = bankAccountRepository.existsById(UUID.randomUUID());
            
            // Then
            assertThat(exists).isFalse();
        }
    }
    
    @Nested
    @DisplayName("update() Tests")
    class UpdateTests {
        
        @Test
        @DisplayName("Should update bank account successfully")
        void shouldUpdateBankAccount() {
            // Given
            BankAccountEntity saved = bankAccountRepository.save(bankAccount1);
            UUID id = saved.getId();
            
            // When
            saved.setBeneficiary("John Doe Jr");
            saved.setNickname("Updated Account");
            bankAccountRepository.save(saved);
            
            // Then
            Optional<BankAccountEntity> updated = bankAccountRepository.findById(id);
            assertThat(updated).isPresent();
            assertThat(updated.get().getBeneficiary()).isEqualTo("John Doe Jr");
            assertThat(updated.get().getNickname()).isEqualTo("Updated Account");
        }
    }
    
    @Nested
    @DisplayName("delete() Tests")
    class DeleteTests {
        
        @Test
        @DisplayName("Should delete bank account successfully")
        void shouldDeleteBankAccount() {
            // Given
            BankAccountEntity saved = bankAccountRepository.save(bankAccount1);
            UUID id = saved.getId();
            
            // When
            bankAccountRepository.deleteById(id);
            
            // Then
            assertThat(bankAccountRepository.findById(id)).isEmpty();
        }
    }
}
