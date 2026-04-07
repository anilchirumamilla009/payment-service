package com.techwave.paymentservice.controller;

import com.techwave.paymentservice.dto.CorporationAuditDto;
import com.techwave.paymentservice.dto.CorporationDto;
import com.techwave.paymentservice.dto.PersonAuditDto;
import com.techwave.paymentservice.dto.PersonDto;
import com.techwave.paymentservice.service.CorporationService;
import com.techwave.paymentservice.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LegalEntitiesController Unit Tests")
class LegalEntitiesControllerTest {

    @Mock
    private PersonService personService;

    @Mock
    private CorporationService corporationService;

    @InjectMocks
    private LegalEntitiesController controller;

    private UUID personId;
    private PersonDto personDto;
    private UUID corpId;
    private CorporationDto corpDto;

    @BeforeEach
    void setUp() {
        personId = UUID.randomUUID();
        personDto = new PersonDto();
        personDto.setId(personId);
        personDto.setFirstName("John");
        personDto.setLastName("Doe");

        corpId = UUID.randomUUID();
        corpDto = new CorporationDto();
        corpDto.setId(corpId);
        corpDto.setName("Acme Corp");
        corpDto.setCode("ACME");
        corpDto.setIncorporationCountry("US");
        corpDto.setIncorporationDate(LocalDate.of(2020, 1, 15));
    }

    // ── People ───────────────────────────────────────────────

    @Test
    @DisplayName("createPerson returns 201 CREATED")
    void createPerson_returns201() {
        when(personService.createPerson(personDto)).thenReturn(personDto);

        ResponseEntity<PersonDto> response =
                controller.createPerson(personDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(personId, response.getBody().getId());
        verify(personService).createPerson(personDto);
    }

    @Test
    @DisplayName("getPerson returns 200 OK")
    void getPerson_returns200() {
        when(personService.getPersonById(personId)).thenReturn(personDto);

        ResponseEntity<PersonDto> response =
                controller.getPerson(personId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John", response.getBody().getFirstName());
    }

    @Test
    @DisplayName("updatePerson returns 200 OK")
    void updatePerson_returns200() {
        PersonDto updateDto = new PersonDto();
        updateDto.setFirstName("Jane");

        PersonDto updatedResult = new PersonDto();
        updatedResult.setId(personId);
        updatedResult.setFirstName("Jane");
        updatedResult.setLastName("Doe");

        when(personService.updatePerson(personId, updateDto))
                .thenReturn(updatedResult);

        ResponseEntity<PersonDto> response =
                controller.updatePerson(personId, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Jane", response.getBody().getFirstName());
    }

    @Test
    @DisplayName("getPersonAuditTrail returns 200 OK")
    void getPersonAuditTrail_returns200() {
        PersonAuditDto auditDto = new PersonAuditDto();
        auditDto.setResource(personId);
        auditDto.setVersion(1);

        when(personService.getAuditTrail(personId))
                .thenReturn(List.of(auditDto));

        ResponseEntity<List<PersonAuditDto>> response =
                controller.getPersonAuditTrail(personId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    // ── Corporations ─────────────────────────────────────────

    @Test
    @DisplayName("createCorporation returns 201 CREATED")
    void createCorporation_returns201() {
        when(corporationService.createCorporation(corpDto))
                .thenReturn(corpDto);

        ResponseEntity<CorporationDto> response =
                controller.createCorporation(corpDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(corpId, response.getBody().getId());
    }

    @Test
    @DisplayName("getCorporation returns 200 OK")
    void getCorporation_returns200() {
        when(corporationService.getCorporationById(corpId))
                .thenReturn(corpDto);

        ResponseEntity<CorporationDto> response =
                controller.getCorporation(corpId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Acme Corp", response.getBody().getName());
    }

    @Test
    @DisplayName("updateCorporation returns 200 OK")
    void updateCorporation_returns200() {
        CorporationDto updateDto = new CorporationDto();
        updateDto.setName("Acme Updated");

        CorporationDto updatedResult = new CorporationDto();
        updatedResult.setId(corpId);
        updatedResult.setName("Acme Updated");

        when(corporationService.updateCorporation(corpId, updateDto))
                .thenReturn(updatedResult);

        ResponseEntity<CorporationDto> response =
                controller.updateCorporation(corpId, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Acme Updated", response.getBody().getName());
    }

    @Test
    @DisplayName("getCorporationAuditTrail returns 200 OK")
    void getCorporationAuditTrail_returns200() {
        CorporationAuditDto auditDto = new CorporationAuditDto();
        auditDto.setResource(corpId);
        auditDto.setVersion(1);

        when(corporationService.getAuditTrail(corpId))
                .thenReturn(List.of(auditDto));

        ResponseEntity<List<CorporationAuditDto>> response =
                controller.getCorporationAuditTrail(corpId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("getCorporationByCode returns 200 OK")
    void getCorporationByCode_returns200() {
        when(corporationService.getCorporationByCode("US", "ACME"))
                .thenReturn(corpDto);

        ResponseEntity<CorporationDto> response =
                controller.getCorporationByCode("US", "ACME");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ACME", response.getBody().getCode());
    }
}

