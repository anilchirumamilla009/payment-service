package com.techwave.paymentservice.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.techwave.paymentservice.exception.BadRequestException;
import com.techwave.paymentservice.exception.ResourceNotFoundException;
import com.techwave.paymentservice.model.Corporation;
import com.techwave.paymentservice.model.CorporationAudit;
import com.techwave.paymentservice.model.Person;
import com.techwave.paymentservice.model.PersonAudit;

@SpringBootTest
class LegalEntitiesServiceTest {

    private static final UUID CORP_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID PERSON_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID NON_EXISTING_ID = UUID.fromString("99999999-9999-9999-9999-999999999999");

    @Autowired
    private LegalEntitiesService legalEntitiesService;

    // ── Corporation ─────────────────────────────────────────────────────

    @Test
    void getCorporation_existingId_returnsCorporation() {
        Corporation corp = legalEntitiesService.getCorporation(CORP_ID);
        assertNotNull(corp);
        assertEquals("Cornerstone FX Ltd", corp.getName());
        assertEquals("corporations", corp.getResourceType());
    }

    @Test
    void getCorporation_nonExistingId_throwsNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> legalEntitiesService.getCorporation(NON_EXISTING_ID));
    }

    @Test
    void createCorporation_validPayload_createsSuccessfully() {
        Corporation dto = new Corporation();
        dto.setName("TestCorp");
        dto.setCode("TC-01");
        dto.setIncorporationCountry("US");

        Corporation result = legalEntitiesService.createCorporation(dto);
        assertNotNull(result.getId());
        assertEquals("corporations", result.getResourceType());
        assertEquals("TestCorp", result.getName());
    }

    @Test
    void createCorporation_missingName_throwsBadRequest() {
        Corporation dto = new Corporation();
        dto.setCode("TC-02");
        assertThrows(BadRequestException.class, () -> legalEntitiesService.createCorporation(dto));
    }

    @Test
    void createCorporation_nullPayload_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> legalEntitiesService.createCorporation(null));
    }

    @Test
    void updateCorporation_existingId_updatesFields() {
        Corporation patch = new Corporation();
        patch.setName("Cornerstone FX Ltd – Updated");

        Corporation result = legalEntitiesService.updateCorporation(CORP_ID, patch);
        assertEquals("Cornerstone FX Ltd – Updated", result.getName());
        // unchanged fields survive
        assertEquals("CFS-UK", result.getCode());
    }

    @Test
    void updateCorporation_nonExistingId_throwsNotFound() {
        Corporation patch = new Corporation();
        patch.setName("X");
        assertThrows(ResourceNotFoundException.class,
                () -> legalEntitiesService.updateCorporation(NON_EXISTING_ID, patch));
    }

    @Test
    void getCorporationAuditTrail_existingId_returnsAudits() {
        List<CorporationAudit> audits = legalEntitiesService.getCorporationAuditTrail(CORP_ID);
        assertNotNull(audits);
        assertFalse(audits.isEmpty());
    }

    @Test
    void getCorporationAuditTrail_nonExistingId_throwsNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> legalEntitiesService.getCorporationAuditTrail(NON_EXISTING_ID));
    }

    @Test
    void getCorporationByCode_existing_returnsCorporation() {
        Corporation corp = legalEntitiesService.getCorporationByCode("GB", "CFS-UK");
        assertNotNull(corp);
        assertEquals(CORP_ID, corp.getId());
    }

    @Test
    void getCorporationByCode_nonExisting_throwsNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> legalEntitiesService.getCorporationByCode("XX", "NOPE"));
    }

    // ── Person ──────────────────────────────────────────────────────────

    @Test
    void getPerson_existingId_returnsPerson() {
        Person person = legalEntitiesService.getPerson(PERSON_ID);
        assertNotNull(person);
        assertEquals("Stephen", person.getFirstName());
        assertEquals("people", person.getResourceType());
    }

    @Test
    void getPerson_nonExistingId_throwsNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> legalEntitiesService.getPerson(NON_EXISTING_ID));
    }

    @Test
    void createPerson_validPayload_createsSuccessfully() {
        Person dto = new Person();
        dto.setFirstName("Alice");
        dto.setLastName("Smith");

        Person result = legalEntitiesService.createPerson(dto);
        assertNotNull(result.getId());
        assertEquals("people", result.getResourceType());
        assertEquals("Alice", result.getFirstName());
    }

    @Test
    void createPerson_missingFirstName_throwsBadRequest() {
        Person dto = new Person();
        dto.setLastName("Smith");
        assertThrows(BadRequestException.class, () -> legalEntitiesService.createPerson(dto));
    }

    @Test
    void createPerson_missingLastName_throwsBadRequest() {
        Person dto = new Person();
        dto.setFirstName("Alice");
        assertThrows(BadRequestException.class, () -> legalEntitiesService.createPerson(dto));
    }

    @Test
    void createPerson_nullPayload_throwsBadRequest() {
        assertThrows(BadRequestException.class, () -> legalEntitiesService.createPerson(null));
    }

    @Test
    void updatePerson_existingId_updatesFields() {
        Person patch = new Person();
        patch.setLastName("Updated");

        Person result = legalEntitiesService.updatePerson(PERSON_ID, patch);
        assertEquals("Updated", result.getLastName());
    }

    @Test
    void updatePerson_nonExistingId_throwsNotFound() {
        Person patch = new Person();
        patch.setLastName("X");
        assertThrows(ResourceNotFoundException.class,
                () -> legalEntitiesService.updatePerson(NON_EXISTING_ID, patch));
    }

    @Test
    void getPersonAuditTrail_existingId_returnsAudits() {
        List<PersonAudit> audits = legalEntitiesService.getPersonAuditTrail(PERSON_ID);
        assertNotNull(audits);
        assertFalse(audits.isEmpty());
    }

    @Test
    void getPersonAuditTrail_nonExistingId_throwsNotFound() {
        assertThrows(ResourceNotFoundException.class,
                () -> legalEntitiesService.getPersonAuditTrail(NON_EXISTING_ID));
    }
}

