package com.techwave.paymentservice.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DTO Getters/Setters Unit Tests")
class DtoGettersSettersTest {

    // ── PersonDto ─────────────────────────────────────────────

    @Test
    @DisplayName("PersonDto getters and setters")
    void personDto_gettersSetters() {
        PersonDto dto = new PersonDto();
        UUID id = UUID.randomUUID();
        UUID dupId = UUID.randomUUID();

        dto.setId(id);
        dto.setResourceType("people");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setDuplicates(dupId);

        assertEquals(id, dto.getId());
        assertEquals("people", dto.getResourceType());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals(dupId, dto.getDuplicates());
    }

    // ── CorporationDto ───────────────────────────────────────

    @Test
    @DisplayName("CorporationDto getters and setters")
    void corporationDto_gettersSetters() {
        CorporationDto dto = new CorporationDto();
        UUID id = UUID.randomUUID();
        UUID dupId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2020, 1, 15);

        dto.setId(id);
        dto.setResourceType("corporations");
        dto.setName("Acme Corp");
        dto.setCode("ACME");
        dto.setIncorporationDate(date);
        dto.setIncorporationCountry("US");
        dto.setType("LLC");
        dto.setDuplicates(dupId);

        assertEquals(id, dto.getId());
        assertEquals("corporations", dto.getResourceType());
        assertEquals("Acme Corp", dto.getName());
        assertEquals("ACME", dto.getCode());
        assertEquals(date, dto.getIncorporationDate());
        assertEquals("US", dto.getIncorporationCountry());
        assertEquals("LLC", dto.getType());
        assertEquals(dupId, dto.getDuplicates());
    }

    // ── BankAccountDto ───────────────────────────────────────

    @Test
    @DisplayName("BankAccountDto getters and setters")
    void bankAccountDto_gettersSetters() {
        BankAccountDto dto = new BankAccountDto();
        UUID id = UUID.randomUUID();

        dto.setId(id);
        dto.setResourceType("bank-accounts");
        dto.setBeneficiary("John Doe");
        dto.setBeneficiaryAddress("123 Main St");
        dto.setNickname("Main Account");
        dto.setIban("DE89370400440532013000");
        dto.setBic("COBADEFFXXX");
        dto.setAccountNumber("1234567890");
        dto.setNationalBankCode("370400");
        dto.setNationalBranchCode("0044");
        dto.setNationalClearingCode("CLEAR01");
        dto.setCurrency("EUR");
        dto.setCountry("DE");

        assertEquals(id, dto.getId());
        assertEquals("bank-accounts", dto.getResourceType());
        assertEquals("John Doe", dto.getBeneficiary());
        assertEquals("123 Main St", dto.getBeneficiaryAddress());
        assertEquals("Main Account", dto.getNickname());
        assertEquals("DE89370400440532013000", dto.getIban());
        assertEquals("COBADEFFXXX", dto.getBic());
        assertEquals("1234567890", dto.getAccountNumber());
        assertEquals("370400", dto.getNationalBankCode());
        assertEquals("0044", dto.getNationalBranchCode());
        assertEquals("CLEAR01", dto.getNationalClearingCode());
        assertEquals("EUR", dto.getCurrency());
        assertEquals("DE", dto.getCountry());
    }

    // ── CustomerAccountDto ───────────────────────────────────

    @Test
    @DisplayName("CustomerAccountDto getters and setters")
    void customerAccountDto_gettersSetters() {
        CustomerAccountDto dto = new CustomerAccountDto();
        UUID id = UUID.randomUUID();
        UUID managerId = UUID.randomUUID();
        OffsetDateTime time = OffsetDateTime.now();

        dto.setId(id);
        dto.setResourceType("customer-accounts");
        dto.setName("Test Account");
        dto.setDescription("Description");
        dto.setAccountType("PERSONAL");
        dto.setAccountState("ACCEPTED");
        dto.setAccountManager(managerId);
        dto.setAccountCreationTime(time);
        dto.setSilo("TREASURY");

        assertEquals(id, dto.getId());
        assertEquals("customer-accounts", dto.getResourceType());
        assertEquals("Test Account", dto.getName());
        assertEquals("Description", dto.getDescription());
        assertEquals("PERSONAL", dto.getAccountType());
        assertEquals("ACCEPTED", dto.getAccountState());
        assertEquals(managerId, dto.getAccountManager());
        assertEquals(time, dto.getAccountCreationTime());
        assertEquals("TREASURY", dto.getSilo());
    }

    // ── CountryDto ───────────────────────────────────────────

    @Test
    @DisplayName("CountryDto getters and setters")
    void countryDto_gettersSetters() {
        CountryDto dto = new CountryDto();
        dto.setId("US");
        dto.setResourceType("countries");
        dto.setName("United States");
        dto.setNumericCode("840");
        dto.setAlpha3Code("USA");
        dto.setEurozone(false);
        dto.setSepa(false);

        assertEquals("US", dto.getId());
        assertEquals("countries", dto.getResourceType());
        assertEquals("United States", dto.getName());
        assertEquals("840", dto.getNumericCode());
        assertEquals("USA", dto.getAlpha3Code());
        assertFalse(dto.getEurozone());
        assertFalse(dto.getSepa());
    }

    // ── CurrencyDto ──────────────────────────────────────────

    @Test
    @DisplayName("CurrencyDto getters and setters")
    void currencyDto_gettersSetters() {
        CurrencyDto dto = new CurrencyDto();
        dto.setId("EUR");
        dto.setResourceType("currencies");
        dto.setName("Euro");

        assertEquals("EUR", dto.getId());
        assertEquals("currencies", dto.getResourceType());
        assertEquals("Euro", dto.getName());
    }

    // ── SiloDto ──────────────────────────────────────────────

    @Test
    @DisplayName("SiloDto getters and setters")
    void siloDto_gettersSetters() {
        SiloDto dto = new SiloDto();
        dto.setId("TREASURY");
        dto.setResourceType("silos");
        dto.setName("Treasury");
        dto.setDescription("Main treasury");
        dto.setEmail("treasury@test.com");
        dto.setDefaultBaseCurrency("USD");
        dto.setDefaultCreditLimit(new BigDecimal("1000000"));
        dto.setDefaultProfitShare(new BigDecimal("0.15"));
        dto.setType("TREASURY");

        assertEquals("TREASURY", dto.getId());
        assertEquals("silos", dto.getResourceType());
        assertEquals("Treasury", dto.getName());
        assertEquals("Main treasury", dto.getDescription());
        assertEquals("treasury@test.com", dto.getEmail());
        assertEquals("USD", dto.getDefaultBaseCurrency());
        assertEquals(new BigDecimal("1000000"), dto.getDefaultCreditLimit());
        assertEquals(new BigDecimal("0.15"), dto.getDefaultProfitShare());
        assertEquals("TREASURY", dto.getType());
    }

    // ── PersonAuditDto ───────────────────────────────────────

    @Test
    @DisplayName("PersonAuditDto getters and setters")
    void personAuditDto_gettersSetters() {
        PersonAuditDto dto = new PersonAuditDto();
        UUID resource = UUID.randomUUID();
        UUID dupId = UUID.randomUUID();

        dto.setResource(resource);
        dto.setResourceType("person-audits");
        dto.setVersion(1);
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setDuplicates(dupId);

        assertEquals(resource, dto.getResource());
        assertEquals("person-audits", dto.getResourceType());
        assertEquals(1, dto.getVersion());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals(dupId, dto.getDuplicates());
    }

    // ── CorporationAuditDto ──────────────────────────────────

    @Test
    @DisplayName("CorporationAuditDto getters and setters")
    void corporationAuditDto_gettersSetters() {
        CorporationAuditDto dto = new CorporationAuditDto();
        UUID resource = UUID.randomUUID();
        UUID dupId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2020, 1, 15);

        dto.setResource(resource);
        dto.setResourceType("corporation-audits");
        dto.setVersion(2);
        dto.setName("Acme Corp");
        dto.setCode("ACME");
        dto.setIncorporationDate(date);
        dto.setIncorporationCountry("US");
        dto.setType("LLC");
        dto.setDuplicates(dupId);

        assertEquals(resource, dto.getResource());
        assertEquals("corporation-audits", dto.getResourceType());
        assertEquals(2, dto.getVersion());
        assertEquals("Acme Corp", dto.getName());
        assertEquals("ACME", dto.getCode());
        assertEquals(date, dto.getIncorporationDate());
        assertEquals("US", dto.getIncorporationCountry());
        assertEquals("LLC", dto.getType());
        assertEquals(dupId, dto.getDuplicates());
    }

    // ── BankAccountAuditDto ──────────────────────────────────

    @Test
    @DisplayName("BankAccountAuditDto getters and setters")
    void bankAccountAuditDto_gettersSetters() {
        BankAccountAuditDto dto = new BankAccountAuditDto();
        UUID resource = UUID.randomUUID();

        dto.setResource(resource);
        dto.setResourceType("bank-account-audits");
        dto.setVersion(1);
        dto.setBeneficiary("John Doe");
        dto.setBeneficiaryAddress("123 Main St");
        dto.setNickname("Main");
        dto.setIban("DE89370400440532013000");
        dto.setBic("COBADEFFXXX");
        dto.setAccountNumber("123456");
        dto.setNationalBankCode("3704");
        dto.setNationalBranchCode("0044");
        dto.setNationalClearingCode("CLR01");
        dto.setCurrency("EUR");
        dto.setCountry("DE");

        assertEquals(resource, dto.getResource());
        assertEquals("bank-account-audits", dto.getResourceType());
        assertEquals(1, dto.getVersion());
        assertEquals("John Doe", dto.getBeneficiary());
        assertEquals("123 Main St", dto.getBeneficiaryAddress());
        assertEquals("Main", dto.getNickname());
        assertEquals("DE89370400440532013000", dto.getIban());
        assertEquals("COBADEFFXXX", dto.getBic());
        assertEquals("123456", dto.getAccountNumber());
        assertEquals("3704", dto.getNationalBankCode());
        assertEquals("0044", dto.getNationalBranchCode());
        assertEquals("CLR01", dto.getNationalClearingCode());
        assertEquals("EUR", dto.getCurrency());
        assertEquals("DE", dto.getCountry());
    }
}

