package com.techwave.paymentservice.repository;

import com.techwave.paymentservice.entity.PersonEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("PersonRepository Integration Tests")
class PersonRepositoryTest {
    
    @Autowired
    private PersonRepository personRepository;
    
    private PersonEntity person1;
    private PersonEntity person2;
    
    @BeforeEach
    void setUp() {
        person1 = new PersonEntity("John", "Doe");
        person2 = new PersonEntity("Jane", "Smith");
    }
    
    @Nested
    @DisplayName("save() Tests")
    class SaveTests {
        
        @Test
        @DisplayName("Should save person successfully")
        void shouldSavePerson_Success() {
            // When
            PersonEntity saved = personRepository.save(person1);
            
            // Then
            assertThat(saved).isNotNull();
            assertThat(saved.getId()).isNotNull();
            assertThat(saved.getFirstName()).isEqualTo("John");
            assertThat(saved.getLastName()).isEqualTo("Doe");
            assertThat(saved.getCreatedAt()).isNotNull();
        }
        
        @Test
        @DisplayName("Should save multiple persons")
        void shouldSaveMultiplePersons() {
            // When
            PersonEntity saved1 = personRepository.save(person1);
            PersonEntity saved2 = personRepository.save(person2);
            
            // Then
            assertThat(saved1.getId()).isNotEqualTo(saved2.getId());
            assertThat(personRepository.count()).isEqualTo(2);
        }
    }
    
    @Nested
    @DisplayName("findById() Tests")
    class FindByIdTests {
        
        @Test
        @DisplayName("Should find person by ID when exists")
        void shouldFindPerson_ById() {
            // Given
            PersonEntity saved = personRepository.save(person1);
            
            // When
            Optional<PersonEntity> found = personRepository.findById(saved.getId());
            
            // Then
            assertThat(found).isPresent();
            assertThat(found.get().getFirstName()).isEqualTo("John");
            assertThat(found.get().getLastName()).isEqualTo("Doe");
        }
        
        @Test
        @DisplayName("Should return empty Optional when person not found")
        void shouldReturnEmpty_NotFound() {
            // When
            Optional<PersonEntity> found = personRepository.findById(UUID.randomUUID());
            
            // Then
            assertThat(found).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("findAll() Tests")
    class FindAllTests {
        
        @Test
        @DisplayName("Should return all persons")
        void shouldReturnAllPersons() {
            // Given
            personRepository.save(person1);
            personRepository.save(person2);
            
            // When
            List<PersonEntity> all = personRepository.findAll();
            
            // Then
            assertThat(all).hasSize(2);
        }
        
        @Test
        @DisplayName("Should return empty list when no persons exist")
        void shouldReturnEmpty_NoPersons() {
            // When
            List<PersonEntity> all = personRepository.findAll();
            
            // Then
            assertThat(all).isEmpty();
        }
    }
    
    @Nested
    @DisplayName("update() Tests")
    class UpdateTests {
        
        @Test
        @DisplayName("Should update person successfully")
        void shouldUpdatePerson() {
            // Given
            PersonEntity saved = personRepository.save(person1);
            UUID id = saved.getId();
            
            // When
            saved.setFirstName("Johnny");
            saved.setLastName("Doeson");
            PersonEntity updated = personRepository.save(saved);
            
            // Then
            Optional<PersonEntity> reloaded = personRepository.findById(id);
            assertThat(reloaded).isPresent();
            assertThat(reloaded.get().getFirstName()).isEqualTo("Johnny");
            assertThat(reloaded.get().getLastName()).isEqualTo("Doeson");
        }
        
        @Test
        @DisplayName("Should increment version on update")
        void shouldIncrementVersion_OnUpdate() {
            // Given
            PersonEntity saved = personRepository.save(person1);
            Long initialVersion = saved.getVersion();
            
            // When
            saved.setFirstName("Updated");
            personRepository.save(saved);
            
            // Then
            Optional<PersonEntity> updated = personRepository.findById(saved.getId());
            assertThat(updated.get().getVersion()).isGreaterThan(initialVersion);
        }
    }
    
    @Nested
    @DisplayName("exists() Tests")
    class ExistsTests {
        
        @Test
        @DisplayName("Should return true when person exists")
        void shouldReturnTrue_PersonExists() {
            // Given
            PersonEntity saved = personRepository.save(person1);
            
            // When
            boolean exists = personRepository.existsById(saved.getId());
            
            // Then
            assertThat(exists).isTrue();
        }
        
        @Test
        @DisplayName("Should return false when person not exists")
        void shouldReturnFalse_PersonNotExists() {
            // When
            boolean exists = personRepository.existsById(UUID.randomUUID());
            
            // Then
            assertThat(exists).isFalse();
        }
    }
    
    @Nested
    @DisplayName("delete() Tests")
    class DeleteTests {
        
        @Test
        @DisplayName("Should delete person by ID")
        void shouldDeletePerson_ById() {
            // Given
            PersonEntity saved = personRepository.save(person1);
            UUID id = saved.getId();
            
            // When
            personRepository.deleteById(id);
            
            // Then
            assertThat(personRepository.findById(id)).isEmpty();
        }
        
        @Test
        @DisplayName("Should delete person by entity")
        void shouldDeletePerson_ByEntity() {
            // Given
            PersonEntity saved = personRepository.save(person1);
            
            // When
            personRepository.delete(saved);
            
            // Then
            assertThat(personRepository.findById(saved.getId())).isEmpty();
        }
    }
}
