package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.request.CorporationRequest;
import com.techwave.paymentservice.dto.request.PersonRequest;
import com.techwave.paymentservice.dto.response.CorporationAuditResponse;
import com.techwave.paymentservice.dto.response.CorporationResponse;
import com.techwave.paymentservice.dto.response.PersonAuditResponse;
import com.techwave.paymentservice.dto.response.PersonResponse;
import com.techwave.paymentservice.entity.CorporationAudit;
import com.techwave.paymentservice.entity.CorporationEntity;
import com.techwave.paymentservice.entity.PersonAudit;
import com.techwave.paymentservice.entity.PersonEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.CorporationMapper;
import com.techwave.paymentservice.mapper.PersonMapper;
import com.techwave.paymentservice.repository.CorporationAuditRepository;
import com.techwave.paymentservice.repository.CorporationRepository;
import com.techwave.paymentservice.repository.PersonAuditRepository;
import com.techwave.paymentservice.repository.PersonRepository;
import com.techwave.paymentservice.service.impl.LegalEntityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LegalEntityService Tests")
class LegalEntityServiceTest {
    
    @Mock
    private CorporationRepository corporationRepository;
    
    @Mock
    private PersonRepository personRepository;
    
    @Mock
    private CorporationAuditRepository corporationAuditRepository;
    
    @Mock
    private PersonAuditRepository personAuditRepository;
    
    @Mock
    private CorporationMapper corporationMapper;
    
    @Mock
    private PersonMapper personMapper;
    
    @InjectMocks
    private LegalEntityServiceImpl legalEntityService;
    
    private UUID personId;
    private UUID corporationId;
    private PersonEntity personEntity;
    private PersonResponse personResponse;
    private CorporationEntity corporationEntity;
    private CorporationResponse corporationResponse;
    
    @BeforeEach
    void setUp() {
        personId = UUID.randomUUID();
        corporationId = UUID.randomUUID();
        
        personEntity = new PersonEntity();
        personEntity.setId(personId);
        personEntity.setFirstName("John");
        personEntity.setLastName("Doe");
        personEntity.setVersion(1);
        
        personResponse = new PersonResponse();
        personResponse.setId(personId);
        personResponse.setFirstName("John");
        personResponse.setLastName("Doe");
        
        corporationEntity = new CorporationEntity();
        corporationEntity.setId(corporationId);
        corporationEntity.setName("ACME Corp");
        corporationEntity.setCode("ACME001");
        corporationEntity.setVersion(1);
        corporationEntity.setIncorporationCountry("US");
        
        corporationResponse = new CorporationResponse();
        corporationResponse.setId(corporationId);
        corporationResponse.setName("ACME Corp");
        corporationResponse.setCode("ACME001");
    }
    
    @Nested
    @DisplayName("Person Tests")
    class PersonTests {
        
        @Nested
        @DisplayName("createPerson() Tests")
        class CreatePersonTests {
            
            @Test
            @DisplayName("Should create person successfully and record audit trail")
            void shouldCreatePerson_Success() {
                // Given
                PersonRequest request = new PersonRequest("John", "Doe");
                
                when(personMapper.toEntity(request)).thenReturn(personEntity);
                when(personRepository.save(personEntity)).thenReturn(personEntity);
                when(personMapper.toResponse(personEntity)).thenReturn(personResponse);
                when(personAuditRepository.save(any(PersonAudit.class))).thenReturn(any());
                
                // When
                PersonResponse result = legalEntityService.createPerson(request);
                
                // Then
                assertThat(result).isEqualTo(personResponse);
                verify(personRepository).save(personEntity);
                verify(personAuditRepository).save(any(PersonAudit.class));
            }
            
            @Test
            @DisplayName("Should throw exception on mapper failure")
            void shouldThrowException_MapperError() {
                // Given
                PersonRequest request = new PersonRequest("John", "Doe");
                
                when(personMapper.toEntity(request)).thenThrow(new RuntimeException("Mapping error"));
                
                // When & Then
                assertThatThrownBy(() -> legalEntityService.createPerson(request))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("Mapping error");
            }
        }
        
        @Nested
        @DisplayName("getPerson() Tests")
        class GetPersonTests {
            
            @Test
            @DisplayName("Should return person when found")
            void shouldGetPerson_Found() {
                // Given
                when(personRepository.findById(personId)).thenReturn(Optional.of(personEntity));
                when(personMapper.toResponse(personEntity)).thenReturn(personResponse);
                
                // When
                PersonResponse result = legalEntityService.getPerson(personId);
                
                // Then
                assertThat(result).isEqualTo(personResponse);
                verify(personRepository).findById(personId);
            }
            
            @Test
            @DisplayName("Should throw ResourceNotFoundException when person not found")
            void shouldThrowException_PersonNotFound() {
                // Given
                UUID nonExistentId = UUID.randomUUID();
                when(personRepository.findById(nonExistentId)).thenReturn(Optional.empty());
                
                // When & Then
                assertThatThrownBy(() -> legalEntityService.getPerson(nonExistentId))
                        .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessageContaining("Person not found");
                
                verify(personRepository).findById(nonExistentId);
                verify(personMapper, never()).toResponse(any());
            }
        }
        
        @Nested
        @DisplayName("updatePerson() Tests")
        class UpdatePersonTests {
            
            @Test
            @DisplayName("Should update person successfully and record audit trail")
            void shouldUpdatePerson_Success() {
                // Given
                PersonRequest request = new PersonRequest("Jane", "Smith");
                PersonEntity updatedEntity = new PersonEntity();
                updatedEntity.setId(personId);
                updatedEntity.setFirstName("Jane");
                updatedEntity.setLastName("Smith");
                updatedEntity.setVersion(2);
                
                PersonResponse updatedResponse = new PersonResponse();
                updatedResponse.setId(personId);
                updatedResponse.setFirstName("Jane");
                updatedResponse.setLastName("Smith");
                
                when(personRepository.findById(personId)).thenReturn(Optional.of(personEntity));
                when(personRepository.save(any(PersonEntity.class))).thenReturn(updatedEntity);
                when(personMapper.toResponse(updatedEntity)).thenReturn(updatedResponse);
                when(personAuditRepository.save(any(PersonAudit.class))).thenReturn(any());
                
                // When
                PersonResponse result = legalEntityService.updatePerson(personId, request);
                
                // Then
                assertThat(result).isEqualTo(updatedResponse);
                ArgumentCaptor<PersonEntity> captor = ArgumentCaptor.forClass(PersonEntity.class);
                verify(personRepository).save(captor.capture());
                PersonEntity saved = captor.getValue();
                assertThat(saved.getFirstName()).isEqualTo("Jane");
                assertThat(saved.getLastName()).isEqualTo("Smith");
            }
            
            @Test
            @DisplayName("Should throw ResourceNotFoundException when updating non-existent person")
            void shouldThrowException_UpdateNonExistent() {
                // Given
                UUID nonExistentId = UUID.randomUUID();
                PersonRequest request = new PersonRequest("Jane", "Smith");
                when(personRepository.findById(nonExistentId)).thenReturn(Optional.empty());
                
                // When & Then
                assertThatThrownBy(() -> legalEntityService.updatePerson(nonExistentId, request))
                        .isInstanceOf(ResourceNotFoundException.class);
                
                verify(personRepository, never()).save(any());
            }
        }
        
        @Nested
        @DisplayName("getPersonAuditTrail() Tests")
        class GetPersonAuditTrailTests {
            
            @Test
            @DisplayName("Should return audit trail in descending version order")
            void shouldGetPersonAuditTrail_Success() {
                // Given
                PersonAudit audit1 = new PersonAudit(personId, 1, "John", "Doe");
                PersonAudit audit2 = new PersonAudit(personId, 2, "Jane", "Doe");
                List<PersonAudit> audits = Arrays.asList(audit2, audit1);
                
                PersonAuditResponse auditResponse1 = new PersonAuditResponse();
                PersonAuditResponse auditResponse2 = new PersonAuditResponse();
                
                when(personRepository.existsById(personId)).thenReturn(true);
                when(personAuditRepository.findByResourceOrderByVersionDesc(personId)).thenReturn(audits);
                when(personMapper.toAuditResponses(audits)).thenReturn(Arrays.asList(auditResponse2, auditResponse1));
                
                // When
                List<PersonAuditResponse> result = legalEntityService.getPersonAuditTrail(personId);
                
                // Then
                assertThat(result).hasSize(2);
                verify(personRepository).existsById(personId);
                verify(personAuditRepository).findByResourceOrderByVersionDesc(personId);
            }
            
            @Test
            @DisplayName("Should throw ResourceNotFoundException for non-existent person")
            void shouldThrowException_PersonNotFound() {
                // Given
                UUID nonExistentId = UUID.randomUUID();
                when(personRepository.existsById(nonExistentId)).thenReturn(false);
                
                // When & Then
                assertThatThrownBy(() -> legalEntityService.getPersonAuditTrail(nonExistentId))
                        .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessageContaining("Person not found");
                
                verify(personAuditRepository, never()).findByResourceOrderByVersionDesc(any());
            }
        }
    }
    
    @Nested
    @DisplayName("Corporation Tests")
    class CorporationTests {
        
        @Nested
        @DisplayName("createCorporation() Tests")
        class CreateCorporationTests {
            
            @Test
            @DisplayName("Should create corporation successfully and record audit trail")
            void shouldCreateCorporation_Success() {
                // Given
                CorporationRequest request = new CorporationRequest("ACME Corp", "ACME001");
                request.setIncorporationCountry("US");
                request.setIncorporationDate(LocalDate.of(2020, 1, 1));
                
                when(corporationMapper.toEntity(request)).thenReturn(corporationEntity);
                when(corporationRepository.save(corporationEntity)).thenReturn(corporationEntity);
                when(corporationMapper.toResponse(corporationEntity)).thenReturn(corporationResponse);
                when(corporationAuditRepository.save(any(CorporationAudit.class))).thenReturn(any());
                
                // When
                CorporationResponse result = legalEntityService.createCorporation(request);
                
                // Then
                assertThat(result).isEqualTo(corporationResponse);
                verify(corporationRepository).save(corporationEntity);
                verify(corporationAuditRepository).save(any(CorporationAudit.class));
            }
        }
        
        @Nested
        @DisplayName("getCorporation() Tests")
        class GetCorporationTests {
            
            @Test
            @DisplayName("Should return corporation when found")
            void shouldGetCorporation_Found() {
                // Given
                when(corporationRepository.findById(corporationId)).thenReturn(Optional.of(corporationEntity));
                when(corporationMapper.toResponse(corporationEntity)).thenReturn(corporationResponse);
                
                // When
                CorporationResponse result = legalEntityService.getCorporation(corporationId);
                
                // Then
                assertThat(result).isEqualTo(corporationResponse);
                verify(corporationRepository).findById(corporationId);
            }
            
            @Test
            @DisplayName("Should throw ResourceNotFoundException when corporation not found")
            void shouldThrowException_CorporationNotFound() {
                // Given
                UUID nonExistentId = UUID.randomUUID();
                when(corporationRepository.findById(nonExistentId)).thenReturn(Optional.empty());
                
                // When & Then
                assertThatThrownBy(() -> legalEntityService.getCorporation(nonExistentId))
                        .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessageContaining("Corporation not found");
            }
        }
        
        @Nested
        @DisplayName("updateCorporation() Tests")
        class UpdateCorporationTests {
            
            @Test
            @DisplayName("Should update corporation successfully")
            void shouldUpdateCorporation_Success() {
                // Given
                CorporationRequest request = new CorporationRequest("Updated Corp", "UPD001");
                request.setIncorporationCountry("GB");
                
                CorporationEntity updatedEntity = new CorporationEntity();
                updatedEntity.setId(corporationId);
                updatedEntity.setName("Updated Corp");
                updatedEntity.setCode("UPD001");
                updatedEntity.setVersion(2);
                updatedEntity.setIncorporationCountry("GB");
                
                CorporationResponse updatedResponse = new CorporationResponse();
                updatedResponse.setId(corporationId);
                updatedResponse.setName("Updated Corp");
                
                when(corporationRepository.findById(corporationId)).thenReturn(Optional.of(corporationEntity));
                when(corporationRepository.save(any(CorporationEntity.class))).thenReturn(updatedEntity);
                when(corporationMapper.toResponse(updatedEntity)).thenReturn(updatedResponse);
                when(corporationAuditRepository.save(any(CorporationAudit.class))).thenReturn(any());
                
                // When
                CorporationResponse result = legalEntityService.updateCorporation(corporationId, request);
                
                // Then
                assertThat(result).isEqualTo(updatedResponse);
                verify(corporationRepository).save(any(CorporationEntity.class));
                verify(corporationAuditRepository).save(any(CorporationAudit.class));
            }
            
            @Test
            @DisplayName("Should throw ResourceNotFoundException when updating non-existent corporation")
            void shouldThrowException_UpdateNonExistent() {
                // Given
                UUID nonExistentId = UUID.randomUUID();
                CorporationRequest request = new CorporationRequest("Test Corp", "TEST001");
                when(corporationRepository.findById(nonExistentId)).thenReturn(Optional.empty());
                
                // When & Then
                assertThatThrownBy(() -> legalEntityService.updateCorporation(nonExistentId, request))
                        .isInstanceOf(ResourceNotFoundException.class);
                
                verify(corporationRepository, never()).save(any());
            }
        }
        
        @Nested
        @DisplayName("getCorporationByCode() Tests")
        class GetCorporationByCodeTests {
            
            @Test
            @DisplayName("Should return corporation by country and code")
            void shouldGetCorporationByCode_Found() {
                // Given
                when(corporationRepository.findByCodeAndIncorporationCountry("ACME001", "US"))
                        .thenReturn(Optional.of(corporationEntity));
                when(corporationMapper.toResponse(corporationEntity)).thenReturn(corporationResponse);
                
                // When
                CorporationResponse result = legalEntityService.getCorporationByCode("US", "ACME001");
                
                // Then
                assertThat(result).isEqualTo(corporationResponse);
                verify(corporationRepository).findByCodeAndIncorporationCountry("ACME001", "US");
            }
            
            @Test
            @DisplayName("Should throw ResourceNotFoundException when code not found")
            void shouldThrowException_CodeNotFound() {
                // Given
                when(corporationRepository.findByCodeAndIncorporationCountry("INVALID", "US"))
                        .thenReturn(Optional.empty());
                
                // When & Then
                assertThatThrownBy(() -> legalEntityService.getCorporationByCode("US", "INVALID"))
                        .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessageContaining("Corporation not found");
            }
        }
        
        @Nested
        @DisplayName("getCorporationAuditTrail() Tests")
        class GetCorporationAuditTrailTests {
            
            @Test
            @DisplayName("Should return corporation audit trail in descending version order")
            void shouldGetCorporationAuditTrail_Success() {
                // Given
                CorporationAudit audit1 = new CorporationAudit(corporationId, 1, "ACME Corp", "ACME001", 
                        LocalDate.of(2020, 1, 1), "US", "");
                CorporationAudit audit2 = new CorporationAudit(corporationId, 2, "ACME Corp Updated", "ACME001", 
                        LocalDate.of(2020, 1, 1), "US", "");
                
                List<CorporationAudit> audits = Arrays.asList(audit2, audit1);
                
                CorporationAuditResponse auditResponse1 = new CorporationAuditResponse();
                CorporationAuditResponse auditResponse2 = new CorporationAuditResponse();
                
                when(corporationRepository.existsById(corporationId)).thenReturn(true);
                when(corporationAuditRepository.findByResourceOrderByVersionDesc(corporationId)).thenReturn(audits);
                when(corporationMapper.toAuditResponses(audits)).thenReturn(Arrays.asList(auditResponse2, auditResponse1));
                
                // When
                List<CorporationAuditResponse> result = legalEntityService.getCorporationAuditTrail(corporationId);
                
                // Then
                assertThat(result).hasSize(2);
                verify(corporationAuditRepository).findByResourceOrderByVersionDesc(corporationId);
            }
            
            @Test
            @DisplayName("Should throw ResourceNotFoundException for non-existent corporation")
            void shouldThrowException_CorporationNotFound() {
                // Given
                UUID nonExistentId = UUID.randomUUID();
                when(corporationRepository.existsById(nonExistentId)).thenReturn(false);
                
                // When & Then
                assertThatThrownBy(() -> legalEntityService.getCorporationAuditTrail(nonExistentId))
                        .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessageContaining("Corporation not found");
                
                verify(corporationAuditRepository, never()).findByResourceOrderByVersionDesc(any());
            }
        }
    }
}
