# Architecture Diagram & Component Relationships

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         REST API (OpenAPI 3.0)                          │
│                                                                           │
│  GET/POST/PATCH                                                          │
│  /countries, /currencies, /silos, /corporations, /people                 │
│  /bank-accounts, /customer-accounts                                      │
└──────────────────────────────────┬──────────────────────────────────────┘
                                   │
                                   ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                    CONTROLLER LAYER (REST Entry Points)                 │
│  ┌──────────────────┬──────────────────┬──────────────────┐              │
│  │ CoreDataCtlr     │ LegalEntityCtlr   │ BankAccountCtlr  │              │
│  │ (Countries+      │ (Corp+Person)     │ (Accounts+       │              │
│  │  Currencies)     │                   │  AuditTrail)     │              │
│  └─────────┬────────┴────────┬──────────┴────────┬─────────┘              │
│            │                 │                   │                       │
│  @Valid    │                 │                   │   CustomerAccountCtlr │
│  @RequestBody               │                   │   (GET only)           │
└────────────┼─────────────────┼───────────────────┼───────────────────────┘
             │                 │                   │
             ▼                 ▼                   ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                          SERVICE LAYER (Business Logic)                  │
│  ┌────────────────┬──────────────────┬──────────────────┐                │
│  │ CoreDataService│ LegalEntityService│ BankAccountService│              │
│  │  (Interfaces)  │   (Create, Read,  │  (UPSERT, Read,  │              │
│  │                │    Update + Audit)│    Audit)        │              │
│  └────────┬───────┴────────┬──────────┴────────┬─────────┘                │
│           │                │                   │                        │
│  ┌────────▼─────────────────▼───────────────────▼──────────────────┐     │
│  │              CustomerAccountService                             │     │
│  │              AuditService (cross-cutting)                        │     │
│  │              + ServiceImpl classes (implementations)              │     │
│  └────────┬─────────────────────────────────────────────────────┬──┘     │
│           │                                                     │         │
└───────────┼─────────────────────────────────────────────────────┼─────────┘
            │                                                     │
            │                MapStruct Mappers                    │
            │    (Entity ↔ DTO conversion)                       │
            ▼                                                     ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                         REPOSITORY LAYER (Data Access)                  │
│  ┌──────────────────────┬──────────────────────┬─────────────────────┐   │
│  │ CountryRepository    │ CorporationRepository│ BankAccountRepository│   │
│  │ CurrencyRepository   │ PersonRepository     │ BankAccountBeneficial
│  │ SiloRepository       │ LegalEntityRepository│ OwnerRepository      │   │
│  │                      │ CorporationAuditRepo │ CustomerAccountRepo  │   │
│  │                      │ PersonAuditRepo      │ CustAcctBeneficial   │   │
│  │                      │                      │ OwnerRepository      │   │
│  │                      │                      │ BankAccountAuditRepo │   │
│  └──────────────────────┴──────────────────────┴─────────────────────┘   │
│                                                                           │
│             Spring Data JPA (Hibernate ORM)                              │
│                                                                           │
└───────────────────────────────────────────────────────────────────────────┘
                                   │
                    SQL Queries (Flyway Migrations)
                                   │
                                   ▼
┌───────────────────────────────────────────────────────────────────────────┐
│                          H2 IN-MEMORY DATABASE                             │
│ ┌─────────────────────────────────────────────────────────────────────┐   │
│ │ Reference Data:        Legal Entities:      Accounts:             │   │
│ │  • countries            • legal_entities    • bank_accounts       │   │
│ │  • currencies           └─ SINGLE_TABLE     • customer_accounts   │   │
│ │  • silos                 INHERITANCE        • beneficial owners   │   │
│ │                                             └─ join tables       │   │
│ │ Audit Trail:                                                     │   │
│ │  • corporation_audits                                             │   │
│ │  • person_audits                                                  │   │
│ │  • bank_account_audits                                            │   │
│ └─────────────────────────────────────────────────────────────────────┘   │
└───────────────────────────────────────────────────────────────────────────┘
```

---

## Entity Relationship Diagram (ERD)

```
┌─────────────────────────────────────────────────────────────────┐
│                  LEGAL_ENTITIES (SINGLE_TABLE)                  │ ◄─────────┐
│                                                                  │            │
│  PK: id (UUID)                                                   │            │
│  Discriminator: entity_type (VARCHAR)                           │            │
│  ├─ "people"       ❋ PersonEntity                              │            │
│  └─ "corporations" ❋ CorporationEntity                         │            │
│                                                                  │            │
│  Shared Fields:                                                 │            │
│  • created_at, updated_at, version                              │            │
│  • duplicates (UUID, nullable)                                  │            │
│                                                                  │            │
│  Person-Specific:                                               │            │
│  • first_name, last_name                                        │            │
│                                                                  │            │
│  Corporation-Specific:                                          │            │
│  • name, code, incorporation_date                               │            │
│  • incorporation_country (FK → countries)                       │            │
│  • corporation_type                                             │            │
└─────────────────────────────────────────────────────────────────┘            │
                         ▲                                                      │
                         │                    1:N (beneficial owners)           │
                         │                                                      │
        ┌────────────────┴────────────────┬─────────────────────────────────────┘
        │                                 │
        │                                 │
┌───────┴──────────────────┐   ┌─────────┴──────────────┐
│   BANK_ACCOUNTS          │   │  CUSTOMER_ACCOUNTS     │
│                          │   │                        │
│  PK: id (UUID)           │   │  PK: id (UUID)         │
│  ├─ beneficiary          │   │  ├─ name               │
│  ├─ iban (UNIQUE)        │   │  ├─ description        │
│  ├─ bic                  │   │  ├─ account_type       │
│  ├─ currency (FK)        │   │  ├─ account_state      │
│  ├─ country (FK) ────────┼───┼──┤ account_manager_id  │
│  │  currencies & countries   │  │  (FK → legal_entities)
│  ├─ created_at, updated_at   │  ├─ account_creation   │
│  └─ version (optimistic lock)│  │  silo_id (FK → silos)
│                          │   │  └─ version            │
│  1:N (beneficial owners) │   │                        │
│        │                 │   │  1:N (beneficial      │
│        │                 │   │       owners)          │
└───┬────┴──────────────────┘   └───┬────────────────────┘
    │                               │
    │ M:N via join table            │ M:N via join table
    │                               │
┌───┴──────────────────┐       ┌────┴─────────────────┐
│ BANK_ACCOUNT_         │       │ CUSTOMER_ACCOUNT_    │
│ BENEFICIAL_OWNERS    │       │ BENEFICIAL_OWNERS    │
│                      │       │                      │
│ PK: (bank_account_id,│       │ PK: (customer_acct_id│
│     legal_entity_id) │       │     legal_entity_id) │
│                      │       │                      │
│ ├─ bank_account_id   │       │ ├─ customer_acct_id  │
│ ├─ legal_entity_id   │       │ └─ legal_entity_id   │
│ └─ created_at        │       │                      │
└───────────────────────┘       └──────────────────────┘
         ▲                             ▲
         │                             │
         └─────────────────┬───────────┘
                           │
                      (Both reference)
                           │
                    LEGAL_ENTITIES via
                      entity_type filter

┌──────────────────────────────────────────────────────┐
│              AUDIT TRAIL ENTITIES                    │
│                                                      │
│  ┌───────────────────────────────────────────────┐   │
│  │ CORPORATION_AUDITS                            │   │
│  │  PK: id (Long, AUTO_INCREMENT)               │   │
│  │  FK: resource_id (→ legal_entities.id)        │   │
│  │  • version (snapshot)                         │   │
│  │  • [all corporation fields]                   │   │
│  │  • created_at                                 │   │
│  └───────────────────────────────────────────────┘   │
│                                                      │
│  ┌───────────────────────────────────────────────┐   │
│  │ PERSON_AUDITS                                 │   │
│  │  PK: id (Long, AUTO_INCREMENT)               │   │
│  │  FK: resource_id (→ legal_entities.id)        │   │
│  │  • version (snapshot)                         │   │
│  │  • [all person fields]                        │   │
│  │  • created_at                                 │   │
│  └───────────────────────────────────────────────┘   │
│                                                      │
│  ┌───────────────────────────────────────────────┐   │
│  │ BANK_ACCOUNT_AUDITS                           │   │
│  │  PK: id (Long, AUTO_INCREMENT)               │   │
│  │  FK: resource_id (→ bank_accounts.id)         │   │
│  │  • version (snapshot)                         │   │
│  │  • [all bank account fields]                  │   │
│  │  • created_at                                 │   │
│  └───────────────────────────────────────────────┘   │
│                                                      │
│  Index Pattern: (resource_id, version DESC)          │
└──────────────────────────────────────────────────────┘

┌────────────────────────┐        ┌────────────────────┐
│    COUNTRIES           │        │   CURRENCIES       │
│                        │        │                    │
│ PK: code (2-char)      │        │ PK: code (3-char)  │
│ ├─ name                │        │ └─ name            │
│ ├─ numeric_code        │        │                    │
│ ├─ alpha3_code         │        │ (Referenced by)    │
│ ├─ is_eurozone         │        │ • silos            │
│ └─ is_sepa             │        │ • bank_accounts    │
│                        │        │ • countries.FK     │
│ (Referenced by)        │        │                    │
│ • silos                │        └────────────────────┘
│ • bank_accounts        │
│ • legal_entities       │
└────────────────────────┘

┌──────────────────────────┐
│       SILOS              │
│                          │
│ PK: id (String)          │
│ ├─ name                  │
│ ├─ description           │
│ ├─ email                 │
│ ├─ default_base_currency │
│ │  (FK → currencies)     │
│ ├─ default_credit_limit  │
│ ├─ default_profit_share  │
│ └─ silo_type             │
│                          │
│ (Referenced by)          │
│ • customer_accounts      │
└──────────────────────────┘
```

---

## Data Flow: Create Corporation (Example)

```
Client (HTTP POST /corporations)
    │
    │ CorporationRequest (JSON)
    ▼
┌─────────────────────────────────┐
│ CorporationController.create()   │ @PostMapping
│ ├─ @Valid annotation validates   │
│ │  CorporationRequest fields      │
│ │  → MethodArgumentNotValidEx     │
│ │     (400 with messages)         │
│ └─ calls service layer            │
└──────────────┬──────────────────┘
               │
               ▼
┌─────────────────────────────────────────────┐
│ LegalEntityService.createCorporation()      │ (Interface)
│ ├─ Injected implementation runs             │
│ └─ CorporationMapper.toEntity()             │
│    (CorporationRequest → CorporationEntity) │
└──────────────┬──────────────────────────────┘
               │
               ▼
┌──────────────────────────────────────┐
│ CorporationRepository.save()         │ (Spring Data JPA)
│ ├─ Hibernate INSERT into legal_entities│
│ │  (entity_type = "corporations")     │
│ │  (version = 1, created_at = now)    │
│ └─ Returns CorporationEntity with id  │
└──────────────┬───────────────────────┘
               │
               ▼
┌──────────────────────────────────────┐
│ AuditService.recordCorporationAudit()│ (Cross-cutting)
│ ├─ Creates CorporationAudit entity   │
│ │  (resource_id = entity.id)         │
│ │  (version = entity.version)        │
│ │  (populated with entity fields)    │
│ └─ CorporationAuditRepository.save() │
└──────────────┬───────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│ CorporationMapper.toResponse()      │ (DTO Mapping)
│ (CorporationEntity → CorporationResp│
│ ├─ resourceType = "corporations"    │
│ └─ All fields mapped                │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│ ResponseEntity.ok(CorporationResp)  │ (HTTP 200)
│ ├─ Serialized to JSON                │
│ └─ Returned to client                │
└─────────────────────────────────────┘
```

---

## Data Flow: Get Bank Account Beneficial Owners (Polymorphic Example)

```
Client (HTTP GET /bank-accounts/{uuid}/beneficial-owners)
    │
    ▼
┌──────────────────────────────────────┐
│ BankAccountController.               │
│  getBankAccountBeneficialOwners()    │
└────────────┬─────────────────────────┘
             │
             ▼
┌────────────────────────────────────────────┐
│ BankAccountService.getBankAccountByIdOrNull│
│ └─ if not found: throw                     │
│    ResourceNotFoundException (404)         │
│ else: continue                             │
└────────────┬─────────────────────────────┘
             │
             ▼
┌──────────────────────────────────────────┐
│ BankAccountBeneficialOwnerRepository.     │
│  findByIdBankAccountId(uuid)             │
│ ├─ SQL: SELECT * FROM                    │
│ │  bank_account_beneficial_owners       │
│ │  WHERE bank_account_id = ?            │
│ └─ Returns: List<BankAcctBeneficialOwner>│
└────────────┬─────────────────────────────┘
             │
             ▼
┌──────────────────────────────────────────┐
│ For each BeneficialOwner:               │
│  ├─ owner.getLegalEntity()              │
│  │  (Loaded via @ManyToOne lazy fetch) │
│  ├─ Check entity_type discriminator:   │
│  │  ├─ "people" → PersonEntity         │
│  │  └─ "corporations" → CorporationEnt │
│  └─ Polymorphic handling              │
└────────────┬─────────────────────────────┘
             │
             ▼
┌──────────────────────────────────────────┐
│ BeneficialOwnerMapper.toResponses()      │
│ ├─ Maps each LegalEntity to polymorphic │
│ │  LegalEntityResponse                  │
│ │  ├─ PersonResponse (resourceType=     │
│ │  │  "people", firstName/lastName)   │
│ │  └─ CorporationResponse (resourceType=│
│ │     "corporations", name/code)       │
│ └─ Returns: List<LegalEntityResponse>  │
└────────────┬─────────────────────────────┘
             │
             ▼
┌──────────────────────────────────────────┐
│ ResponseEntity.ok(List<LegalEntityResp>)│
│ ├─ Serialized to JSON array              │
│ │ [ { id, resourceType, firstName... }, │
│ │   { id, resourceType, name...} ]     │
│ └─ Returned to client                   │
└──────────────────────────────────────────┘
```

---

## Audit Trail Recording Flow

```
Service Method (e.g., updateCorporation)
    │
    ├─ 1. Load entity: corporationRepository.findByIdOrElseThrow()
    │
    ├─ 2. Update fields from request
    │
    ├─ 3. Persist: corporationRepository.save()
    │    └─ Hibernate:
    │       ├─ version += 1 (optimistic lock)
    │       ├─ updated_at = now()
    │       └─ INSERT/UPDATE legal_entities
    │
    ├─ 4. Call AuditService.recordCorporationAudit(entity)
    │    └─ Create CorporationAudit:
    │       ├─ resource_id = entity.id
    │       ├─ version = entity.version (NEW version)
    │       ├─ Copy all corporation fields (current state)
    │       ├─ created_at = now()
    │       └─ INSERT corporation_audits
    │
    └─ 5. Map to response: corporationMapper.toResponse(entity)
```

---

## Exception Mapping (HTTP Status)

```
Exception Flow → HTTP Response

┌─────────────────────────────────────┐    ┌──────────────────────┐
│ ResourceNotFoundException            │───▶│ 404 Not Found        │
│ (Entity lookup fails)               │    │ {status: 404,        │
└─────────────────────────────────────┘    │  error: "Not Found", │
                                           │  message: "...",     │
┌─────────────────────────────────────┐    │  messages: [...] }   │
│ BadRequestException                 │    └──────────────────────┘
│ (Business rule violation)           │
└────────────┬────────────────────────┘    ┌──────────────────────┐
             │                             │ 400 Bad Request      │
             └────────────────────────────▶│ {status: 400,        │
                                           │  error: "Bad Request"│
┌─────────────────────────────────────┐    │  message: "...",     │
│ ConflictException                   │    │  messages: [...] }   │
│ (Duplicate, Conflict)               │    └──────────────────────┘
└────────────┬────────────────────────┘
             │                             ┌──────────────────────┐
             └────────────────────────────▶│ 409 Conflict         │
                                           │ {status: 409,        │
┌─────────────────────────────────────┐    │  error: "Conflict",  │
│ MethodArgumentNotValidException     │    │  message: "...",     │
│ (Validation failure)                │    │  messages: [         │
└────────────┬────────────────────────┘    │    "Field error 1",  │
             │                             │    "Field error 2"   │
             └────────────────────────────▶│  ]}                  │
                                           │ 400 Bad Request      │
                                           └──────────────────────┘
```

---

## Component Summary Matrix

| Component | Type | Responsibility | Dependency |
|-----------|------|-----------------|------------|
| CoreDataController | Controller | REST routes for reference data | CoreDataService |
| CoreDataService | Interface | Business logic for countries/currencies/silos | Repositories, Mappers |
| CoreDataServiceImpl | Service | Implementation | Repositories, Mappers |
| CountryRepository | Repository | Database access for countries | H2 Database |
| CountryEntity | Entity | JPA entity mapping | None |
| CountryResponse | DTO | Response serialization | None |
| CountryMapper | Mapper | Entity ↔ DTO conversion | MapStruct (compiler) |
| GlobalExceptionHandler | Handler | Exception to HTTP response mapping | Spring |
| CorporationAudit | Entity | Audit trail versioning | Audit tables |
| LegalEntityRepository | Repository | Base repository for all legal entities | H2 Database |

---

This diagram provides visual context for the architecture design. Refer to ARCHITECTURE_DESIGN.md for detailed specifications of each component.

