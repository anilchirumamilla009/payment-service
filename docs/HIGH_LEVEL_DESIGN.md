# Payment Service – High-Level Design (HLD)

## 1. Overview

The **Payment Service** is a Spring Boot 3.2.5 microservice that manages core data entities for a payment processing platform. It provides RESTful APIs for managing reference data (countries, currencies, silos), legal entities (people and corporations), bank accounts, and customer accounts. The service follows a layered architecture with full audit trail support, idempotent operations, and centralized error handling.

| Attribute             | Value                               |
|-----------------------|-------------------------------------|
| Framework             | Spring Boot 3.2.5                   |
| Language              | Java 17                             |
| Build Tool            | Maven                               |
| Database              | H2 (embedded, runtime)              |
| ORM                   | Spring Data JPA / Hibernate         |
| Migration             | Flyway                              |
| DTO Mapping           | MapStruct 1.5.5                     |
| API Specification     | OpenAPI 3.0                         |

---

## 2. System Context Diagram

```mermaid
C4Context
    title System Context – Payment Service

    Person(client, "API Client", "External consumer or upstream service")
    System(paymentService, "Payment Service", "Manages core data entities: legal entities, bank accounts, customer accounts, reference data")
    SystemDb(h2db, "H2 Database", "Embedded relational database storing all entity and audit data")

    Rel(client, paymentService, "REST/JSON over HTTP", "HTTPS")
    Rel(paymentService, h2db, "JDBC", "SQL")
```

---

## 3. High-Level Architecture Diagram

```mermaid
graph TB
    subgraph "API Clients"
        C[REST Client / UI / Upstream Service]
    end

    subgraph "Payment Service"
        subgraph "Presentation Layer"
            CC[CoreController]
            LC[LegalEntitiesController]
            BC[BankAccountsController]
            CAC[CustomerAccountsController]
        end

        GEH[GlobalExceptionHandler]

        subgraph "Service Layer"
            CS[CountryService]
            CUS[CurrencyService]
            SS[SiloService]
            PS[PersonService]
            COS[CorporationService]
            BAS[BankAccountService]
            CUAS[CustomerAccountService]
        end

        subgraph "Mapper Layer"
            CM[CountryMapper]
            CUM[CurrencyMapper]
            SM[SiloMapper]
            PM[PersonMapper]
            COM[CorporationMapper]
            BAM[BankAccountMapper]
            CUAM[CustomerAccountMapper]
        end

        subgraph "Repository Layer"
            CR[CountryRepository]
            CUR[CurrencyRepository]
            SR[SiloRepository]
            PR[PersonRepository]
            COR[CorporationRepository]
            BAR[BankAccountRepository]
            CUAR[CustomerAccountRepository]
            PAR[PersonAuditRepository]
            COAR[CorporationAuditRepository]
            BAAR[BankAccountAuditRepository]
        end

        subgraph "Entity Layer"
            direction LR
            E1[CountryEntity]
            E2[CurrencyEntity]
            E3[SiloEntity]
            E4[LegalEntityBase]
            E5[PersonEntity]
            E6[CorporationEntity]
            E7[BankAccountEntity]
            E8[CustomerAccountEntity]
            E9[PersonAuditEntity]
            E10[CorporationAuditEntity]
            E11[BankAccountAuditEntity]
        end
    end

    subgraph "Database"
        DB[(H2 Database)]
        FW[Flyway Migrations]
    end

    C -->|HTTP/JSON| CC
    C -->|HTTP/JSON| LC
    C -->|HTTP/JSON| BC
    C -->|HTTP/JSON| CAC

    CC --> CS & CUS & SS
    LC --> PS & COS
    BC --> BAS
    CAC --> CUAS

    CS --> CM --> CR
    CUS --> CUM --> CUR
    SS --> SM --> SR
    PS --> PM --> PR & PAR
    COS --> COM --> COR & COAR
    BAS --> BAM --> BAR & BAAR
    CUAS --> CUAM --> CUAR

    CR & CUR & SR & PR & COR & BAR & CUAR & PAR & COAR & BAAR --> DB
    FW -->|Schema Versioning| DB
```

---

## 4. API Endpoint Summary

### 4.1 Core Endpoints (Reference Data)

| Method | Path               | Operation        | Description                 |
|--------|--------------------|------------------|-----------------------------|
| GET    | `/countries`       | getCountries     | Retrieve all countries      |
| GET    | `/countries/{id}`  | getCountry       | Retrieve country by code    |
| GET    | `/currencies`      | getCurrencies    | Retrieve all currencies     |
| GET    | `/currencies/{id}` | getCurrency      | Retrieve currency by code   |
| GET    | `/silos`           | getSilos         | Retrieve all silos          |
| GET    | `/silos/{id}`      | getSilo          | Retrieve silo by ID         |

### 4.2 Legal Entity Endpoints

| Method | Path                                 | Operation               | Description                           |
|--------|--------------------------------------|-------------------------|---------------------------------------|
| POST   | `/people`                            | createPerson            | Create a new person                   |
| GET    | `/people/{uuid}`                     | getPerson               | Retrieve person by UUID               |
| PATCH  | `/people/{uuid}`                     | updatePerson            | Partial update of a person            |
| GET    | `/people/{uuid}/audit-trail`         | getPersonAuditTrail     | Person version history                |
| POST   | `/corporations`                      | createCorporation       | Create a new corporation              |
| GET    | `/corporations/{uuid}`               | getCorporation          | Retrieve corporation by UUID          |
| PATCH  | `/corporations/{uuid}`               | updateCorporation       | Partial update of a corporation       |
| GET    | `/corporations/{uuid}/audit-trail`   | getCorporationAuditTrail| Corporation version history            |
| GET    | `/corporations/{country}/{code}`     | getCorporationByCode    | Retrieve corporation by country+code  |

### 4.3 Bank Account Endpoints

| Method | Path                                          | Operation                      | Description                       |
|--------|-----------------------------------------------|--------------------------------|-----------------------------------|
| PUT    | `/bank-accounts`                              | createBankAccount              | Create or locate (idempotent)     |
| GET    | `/bank-accounts/{uuid}`                       | getBankAccount                 | Retrieve bank account by UUID     |
| GET    | `/bank-accounts/{uuid}/audit-trail`           | getBankAccountAuditTrail       | Bank account version history      |
| GET    | `/bank-accounts/{uuid}/beneficial-owners`     | getBankAccountBeneficialOwners | Beneficial owners for the account |

### 4.4 Customer Account Endpoints

| Method | Path                                               | Operation                          | Description                       |
|--------|----------------------------------------------------|------------------------------------|-----------------------------------|
| GET    | `/customer-accounts/{uuid}`                        | getCustomerAccount                 | Retrieve customer account by UUID |
| GET    | `/customer-accounts/{uuid}/beneficial-owners`      | getCustomerAccountBeneficialOwners | Beneficial owners for the account |

---

## 5. Technology Stack Diagram

```mermaid
graph LR
    subgraph "Runtime"
        JDK[Java 17]
        SB[Spring Boot 3.2.5]
    end

    subgraph "Web Layer"
        MVC[Spring MVC]
        VAL[Bean Validation]
    end

    subgraph "Data Layer"
        JPA[Spring Data JPA]
        HIB[Hibernate ORM]
        FW[Flyway]
        H2[H2 Database]
    end

    subgraph "Mapping"
        MS[MapStruct 1.5.5]
    end

    subgraph "Testing"
        JU[JUnit 5]
        MO[Mockito]
        SPT[Spring Test]
    end

    subgraph "Build"
        MVN[Maven]
        MCP[Maven Compiler Plugin]
    end

    JDK --> SB --> MVC
    SB --> JPA --> HIB --> H2
    SB --> FW --> H2
    SB --> VAL
    MCP --> MS
    SB --> JU & MO & SPT
```

---

## 6. Data Flow – Create Bank Account (Idempotent PUT)

```mermaid
sequenceDiagram
    actor Client
    participant Controller as BankAccountsController
    participant Service as BankAccountServiceImpl
    participant Mapper as BankAccountMapper
    participant Repo as BankAccountRepository
    participant AuditRepo as BankAccountAuditRepository
    participant DB as H2 Database

    Client->>Controller: PUT /bank-accounts (BankAccountDto)
    Controller->>Service: createOrLocateBankAccount(dto)

    alt IBAN provided
        Service->>Repo: findByIban(iban)
        Repo->>DB: SELECT * FROM bank_accounts WHERE iban = ?
        DB-->>Repo: Result
        alt Account exists
            Repo-->>Service: Optional.of(existingEntity)
            Service->>Mapper: toDto(existingEntity)
            Mapper-->>Service: BankAccountDto
            Service-->>Controller: BankAccountDto (existing)
            Controller-->>Client: 200 OK (existing account)
        else Account does not exist
            Repo-->>Service: Optional.empty()
            Note over Service: Proceed to create
        end
    end

    Service->>Mapper: toEntity(dto)
    Mapper-->>Service: BankAccountEntity
    Service->>Service: Set UUID, version=1, timestamps
    Service->>Repo: save(entity)
    Repo->>DB: INSERT INTO bank_accounts
    DB-->>Repo: Saved entity
    Service->>Service: createAuditRecord(entity)
    Service->>AuditRepo: save(auditEntity)
    AuditRepo->>DB: INSERT INTO bank_account_audits
    Service->>Mapper: toDto(savedEntity)
    Mapper-->>Service: BankAccountDto
    Service-->>Controller: BankAccountDto (new)
    Controller-->>Client: 200 OK (new account)
```

---

## 7. Data Flow – Update Person with Audit Trail

```mermaid
sequenceDiagram
    actor Client
    participant Controller as LegalEntitiesController
    participant Service as PersonServiceImpl
    participant Mapper as PersonMapper
    participant Repo as PersonRepository
    participant AuditRepo as PersonAuditRepository
    participant DB as H2 Database

    Client->>Controller: PATCH /people/{uuid} (PersonDto)
    Controller->>Service: updatePerson(uuid, dto)
    Service->>Repo: findById(uuid)
    Repo->>DB: SELECT * FROM legal_entities WHERE id = ?
    DB-->>Repo: PersonEntity
    Repo-->>Service: Optional.of(entity)

    Service->>Service: Apply partial updates (firstName, lastName, duplicates)
    Service->>Service: Increment version, update timestamp
    Service->>Repo: save(updatedEntity)
    Repo->>DB: UPDATE legal_entities SET ...
    DB-->>Repo: Updated entity

    Service->>Service: createAuditRecord(entity)
    Service->>AuditRepo: save(auditEntity)
    AuditRepo->>DB: INSERT INTO person_audits (version N)

    Service->>Mapper: toDto(updatedEntity)
    Mapper-->>Service: PersonDto
    Service-->>Controller: PersonDto
    Controller-->>Client: 200 OK (updated person)
```

---

## 8. Exception Handling Flow

```mermaid
flowchart TD
    A[Client Request] --> B[Controller]
    B --> C[Service Layer]
    C -->|Success| D[Return DTO]
    C -->|ResourceNotFoundException| E[GlobalExceptionHandler]
    C -->|BadRequestException| E
    B -->|MethodArgumentNotValidException| E
    B -->|HttpMessageNotReadableException| E
    B -->|MethodArgumentTypeMismatchException| E
    B -->|HttpRequestMethodNotSupportedException| E
    B -->|NoResourceFoundException| E
    B -->|ConstraintViolationException| E
    C -->|Unexpected Exception| E

    E -->|404| F["ExceptionDetailDto (NOT_FOUND)"]
    E -->|400| G["ExceptionDetailDto (BAD_REQUEST)"]
    E -->|405| H["ExceptionDetailDto (METHOD_NOT_ALLOWED)"]
    E -->|500| I["ExceptionDetailDto (INTERNAL_SERVER_ERROR)"]

    F --> J[JSON Response to Client]
    G --> J
    H --> J
    I --> J
```

---

## 9. Deployment Architecture

```mermaid
graph TB
    subgraph "Application Runtime"
        APP[Payment Service JAR<br/>Spring Boot 3.2.5<br/>Java 17]
        EMB[Embedded Tomcat]
        APP --- EMB
    end

    subgraph "Embedded Database"
        H2[(H2 In-Memory DB)]
        FW[Flyway Migrations<br/>V1: Schema<br/>V2: Seed Data]
    end

    subgraph "Build Pipeline"
        MVN[Maven Build]
        MSP[MapStruct Annotation Processor]
        MVN --> MSP
        MVN --> APP
    end

    EMB -->|Port 8080| CLIENT[API Clients]
    APP -->|JDBC| H2
    FW -->|Auto-execute on startup| H2
```

---

## 10. Key Design Decisions

| Decision                        | Rationale                                                                 |
|---------------------------------|---------------------------------------------------------------------------|
| Single-Table Inheritance        | `legal_entities` table stores both Person and Corporation via `resource_type` discriminator, simplifying queries and joins |
| Idempotent Bank Account Create  | PUT on `/bank-accounts` uses IBAN as natural key; returns existing if found, preventing duplicate records |
| Version-Based Audit Trail       | Every mutation creates an audit record with an incremented version number for full traceability |
| MapStruct for DTO Mapping       | Compile-time code generation avoids runtime reflection overhead and catches mapping errors at build time |
| Flyway for Migrations           | Versioned SQL scripts ensure reproducible schema evolution across environments |
| Centralized Exception Handling  | `@RestControllerAdvice` provides consistent `ExceptionDetailDto` responses for all error scenarios |
| Many-to-Many Beneficial Owners  | Junction tables link both bank accounts and customer accounts to legal entities (Person/Corporation) |
| H2 Embedded Database            | Simplifies development and testing; easily replaceable with PostgreSQL/MySQL for production |

---

## 11. Non-Functional Considerations

- **Scalability**: Stateless REST service behind a load balancer; database is the bottleneck (swap H2 for production DB)
- **Security**: Input validation via Bean Validation; OWASP-aligned exception handling; parameterized queries via JPA
- **Observability**: SLF4J logging at controller and service layers with contextual UUIDs
- **Maintainability**: Layered architecture with clear separation of concerns; MapStruct eliminates manual mapping code
- **Data Integrity**: `@Transactional` boundaries on service methods; audit records within the same transaction as data mutations
