package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.CorporationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("CorporationRepository Integration Tests")
class CorporationRepositoryTest {
    
    @Autowired
    private CorporationRepository corporationRepository;
    
    private CorporationEntity corporation1;
    private CorporationEntity corporation2;
    
    @BeforeEach
    void setUp() {
        corporation1 = new CorporationEntity();
        corporation1.setName("ACME Corp");
        corporation1.setCode("ACME001");
        corporation1.setIncorporationCountry("US");
        corporation1.setIncorporationDate(LocalDate.of(2020, 1, 1));
        
        corporation2 = new CorporationEntity();
        corporation2.setName("Tech Corp");
        corporation2.setCode("TECH001");
        corporation2.setIncorporationCountry("GB");
        corporation2.setIncorporationDate(LocalDate.of(2021, 6, 15));
    }
    
    @Nested
    @DisplayName("save() Tests")
    class SaveTests {
        
        @Test
        @DisplayName("Should save corporation successfully")
        void shouldSaveCorporation_Success() {
            // When
            CorporationEntity saved = corporationRepository.save(corporation1);
            
            // Then
            assertThat(saved).isNotNull();
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getName()).isEqualTo("ACME Corp");
            assertThat(saved.getCode()).isEqualTo("ACME001");
            assertThat(saved.getIncorporationCountry()).isEqualTo("US");
        }
    }
    
    @Nested
    @DisplayName("findByCodeAndIncorporationCountry() Tests")
    class FindByCodeAndCountryTests {
        
        @Test
        @DisplayName("Should find corporation by code and country")
        void shouldFindCorporation_ByCodeAndCountry() {
            // Given
            corporationRepository.save(corporation1);
            
            // When
            Optional<CorporationEntity> found = corporationRepository
                    .findByCodeAndIncorporationCountry("ACME001", "US");
            
            // Then
            assertThat(found).isPresent();
            assertThat(found.get().getName()).isEqualTo("ACME Corp");
        }
        
        @Test
        @DisplayName("Should return empty when code and country combination not found")
        void shouldReturnEmpty_NotFound() {
            // When
            Optional<CorporationEntity> found = corporationRepository
                    .findByCodeAndIncorporationCountry("INVALID", "US");
            
            // Then
            assertThat(found).isEmpty();
        }
        
        @Test
        @DisplayName("Should distinguish corporations by country")
        void shouldDistinguishByCountry() {
            // Given
            corporationRepository.save(corporation1); // US
            corporationRepository.save(corporation2); // GB
            
            // When
            Optional<CorporationEntity> found1 = corporationRepository
                    .findByCodeAndIncorporationCountry("ACME001", "US");
            Optional<CorporationEntity> found2 = corporationRepository
                    .findByCodeAndIncorporationCountry("TECH001", "GB");
            Optional<CorporationEntity> notFound = corporationRepository
                    .findByCodeAndIncorporationCountry("ACME001", "GB");
            
            // Then
            assertThat(found1).isPresent();
            assertThat(found2).isPresent();
            assertThat(notFound).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("findById() Tests")
    class FindByIdTests {
        
        @Test
        @DisplayName("Should find corporation by ID")
        void shouldFindCorporation_ById() {
            // Given
            CorporationEntity saved = corporationRepository.save(corporation1);
            
            // When
            Optional<CorporationEntity> found = corporationRepository.findById(saved.getId());
            
            // Then
            assertThat(found).isPresent();
            assertThat(found.get().getName()).isEqualTo("ACME Corp");
        }
    }
    
    @Nested
    @DisplayName("update() Tests")
    class UpdateTests {
        
        @Test
        @DisplayName("Should update corporation successfully")
        void shouldUpdateCorporation() {
            // Given
            CorporationEntity saved = corporationRepository.save(corporation1);
            UUID id = saved.getId();
            
            // When
            saved.setName("ACME Corporation");
            saved.setType("LLC");
            corporationRepository.save(saved);
            
            // Then
            Optional<CorporationEntity> updated = corporationRepository.findById(id);
            assertThat(updated).isPresent();
            assertThat(updated.get().getName()).isEqualTo("ACME Corporation");
            assertThat(updated.get().getType()).isEqualTo("LLC");
        }
    }
    
    @Nested
    @DisplayName("delete() Tests")
    class DeleteTests {
        
        @Test
        @DisplayName("Should delete corporation successfully")
        void shouldDeleteCorporation() {
            // Given
            CorporationEntity saved = corporationRepository.save(corporation1);
            UUID id = saved.getId();
            
            // When
            corporationRepository.deleteById(id);
            
            // Then
            assertThat(corporationRepository.findById(id)).isEmpty();
        }
    }
}
