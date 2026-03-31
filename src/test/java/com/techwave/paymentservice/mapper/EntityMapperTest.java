package com.techwave.paymentservice.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.techwave.paymentservice.entity.*;
import com.techwave.paymentservice.model.*;

class EntityMapperTest {

    private final EntityMapper mapper = new EntityMapper();

    @Test
    void toDto_countryEntity_mapsAllFields() {
        CountryEntity e = new CountryEntity();
        e.setId("GB"); e.setName("United Kingdom"); e.setNumericCode("826");
        e.setAlpha3Code("GBR"); e.setEurozone(false); e.setSepa(true);

        Country dto = mapper.toDto(e);
        assertEquals("GB", dto.getId());
        assertEquals("countries", dto.getResourceType());
        assertEquals("United Kingdom", dto.getName());
    }

    @Test
    void toDto_currencyEntity_mapsAllFields() {
        CurrencyEntity e = new CurrencyEntity();
        e.setId("EUR"); e.setName("Euro");

        Currency dto = mapper.toDto(e);
        assertEquals("EUR", dto.getId());
        assertEquals("currencies", dto.getResourceType());
    }

    @Test
    void toDto_siloEntity_mapsAllFields() {
        SiloEntity e = new SiloEntity();
        e.setId("silo-1"); e.setName("Test Silo"); e.setType("TREASURY");
        e.setDefaultCreditLimit(BigDecimal.TEN);

        Silo dto = mapper.toDto(e);
        assertEquals("silo-1", dto.getId());
        assertEquals("silos", dto.getResourceType());
    }

    @Test
    void toDto_corporationEntity_mapsAllFields() {
        CorporationEntity e = new CorporationEntity();
        e.setId(UUID.randomUUID()); e.setName("Test Corp"); e.setCode("TC-1");
        e.setIncorporationDate(LocalDate.of(2020, 1, 1)); e.setIncorporationCountry("US");

        Corporation dto = mapper.toDto(e);
        assertEquals("corporations", dto.getResourceType());
        assertEquals("Test Corp", dto.getName());
    }

    @Test
    void toEntity_corporation_mapsAllFields() {
        Corporation dto = new Corporation();
        dto.setId(UUID.randomUUID()); dto.setName("Corp"); dto.setCode("C1");

        CorporationEntity entity = mapper.toEntity(dto);
        assertEquals(dto.getId(), entity.getId());
        assertEquals("Corp", entity.getName());
    }

    @Test
    void toDto_personEntity_mapsAllFields() {
        PersonEntity e = new PersonEntity();
        e.setId(UUID.randomUUID()); e.setFirstName("John"); e.setLastName("Doe");

        Person dto = mapper.toDto(e);
        assertEquals("people", dto.getResourceType());
        assertEquals("John", dto.getFirstName());
    }

    @Test
    void toEntity_person_mapsAllFields() {
        Person dto = new Person();
        dto.setId(UUID.randomUUID()); dto.setFirstName("Jane"); dto.setLastName("Smith");

        PersonEntity entity = mapper.toEntity(dto);
        assertEquals("Jane", entity.getFirstName());
    }

    @Test
    void toDto_bankAccountEntity_mapsAllFields() {
        BankAccountEntity e = new BankAccountEntity();
        e.setId(UUID.randomUUID()); e.setBeneficiary("Test"); e.setCurrency("EUR"); e.setCountry("GB");

        BankAccount dto = mapper.toDto(e);
        assertEquals("bank-accounts", dto.getResourceType());
        assertEquals("Test", dto.getBeneficiary());
    }

    @Test
    void toAuditEntity_corporation_setsVersion() {
        CorporationEntity e = new CorporationEntity();
        e.setId(UUID.randomUUID()); e.setName("Audit Corp");

        CorporationAuditEntity audit = mapper.toAuditEntity(e, 3);
        assertEquals(3, audit.getVersion());
        assertEquals(e.getId(), audit.getResource());
    }

    @Test
    void toAuditEntity_person_setsVersion() {
        PersonEntity e = new PersonEntity();
        e.setId(UUID.randomUUID()); e.setFirstName("A"); e.setLastName("B");

        PersonAuditEntity audit = mapper.toAuditEntity(e, 2);
        assertEquals(2, audit.getVersion());
    }

    @Test
    void toAuditEntity_bankAccount_setsVersion() {
        BankAccountEntity e = new BankAccountEntity();
        e.setId(UUID.randomUUID()); e.setBeneficiary("BA");

        BankAccountAuditEntity audit = mapper.toAuditEntity(e, 1);
        assertEquals(1, audit.getVersion());
    }

    @Test
    void toLegalEntityDto_person_returnsPersonDto() {
        BeneficialOwnerEntity bo = new BeneficialOwnerEntity();
        bo.setOwnerType("PERSON");
        PersonEntity pe = new PersonEntity();
        pe.setId(UUID.randomUUID()); pe.setFirstName("X"); pe.setLastName("Y");

        LegalEntity dto = mapper.toLegalEntityDto(bo, pe);
        assertInstanceOf(Person.class, dto);
        assertEquals("people", dto.getResourceType());
    }

    @Test
    void toLegalEntityDto_corporation_returnsCorporationDto() {
        BeneficialOwnerEntity bo = new BeneficialOwnerEntity();
        bo.setOwnerType("CORPORATION");
        CorporationEntity ce = new CorporationEntity();
        ce.setId(UUID.randomUUID()); ce.setName("BO Corp");

        LegalEntity dto = mapper.toLegalEntityDto(bo, ce);
        assertInstanceOf(Corporation.class, dto);
        assertEquals("corporations", dto.getResourceType());
    }
}

