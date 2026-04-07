package com.techwave.paymentservice.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Entity Getters/Setters & Equals/HashCode Unit Tests")
class EntityGettersSettersTest {

    // ── PersonEntity ──────────────────────────────────────────

    @Test
    @DisplayName("PersonEntity getters, setters, equals, hashCode")
    void personEntity_gettersSetters() {
        PersonEntity entity = new PersonEntity();
        UUID id = UUID.randomUUID();
        UUID dupId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        entity.setId(id);
        entity.setResourceType("people");
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setDuplicates(dupId);
        entity.setVersion(1);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        assertEquals(id, entity.getId());
        assertEquals("people", entity.getResourceType());
        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertEquals(dupId, entity.getDuplicates());
        assertEquals(1, entity.getVersion());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());

        // equals/hashCode
        PersonEntity same = new PersonEntity();
        same.setId(id);
        assertEquals(entity, same);
        assertTrue(entity.equals(entity));
        assertFalse(entity.equals(null));
        assertFalse(entity.equals("not an entity"));

        PersonEntity other = new PersonEntity();
        other.setId(UUID.randomUUID());
        assertNotEquals(entity, other);

        // null id
        PersonEntity nullId = new PersonEntity();
        assertFalse(nullId.equals(entity));
    }

    // ── CorporationEntity ────────────────────────────────────

    @Test
    @DisplayName("CorporationEntity getters and setters")
    void corporationEntity_gettersSetters() {
        CorporationEntity entity = new CorporationEntity();
        UUID id = UUID.randomUUID();
        LocalDate date = LocalDate.of(2020, 1, 15);

        entity.setId(id);
        entity.setName("Acme Corp");
        entity.setCode("ACME");
        entity.setIncorporationDate(date);
        entity.setIncorporationCountry("US");
        entity.setType("LLC");
        entity.setVersion(1);

        assertEquals(id, entity.getId());
        assertEquals("Acme Corp", entity.getName());
        assertEquals("ACME", entity.getCode());
        assertEquals(date, entity.getIncorporationDate());
        assertEquals("US", entity.getIncorporationCountry());
        assertEquals("LLC", entity.getType());
    }

    // ── BankAccountEntity ────────────────────────────────────

    @Test
    @DisplayName("BankAccountEntity getters, setters, equals, hashCode")
    void bankAccountEntity_gettersSetters() {
        BankAccountEntity entity = new BankAccountEntity();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        entity.setId(id);
        entity.setBeneficiary("John Doe");
        entity.setBeneficiaryAddress("123 Main St");
        entity.setNickname("Main");
        entity.setIban("DE89370400440532013000");
        entity.setBic("COBADEFFXXX");
        entity.setAccountNumber("1234567890");
        entity.setNationalBankCode("370400");
        entity.setNationalBranchCode("0044");
        entity.setNationalClearingCode("CLEAR01");
        entity.setCurrency("EUR");
        entity.setCountry("DE");
        entity.setVersion(1);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        entity.setBeneficialOwners(new HashSet<>());

        assertEquals(id, entity.getId());
        assertEquals("John Doe", entity.getBeneficiary());
        assertEquals("123 Main St", entity.getBeneficiaryAddress());
        assertEquals("Main", entity.getNickname());
        assertEquals("DE89370400440532013000", entity.getIban());
        assertEquals("COBADEFFXXX", entity.getBic());
        assertEquals("1234567890", entity.getAccountNumber());
        assertEquals("370400", entity.getNationalBankCode());
        assertEquals("0044", entity.getNationalBranchCode());
        assertEquals("CLEAR01", entity.getNationalClearingCode());
        assertEquals("EUR", entity.getCurrency());
        assertEquals("DE", entity.getCountry());
        assertEquals(1, entity.getVersion());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
        assertNotNull(entity.getBeneficialOwners());

        // equals/hashCode
        BankAccountEntity same = new BankAccountEntity();
        same.setId(id);
        assertEquals(entity, same);
        assertTrue(entity.equals(entity));
        assertFalse(entity.equals(null));
        assertFalse(entity.equals("string"));

        BankAccountEntity diff = new BankAccountEntity();
        diff.setId(UUID.randomUUID());
        assertNotEquals(entity, diff);

        BankAccountEntity nullId = new BankAccountEntity();
        assertFalse(nullId.equals(entity));
    }

    // ── CustomerAccountEntity ────────────────────────────────

    @Test
    @DisplayName("CustomerAccountEntity getters, setters, equals, hashCode")
    void customerAccountEntity_gettersSetters() {
        CustomerAccountEntity entity = new CustomerAccountEntity();
        UUID id = UUID.randomUUID();
        UUID managerId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        entity.setId(id);
        entity.setName("Test Account");
        entity.setDescription("Desc");
        entity.setAccountType(
                CustomerAccountEntity.CustomerAccountType.PERSONAL);
        entity.setAccountState(
                CustomerAccountEntity.CustomerAccountState.ACCEPTED);
        entity.setAccountManager(managerId);
        entity.setAccountCreationTime(now);
        entity.setSilo("TREASURY");
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        entity.setBeneficialOwners(new HashSet<>());

        assertEquals(id, entity.getId());
        assertEquals("Test Account", entity.getName());
        assertEquals("Desc", entity.getDescription());
        assertEquals(CustomerAccountEntity.CustomerAccountType.PERSONAL,
                entity.getAccountType());
        assertEquals(CustomerAccountEntity.CustomerAccountState.ACCEPTED,
                entity.getAccountState());
        assertEquals(managerId, entity.getAccountManager());
        assertEquals(now, entity.getAccountCreationTime());
        assertEquals("TREASURY", entity.getSilo());
        assertEquals(now, entity.getCreatedAt());
        assertEquals(now, entity.getUpdatedAt());
        assertNotNull(entity.getBeneficialOwners());

        // equals/hashCode
        CustomerAccountEntity same = new CustomerAccountEntity();
        same.setId(id);
        assertEquals(entity, same);
        assertTrue(entity.equals(entity));
        assertFalse(entity.equals(null));
        assertFalse(entity.equals("string"));

        CustomerAccountEntity diff = new CustomerAccountEntity();
        diff.setId(UUID.randomUUID());
        assertNotEquals(entity, diff);

        CustomerAccountEntity nullId = new CustomerAccountEntity();
        assertFalse(nullId.equals(entity));
    }

    @Test
    @DisplayName("CustomerAccountType enum values")
    void customerAccountType_values() {
        CustomerAccountEntity.CustomerAccountType[] types =
                CustomerAccountEntity.CustomerAccountType.values();
        assertEquals(6, types.length);
        assertNotNull(CustomerAccountEntity.CustomerAccountType
                .valueOf("PERSONAL"));
        assertNotNull(CustomerAccountEntity.CustomerAccountType
                .valueOf("CORPORATE"));
    }

    @Test
    @DisplayName("CustomerAccountState enum values")
    void customerAccountState_values() {
        CustomerAccountEntity.CustomerAccountState[] states =
                CustomerAccountEntity.CustomerAccountState.values();
        assertEquals(8, states.length);
        assertNotNull(CustomerAccountEntity.CustomerAccountState
                .valueOf("ACCEPTED"));
        assertNotNull(CustomerAccountEntity.CustomerAccountState
                .valueOf("REJECTED"));
    }

    // ── CountryEntity ────────────────────────────────────────

    @Test
    @DisplayName("CountryEntity getters, setters, constructors, equals")
    void countryEntity_gettersSetters() {
        CountryEntity entity =
                new CountryEntity("US", "United States",
                        "840", "USA", false, false);

        assertEquals("US", entity.getId());
        assertEquals("United States", entity.getName());
        assertEquals("840", entity.getNumericCode());
        assertEquals("USA", entity.getAlpha3Code());
        assertFalse(entity.getEurozone());
        assertFalse(entity.getSepa());

        // Setters
        entity.setId("DE");
        entity.setName("Germany");
        entity.setNumericCode("276");
        entity.setAlpha3Code("DEU");
        entity.setEurozone(true);
        entity.setSepa(true);

        assertEquals("DE", entity.getId());
        assertEquals("Germany", entity.getName());
        assertEquals("276", entity.getNumericCode());
        assertEquals("DEU", entity.getAlpha3Code());
        assertTrue(entity.getEurozone());
        assertTrue(entity.getSepa());

        // No-args constructor
        CountryEntity empty = new CountryEntity();
        assertNull(empty.getId());

        // equals/hashCode
        CountryEntity same = new CountryEntity();
        same.setId("DE");
        assertEquals(entity, same);
        assertTrue(entity.equals(entity));
        assertFalse(entity.equals(null));
        assertFalse(entity.equals("string"));

        CountryEntity diff = new CountryEntity();
        diff.setId("FR");
        assertNotEquals(entity, diff);

        CountryEntity nullId = new CountryEntity();
        assertFalse(nullId.equals(entity));
    }

    // ── CurrencyEntity ───────────────────────────────────────

    @Test
    @DisplayName("CurrencyEntity getters, setters, constructors, equals")
    void currencyEntity_gettersSetters() {
        CurrencyEntity entity = new CurrencyEntity("EUR", "Euro");

        assertEquals("EUR", entity.getId());
        assertEquals("Euro", entity.getName());

        entity.setId("USD");
        entity.setName("US Dollar");

        assertEquals("USD", entity.getId());
        assertEquals("US Dollar", entity.getName());

        // No-args constructor
        CurrencyEntity empty = new CurrencyEntity();
        assertNull(empty.getId());

        // equals/hashCode
        CurrencyEntity same = new CurrencyEntity();
        same.setId("USD");
        assertEquals(entity, same);
        assertTrue(entity.equals(entity));
        assertFalse(entity.equals(null));
        assertFalse(entity.equals("string"));

        CurrencyEntity diff = new CurrencyEntity();
        diff.setId("GBP");
        assertNotEquals(entity, diff);

        CurrencyEntity nullId = new CurrencyEntity();
        assertFalse(nullId.equals(entity));
    }

    // ── SiloEntity ───────────────────────────────────────────

    @Test
    @DisplayName("SiloEntity getters, setters, equals, hashCode")
    void siloEntity_gettersSetters() {
        SiloEntity entity = new SiloEntity();
        entity.setId("TREASURY");
        entity.setName("Treasury");
        entity.setDescription("Main treasury");
        entity.setEmail("treasury@test.com");
        entity.setDefaultBaseCurrency("USD");
        entity.setDefaultCreditLimit(new BigDecimal("1000000"));
        entity.setDefaultProfitShare(new BigDecimal("0.15"));
        entity.setType(SiloEntity.SiloType.TREASURY);

        assertEquals("TREASURY", entity.getId());
        assertEquals("Treasury", entity.getName());
        assertEquals("Main treasury", entity.getDescription());
        assertEquals("treasury@test.com", entity.getEmail());
        assertEquals("USD", entity.getDefaultBaseCurrency());
        assertEquals(new BigDecimal("1000000"),
                entity.getDefaultCreditLimit());
        assertEquals(new BigDecimal("0.15"),
                entity.getDefaultProfitShare());
        assertEquals(SiloEntity.SiloType.TREASURY, entity.getType());

        // equals/hashCode
        SiloEntity same = new SiloEntity();
        same.setId("TREASURY");
        assertEquals(entity, same);
        assertTrue(entity.equals(entity));
        assertFalse(entity.equals(null));
        assertFalse(entity.equals("string"));

        SiloEntity diff = new SiloEntity();
        diff.setId("AGENT");
        assertNotEquals(entity, diff);

        SiloEntity nullId = new SiloEntity();
        assertFalse(nullId.equals(entity));
    }

    @Test
    @DisplayName("SiloType enum values")
    void siloType_values() {
        SiloEntity.SiloType[] types = SiloEntity.SiloType.values();
        assertEquals(4, types.length);
        assertNotNull(SiloEntity.SiloType.valueOf("TREASURY"));
        assertNotNull(SiloEntity.SiloType.valueOf("AGENT"));
    }

    // ── BankAccountAuditEntity ───────────────────────────────

    @Test
    @DisplayName("BankAccountAuditEntity getters, setters, equals")
    void bankAccountAuditEntity_gettersSetters() {
        BankAccountAuditEntity entity = new BankAccountAuditEntity();
        UUID resource = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        entity.setId(1L);
        entity.setResource(resource);
        entity.setVersion(1);
        entity.setBeneficiary("John Doe");
        entity.setBeneficiaryAddress("123 Main St");
        entity.setNickname("Main");
        entity.setIban("DE89370400440532013000");
        entity.setBic("COBADEFFXXX");
        entity.setAccountNumber("123456");
        entity.setNationalBankCode("3704");
        entity.setNationalBranchCode("0044");
        entity.setNationalClearingCode("CLR01");
        entity.setCurrency("EUR");
        entity.setCountry("DE");
        entity.setCreatedAt(now);

        assertEquals(1L, entity.getId());
        assertEquals(resource, entity.getResource());
        assertEquals(1, entity.getVersion());
        assertEquals("John Doe", entity.getBeneficiary());
        assertEquals("123 Main St", entity.getBeneficiaryAddress());
        assertEquals("Main", entity.getNickname());
        assertEquals("DE89370400440532013000", entity.getIban());
        assertEquals("COBADEFFXXX", entity.getBic());
        assertEquals("123456", entity.getAccountNumber());
        assertEquals("3704", entity.getNationalBankCode());
        assertEquals("0044", entity.getNationalBranchCode());
        assertEquals("CLR01", entity.getNationalClearingCode());
        assertEquals("EUR", entity.getCurrency());
        assertEquals("DE", entity.getCountry());
        assertEquals(now, entity.getCreatedAt());

        // equals/hashCode
        BankAccountAuditEntity same = new BankAccountAuditEntity();
        same.setId(1L);
        assertEquals(entity, same);
        assertTrue(entity.equals(entity));
        assertFalse(entity.equals(null));

        BankAccountAuditEntity diff = new BankAccountAuditEntity();
        diff.setId(2L);
        assertNotEquals(entity, diff);
    }

    // ── PersonAuditEntity ────────────────────────────────────

    @Test
    @DisplayName("PersonAuditEntity getters, setters, equals")
    void personAuditEntity_gettersSetters() {
        PersonAuditEntity entity = new PersonAuditEntity();
        UUID resource = UUID.randomUUID();
        UUID dupId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        entity.setId(1L);
        entity.setResource(resource);
        entity.setVersion(1);
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setDuplicates(dupId);
        entity.setCreatedAt(now);

        assertEquals(1L, entity.getId());
        assertEquals(resource, entity.getResource());
        assertEquals(1, entity.getVersion());
        assertEquals("John", entity.getFirstName());
        assertEquals("Doe", entity.getLastName());
        assertEquals(dupId, entity.getDuplicates());
        assertEquals(now, entity.getCreatedAt());

        // equals/hashCode
        PersonAuditEntity same = new PersonAuditEntity();
        same.setId(1L);
        assertEquals(entity, same);
        assertTrue(entity.equals(entity));
        assertFalse(entity.equals(null));

        PersonAuditEntity diff = new PersonAuditEntity();
        diff.setId(2L);
        assertNotEquals(entity, diff);
    }

    // ── CorporationAuditEntity ───────────────────────────────

    @Test
    @DisplayName("CorporationAuditEntity getters, setters, equals")
    void corporationAuditEntity_gettersSetters() {
        CorporationAuditEntity entity = new CorporationAuditEntity();
        UUID resource = UUID.randomUUID();
        UUID dupId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2020, 1, 15);
        LocalDateTime now = LocalDateTime.now();

        entity.setId(1L);
        entity.setResource(resource);
        entity.setVersion(1);
        entity.setName("Acme Corp");
        entity.setCode("ACME");
        entity.setIncorporationDate(date);
        entity.setIncorporationCountry("US");
        entity.setType("LLC");
        entity.setDuplicates(dupId);
        entity.setCreatedAt(now);

        assertEquals(1L, entity.getId());
        assertEquals(resource, entity.getResource());
        assertEquals(1, entity.getVersion());
        assertEquals("Acme Corp", entity.getName());
        assertEquals("ACME", entity.getCode());
        assertEquals(date, entity.getIncorporationDate());
        assertEquals("US", entity.getIncorporationCountry());
        assertEquals("LLC", entity.getType());
        assertEquals(dupId, entity.getDuplicates());
        assertEquals(now, entity.getCreatedAt());

        // equals/hashCode
        CorporationAuditEntity same = new CorporationAuditEntity();
        same.setId(1L);
        assertEquals(entity, same);
        assertTrue(entity.equals(entity));
        assertFalse(entity.equals(null));

        CorporationAuditEntity diff = new CorporationAuditEntity();
        diff.setId(2L);
        assertNotEquals(entity, diff);
    }
}

