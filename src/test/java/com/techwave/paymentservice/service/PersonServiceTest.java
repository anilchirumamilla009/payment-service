package com.techwave.paymentservice.service;

import com.techwave.paymentservice.dto.PersonAuditDto;
import com.techwave.paymentservice.dto.PersonDto;
import com.techwave.paymentservice.entity.PersonAuditEntity;
import com.techwave.paymentservice.entity.PersonEntity;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.mapper.PersonMapper;
import com.techwave.paymentservice.repository.PersonAuditRepository;
import com.techwave.paymentservice.repository.PersonRepository;
import com.techwave.paymentservice.service.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PersonService Unit Tests")
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonAuditRepository personAuditRepository;

    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonServiceImpl personService;

    private UUID personId;
    private PersonEntity entity;
    private PersonDto dto;

    @BeforeEach
    void setUp() {
        personId = UUID.randomUUID();

        entity = new PersonEntity();
        entity.setId(personId);
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setVersion(1);

        dto = new PersonDto();
        dto.setId(personId);
        dto.setFirstName("John");
        dto.setLastName("Doe");
    }

    @Test
    @DisplayName("createPerson saves entity and creates audit")
    void createPerson_savesAndAudits() {
        PersonEntity inputEntity = new PersonEntity();
        inputEntity.setFirstName("John");
        inputEntity.setLastName("Doe");

        when(personMapper.toEntity(dto)).thenReturn(inputEntity);
        when(personRepository.save(any(PersonEntity.class)))
                .thenReturn(entity);
        when(personMapper.toDto(entity)).thenReturn(dto);

        PersonDto result = personService.createPerson(dto);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(personRepository).save(any(PersonEntity.class));
        verify(personAuditRepository).save(any(PersonAuditEntity.class));
    }

    @Test
    @DisplayName("getPersonById returns DTO when found")
    void getPersonById_found() {
        when(personRepository.findById(personId))
                .thenReturn(Optional.of(entity));
        when(personMapper.toDto(entity)).thenReturn(dto);

        PersonDto result = personService.getPersonById(personId);

        assertNotNull(result);
        assertEquals(personId, result.getId());
    }

    @Test
    @DisplayName("getPersonById throws when not found")
    void getPersonById_notFound() {
        UUID id = UUID.randomUUID();
        when(personRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> personService.getPersonById(id));
    }

    @Test
    @DisplayName("updatePerson increments version and creates audit")
    void updatePerson_updatesVersionAndAudits() {
        PersonDto updateDto = new PersonDto();
        updateDto.setFirstName("Jane");

        PersonEntity updatedEntity = new PersonEntity();
        updatedEntity.setId(personId);
        updatedEntity.setFirstName("Jane");
        updatedEntity.setLastName("Doe");
        updatedEntity.setVersion(2);

        PersonDto updatedDto = new PersonDto();
        updatedDto.setId(personId);
        updatedDto.setFirstName("Jane");
        updatedDto.setLastName("Doe");

        when(personRepository.findById(personId))
                .thenReturn(Optional.of(entity));
        when(personRepository.saveAndFlush(entity)).thenReturn(updatedEntity);
        when(personMapper.toDto(updatedEntity)).thenReturn(updatedDto);

        PersonDto result = personService.updatePerson(personId, updateDto);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        verify(personMapper).updateEntity(updateDto, entity);
        verify(personAuditRepository).save(any(PersonAuditEntity.class));
    }

    @Test
    @DisplayName("getAuditTrail returns ordered audit entries")
    void getAuditTrail_returnsEntries() {
        PersonAuditEntity auditEntity = new PersonAuditEntity();
        auditEntity.setResource(personId);
        auditEntity.setVersion(1);

        PersonAuditDto auditDto = new PersonAuditDto();
        auditDto.setResource(personId);
        auditDto.setVersion(1);

        when(personRepository.existsById(personId)).thenReturn(true);
        when(personAuditRepository
                .findByResourceOrderByVersionAsc(personId))
                .thenReturn(List.of(auditEntity));
        when(personMapper.toAuditDtoList(List.of(auditEntity)))
                .thenReturn(List.of(auditDto));

        List<PersonAuditDto> result =
                personService.getAuditTrail(personId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getVersion());
    }
}

