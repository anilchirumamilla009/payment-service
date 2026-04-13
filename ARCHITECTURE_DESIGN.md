# Payment Service — Complete Architecture Design

## 1. Service Overview

**Purpose**: Manage state and access to core data entities: countries, currencies, silos, legal entities (people/corporations), bank accounts, and customer accounts.

**Tech Stack**:
- Language: Java 17
- Framework: Spring Boot 3.2.5
- Database: H2 (in-memory)
- ORM: Spring Data JPA / Hibernate
- Migrations: Flyway
- DTO Mapping: MapStruct 1.5.5
- API Spec: OpenAPI 3.0
- Testing: JUnit 5, Mockito, Spring MockMvc

**Architecture Pattern**: Layered (Controller → Service → Repository → Database)

---

## 2. Package Structure

```
src/main/java/com/techwave/paymentservice/
├── PaymentServiceApplication.java                  # Entry point
├── config/
│   ├── ApplicationConfig.java                     # @Configuration for Spring beans
│   └── AuditConfig.java                           # JPA audit configuration (if needed)
├── controller/
│   ├── CoreDataController.java                    # Countries, Currencies, Silos (GET only)
│   ├── CorporationController.java                 # POST, GET, PATCH, /audit-trail
│   ├── PersonController.java                      # POST, GET, PATCH, /audit-trail
│   ├── BankAccountController.java                 # PUT, GET, /audit-trail, /beneficial-owners
│   └── CustomerAccountController.java             # GET, /beneficial-owners
├── service/
│   ├── CoreDataService.java                       # Interface: Countries, Currencies, Silos
│   ├── LegalEntityService.java                    # Interface: Corporations & People
│   ├── BankAccountService.java                    # Interface: Bank Accounts
│   ├── CustomerAccountService.java                # Interface: Customer Accounts
│   ├── AuditService.java                          # Interface: Audit trail operations
│   └── impl/
│       ├── CoreDataServiceImpl.java
│       ├── LegalEntityServiceImpl.java
│       ├── BankAccountServiceImpl.java
│       ├── CustomerAccountServiceImpl.java
│       └── AuditServiceImpl.java
├── repository/
│   ├── CountryRepository.java                     # Spring Data JPA
│   ├── CurrencyRepository.java
│   ├── SiloRepository.java
│   ├── LegalEntityRepository.java                 # Base entity repository
│   ├── CorporationRepository.java                 # Specialized queries for Corporation
│   ├── PersonRepository.java                      # Specialized queries for Person
│   ├── BankAccountRepository.java
│   ├── BankAccountBeneficialOwnerRepository.java  # Join table repository
│   ├── CustomerAccountRepository.java
│   ├── CustomerAccountBeneficialOwnerRepository.java
│   ├── AuditLogRepository.java                    # Audit trail versioning
│   ├── CorporationAuditRepository.java
│   ├── PersonAuditRepository.java
│   └── BankAccountAuditRepository.java
├── entity/
│   ├── country/
│   │   └── CountryEntity.java                     # @Entity(true), @Id String, composable key
│   ├── currency/
│   │   └── CurrencyEntity.java                    # @Entity, @Id String
│   ├── silo/
│   │   ├── SiloEntity.java                        # @Entity
│   │   └── SiloType.java                          # Enum
│   ├── legalentity/
│   │   ├── LegalEntityEntity.java                 # @Entity @Inheritance(SINGLE_TABLE)
│   │   ├── PersonEntity.java                      # @Entity @DiscriminatorValue("people")
│   │   ├── CorporationEntity.java                 # @Entity @DiscriminatorValue("corporations")
│   │   └── ResourceType.java                      # Enum
│   ├── bankaccount/
│   │   ├── BankAccountEntity.java                 # @Entity, @Id UUID, many beneficial owners
│   │   ├── BankAccountBeneficialOwner.java        # Join entity @Entity
│   │   ├── BankAccountBeneficialOwnerId.java      # Composite key @Embeddable
│   │   └── AuditLogEntity.java                    # Audit trail for BankAccount
│   ├── customeraccount/
│   │   ├── CustomerAccountEntity.java             # @Entity
│   │   ├── CustomerAccountType.java               # Enum
│   │   ├── CustomerAccountState.java              # Enum
│   │   ├── CustomerAccountBeneficialOwner.java    # Join entity
│   │   ├── CustomerAccountBeneficialOwnerId.java  # Composite key
│   │   ├── CustomerAccountAuditLog.java           # Audit trail
│   │   └── ResourceType.java                      # Enum
│   └── audit/
│       ├── CorporationAudit.java                  # Audit versioning entity
│       ├── PersonAudit.java
│       └── BankAccountAudit.java
├── dto/
│   ├── request/
│   │   ├── CorporationRequest.java                # Create/Update
│   │   ├── PersonRequest.java
│   │   ├── BankAccountRequest.java
│   │   └── ValidationGroups.java                  # Validation groups for Create/Update
│   ├── response/
│   │   ├── CountryResponse.java
│   │   ├── CurrencyResponse.java
│   │   ├── SiloResponse.java
│   │   ├── CorporationResponse.java
│   │   ├── PersonResponse.java
│   │   ├── BankAccountResponse.java
│   │   ├── BankAccountAuditResponse.java
│   │   ├── CorporationAuditResponse.java
│   │   ├── PersonAuditResponse.java
│   │   ├── CustomerAccountResponse.java
│   │   ├── LegalEntityResponse.java                # Base response for polymorphism
│   │   └── BeneficialOwnerResponse.java            # Beneficial owners list
│   └── common/
│       ├── ExceptionDetailResponse.java            # Error responses
│       └── ResourceTypeEnum.java                   # resourceType field values
├── mapper/
│   ├── CountryMapper.java                         # @Mapper(componentModel = "spring")
│   ├── CurrencyMapper.java
│   ├── SiloMapper.java
│   ├── LegalEntityMapper.java                     # Base mapper with polymorphism
│   ├── CorporationMapper.java                     # @Mapper + extends
│   ├── PersonMapper.java
│   ├── BankAccountMapper.java
│   ├── BankAccountAuditMapper.java
│   ├── CustomerAccountMapper.java
│   ├── BeneficialOwnerMapper.java
│   ├── AuditMapper.java                           # Generic audit mapping
│   └── EnumMappers.java                           # Enum conversion utilities
├── exception/
│   ├── ResourceNotFoundException.java              # RuntimeException, HTTP 404
│   ├── BadRequestException.java                    # RuntimeException, HTTP 400
│   ├── ConflictException.java                      # RuntimeException, HTTP 409
│   ├── GlobalExceptionHandler.java                 # @RestControllerAdvice, @ControllerAdvice
│   └── ErrorResponse.java                          # Response structure
└── util/
    ├── AuditUtil.java                              # Audit trail helpers
    └── ValidationUtil.java                         # Validation helpers

src/main/resources/
├── application.yml                                 # Spring Boot configuration
├── application-dev.yml                             # Dev-specific config
├── openapi.yaml                                    # API contract (source of truth)
└── db/migration/
    ├── V1__init_schema.sql                         # Create all tables
    ├── V2__insert_reference_data.sql               # Countries, Currencies
    └── V3__audit_tables.sql                        # Audit trail tables

src/test/java/com/techwave/paymentservice/
├── service/
│   ├── CoreDataServiceImplTest.java               # Unit tests with Mockito
│   ├── LegalEntityServiceImplTest.java
│   ├── BankAccountServiceImplTest.java
│   └── CustomerAccountServiceImplTest.java
├── mapper/
│   ├── CorporationMapperTest.java
│   ├── PersonMapperTest.java
│   └── BankAccountMapperTest.java
└── integration/
    ├── CoreDataControllerTest.java                 # Integration tests with MockMvc
    ├── CorporationControllerTest.java
    ├── PersonControllerTest.java
    ├── BankAccountControllerTest.java
    └── CustomerAccountControllerTest.java
```

---

## 3. Entity Data Model

### 3.1 Reference Data Entities (Read-only)

#### CountryEntity
```java
@Entity
@Table(name = "countries", indexes = {
    @Index(name = "idx_country_alpha3", columnList = "alpha3_code"),
    @Index(name = "idx_country_numeric", columnList = "numeric_code")
})
public class CountryEntity {
    @Id
    @Column(name = "code", length = 2)
    private String id;  // ISO 3166-1 Alpha-2
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "numeric_code", nullable = false, length = 3)
    private String numericCode;  // ISO 3166-1 numeric
    
    @Column(name = "alpha3_code", nullable = false, length = 3)
    private String alpha3Code;  // ISO 3166-1 Alpha-3
    
    @Column(name = "is_eurozone", nullable = false)
    private boolean eurozone;
    
    @Column(name = "is_sepa", nullable = false)
    private boolean sepa;
    
    // Getters, Setters (no Lombok)
}
```

#### CurrencyEntity
```java
@Entity
@Table(name = "currencies")
public class CurrencyEntity {
    @Id
    @Column(name = "code", length = 3)
    private String id;  // ISO 4217 (e.g., USD, EUR)
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    // Getters, Setters
}
```

#### SiloEntity
```java
@Entity
@Table(name = "silos")
public class SiloEntity {
    @Id
    @Column(name = "id", length = 100)
    private String id;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "email", length = 255)
    private String email;
    
    @Column(name = "default_base_currency", length = 3)
    private String defaultBaseCurrency;  // CurrencyCode
    
    @Column(name = "default_credit_limit", precision = 20, scale = 2)
    private BigDecimal defaultCreditLimit;
    
    @Column(name = "default_profit_share", precision = 3, scale = 2)
    private BigDecimal defaultProfitShare;  // 0.0-1.0
    
    @Column(name = "silo_type", length = 50)
    @Enumerated(EnumType.STRING)
    private SiloType type;
    
    // Getters, Setters
}
```

#### SiloType Enum
```java
public enum SiloType {
    TREASURY,
    BUSINESS_UNIT,
    SUBSIDIARY,
    AGENT
}
```

### 3.2 Legal Entities (Single-Table Inheritance)

#### LegalEntityEntity (Base Class)
```java
@Entity
@Table(name = "legal_entities", indexes = {
    @Index(name = "idx_legal_entity_type", columnList = "entity_type"),
    @Index(name = "idx_legal_entity_created", columnList = "created_at")
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.STRING, length = 20)
public abstract class LegalEntityEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "version", nullable = false)
    @Version
    private Long version;
    
    // Getters, Setters
}
```

#### PersonEntity (Subclass)
```java
@Entity
@DiscriminatorValue("people")
public class PersonEntity extends LegalEntityEntity {
    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 255)
    private String lastName;
    
    @Column(name = "duplicates", columnDefinition = "UUID")
    private UUID duplicates;  // Reference to another Person
    
    // Getters, Setters
}
```

#### CorporationEntity (Subclass)
```java
@Entity
@DiscriminatorValue("corporations")
public class CorporationEntity extends LegalEntityEntity {
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "code", nullable = false, length = 100)
    private String code;
    
    @Column(name = "incorporation_date")
    private LocalDate incorporationDate;
    
    @Column(name = "incorporation_country", length = 2)
    private String incorporationCountry;  // CountryCode
    
    @Column(name = "corporation_type", length = 255)
    private String type;
    
    @Column(name = "duplicates", columnDefinition = "UUID")
    private UUID duplicates;  // Reference to another Corporation
    
    // Getters, Setters
}
```

### 3.3 Bank Accounts & Beneficial Owners

#### BankAccountEntity
```java
@Entity
@Table(name = "bank_accounts", indexes = {
    @Index(name = "idx_bank_account_iban", columnList = "iban"),
    @Index(name = "idx_bank_account_country", columnList = "country")
})
public class BankAccountEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "beneficiary", length = 255)
    private String beneficiary;
    
    @Column(name = "beneficiary_address", columnDefinition = "TEXT")
    private String beneficiaryAddress;
    
    @Column(name = "nickname", length = 255)
    private String nickname;
    
    @Column(name = "iban", length = 34)
    private String iban;  // ISO 13616
    
    @Column(name = "bic", length = 11)
    private String bic;  // ISO 9362
    
    @Column(name = "account_number", length = 255)
    private String accountNumber;
    
    @Column(name = "national_bank_code", length = 50)
    private String nationalBankCode;
    
    @Column(name = "national_branch_code", length = 50)
    private String nationalBranchCode;
    
    @Column(name = "national_clearing_code", length = 50)
    private String nationalClearingCode;
    
    @Column(name = "currency", length = 3)
    private String currency;  // CurrencyCode
    
    @Column(name = "country", length = 2)
    private String country;  // CountryCode
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "version", nullable = false)
    @Version
    private Long version;
    
    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BankAccountBeneficialOwner> beneficialOwners = new HashSet<>();
    
    // Getters, Setters
}
```

#### BankAccountBeneficialOwner (Join Entity)
```java
@Entity
@Table(name = "bank_account_beneficial_owners", indexes = {
    @Index(name = "idx_ba_bo_legal_entity", columnList = "legal_entity_id")
})
public class BankAccountBeneficialOwner {
    @EmbeddedId
    private BankAccountBeneficialOwnerId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bankAccountId")
    @JoinColumn(name = "bank_account_id")
    private BankAccountEntity bankAccount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("legalEntityId")
    @JoinColumn(name = "legal_entity_id")
    private LegalEntityEntity legalEntity;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Getters, Setters
}
```

#### BankAccountBeneficialOwnerId (Composite Key)
```java
@Embeddable
public class BankAccountBeneficialOwnerId implements Serializable {
    @Column(name = "bank_account_id")
    private UUID bankAccountId;
    
    @Column(name = "legal_entity_id")
    private UUID legalEntityId;
    
    // Constructor, Equals, HashCode, Getters, Setters
}
```

### 3.4 Customer Accounts

#### CustomerAccountEntity
```java
@Entity
@Table(name = "customer_accounts", indexes = {
    @Index(name = "idx_cust_account_silo", columnList = "silo_id"),
    @Index(name = "idx_cust_account_manager", columnList = "account_manager_id")
})
public class CustomerAccountEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(name = "name", nullable = false, length = 255)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "account_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private CustomerAccountType accountType;
    
    @Column(name = "account_state", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private CustomerAccountState accountState;
    
    @Column(name = "account_manager_id")
    private UUID accountManager;  // Reference to Person UUID
    
    @CreatedDate
    @Column(name = "account_creation_time", nullable = false, updatable = false)
    private LocalDateTime accountCreationTime;
    
    @Column(name = "silo_id", length = 100)
    private String silo;  // SiloEntity.id
    
    @OneToMany(mappedBy = "customerAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CustomerAccountBeneficialOwner> beneficialOwners = new HashSet<>();
    
    @Version
    @Column(name = "version", nullable = false)
    private Long version;
    
    // Getters, Setters
}
```

#### CustomerAccountType & State Enums
```java
public enum CustomerAccountType {
    PERSONAL,
    CORPORATE,
    CHARITY,
    AGENT,
    LIQUIDITY_PROVIDER,
    BANKER
}

public enum CustomerAccountState {
    DATA_REQUIRED,
    UNDER_REVIEW,
    ACCEPTED,
    REJECTED,
    NOT_REQUIRED,
    LAPSED,
    INACTIVE,
    CLOSED
}
```

#### CustomerAccountBeneficialOwner (Join Entity)
```java
@Entity
@Table(name = "customer_account_beneficial_owners")
public class CustomerAccountBeneficialOwner {
    @EmbeddedId
    private CustomerAccountBeneficialOwnerId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerAccountId")
    @JoinColumn(name = "customer_account_id")
    private CustomerAccountEntity customerAccount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("legalEntityId")
    @JoinColumn(name = "legal_entity_id")
    private LegalEntityEntity legalEntity;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Getters, Setters
}
```

#### CustomerAccountBeneficialOwnerId (Composite Key)
```java
@Embeddable
public class CustomerAccountBeneficialOwnerId implements Serializable {
    @Column(name = "customer_account_id")
    private UUID customerAccountId;
    
    @Column(name = "legal_entity_id")
    private UUID legalEntityId;
    
    // Constructor, Equals, HashCode, Getters, Setters
}
```

### 3.5 Audit Trail Entities

#### BankAccountAudit
```java
@Entity
@Table(name = "bank_account_audits")
public class BankAccountAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "resource_id", nullable = false)
    private UUID resource;  // BankAccountEntity.id
    
    @Column(name = "version", nullable = false)
    private Long version;  // revision number
    
    @Column(name = "beneficiary", length = 255)
    private String beneficiary;
    
    @Column(name = "beneficiary_address", columnDefinition = "TEXT")
    private String beneficiaryAddress;
    
    @Column(name = "nickname", length = 255)
    private String nickname;
    
    @Column(name = "iban", length = 34)
    private String iban;
    
    @Column(name = "bic", length = 11)
    private String bic;
    
    @Column(name = "account_number", length = 255)
    private String accountNumber;
    
    @Column(name = "national_bank_code", length = 50)
    private String nationalBankCode;
    
    @Column(name = "national_branch_code", length = 50)
    private String nationalBranchCode;
    
    @Column(name = "national_clearing_code", length = 50)
    private String nationalClearingCode;
    
    @Column(name = "currency", length = 3)
    private String currency;
    
    @Column(name = "country", length = 2)
    private String country;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Getters, Setters
}
```

#### CorporationAudit
```java
@Entity
@Table(name = "corporation_audits")
public class CorporationAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "resource_id", nullable = false)
    private UUID resource;  // CorporationEntity.id
    
    @Column(name = "version", nullable = false)
    private Long version;
    
    @Column(name = "name", length = 255)
    private String name;
    
    @Column(name = "code", length = 100)
    private String code;
    
    @Column(name = "incorporation_date")
    private LocalDate incorporationDate;
    
    @Column(name = "incorporation_country", length = 2)
    private String incorporationCountry;
    
    @Column(name = "corporation_type", length = 255)
    private String type;
    
    @Column(name = "duplicates")
    private UUID duplicates;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Getters, Setters
}
```

#### PersonAudit
```java
@Entity
@Table(name = "person_audits")
public class PersonAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "resource_id", nullable = false)
    private UUID resource;  // PersonEntity.id
    
    @Column(name = "version", nullable = false)
    private Long version;
    
    @Column(name = "first_name", length = 255)
    private String firstName;
    
    @Column(name = "last_name", length = 255)
    private String lastName;
    
    @Column(name = "duplicates")
    private UUID duplicates;
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Getters, Setters
}
```

---

## 4. DTO Design

### 4.1 Request DTOs (with Validation)

#### CorporationRequest / PersonRequest
```java
public record CorporationRequest(
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 255)
    String name,
    
    @NotBlank(message = "Code is required")
    @Size(min = 1, max = 100)
    String code,
    
    @PastOrPresent(message = "Incorporation date must not be in future")
    LocalDate incorporationDate,
    
    @Pattern(regexp = "^[A-Z]{2}$", message = "Invalid country code")
    String incorporationCountry,
    
    @Size(max = 255)
    String type,
    
    @Null(message = "Duplicates cannot be set on creation")
    UUID duplicates
) {}

public record PersonRequest(
    @NotBlank(message = "First name is required")
    @Size(min = 1, max = 255)
    String firstName,
    
    @NotBlank(message = "Last name is required")
    @Size(min = 1, max = 255)
    String lastName,
    
    @Null(message = "Duplicates cannot be set on creation")
    UUID duplicates
) {}
```

#### BankAccountRequest
```java
public record BankAccountRequest(
    @Size(max = 255)
    String beneficiary,
    
    @Size(max = 2000)
    String beneficiaryAddress,
    
    @Size(max = 255)
    String nickname,
    
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{30}$", message = "Invalid IBAN format")
    String iban,
    
    @Pattern(regexp = "^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$", message = "Invalid BIC format")
    String bic,
    
    @Size(max = 255)
    String accountNumber,
    
    @Size(max = 50)
    String nationalBankCode,
    
    @Size(max = 50)
    String nationalBranchCode,
    
    @Size(max = 50)
    String nationalClearingCode,
    
    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid currency code")
    String currency,
    
    @NotNull(message = "Country is required")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Invalid country code")
    String country
) {}
```

### 4.2 Response DTOs

#### CorporationResponse / PersonResponse
```java
public record CorporationResponse(
    UUID id,
    String resourceType,  // "corporations"
    String name,
    String code,
    LocalDate incorporationDate,
    String incorporationCountry,
    String type,
    UUID duplicates
) {}

public record PersonResponse(
    UUID id,
    String resourceType,  // "people"
    String firstName,
    String lastName,
    UUID duplicates
) {}
```

#### BankAccountResponse
```java
public record BankAccountResponse(
    UUID id,
    String resourceType,  // "bank-accounts"
    String beneficiary,
    String beneficiaryAddress,
    String nickname,
    String iban,
    String bic,
    String accountNumber,
    String nationalBankCode,
    String nationalBranchCode,
    String nationalClearingCode,
    String currency,
    String country
) {}
```

#### AuditResponse DTOs
```java
public record CorporationAuditResponse(
    UUID resource,
    String resourceType,  // "corporation-audits"
    Long version,
    String name,
    String code,
    LocalDate incorporationDate,
    String incorporationCountry,
    String type,
    UUID duplicates
) {}

public record PersonAuditResponse(
    UUID resource,
    String resourceType,  // "person-audits"
    Long version,
    String firstName,
    String lastName,
    UUID duplicates
) {}

public record BankAccountAuditResponse(
    UUID resource,
    String resourceType,  // "bank-account-audits"
    Long version,
    String beneficiary,
    String beneficiaryAddress,
    String nickname,
    String iban,
    String bic,
    String accountNumber,
    String nationalBankCode,
    String nationalBranchCode,
    String nationalClearingCode,
    String currency,
    String country
) {}
```

#### Reference Data Responses
```java
public record CountryResponse(
    String id,
    String resourceType,  // "countries"
    String name,
    String numericCode,
    String alpha3Code,
    boolean eurozone,
    boolean sepa
) {}

public record CurrencyResponse(
    String id,
    String resourceType,  // "currencies"
    String name
) {}

public record SiloResponse(
    String id,
    String resourceType,  // "silos"
    String name,
    String description,
    String email,
    String defaultBaseCurrency,
    BigDecimal defaultCreditLimit,
    BigDecimal defaultProfitShare,
    String type
) {}

public record CustomerAccountResponse(
    UUID id,
    String resourceType,  // "customer-accounts"
    String name,
    String description,
    String accountType,
    String accountState,
    UUID accountManager,
    LocalDateTime accountCreationTime,
    String silo
) {}

public record LegalEntityResponse(
    UUID id,
    String resourceType  // "people" or "corporations"
) {}

public record BeneficialOwnerResponse(
    UUID id,
    String resourceType,  // "people" or "corporations"
    String firstName,
    String lastName,
    String name,
    String code
) {}
```

---

## 5. Repository Interfaces

```java
// Reference Data Repositories
public interface CountryRepository extends JpaRepository<CountryEntity, String> {
    List<CountryEntity> findByEurozone(boolean eurozone);
    List<CountryEntity> findBySepa(boolean sepa);
}

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, String> {}

public interface SiloRepository extends JpaRepository<SiloEntity, String> {
    List<SiloEntity> findByType(SiloType type);
}

// Legal Entity Repositories
public interface LegalEntityRepository extends JpaRepository<LegalEntityEntity, UUID> {}

public interface CorporationRepository extends JpaRepository<CorporationEntity, UUID> {
    Optional<CorporationEntity> findByCodeAndIncorporationCountry(String code, String country);
    List<CorporationEntity> findByIncorporationCountry(String country);
}

public interface PersonRepository extends JpaRepository<PersonEntity, UUID> {
    List<PersonEntity> findByFirstNameAndLastName(String firstName, String lastName);
}

// Bank Account Repositories
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, UUID> {
    Optional<BankAccountEntity> findByIban(String iban);
    List<BankAccountEntity> findByCurrency(String currency);
    List<BankAccountEntity> findByCountry(String country);
}

public interface BankAccountBeneficialOwnerRepository 
    extends JpaRepository<BankAccountBeneficialOwner, BankAccountBeneficialOwnerId> {
    List<BankAccountBeneficialOwner> findByIdBankAccountId(UUID bankAccountId);
    List<BankAccountBeneficialOwner> findByIdLegalEntityId(UUID legalEntityId);
}

// Customer Account Repositories
public interface CustomerAccountRepository extends JpaRepository<CustomerAccountEntity, UUID> {
    List<CustomerAccountEntity> findBySilo(String silo);
    List<CustomerAccountEntity> findByAccountType(CustomerAccountType type);
    List<CustomerAccountEntity> findByAccountState(CustomerAccountState state);
}

public interface CustomerAccountBeneficialOwnerRepository 
    extends JpaRepository<CustomerAccountBeneficialOwner, CustomerAccountBeneficialOwnerId> {
    List<CustomerAccountBeneficialOwner> findByIdCustomerAccountId(UUID customerAccountId);
}

// Audit Repositories
public interface CorporationAuditRepository extends JpaRepository<CorporationAudit, Long> {
    List<CorporationAudit> findByResourceOrderByVersionDesc(UUID resourceId);
}

public interface PersonAuditRepository extends JpaRepository<PersonAudit, Long> {
    List<PersonAudit> findByResourceOrderByVersionDesc(UUID resourceId);
}

public interface BankAccountAuditRepository extends JpaRepository<BankAccountAudit, Long> {
    List<BankAccountAudit> findByResourceOrderByVersionDesc(UUID resourceId);
}
```

---

## 6. Service Layer Specifications

### 6.1 Service Interfaces

```java
// Core Data Service
public interface CoreDataService {
    List<CountryResponse> getCountries();
    CountryResponse getCountry(String id);
    List<CurrencyResponse> getCurrencies();
    CurrencyResponse getCurrency(String id);
    List<SiloResponse> getSilos();
    SiloResponse getSilo(String id);
}

// Legal Entity Service
public interface LegalEntityService {
    // Corporations
    CorporationResponse createCorporation(CorporationRequest request);
    CorporationResponse getCorporation(UUID id);
    CorporationResponse updateCorporation(UUID id, CorporationRequest request);
    CorporationResponse getCorporationByCode(String country, String code);
    List<CorporationAuditResponse> getCorporationAuditTrail(UUID id);
    
    // People
    PersonResponse createPerson(PersonRequest request);
    PersonResponse getPerson(UUID id);
    PersonResponse updatePerson(UUID id, PersonRequest request);
    List<PersonAuditResponse> getPersonAuditTrail(UUID id);
}

// Bank Account Service
public interface BankAccountService {
    BankAccountResponse createOrLocateBankAccount(BankAccountRequest request);
    BankAccountResponse getBankAccount(UUID id);
    List<BankAccountAuditResponse> getBankAccountAuditTrail(UUID id);
    List<LegalEntityResponse> getBankAccountBeneficialOwners(UUID id);
}

// Customer Account Service
public interface CustomerAccountService {
    CustomerAccountResponse getCustomerAccount(UUID id);
    List<LegalEntityResponse> getCustomerAccountBeneficialOwners(UUID id);
}

// Audit Service
public interface AuditService {
    void recordCorporationAudit(CorporationEntity entity);
    void recordPersonAudit(PersonEntity entity);
    void recordBankAccountAudit(BankAccountEntity entity);
}
```

### 6.2 Service Implementations

Each `*ServiceImpl` will:
- Inject repositories via constructor
- Handle business logic
- Use mappers to convert Entity ↔ DTO
- Throw `ResourceNotFoundException` for missing resources
- Throw `BadRequestException` for validation errors
- Record audit trail on mutations
- Return appropriate DTOs

---

## 7. Controller Layer Specifications

### 7.1 Controller Routes

```java
@RestController
@RequestMapping("/api/v1")
public class CoreDataController {
    // GET /countries
    @GetMapping("/countries")
    public List<CountryResponse> getCountries() { }
    
    // GET /countries/{id}
    @GetMapping("/countries/{id}")
    public CountryResponse getCountry(@PathVariable String id) { }
    
    // GET /currencies
    @GetMapping("/currencies")
    public List<CurrencyResponse> getCurrencies() { }
    
    // GET /currencies/{id}
    @GetMapping("/currencies/{id}")
    public CurrencyResponse getCurrency(@PathVariable String id) { }
    
    // GET /silos
    @GetMapping("/silos")
    public List<SiloResponse> getSilos() { }
    
    // GET /silos/{id}
    @GetMapping("/silos/{id}")
    public SiloResponse getSilo(@PathVariable String id) { }
}

@RestController
@RequestMapping("/api/v1")
public class CorporationController {
    // POST /corporations
    @PostMapping("/corporations")
    public ResponseEntity<CorporationResponse> createCorporation(
        @Valid @RequestBody CorporationRequest request) { }
    
    // GET /corporations/{uuid}
    @GetMapping("/corporations/{uuid}")
    public CorporationResponse getCorporation(@PathVariable UUID uuid) { }
    
    // PATCH /corporations/{uuid}
    @PatchMapping("/corporations/{uuid}")
    public CorporationResponse updateCorporation(
        @PathVariable UUID uuid,
        @Valid @RequestBody CorporationRequest request) { }
    
    // GET /corporations/{uuid}/audit-trail
    @GetMapping("/corporations/{uuid}/audit-trail")
    public List<CorporationAuditResponse> getCorporationAuditTrail(
        @PathVariable UUID uuid) { }
    
    // GET /corporations/{country}/{code}
    @GetMapping("/corporations/{country}/{code}")
    public CorporationResponse getCorporationByCode(
        @PathVariable String country,
        @PathVariable String code) { }
}

@RestController
@RequestMapping("/api/v1")
public class PersonController {
    // POST /people
    @PostMapping("/people")
    public ResponseEntity<PersonResponse> createPerson(
        @Valid @RequestBody PersonRequest request) { }
    
    // GET /people/{uuid}
    @GetMapping("/people/{uuid}")
    public PersonResponse getPerson(@PathVariable UUID uuid) { }
    
    // PATCH /people/{uuid}
    @PatchMapping("/people/{uuid}")
    public PersonResponse updatePerson(
        @PathVariable UUID uuid,
        @Valid @RequestBody PersonRequest request) { }
    
    // GET /people/{uuid}/audit-trail
    @GetMapping("/people/{uuid}/audit-trail")
    public List<PersonAuditResponse> getPersonAuditTrail(
        @PathVariable UUID uuid) { }
}

@RestController
@RequestMapping("/api/v1")
public class BankAccountController {
    // PUT /bank-accounts
    @PutMapping("/bank-accounts")
    public ResponseEntity<BankAccountResponse> createBankAccount(
        @Valid @RequestBody BankAccountRequest request) { }
    
    // GET /bank-accounts/{uuid}
    @GetMapping("/bank-accounts/{uuid}")
    public BankAccountResponse getBankAccount(@PathVariable UUID uuid) { }
    
    // GET /bank-accounts/{uuid}/audit-trail
    @GetMapping("/bank-accounts/{uuid}/audit-trail")
    public List<BankAccountAuditResponse> getBankAccountAuditTrail(
        @PathVariable UUID uuid) { }
    
    // GET /bank-accounts/{uuid}/beneficial-owners
    @GetMapping("/bank-accounts/{uuid}/beneficial-owners")
    public List<LegalEntityResponse> getBankAccountBeneficialOwners(
        @PathVariable UUID uuid) { }
}

@RestController
@RequestMapping("/api/v1")
public class CustomerAccountController {
    // GET /customer-accounts/{uuid}
    @GetMapping("/customer-accounts/{uuid}")
    public CustomerAccountResponse getCustomerAccount(@PathVariable UUID uuid) { }
    
    // GET /customer-accounts/{uuid}/beneficial-owners
    @GetMapping("/customer-accounts/{uuid}/beneficial-owners")
    public List<LegalEntityResponse> getCustomerAccountBeneficialOwners(
        @PathVariable UUID uuid) { }
}
```

---

## 8. MapStruct Mapper Specifications

```java
@Mapper(componentModel = "spring")
public interface CountryMapper {
    CountryResponse toResponse(CountryEntity entity);
    List<CountryResponse> toResponses(List<CountryEntity> entities);
}

@Mapper(componentModel = "spring")
public interface CorporationMapper {
    CorporationResponse toResponse(CorporationEntity entity);
    CorporationEntity toEntity(CorporationRequest request);
    CorporationAuditResponse toAuditResponse(CorporationAudit audit);
    List<CorporationAuditResponse> toAuditResponses(List<CorporationAudit> audits);
}

@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonResponse toResponse(PersonEntity entity);
    PersonEntity toEntity(PersonRequest request);
    PersonAuditResponse toAuditResponse(PersonAudit audit);
    List<PersonAuditResponse> toAuditResponses(List<PersonAudit> audits);
}

@Mapper(componentModel = "spring")
public interface BankAccountMapper {
    BankAccountResponse toResponse(BankAccountEntity entity);
    BankAccountEntity toEntity(BankAccountRequest request);
    BankAccountAuditResponse toAuditResponse(BankAccountAudit audit);
    List<BankAccountAuditResponse> toAuditResponses(List<BankAccountAudit> audits);
}

@Mapper(componentModel = "spring")
public interface BeneficialOwnerMapper {
    LegalEntityResponse toResponse(LegalEntityEntity entity);
    LegalEntityResponse toResponse(PersonEntity entity);
    LegalEntityResponse toResponse(CorporationEntity entity);
    List<LegalEntityResponse> toResponses(List<LegalEntityEntity> entities);
}
```

---

## 9. Exception Handling

```java
// Custom Exceptions
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, String identifier) {
        super(String.format("%s not found: %s", resourceType, identifier));
    }
}

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}

// Global Exception Handler
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
        ResourceNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(404, "Not Found", ex.getMessage()));
    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(
        BadRequestException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(400, "Bad Request", ex.getMessage()));
    }
    
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(
        ConflictException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ErrorResponse(409, "Conflict", ex.getMessage()));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
        MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> messages = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(400, "Validation Error", 
                "Validation failed", messages));
    }
}

// Error Response DTO
public record ErrorResponse(
    int status,
    String error,
    String message,
    List<String> messages
) {
    public ErrorResponse(int status, String error, String message) {
        this(status, error, message, List.of(message));
    }
}
```

---

## 10. Database Schema (Flyway Migrations)

### V1__init_schema.sql
```sql
-- Reference Data Tables

CREATE TABLE countries (
    code VARCHAR(2) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    numeric_code VARCHAR(3) NOT NULL UNIQUE,
    alpha3_code VARCHAR(3) NOT NULL UNIQUE,
    is_eurozone BOOLEAN NOT NULL DEFAULT FALSE,
    is_sepa BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_country_alpha3 ON countries(alpha3_code);
CREATE INDEX idx_country_numeric ON countries(numeric_code);

CREATE TABLE currencies (
    code VARCHAR(3) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE silos (
    id VARCHAR(100) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    email VARCHAR(255),
    default_base_currency VARCHAR(3),
    default_credit_limit DECIMAL(20, 2),
    default_profit_share DECIMAL(3, 2),
    silo_type VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (default_base_currency) REFERENCES currencies(code)
);

-- Legal Entities (Single-Table Inheritance)

CREATE TABLE legal_entities (
    id UUID PRIMARY KEY,
    entity_type VARCHAR(20) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    name VARCHAR(255),
    code VARCHAR(100),
    incorporation_date DATE,
    incorporation_country VARCHAR(2),
    corporation_type VARCHAR(255),
    duplicates UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 1,
    FOREIGN KEY (incorporation_country) REFERENCES countries(code)
);

CREATE INDEX idx_legal_entity_type ON legal_entities(entity_type);
CREATE INDEX idx_legal_entity_created ON legal_entities(created_at);

-- Bank Accounts

CREATE TABLE bank_accounts (
    id UUID PRIMARY KEY,
    beneficiary VARCHAR(255),
    beneficiary_address TEXT,
    nickname VARCHAR(255),
    iban VARCHAR(34),
    bic VARCHAR(11),
    account_number VARCHAR(255),
    national_bank_code VARCHAR(50),
    national_branch_code VARCHAR(50),
    national_clearing_code VARCHAR(50),
    currency VARCHAR(3),
    country VARCHAR(2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 1,
    FOREIGN KEY (currency) REFERENCES currencies(code),
    FOREIGN KEY (country) REFERENCES countries(code),
    UNIQUE(iban)
);

CREATE INDEX idx_bank_account_iban ON bank_accounts(iban);
CREATE INDEX idx_bank_account_country ON bank_accounts(country);

-- Bank Account Beneficial Owners (Many-to-Many)

CREATE TABLE bank_account_beneficial_owners (
    bank_account_id UUID NOT NULL,
    legal_entity_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (bank_account_id, legal_entity_id),
    FOREIGN KEY (bank_account_id) REFERENCES bank_accounts(id) ON DELETE CASCADE,
    FOREIGN KEY (legal_entity_id) REFERENCES legal_entities(id)
);

CREATE INDEX idx_ba_bo_legal_entity ON bank_account_beneficial_owners(legal_entity_id);

-- Customer Accounts

CREATE TABLE customer_accounts (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    account_type VARCHAR(50) NOT NULL,
    account_state VARCHAR(50) NOT NULL,
    account_manager_id UUID,
    account_creation_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    silo_id VARCHAR(100),
    version BIGINT NOT NULL DEFAULT 1,
    FOREIGN KEY (account_manager_id) REFERENCES legal_entities(id),
    FOREIGN KEY (silo_id) REFERENCES silos(id)
);

CREATE INDEX idx_cust_account_silo ON customer_accounts(silo_id);
CREATE INDEX idx_cust_account_manager ON customer_accounts(account_manager_id);

-- Customer Account Beneficial Owners (Many-to-Many)

CREATE TABLE customer_account_beneficial_owners (
    customer_account_id UUID NOT NULL,
    legal_entity_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (customer_account_id, legal_entity_id),
    FOREIGN KEY (customer_account_id) REFERENCES customer_accounts(id) ON DELETE CASCADE,
    FOREIGN KEY (legal_entity_id) REFERENCES legal_entities(id)
);
```

### V2__insert_reference_data.sql
```sql
-- Insert reference countries (sample)
INSERT INTO countries (code, name, numeric_code, alpha3_code, is_eurozone, is_sepa) VALUES
('US', 'United States', '840', 'USA', false, false),
('GB', 'United Kingdom', '826', 'GBR', false, false),
('DE', 'Germany', '276', 'DEU', true, true),
('FR', 'France', '250', 'FRA', true, true),
('IT', 'Italy', '380', 'ITA', true, true),
('ES', 'Spain', '724', 'ESP', true, true),
('IE', 'Ireland', '372', 'IRL', true, true);

-- Insert reference currencies (sample)
INSERT INTO currencies (code, name) VALUES
('USD', 'US Dollar'),
('EUR', 'Euro'),
('GBP', 'British Pound'),
('JPY', 'Japanese Yen'),
('CHF', 'Swiss Franc');
```

### V3__audit_tables.sql
```sql
-- Bank Account Audit Trail

CREATE TABLE bank_account_audits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resource_id UUID NOT NULL,
    version BIGINT NOT NULL,
    beneficiary VARCHAR(255),
    beneficiary_address TEXT,
    nickname VARCHAR(255),
    iban VARCHAR(34),
    bic VARCHAR(11),
    account_number VARCHAR(255),
    national_bank_code VARCHAR(50),
    national_branch_code VARCHAR(50),
    national_clearing_code VARCHAR(50),
    currency VARCHAR(3),
    country VARCHAR(2),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ba_audit_resource ON bank_account_audits(resource_id, version DESC);

-- Corporation Audit Trail

CREATE TABLE corporation_audits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resource_id UUID NOT NULL,
    version BIGINT NOT NULL,
    name VARCHAR(255),
    code VARCHAR(100),
    incorporation_date DATE,
    incorporation_country VARCHAR(2),
    corporation_type VARCHAR(255),
    duplicates UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_corp_audit_resource ON corporation_audits(resource_id, version DESC);

-- Person Audit Trail

CREATE TABLE person_audits (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resource_id UUID NOT NULL,
    version BIGINT NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    duplicates UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_person_audit_resource ON person_audits(resource_id, version DESC);
```

---

## 11. Configuration

### application.yml
```yaml
spring:
  application:
    name: payment-service
  
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.H2Dialect
        jdbc:
          batch_size: 15
          fetch_size: 50
  
  datasource:
    url: jdbc:h2:mem:paymentdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 2
  
  h2:
    console:
      enabled: true
      path: /h2-console
  
  flyway:
    baseline-on-migrate: true
    locations: classpath:db/migration

server:
  port: 8080
  servlet:
    context-path: /api/v1

logging:
  level:
    root: INFO
    com.techwave.paymentservice: DEBUG
    org.springframework.web: INFO
    org.hibernate.sql: DEBUG
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE

management:
  endpoints:
    web:
      exposure:
        include: health,info
  endpoint:
    health:
      show-details: when-authorized
```

---

## 12. pom.xml Dependencies Addition

**Note**: The following dependency is required for proper audit trail support:

```xml
<!-- Hibernate Envers for Audit Support (Optional) -->
<!-- If using Envers instead of custom audit tables -->
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-envers</artifactId>
</dependency>

<!-- MapStruct Processor (already in pom.xml, ensure version matches) -->
<annotationProcessorPath>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct-processor</artifactId>
    <version>${mapstruct.version}</version>
</annotationProcessorPath>
```

**Current pom.xml is sufficient** for the base implementation. No additional dependencies are strictly required, but the audit implementation can be enhanced with Hibernate Envers if desired.

---

## 13. Implementation Order (Dependency-First)

### Phase 1: Foundation (Reference Data & Base Infrastructure)
1. **Create Entity classes** (CountryEntity, CurrencyEntity, SiloEntity, LegalEntityEntity base)
2. **Create Repository interfaces** (CountryRepository, CurrencyRepository, SiloRepository, LegalEntityRepository)
3. **Create DTOs** (response DTOs for reference data)
4. **Create Mappers** (CountryMapper, CurrencyMapper, SiloMapper)
5. **Create Service interfaces** (CoreDataService)
6. **Create Service implementations** (CoreDataServiceImpl)
7. **Create Controller** (CoreDataController)
8. **Create Flyway migrations** (V1, V2 - reference data)

### Phase 2: Legal Entities (with inheritance)
1. **Create PersonEntity & CorporationEntity** (extending LegalEntityEntity)
2. **Create specialized repositories** (PersonRepository, CorporationRepository)
3. **Create Request/Response DTOs** (PersonRequest, CorporationRequest, PersonResponse, CorporationResponse)
4. **Create Mappers** (PersonMapper, CorporationMapper)
5. **Create AuditLog entities & repositories** (PersonAudit, CorporationAudit)
6. **Create Service interface & impl** (LegalEntityService/Impl - Corporations and People)
7. **Create Controllers** (CorporationController, PersonController)
8. **Create exception handlers** (GlobalExceptionHandler)

### Phase 3: Bank Accounts & Beneficial Owners
1. **Create BankAccountEntity** (with version for optimistic locking)
2. **Create beneficial owner join entity & composite key** (BankAccountBeneficialOwner, BankAccountBeneficialOwnerId)
3. **Create repositories** (BankAccountRepository, BankAccountBeneficialOwnerRepository)
4. **Create BankAccountAudit entity & repository**
5. **Create Request/Response DTOs** (BankAccountRequest, BankAccountResponse, AuditResponse)
6. **Create Mappers** (BankAccountMapper, BankAccountAuditMapper)
7. **Create Service interface & impl** (BankAccountService/Impl)
8. **Create Controller** (BankAccountController)
9. **Create Flyway migration** (update schema with bank accounts)

### Phase 4: Customer Accounts
1. **Create CustomerAccountEntity** (with state and type enums)
2. **Create beneficial owner join entity** (CustomerAccountBeneficialOwner)
3. **Create repositories** (CustomerAccountRepository, CustomerAccountBeneficialOwnerRepository)
4. **Create Request/Response DTOs** (CustomerAccountResponse)
5. **Create Mapper** (CustomerAccountMapper, BeneficialOwnerMapper)
6. **Create Service interface & impl** (CustomerAccountService/Impl)
7. **Create Controller** (CustomerAccountController)

### Phase 5: Audit & Testing
1. **Implement AuditService & AuditServiceImpl** (record mutations)
2. **Integrate audit recording into service implementations** (on create/update)
3. **Write unit tests** (Service layer with Mockito)
4. **Write integration tests** (Controller layer with MockMvc)
5. **Finalize Flyway migrations** (V3 - audit tables)
6. **Configuration & application.yml finalization**

---

## 14. Key Design Decisions

### Single-Table Inheritance for Legal Entities
- **Why**: Simplifies querying for all legal entities in beneficial owner relationships
- **Strategy**: `InheritanceType.SINGLE_TABLE` with `@DiscriminatorColumn(name = "entity_type")`
- **Values**: "people", "corporations"

### Composite Keys for Beneficial Owners
- **Why**: Ensures uniqueness on (account, legalEntity) pairs
- **Implementation**: `@Embeddable` class with `@EmbeddedId` in join entity

### Optimistic Locking via @Version
- **Why**: Prevents concurrent modification issues
- **Fields**: `version` on LegalEntityEntity, BankAccountEntity, CustomerAccountEntity

### Manual Audit Trail (Custom Tables)
- **Why**: Flexibility over Envers
- **Implementation**: Separate audit entities with version tracking
- **Pattern**: On entity mutation, record previous state in audit table

### MapStruct Without Lombok
- **Why**: Explicit getters/setters in generated mappers
- **Pattern**: Use Java records for DTOs where possible, explicit pojos for entities

### UUID Primary Keys
- **Why**: Distributed system friendly, privacy-preserving vs sequential IDs
- **Generation**: `@GeneratedValue(strategy = GenerationType.UUID)` (Java 17 compatible)

### Error Response Consistency
- **Structure**: `{ status, error, message, messages[] }`
- **HTTP Status Codes**: 
  - 200 OK (GET, successful operations)
  - 201 Created (POST)
  - 204 No Content (DELETE)
  - 400 Bad Request (validation)
  - 404 Not Found
  - 409 Conflict
  - 5XX Server errors

---

## 15. Validation Summary

| Entity | Constraint | Rule |
|--------|-----------|------|
| Corporation | name | Required, max 255 chars |
| Corporation | code | Required, max 100 chars |
| Corporation | incorporationDate | Must be past or present |
| Person | firstName | Required, max 255 chars |
| Person | lastName | Required, max 255 chars |
| BankAccount | country | Required, 2-char country code |
| BankAccount | iban | ISO 13616 format (if provided) |
| BankAccount | bic | ISO 9362 format (if provided) |
| CustomerAccount | name | Required, max 255 chars |
| CustomerAccount | accountType | Required, one of enum values |
| CustomerAccount | accountState | Required, one of enum values |

---

## 16. API Contract Summary (OpenAPI Conformance)

| Endpoint | Method | Request | Response | Errors |
|----------|--------|---------|----------|--------|
| /countries | GET | - | Country[] | 400, 401, 403, 5XX |
| /countries/{id} | GET | - | Country | 400, 401, 403, 5XX |
| /corporations | POST | CorporationRequest | Corporation | 400, 401, 403, 5XX |
| /corporations/{uuid} | GET | - | Corporation | 400, 401, 403, 5XX |
| /corporations/{uuid} | PATCH | CorporationRequest | Corporation | 400, 401, 403, 5XX |
| /corporations/{uuid}/audit-trail | GET | - | CorporationAudit[] | 400, 401, 403, 5XX |
| /corporations/{country}/{code} | GET | - | Corporation | 400, 401, 403, 5XX |
| /people | POST | PersonRequest | Person | 400, 401, 403, 5XX |
| /people/{uuid} | GET | - | Person | 400, 401, 403, 5XX |
| /people/{uuid} | PATCH | PersonRequest | Person | 400, 401, 403, 5XX |
| /people/{uuid}/audit-trail | GET | - | PersonAudit[] | 400, 401, 403, 5XX |
| /bank-accounts | PUT | BankAccountRequest | BankAccount | 400, 401, 403, 5XX |
| /bank-accounts/{uuid} | GET | - | BankAccount | 400, 401, 403, 5XX |
| /bank-accounts/{uuid}/audit-trail | GET | - | BankAccountAudit[] | 400, 401, 403, 5XX |
| /bank-accounts/{uuid}/beneficial-owners | GET | - | LegalEntity[] | 400, 401, 403, 5XX |
| /customer-accounts/{uuid} | GET | - | CustomerAccount | 400, 401, 403, 5XX |
| /customer-accounts/{uuid}/beneficial-owners | GET | - | LegalEntity[] | 400, 401, 403, 5XX |

---

## 17. Testing Strategy

### Unit Tests (Service Layer)
- Mock repositories
- Test business logic isolation
- Verify mapper calls
- Test exception scenarios
- Framework: Mockito + JUnit 5

### Integration Tests (Controller Layer)
- Full Spring context
- Use MockMvc for HTTP layer testing
- Test request/response serialization
- Framework: Spring Test + JUnit 5

### Coverage Target
- Services: >85% line coverage
- Controllers: >80% line coverage
- Mappers: >75% line coverage

---

This architecture design is ready for implementation. Each component has clear responsibilities, dependencies are minimized through layering, and the design follows the conventions outlined in AGENTS.md. The implementation order ensures that base dependencies are created first, reducing circular dependencies and rework.

