# Payment Service – Low-Level Design (LLD)

## 1. Package Structure

```
com.techwave.paymentservice
├── PaymentServiceApplication.java          # Spring Boot entry point
├── controller/
│   ├── CoreController.java                 # Countries, Currencies, Silos
│   ├── LegalEntitiesController.java        # People, Corporations
│   ├── BankAccountsController.java         # Bank Accounts
│   └── CustomerAccountsController.java     # Customer Accounts
├── dto/
│   ├── CountryDto.java
│   ├── CurrencyDto.java
│   ├── SiloDto.java
│   ├── PersonDto.java
│   ├── CorporationDto.java
│   ├── LegalEntityDto.java                 # Discriminated base DTO
│   ├── BankAccountDto.java
│   ├── CustomerAccountDto.java
│   ├── PersonAuditDto.java
│   ├── CorporationAuditDto.java
│   ├── BankAccountAuditDto.java
│   └── ExceptionDetailDto.java
├── entity/
│   ├── LegalEntityBase.java                # Abstract, single-table inheritance
│   ├── PersonEntity.java                   # @DiscriminatorValue("people")
│   ├── CorporationEntity.java             # @DiscriminatorValue("corporations")
│   ├── BankAccountEntity.java
│   ├── CustomerAccountEntity.java
│   ├── CountryEntity.java
│   ├── CurrencyEntity.java
│   ├── SiloEntity.java
│   ├── SiloType.java                       # Enum
│   ├── CustomerAccountType.java            # Enum
│   ├── CustomerAccountState.java           # Enum
│   ├── PersonAuditEntity.java
│   ├── CorporationAuditEntity.java
│   └── BankAccountAuditEntity.java
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── BadRequestException.java
│   └── GlobalExceptionHandler.java         # @RestControllerAdvice
├── mapper/
│   ├── CountryMapper.java
│   ├── CurrencyMapper.java
│   ├── SiloMapper.java
│   ├── PersonMapper.java
│   ├── CorporationMapper.java
│   ├── BankAccountMapper.java
│   └── CustomerAccountMapper.java
├── repository/
│   ├── CountryRepository.java
│   ├── CurrencyRepository.java
│   ├── SiloRepository.java
│   ├── PersonRepository.java
│   ├── CorporationRepository.java
│   ├── BankAccountRepository.java
│   ├── CustomerAccountRepository.java
│   ├── PersonAuditRepository.java
│   ├── CorporationAuditRepository.java
│   └── BankAccountAuditRepository.java
└── service/
    ├── CountryService.java
    ├── CurrencyService.java
    ├── SiloService.java
    ├── PersonService.java
    ├── CorporationService.java
    ├── BankAccountService.java
    ├── CustomerAccountService.java
    └── impl/
        ├── CountryServiceImpl.java
        ├── CurrencyServiceImpl.java
        ├── SiloServiceImpl.java
        ├── PersonServiceImpl.java
        ├── CorporationServiceImpl.java
        ├── BankAccountServiceImpl.java
        └── CustomerAccountServiceImpl.java
```

---

## 2. Entity-Relationship Diagram

```mermaid
erDiagram
    countries {
        VARCHAR2 id PK "Alpha-2 code"
        VARCHAR name "Country name"
        VARCHAR3 numeric_code "ISO numeric"
        VARCHAR3 alpha3_code "ISO alpha-3"
        BOOLEAN eurozone
        BOOLEAN sepa
    }

    currencies {
        VARCHAR3 id PK "ISO 4217 code"
        VARCHAR name "Currency name"
    }

    silos {
        VARCHAR100 id PK
        VARCHAR name
        VARCHAR1000 description
        VARCHAR email
        VARCHAR3 default_base_currency FK
        DECIMAL default_credit_limit
        DECIMAL default_profit_share
        VARCHAR50 type "TREASURY|BUSINESS_UNIT|SUBSIDIARY|AGENT"
    }

    legal_entities {
        UUID id PK
        VARCHAR50 resource_type "Discriminator: people|corporations"
        VARCHAR first_name "Person only"
        VARCHAR last_name "Person only"
        VARCHAR name "Corporation only"
        VARCHAR100 code "Corporation only"
        DATE incorporation_date "Corporation only"
        VARCHAR2 incorporation_country "Corporation only"
        VARCHAR100 type "Corporation only"
        UUID duplicates
        INTEGER version
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

    bank_accounts {
        UUID id PK
        VARCHAR beneficiary
        VARCHAR500 beneficiary_address
        VARCHAR nickname
        VARCHAR34 iban
        VARCHAR11 bic
        VARCHAR50 account_number
        VARCHAR50 national_bank_code
        VARCHAR50 national_branch_code
        VARCHAR50 national_clearing_code
        VARCHAR3 currency
        VARCHAR2 country
        INTEGER version
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

    customer_accounts {
        UUID id PK
        VARCHAR name
        VARCHAR1000 description
        VARCHAR50 account_type "Enum"
        VARCHAR50 account_state "Enum"
        UUID account_manager
        TIMESTAMP account_creation_time
        VARCHAR100 silo
        TIMESTAMP created_at
        TIMESTAMP updated_at
    }

    person_audits {
        BIGINT id PK "Auto-increment"
        UUID resource FK
        INTEGER version
        VARCHAR first_name
        VARCHAR last_name
        UUID duplicates
        TIMESTAMP created_at
    }

    corporation_audits {
        BIGINT id PK "Auto-increment"
        UUID resource FK
        INTEGER version
        VARCHAR name
        VARCHAR100 code
        DATE incorporation_date
        VARCHAR2 incorporation_country
        VARCHAR100 type
        UUID duplicates
        TIMESTAMP created_at
    }

    bank_account_audits {
        BIGINT id PK "Auto-increment"
        UUID resource FK
        INTEGER version
        VARCHAR beneficiary
        VARCHAR500 beneficiary_address
        VARCHAR nickname
        VARCHAR34 iban
        VARCHAR11 bic
        VARCHAR50 account_number
        VARCHAR50 national_bank_code
        VARCHAR50 national_branch_code
        VARCHAR50 national_clearing_code
        VARCHAR3 currency
        VARCHAR2 country
        TIMESTAMP created_at
    }

    bank_account_beneficial_owners {
        UUID bank_account_id PK_FK
        UUID legal_entity_id PK_FK
    }

    customer_account_beneficial_owners {
        UUID customer_account_id PK_FK
        UUID legal_entity_id PK_FK
    }

    silos ||--o{ currencies : "default_base_currency"
    legal_entities ||--o{ person_audits : "resource"
    legal_entities ||--o{ corporation_audits : "resource"
    bank_accounts ||--o{ bank_account_audits : "resource"
    bank_accounts ||--|{ bank_account_beneficial_owners : "bank_account_id"
    legal_entities ||--|{ bank_account_beneficial_owners : "legal_entity_id"
    customer_accounts ||--|{ customer_account_beneficial_owners : "customer_account_id"
    legal_entities ||--|{ customer_account_beneficial_owners : "legal_entity_id"
```

---

## 3. Class Diagrams

### 3.1 Entity Layer – Inheritance Hierarchy

```mermaid
classDiagram
    class LegalEntityBase {
        <<abstract>>
        -UUID id
        -String resourceType
        -UUID duplicates
        -Integer version
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        +getId() UUID
        +setId(UUID)
        +getResourceType() String
        +getDuplicates() UUID
        +setDuplicates(UUID)
        +getVersion() Integer
        +setVersion(Integer)
        +getCreatedAt() LocalDateTime
        +setCreatedAt(LocalDateTime)
        +getUpdatedAt() LocalDateTime
        +setUpdatedAt(LocalDateTime)
    }

    class PersonEntity {
        -String firstName
        -String lastName
        +getFirstName() String
        +setFirstName(String)
        +getLastName() String
        +setLastName(String)
    }

    class CorporationEntity {
        -String name
        -String code
        -LocalDate incorporationDate
        -String incorporationCountry
        -String type
        +getName() String
        +setName(String)
        +getCode() String
        +setCode(String)
        +getIncorporationDate() LocalDate
        +setIncorporationDate(LocalDate)
        +getIncorporationCountry() String
        +setIncorporationCountry(String)
        +getType() String
        +setType(String)
    }

    LegalEntityBase <|-- PersonEntity : extends
    LegalEntityBase <|-- CorporationEntity : extends

    note for LegalEntityBase "@Entity\n@Table(legal_entities)\n@Inheritance(SINGLE_TABLE)\n@DiscriminatorColumn(resource_type)"
    note for PersonEntity "@DiscriminatorValue(people)"
    note for CorporationEntity "@DiscriminatorValue(corporations)"
```

### 3.2 Entity Layer – Accounts and Relationships

```mermaid
classDiagram
    class BankAccountEntity {
        -UUID id
        -String beneficiary
        -String beneficiaryAddress
        -String nickname
        -String iban
        -String bic
        -String accountNumber
        -String nationalBankCode
        -String nationalBranchCode
        -String nationalClearingCode
        -String currency
        -String country
        -Integer version
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        -Set~LegalEntityBase~ beneficialOwners
    }

    class CustomerAccountEntity {
        -UUID id
        -String name
        -String description
        -CustomerAccountType accountType
        -CustomerAccountState accountState
        -UUID accountManager
        -LocalDateTime accountCreationTime
        -String silo
        -LocalDateTime createdAt
        -LocalDateTime updatedAt
        -Set~LegalEntityBase~ beneficialOwners
    }

    class CustomerAccountType {
        <<enum>>
        PERSONAL
        CORPORATE
        CHARITY
        AGENT
        LIQUIDITY_PROVIDER
        BANKER
    }

    class CustomerAccountState {
        <<enum>>
        DATA_REQUIRED
        UNDER_REVIEW
        ACCEPTED
        REJECTED
        NOT_REQUIRED
        LAPSED
        INACTIVE
        CLOSED
    }

    class SiloType {
        <<enum>>
        TREASURY
        BUSINESS_UNIT
        SUBSIDIARY
        AGENT
    }

    class LegalEntityBase {
        <<abstract>>
    }

    BankAccountEntity "1" --> "*" LegalEntityBase : beneficialOwners\n@ManyToMany
    CustomerAccountEntity "1" --> "*" LegalEntityBase : beneficialOwners\n@ManyToMany
    CustomerAccountEntity --> CustomerAccountType
    CustomerAccountEntity --> CustomerAccountState
```

### 3.3 Entity Layer – Reference Data

```mermaid
classDiagram
    class CountryEntity {
        -String id
        -String name
        -String numericCode
        -String alpha3Code
        -Boolean eurozone
        -Boolean sepa
    }

    class CurrencyEntity {
        -String id
        -String name
    }

    class SiloEntity {
        -String id
        -String name
        -String description
        -String email
        -String defaultBaseCurrency
        -BigDecimal defaultCreditLimit
        -BigDecimal defaultProfitShare
        -SiloType type
    }

    class SiloType {
        <<enum>>
        TREASURY
        BUSINESS_UNIT
        SUBSIDIARY
        AGENT
    }

    SiloEntity --> SiloType
    SiloEntity ..> CurrencyEntity : defaultBaseCurrency FK
```

### 3.4 Entity Layer – Audit Entities

```mermaid
classDiagram
    class PersonAuditEntity {
        -Long id
        -UUID resource
        -Integer version
        -String firstName
        -String lastName
        -UUID duplicates
        -LocalDateTime createdAt
    }

    class CorporationAuditEntity {
        -Long id
        -UUID resource
        -Integer version
        -String name
        -String code
        -LocalDate incorporationDate
        -String incorporationCountry
        -String type
        -UUID duplicates
        -LocalDateTime createdAt
    }

    class BankAccountAuditEntity {
        -Long id
        -UUID resource
        -Integer version
        -String beneficiary
        -String beneficiaryAddress
        -String nickname
        -String iban
        -String bic
        -String accountNumber
        -String nationalBankCode
        -String nationalBranchCode
        -String nationalClearingCode
        -String currency
        -String country
        -LocalDateTime createdAt
    }

    class PersonEntity {
        <<audited>>
    }
    class CorporationEntity {
        <<audited>>
    }
    class BankAccountEntity {
        <<audited>>
    }

    PersonEntity ..> PersonAuditEntity : "creates audit on mutate"
    CorporationEntity ..> CorporationAuditEntity : "creates audit on mutate"
    BankAccountEntity ..> BankAccountAuditEntity : "creates audit on create"
```

### 3.5 Service Layer

```mermaid
classDiagram
    class CountryService {
        <<interface>>
        +getAllCountries() List~CountryDto~
        +getCountryById(String id) CountryDto
    }

    class CurrencyService {
        <<interface>>
        +getAllCurrencies() List~CurrencyDto~
        +getCurrencyById(String id) CurrencyDto
    }

    class SiloService {
        <<interface>>
        +getAllSilos() List~SiloDto~
        +getSiloById(String id) SiloDto
    }

    class PersonService {
        <<interface>>
        +createPerson(PersonDto) PersonDto
        +getPersonById(UUID) PersonDto
        +updatePerson(UUID, PersonDto) PersonDto
        +getAuditTrail(UUID) List~PersonAuditDto~
    }

    class CorporationService {
        <<interface>>
        +createCorporation(CorporationDto) CorporationDto
        +getCorporationById(UUID) CorporationDto
        +updateCorporation(UUID, CorporationDto) CorporationDto
        +getAuditTrail(UUID) List~CorporationAuditDto~
        +getCorporationByCode(String, String) CorporationDto
    }

    class BankAccountService {
        <<interface>>
        +createOrLocateBankAccount(BankAccountDto) BankAccountDto
        +getBankAccountById(UUID) BankAccountDto
        +getAuditTrail(UUID) List~BankAccountAuditDto~
        +getBeneficialOwners(UUID) List~LegalEntityDto~
    }

    class CustomerAccountService {
        <<interface>>
        +getCustomerAccountById(UUID) CustomerAccountDto
        +getBeneficialOwners(UUID) List~LegalEntityDto~
    }

    class CountryServiceImpl {
        -CountryRepository countryRepository
        -CountryMapper countryMapper
    }
    class CurrencyServiceImpl {
        -CurrencyRepository currencyRepository
        -CurrencyMapper currencyMapper
    }
    class SiloServiceImpl {
        -SiloRepository siloRepository
        -SiloMapper siloMapper
    }
    class PersonServiceImpl {
        -PersonRepository personRepository
        -PersonAuditRepository personAuditRepository
        -PersonMapper personMapper
    }
    class CorporationServiceImpl {
        -CorporationRepository corporationRepository
        -CorporationAuditRepository corporationAuditRepository
        -CorporationMapper corporationMapper
    }
    class BankAccountServiceImpl {
        -BankAccountRepository bankAccountRepository
        -BankAccountAuditRepository bankAccountAuditRepository
        -BankAccountMapper bankAccountMapper
    }
    class CustomerAccountServiceImpl {
        -CustomerAccountRepository customerAccountRepository
        -CustomerAccountMapper customerAccountMapper
    }

    CountryService <|.. CountryServiceImpl
    CurrencyService <|.. CurrencyServiceImpl
    SiloService <|.. SiloServiceImpl
    PersonService <|.. PersonServiceImpl
    CorporationService <|.. CorporationServiceImpl
    BankAccountService <|.. BankAccountServiceImpl
    CustomerAccountService <|.. CustomerAccountServiceImpl
```

### 3.6 Controller Layer

```mermaid
classDiagram
    class CoreController {
        -CountryService countryService
        -CurrencyService currencyService
        -SiloService siloService
        +getCountries() ResponseEntity~List~CountryDto~~
        +getCountry(String id) ResponseEntity~CountryDto~
        +getCurrencies() ResponseEntity~List~CurrencyDto~~
        +getCurrency(String id) ResponseEntity~CurrencyDto~
        +getSilos() ResponseEntity~List~SiloDto~~
        +getSilo(String id) ResponseEntity~SiloDto~
    }

    class LegalEntitiesController {
        -PersonService personService
        -CorporationService corporationService
        +createPerson(PersonDto) ResponseEntity~PersonDto~
        +getPerson(UUID) ResponseEntity~PersonDto~
        +updatePerson(UUID, PersonDto) ResponseEntity~PersonDto~
        +getPersonAuditTrail(UUID) ResponseEntity~List~PersonAuditDto~~
        +createCorporation(CorporationDto) ResponseEntity~CorporationDto~
        +getCorporation(UUID) ResponseEntity~CorporationDto~
        +updateCorporation(UUID, CorporationDto) ResponseEntity~CorporationDto~
        +getCorporationAuditTrail(UUID) ResponseEntity~List~CorporationAuditDto~~
        +getCorporationByCode(String, String) ResponseEntity~CorporationDto~
    }

    class BankAccountsController {
        -BankAccountService bankAccountService
        +createBankAccount(BankAccountDto) ResponseEntity~BankAccountDto~
        +getBankAccount(UUID) ResponseEntity~BankAccountDto~
        +getBankAccountAuditTrail(UUID) ResponseEntity~List~BankAccountAuditDto~~
        +getBankAccountBeneficialOwners(UUID) ResponseEntity~List~LegalEntityDto~~
    }

    class CustomerAccountsController {
        -CustomerAccountService customerAccountService
        +getCustomerAccount(UUID) ResponseEntity~CustomerAccountDto~
        +getCustomerAccountBeneficialOwners(UUID) ResponseEntity~List~LegalEntityDto~~
    }

    CoreController --> CountryService
    CoreController --> CurrencyService
    CoreController --> SiloService
    LegalEntitiesController --> PersonService
    LegalEntitiesController --> CorporationService
    BankAccountsController --> BankAccountService
    CustomerAccountsController --> CustomerAccountService

    note for CoreController "@RestController"
    note for LegalEntitiesController "@RestController"
    note for BankAccountsController "@RestController"
    note for CustomerAccountsController "@RestController"
```

### 3.7 Repository Layer

```mermaid
classDiagram
    class JpaRepository~T_ID~ {
        <<interface>>
        +findById(ID) Optional~T~
        +findAll() List~T~
        +save(T) T
        +existsById(ID) boolean
        +deleteById(ID)
    }

    class CountryRepository {
        <<interface>>
    }
    class CurrencyRepository {
        <<interface>>
    }
    class SiloRepository {
        <<interface>>
    }
    class PersonRepository {
        <<interface>>
    }
    class CorporationRepository {
        <<interface>>
        +findByIncorporationCountryAndCode(String, String) Optional~CorporationEntity~
    }
    class BankAccountRepository {
        <<interface>>
        +findByIban(String) Optional~BankAccountEntity~
    }
    class CustomerAccountRepository {
        <<interface>>
    }
    class PersonAuditRepository {
        <<interface>>
        +findByResourceOrderByVersionAsc(UUID) List~PersonAuditEntity~
    }
    class CorporationAuditRepository {
        <<interface>>
        +findByResourceOrderByVersionAsc(UUID) List~CorporationAuditEntity~
    }
    class BankAccountAuditRepository {
        <<interface>>
        +findByResourceOrderByVersionAsc(UUID) List~BankAccountAuditEntity~
    }

    JpaRepository <|-- CountryRepository : "~CountryEntity, String~"
    JpaRepository <|-- CurrencyRepository : "~CurrencyEntity, String~"
    JpaRepository <|-- SiloRepository : "~SiloEntity, String~"
    JpaRepository <|-- PersonRepository : "~PersonEntity, UUID~"
    JpaRepository <|-- CorporationRepository : "~CorporationEntity, UUID~"
    JpaRepository <|-- BankAccountRepository : "~BankAccountEntity, UUID~"
    JpaRepository <|-- CustomerAccountRepository : "~CustomerAccountEntity, UUID~"
    JpaRepository <|-- PersonAuditRepository : "~PersonAuditEntity, Long~"
    JpaRepository <|-- CorporationAuditRepository : "~CorporationAuditEntity, Long~"
    JpaRepository <|-- BankAccountAuditRepository : "~BankAccountAuditEntity, Long~"
```

### 3.8 Mapper Layer

```mermaid
classDiagram
    class PersonMapper {
        <<interface>>
        +toDto(PersonEntity) PersonDto
        +toEntity(PersonDto) PersonEntity
        +updateEntity(PersonDto, PersonEntity) void
        +toAuditDto(PersonAuditEntity) PersonAuditDto
        +toAuditDtoList(List~PersonAuditEntity~) List~PersonAuditDto~
    }

    class CorporationMapper {
        <<interface>>
        +toDto(CorporationEntity) CorporationDto
        +toEntity(CorporationDto) CorporationEntity
        +updateEntity(CorporationDto, CorporationEntity) void
        +toAuditDto(CorporationAuditEntity) CorporationAuditDto
        +toAuditDtoList(List~CorporationAuditEntity~) List~CorporationAuditDto~
    }

    class BankAccountMapper {
        <<interface>>
        +toDto(BankAccountEntity) BankAccountDto
        +toEntity(BankAccountDto) BankAccountEntity
        +toAuditDto(BankAccountAuditEntity) BankAccountAuditDto
        +toAuditDtoList(List~BankAccountAuditEntity~) List~BankAccountAuditDto~
        +toLegalEntityDto(LegalEntityBase) LegalEntityDto
        +toLegalEntityDtoList(Set~LegalEntityBase~) List~LegalEntityDto~
    }

    class CustomerAccountMapper {
        <<interface>>
        +toDto(CustomerAccountEntity) CustomerAccountDto
        +toLegalEntityDto(LegalEntityBase) LegalEntityDto
        +toLegalEntityDtoList(Set~LegalEntityBase~) List~LegalEntityDto~
    }

    class CountryMapper {
        <<interface>>
        +toDto(CountryEntity) CountryDto
        +toDtoList(List~CountryEntity~) List~CountryDto~
    }

    class CurrencyMapper {
        <<interface>>
        +toDto(CurrencyEntity) CurrencyDto
        +toDtoList(List~CurrencyEntity~) List~CurrencyDto~
    }

    class SiloMapper {
        <<interface>>
        +toDto(SiloEntity) SiloDto
        +toDtoList(List~SiloEntity~) List~SiloDto~
    }

    note for PersonMapper "@Mapper(componentModel=spring)\nNullValuePropertyMappingStrategy.IGNORE"
    note for BankAccountMapper "@Mapper(componentModel=spring)\nNullValuePropertyMappingStrategy.IGNORE"
```

### 3.9 Exception Handling

```mermaid
classDiagram
    class GlobalExceptionHandler {
        <<RestControllerAdvice>>
        +handleResourceNotFound(ResourceNotFoundException) ResponseEntity~ExceptionDetailDto~
        +handleBadRequest(BadRequestException) ResponseEntity~ExceptionDetailDto~
        +handleValidationErrors(MethodArgumentNotValidException) ResponseEntity~ExceptionDetailDto~
        +handleConstraintViolation(ConstraintViolationException) ResponseEntity~ExceptionDetailDto~
        +handleUnreadableMessage(HttpMessageNotReadableException) ResponseEntity~ExceptionDetailDto~
        +handleMissingParam(MissingServletRequestParameterException) ResponseEntity~ExceptionDetailDto~
        +handleTypeMismatch(MethodArgumentTypeMismatchException) ResponseEntity~ExceptionDetailDto~
        +handleMethodNotSupported(HttpRequestMethodNotSupportedException) ResponseEntity~ExceptionDetailDto~
        +handleNoResourceFound(NoResourceFoundException) ResponseEntity~ExceptionDetailDto~
        +handleGenericException(Exception) ResponseEntity~ExceptionDetailDto~
        -buildDetail(int, String, String, List~String~) ExceptionDetailDto
    }

    class ResourceNotFoundException {
        -String resourceType
        -String identifier
    }

    class BadRequestException {
        -String message
    }

    class ExceptionDetailDto {
        -Integer status
        -String error
        -String message
    }

    class RuntimeException

    RuntimeException <|-- ResourceNotFoundException
    RuntimeException <|-- BadRequestException
    GlobalExceptionHandler ..> ResourceNotFoundException : handles
    GlobalExceptionHandler ..> BadRequestException : handles
    GlobalExceptionHandler ..> ExceptionDetailDto : produces
```

---

## 4. Detailed Sequence Diagrams

### 4.1 Create Person

```mermaid
sequenceDiagram
    actor Client
    participant Ctrl as LegalEntitiesController
    participant Svc as PersonServiceImpl
    participant Mapper as PersonMapper
    participant Repo as PersonRepository
    participant AuditRepo as PersonAuditRepository
    participant DB as H2 Database

    Client->>Ctrl: POST /people {"firstName":"John","lastName":"Doe"}
    Ctrl->>Svc: createPerson(personDto)

    activate Svc
    Note over Svc: @Transactional begins

    Svc->>Mapper: toEntity(personDto)
    Mapper-->>Svc: PersonEntity

    Svc->>Svc: entity.setId(UUID.randomUUID())
    Svc->>Svc: entity.setVersion(1)
    Svc->>Svc: entity.setCreatedAt(now())
    Svc->>Svc: entity.setUpdatedAt(now())

    Svc->>Repo: save(entity)
    Repo->>DB: INSERT INTO legal_entities (id, resource_type, first_name, last_name, version, ...)
    DB-->>Repo: PersonEntity (persisted)
    Repo-->>Svc: PersonEntity

    Svc->>Svc: createAuditRecord(entity)
    Svc->>AuditRepo: save(PersonAuditEntity{resource, version=1, firstName, lastName})
    AuditRepo->>DB: INSERT INTO person_audits
    DB-->>AuditRepo: PersonAuditEntity

    Svc->>Mapper: toDto(entity)
    Mapper-->>Svc: PersonDto {id, resourceType="people", firstName, lastName}

    Note over Svc: @Transactional commits
    deactivate Svc

    Svc-->>Ctrl: PersonDto
    Ctrl-->>Client: 200 OK PersonDto
```

### 4.2 Update Corporation with Audit

```mermaid
sequenceDiagram
    actor Client
    participant Ctrl as LegalEntitiesController
    participant Svc as CorporationServiceImpl
    participant Mapper as CorporationMapper
    participant Repo as CorporationRepository
    participant AuditRepo as CorporationAuditRepository
    participant DB as H2 Database

    Client->>Ctrl: PATCH /corporations/{uuid} {"name":"New Corp Name"}
    Ctrl->>Svc: updateCorporation(uuid, dto)

    activate Svc
    Note over Svc: @Transactional begins

    Svc->>Repo: findById(uuid)
    Repo->>DB: SELECT * FROM legal_entities WHERE id = ? AND resource_type = 'corporations'
    DB-->>Repo: CorporationEntity
    Repo-->>Svc: Optional.of(entity)

    alt Entity not found
        Svc-->>Ctrl: throw ResourceNotFoundException
        Ctrl-->>Client: 404 ExceptionDetailDto
    end

    Svc->>Mapper: updateEntity(dto, entity)
    Note over Mapper: NullValuePropertyMappingStrategy.IGNORE\nOnly non-null fields overwrite existing values

    Svc->>Svc: entity.setVersion(version + 1)
    Svc->>Svc: entity.setUpdatedAt(now())

    Svc->>Repo: save(entity)
    Repo->>DB: UPDATE legal_entities SET name=?, version=?, updated_at=? WHERE id=?
    DB-->>Repo: CorporationEntity (updated)

    Svc->>Svc: createAuditRecord(entity)
    Svc->>AuditRepo: save(CorporationAuditEntity{resource, version=N, name, code, ...})
    AuditRepo->>DB: INSERT INTO corporation_audits
    DB-->>AuditRepo: CorporationAuditEntity

    Svc->>Mapper: toDto(entity)
    Mapper-->>Svc: CorporationDto

    Note over Svc: @Transactional commits
    deactivate Svc

    Svc-->>Ctrl: CorporationDto
    Ctrl-->>Client: 200 OK CorporationDto
```

### 4.3 Idempotent Bank Account Create-or-Locate

```mermaid
sequenceDiagram
    actor Client
    participant Ctrl as BankAccountsController
    participant Svc as BankAccountServiceImpl
    participant Mapper as BankAccountMapper
    participant Repo as BankAccountRepository
    participant AuditRepo as BankAccountAuditRepository
    participant DB as H2 Database

    Client->>Ctrl: PUT /bank-accounts {"iban":"GB82WEST12345698765432","bic":"WESTGB2L",...}
    Ctrl->>Svc: createOrLocateBankAccount(dto)

    activate Svc
    Note over Svc: @Transactional begins

    alt IBAN is provided
        Svc->>Repo: findByIban("GB82WEST12345698765432")
        Repo->>DB: SELECT * FROM bank_accounts WHERE iban = ?
        DB-->>Repo: Result

        alt Account found (idempotent return)
            Repo-->>Svc: Optional.of(existingEntity)
            Svc->>Mapper: toDto(existingEntity)
            Mapper-->>Svc: BankAccountDto (existing)
            Svc-->>Ctrl: BankAccountDto
            Ctrl-->>Client: 200 OK (existing account returned)
        else Account not found
            Repo-->>Svc: Optional.empty()
        end
    end

    Note over Svc: Create new account

    Svc->>Mapper: toEntity(dto)
    Mapper-->>Svc: BankAccountEntity (version, timestamps, beneficialOwners ignored)

    Svc->>Svc: entity.setId(UUID.randomUUID())
    Svc->>Svc: entity.setVersion(1)
    Svc->>Svc: entity.setCreatedAt(now())
    Svc->>Svc: entity.setUpdatedAt(now())

    Svc->>Repo: save(entity)
    Repo->>DB: INSERT INTO bank_accounts
    DB-->>Repo: BankAccountEntity (saved)

    Svc->>Svc: createAuditRecord(entity)
    Svc->>AuditRepo: save(BankAccountAuditEntity{resource, version=1, iban, bic, ...})
    AuditRepo->>DB: INSERT INTO bank_account_audits

    Svc->>Mapper: toDto(entity)
    Mapper-->>Svc: BankAccountDto (new)

    Note over Svc: @Transactional commits
    deactivate Svc

    Svc-->>Ctrl: BankAccountDto
    Ctrl-->>Client: 200 OK (new account created)
```

### 4.4 Retrieve Beneficial Owners

```mermaid
sequenceDiagram
    actor Client
    participant Ctrl as BankAccountsController
    participant Svc as BankAccountServiceImpl
    participant Mapper as BankAccountMapper
    participant Repo as BankAccountRepository
    participant DB as H2 Database

    Client->>Ctrl: GET /bank-accounts/{uuid}/beneficial-owners
    Ctrl->>Svc: getBeneficialOwners(uuid)

    activate Svc
    Note over Svc: @Transactional(readOnly=true)

    Svc->>Repo: findById(uuid)
    Repo->>DB: SELECT * FROM bank_accounts WHERE id = ?
    DB-->>Repo: BankAccountEntity

    alt Account not found
        Svc-->>Ctrl: throw ResourceNotFoundException
        Ctrl-->>Client: 404 ExceptionDetailDto
    end

    Repo-->>Svc: BankAccountEntity

    Note over Svc: Lazy load beneficial owners
    Svc->>Svc: entity.getBeneficialOwners()
    Note over DB: SELECT le.* FROM legal_entities le<br/>JOIN bank_account_beneficial_owners babo<br/>ON le.id = babo.legal_entity_id<br/>WHERE babo.bank_account_id = ?

    Svc->>Mapper: toLegalEntityDtoList(Set~LegalEntityBase~)
    Mapper-->>Svc: List~LegalEntityDto~ (id + resourceType per owner)

    deactivate Svc

    Svc-->>Ctrl: List~LegalEntityDto~
    Ctrl-->>Client: 200 OK [{"id":"...","resourceType":"people"},{"id":"...","resourceType":"corporations"}]
```

### 4.5 Get Audit Trail

```mermaid
sequenceDiagram
    actor Client
    participant Ctrl as LegalEntitiesController
    participant Svc as PersonServiceImpl
    participant Mapper as PersonMapper
    participant Repo as PersonRepository
    participant AuditRepo as PersonAuditRepository
    participant DB as H2 Database

    Client->>Ctrl: GET /people/{uuid}/audit-trail
    Ctrl->>Svc: getAuditTrail(uuid)

    activate Svc
    Note over Svc: @Transactional(readOnly=true)

    Svc->>Repo: existsById(uuid)
    Repo->>DB: SELECT COUNT(*) FROM legal_entities WHERE id = ?
    DB-->>Repo: exists check

    alt Person not found
        Svc-->>Ctrl: throw ResourceNotFoundException
        Ctrl-->>Client: 404 ExceptionDetailDto
    end

    Svc->>AuditRepo: findByResourceOrderByVersionAsc(uuid)
    AuditRepo->>DB: SELECT * FROM person_audits WHERE resource = ? ORDER BY version ASC
    DB-->>AuditRepo: List~PersonAuditEntity~ [v1, v2, v3, ...]

    Svc->>Mapper: toAuditDtoList(auditEntities)
    Mapper-->>Svc: List~PersonAuditDto~

    deactivate Svc

    Svc-->>Ctrl: List~PersonAuditDto~
    Ctrl-->>Client: 200 OK [{version:1,...},{version:2,...},...]
```

---

## 5. Exception Handling Flow

```mermaid
flowchart TB
    REQ[Incoming HTTP Request] --> CTRL[Controller Method]

    CTRL --> SVC[Service Layer]

    SVC -->|Success| DTO[Return DTO in ResponseEntity 200]
    SVC -->|Entity not found| RNF[throw ResourceNotFoundException]
    SVC -->|Invalid request| BRE[throw BadRequestException]

    CTRL -->|Invalid UUID format| TM[MethodArgumentTypeMismatchException]
    CTRL -->|Malformed JSON| MNR[HttpMessageNotReadableException]
    CTRL -->|@Valid fails| MNV[MethodArgumentNotValidException]
    CTRL -->|@Validated fails| CV[ConstraintViolationException]
    CTRL -->|Wrong HTTP method| MNS[HttpRequestMethodNotSupportedException]
    CTRL -->|No route match| NRF[NoResourceFoundException]
    CTRL -->|Unexpected| GEN[Exception]

    RNF --> GEH[GlobalExceptionHandler]
    BRE --> GEH
    TM --> GEH
    MNR --> GEH
    MNV --> GEH
    CV --> GEH
    MNS --> GEH
    NRF --> GEH
    GEN --> GEH

    GEH -->|ResourceNotFoundException| R404["404 Not Found\nExceptionDetailDto"]
    GEH -->|BadRequestException| R400a["400 Bad Request\nExceptionDetailDto"]
    GEH -->|MethodArgumentNotValidException| R400b["400 Bad Request\nExceptionDetailDto + field errors"]
    GEH -->|ConstraintViolationException| R400c["400 Bad Request\nExceptionDetailDto + violations"]
    GEH -->|HttpMessageNotReadableException| R400d["400 Bad Request\nMalformed body"]
    GEH -->|MissingServletRequestParameterException| R400e["400 Bad Request\nMissing param"]
    GEH -->|MethodArgumentTypeMismatchException| R400f["400 Bad Request\nType mismatch"]
    GEH -->|HttpRequestMethodNotSupportedException| R405["405 Method Not Allowed"]
    GEH -->|NoResourceFoundException| R404b["404 Not Found"]
    GEH -->|Exception catch-all| R500["500 Internal Server Error"]
```

---

## 6. Database Migration Strategy

```mermaid
flowchart LR
    subgraph "Flyway Migration Pipeline"
        V1[V1__init_schema.sql] --> V2[V2__seed_data.sql]
    end

    subgraph "V1: Schema Creation"
        direction TB
        T1[countries] --> T2[currencies]
        T2 --> T3[silos]
        T3 --> T4[legal_entities]
        T4 --> T5[bank_accounts]
        T5 --> T6[customer_accounts]
        T6 --> T7[person_audits]
        T7 --> T8[corporation_audits]
        T8 --> T9[bank_account_audits]
        T9 --> T10[bank_account_beneficial_owners]
        T10 --> T11[customer_account_beneficial_owners]
    end

    subgraph "V2: Seed Data"
        direction TB
        S1["10 Currencies (EUR, GBP, USD, ...)"]
        S2["10 Countries (DE, FR, GB, IE, ...)"]
        S3["3 Silos (TREASURY, OPS, AGENT)"]
    end

    V1 --> T1
    V2 --> S1
```

---

## 7. Dependency Injection Wiring

```mermaid
flowchart TD
    subgraph "Spring IoC Container"
        subgraph "Controllers"
            CC[CoreController]
            LC[LegalEntitiesController]
            BC[BankAccountsController]
            CuC[CustomerAccountsController]
        end

        subgraph "Services @Service"
            CSI[CountryServiceImpl]
            CuSI[CurrencyServiceImpl]
            SSI[SiloServiceImpl]
            PSI[PersonServiceImpl]
            CoSI[CorporationServiceImpl]
            BASI[BankAccountServiceImpl]
            CuASI[CustomerAccountServiceImpl]
        end

        subgraph "Mappers @Mapper(spring)"
            CMap[CountryMapperImpl]
            CuMap[CurrencyMapperImpl]
            SMap[SiloMapperImpl]
            PMap[PersonMapperImpl]
            CoMap[CorporationMapperImpl]
            BAMap[BankAccountMapperImpl]
            CuAMap[CustomerAccountMapperImpl]
        end

        subgraph "Repositories @Repository"
            CRepo[CountryRepository]
            CuRepo[CurrencyRepository]
            SRepo[SiloRepository]
            PRepo[PersonRepository]
            CoRepo[CorporationRepository]
            BARepo[BankAccountRepository]
            CuARepo[CustomerAccountRepository]
            PARepo[PersonAuditRepository]
            CoARepo[CorporationAuditRepository]
            BAARepo[BankAccountAuditRepository]
        end
    end

    CC -->|injects| CSI & CuSI & SSI
    LC -->|injects| PSI & CoSI
    BC -->|injects| BASI
    CuC -->|injects| CuASI

    CSI -->|injects| CRepo & CMap
    CuSI -->|injects| CuRepo & CuMap
    SSI -->|injects| SRepo & SMap
    PSI -->|injects| PRepo & PARepo & PMap
    CoSI -->|injects| CoRepo & CoARepo & CoMap
    BASI -->|injects| BARepo & BAARepo & BAMap
    CuASI -->|injects| CuARepo & CuAMap
```

---

## 8. Audit Trail Design Pattern

```mermaid
flowchart TD
    subgraph "Audit Trail Pattern"
        direction TB
        A[Client mutates entity] --> B{Operation type?}

        B -->|CREATE| C[Build new entity]
        C --> D[Set UUID, version=1, timestamps]
        D --> E[repository.save entity]
        E --> F[createAuditRecord entity]
        F --> G[Copy all fields + version to audit entity]
        G --> H[auditRepository.save audit]

        B -->|UPDATE| I[repository.findById]
        I --> J[mapper.updateEntity dto, entity]
        J --> K[entity.setVersion version + 1]
        K --> L[entity.setUpdatedAt now]
        L --> M[repository.save entity]
        M --> N[createAuditRecord entity]
        N --> O[Copy all fields + version to audit entity]
        O --> P[auditRepository.save audit]

        B -->|READ audit trail| Q[Verify entity exists]
        Q --> R[auditRepository.findByResourceOrderByVersionAsc]
        R --> S["Return List of audits [v1, v2, ..., vN]"]
    end

    style F fill:#f9f,stroke:#333
    style N fill:#f9f,stroke:#333
```

---

## 9. MapStruct Mapping Strategy

```mermaid
flowchart LR
    subgraph "Entity to DTO"
        E1[PersonEntity] -->|toDto| D1[PersonDto]
        E2[CorporationEntity] -->|toDto| D2[CorporationDto]
        E3[BankAccountEntity] -->|toDto| D3[BankAccountDto]
        E4[CustomerAccountEntity] -->|toDto| D4[CustomerAccountDto]
    end

    subgraph "DTO to Entity"
        D5[PersonDto] -->|toEntity| E5[PersonEntity]
        D6[CorporationDto] -->|toEntity| E6[CorporationEntity]
        D7[BankAccountDto] -->|toEntity| E7[BankAccountEntity]
    end

    subgraph "Partial Update"
        D8[PersonDto] -->|updateEntity| E8["PersonEntity @MappingTarget"]
        D9[CorporationDto] -->|updateEntity| E9["CorporationEntity @MappingTarget"]
    end

    subgraph "Audit Mapping"
        AE1[PersonAuditEntity] -->|toAuditDto| AD1[PersonAuditDto]
        AE2[CorporationAuditEntity] -->|toAuditDto| AD2[CorporationAuditDto]
        AE3[BankAccountAuditEntity] -->|toAuditDto| AD3[BankAccountAuditDto]
    end

    subgraph "Beneficial Owners"
        BO["Set~LegalEntityBase~"] -->|toLegalEntityDtoList| BOD["List~LegalEntityDto~"]
    end

    subgraph "Key Configurations"
        direction TB
        KC1["NullValuePropertyMappingStrategy.IGNORE"]
        KC2["componentModel = spring"]
        KC3["@Mapping(target=resourceType, constant=...)"]
        KC4["@Mapping(target=version, ignore=true)"]
        KC5["@Mapping(target=createdAt, ignore=true)"]
    end
```

---

## 10. Request/Response Mapping Table

### 10.1 Entity → DTO Field Mappings

| Entity Field              | DTO Field            | Mapping Notes                                   |
|---------------------------|----------------------|-------------------------------------------------|
| `PersonEntity.id`         | `PersonDto.id`       | Direct mapping (UUID)                           |
| —                         | `PersonDto.resourceType` | Constant: `"people"`                         |
| `PersonEntity.firstName`  | `PersonDto.firstName`| Direct                                          |
| `PersonEntity.lastName`   | `PersonDto.lastName` | Direct                                          |
| `PersonEntity.duplicates` | `PersonDto.duplicates`| Direct (UUID)                                  |
| `CorporationEntity.id`   | `CorporationDto.id`  | Direct (UUID)                                   |
| —                         | `CorporationDto.resourceType` | Constant: `"corporations"`            |
| `CorporationEntity.name` | `CorporationDto.name`| Direct                                          |
| `CorporationEntity.code` | `CorporationDto.code`| Direct                                          |
| `CorporationEntity.incorporationDate` | `CorporationDto.incorporationDate` | LocalDate → String (date format) |
| `CorporationEntity.incorporationCountry` | `CorporationDto.incorporationCountry` | Direct       |
| `BankAccountEntity.id`   | `BankAccountDto.id`   | Direct (UUID)                                   |
| —                         | `BankAccountDto.resourceType` | Constant: `"bank-accounts"`          |
| `BankAccountEntity.iban`  | `BankAccountDto.iban` | Direct                                          |
| `BankAccountEntity.bic`   | `BankAccountDto.bic`  | Direct                                          |
| `SiloEntity.type`         | `SiloDto.type`        | Enum `SiloType` → String                       |
| `CustomerAccountEntity.accountType` | `CustomerAccountDto.accountType` | Enum → String      |
| `CustomerAccountEntity.accountState` | `CustomerAccountDto.accountState` | Enum → String     |

### 10.2 Ignored Fields (DTO → Entity)

| Field           | Direction       | Reason                                |
|-----------------|-----------------|---------------------------------------|
| `version`       | DTO → Entity    | Managed by service layer              |
| `createdAt`     | DTO → Entity    | Set programmatically on create        |
| `updatedAt`     | DTO → Entity    | Set programmatically on create/update |
| `id`            | DTO → Entity (update) | Immutable primary key           |
| `resourceType`  | DTO → Entity    | JPA discriminator, not writable       |
| `beneficialOwners` | DTO → Entity | Managed separately through junction tables |

---

## 11. Database Index Strategy

| Table              | Index Name                        | Columns                          | Purpose                                       |
|--------------------|-----------------------------------|----------------------------------|-----------------------------------------------|
| `legal_entities`   | `idx_legal_entities_resource_type`| `resource_type`                  | Filter by entity type (Person vs Corporation) |
| `legal_entities`   | `idx_corporation_country_code`    | `incorporation_country, code`    | Lookup corporation by country + company code  |
| `person_audits`    | `idx_person_audits_resource`      | `resource`                       | Fast audit trail lookup by person UUID        |
| `corporation_audits` | `idx_corporation_audits_resource`| `resource`                       | Fast audit trail lookup by corporation UUID   |
| `bank_account_audits` | `idx_bank_account_audits_resource`| `resource`                     | Fast audit trail lookup by bank account UUID  |

---

## 12. Transaction Boundaries

| Service Method                          | Transaction Type    | Description                                                    |
|-----------------------------------------|--------------------|-----------------------------------------------------------------|
| `createPerson()`                        | `@Transactional`   | Insert entity + insert audit in same transaction               |
| `updatePerson()`                        | `@Transactional`   | Update entity + insert audit in same transaction               |
| `getPersonById()`                       | `readOnly = true`  | Read-only, no write lock                                       |
| `getAuditTrail()` (Person)              | `readOnly = true`  | Read-only, no write lock                                       |
| `createCorporation()`                   | `@Transactional`   | Insert entity + insert audit in same transaction               |
| `updateCorporation()`                   | `@Transactional`   | Update entity + insert audit in same transaction               |
| `getCorporationById()`                  | `readOnly = true`  | Read-only                                                      |
| `getCorporationByCode()`               | `readOnly = true`  | Read-only                                                      |
| `createOrLocateBankAccount()`           | `@Transactional`   | IBAN lookup + conditional insert + audit in same transaction   |
| `getBankAccountById()`                  | `readOnly = true`  | Read-only                                                      |
| `getBeneficialOwners()` (BankAccount)   | `readOnly = true`  | Read-only, triggers lazy load of M:N relationship              |
| `getCustomerAccountById()`              | `readOnly = true`  | Read-only                                                      |
| `getBeneficialOwners()` (CustomerAccount) | `readOnly = true`| Read-only, triggers lazy load of M:N relationship              |
| `getAllCountries()`, `getCountryById()` | `readOnly = true`  | Read-only reference data                                       |
| `getAllCurrencies()`, `getCurrencyById()` | `readOnly = true`| Read-only reference data                                       |
| `getAllSilos()`, `getSiloById()`        | `readOnly = true`  | Read-only reference data                                       |
