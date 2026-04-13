# Implementation Quick Reference

## Package Hierarchy Summary

```
com.techwave.paymentservice/
├── config/          → Application beans, audit config
├── controller/      → 5 REST controllers (Core, Corp, Person, BankAcct, CustAcct)
├── service/         → 4 service interfaces + 4 implementations
├── repository/      → 12 JPA repositories
├── entity/          → 15 JPA entities across 6 packages
├── dto/             → Request, Response, Common DTOs
├── mapper/          → 8+ MapStruct mappers
├── exception/       → 3 exceptions + GlobalExceptionHandler
└── util/            → Audit & validation helpers
```

## Entity Hierarchy Map

```
LegalEntityEntity (SINGLE_TABLE)
├── PersonEntity (@DiscriminatorValue("people"))
└── CorporationEntity (@DiscriminatorValue("corporations"))

Reference Data:
├── CountryEntity
├── CurrencyEntity
└── SiloEntity

Accounts:
├── BankAccountEntity
│   └── BankAccountBeneficialOwner (join)
├── CustomerAccountEntity
│   └── CustomerAccountBeneficialOwner (join)

Audit Trail:
├── PersonAudit
├── CorporationAudit
└── BankAccountAudit
```

## Repository Count by Type

| Type | Count | Names |
|------|-------|-------|
| Reference Data | 3 | Country, Currency, Silo |
| Legal Entities | 3 | LegalEntity (base), Corporation, Person |
| Bank Accounts | 2 | BankAccount, BankAccountBeneficialOwner |
| Customer Accounts | 2 | CustomerAccount, CustomerAccountBeneficialOwner |
| Audit Trail | 3 | CorporationAudit, PersonAudit, BankAccountAudit |
| **Total** | **13** | |

## DTO Structure

### Request DTOs (All use Java Records)
- `CorporationRequest`: name, code, incorporationDate, incorporationCountry, type, duplicates
- `PersonRequest`: firstName, lastName, duplicates
- `BankAccountRequest`: beneficiary*, address*, nickname, iban, bic, account*, bank codes, currency, country*

### Response DTOs (All use Java Records)
- `CorporationResponse` / `PersonResponse`: id, resourceType, fields...
- `BankAccountResponse`: id, resourceType, all account fields
- `CustomerAccountResponse`: id, resourceType, name, description, type*, state*, manager, creationTime, silo
- `CountryResponse` / `CurrencyResponse` / `SiloResponse`: reference data
- `*AuditResponse`: resource, resourceType, version, data fields, createdAt
- `LegalEntityResponse`: minimal (id, resourceType) for beneficial owners
- `BeneficialOwnerResponse`: polymorphic person/corporation fields

*Note: Fields marked with * are @NotNull/required*

## Controller Routes (by HTTP Method)

### Reference Data (READ-ONLY)
```
GET    /countries
GET    /countries/{id}
GET    /currencies
GET    /currencies/{id}
GET    /silos
GET    /silos/{id}
```

### Corporations
```
POST   /corporations
GET    /corporations/{uuid}
PATCH  /corporations/{uuid}
GET    /corporations/{uuid}/audit-trail
GET    /corporations/{country}/{code}
```

### People
```
POST   /people
GET    /people/{uuid}
PATCH  /people/{uuid}
GET    /people/{uuid}/audit-trail
```

### Bank Accounts
```
PUT    /bank-accounts          (create or locate)
GET    /bank-accounts/{uuid}
GET    /bank-accounts/{uuid}/audit-trail
GET    /bank-accounts/{uuid}/beneficial-owners
```

### Customer Accounts
```
GET    /customer-accounts/{uuid}
GET    /customer-accounts/{uuid}/beneficial-owners
```

## Service Interface Summary

### CoreDataService
- `getCountries() → List<CountryResponse>`
- `getCountry(id) → CountryResponse`
- `getCurrencies() → List<CurrencyResponse>`
- `getCurrency(id) → CurrencyResponse`
- `getSilos() → List<SiloResponse>`
- `getSilo(id) → SiloResponse`

### LegalEntityService
- **Corporations**: createCorporation, getCorporation, updateCorporation, getCorporationAuditTrail, getCorporationByCode
- **People**: createPerson, getPerson, updatePerson, getPersonAuditTrail

### BankAccountService
- `createOrLocateBankAccount(request) → BankAccountResponse`
- `getBankAccount(uuid) → BankAccountResponse`
- `getBankAccountAuditTrail(uuid) → List<BankAccountAuditResponse>`
- `getBankAccountBeneficialOwners(uuid) → List<LegalEntityResponse>`

### CustomerAccountService
- `getCustomerAccount(uuid) → CustomerAccountResponse`
- `getCustomerAccountBeneficialOwners(uuid) → List<LegalEntityResponse>`

### AuditService
- `recordCorporationAudit(entity): void`
- `recordPersonAudit(entity): void`
- `recordBankAccountAudit(entity): void`

## Mapper Specifications

### Naming Convention: `{Entity}Mapper`
```
CountryMapper
CurrencyMapper
SiloMapper
CorporationMapper
PersonMapper
BankAccountMapper
BankAccountAuditMapper
CustomerAccountMapper
BeneficialOwnerMapper
AuditMapper (utility)
EnumMappers (utility)
```

### Mapping Patterns
```
toResponse(Entity) → Response DTO
toEntity(Request) → Entity
toAuditResponse(AuditEntity) → AuditResponse DTO
toResponses(List<Entity>) → List<Response>
```

## Exception Handling

### Custom Exception Classes
1. `ResourceNotFoundException` → HTTP 404
2. `BadRequestException` → HTTP 400
3. `ConflictException` → HTTP 409

### Error Response Structure
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Resource not found: {id}",
  "messages": ["Resource not found: {id}"]
}
```

### GlobalExceptionHandler Handlers
- `@ExceptionHandler(ResourceNotFoundException.class)` → 404
- `@ExceptionHandler(BadRequestException.class)` → 400
- `@ExceptionHandler(ConflictException.class)` → 409
- `@ExceptionHandler(MethodArgumentNotValidException.class)` → 400 + validation messages

## Database Schema Overview

### Tables: 15 Total

**Reference Data (3 tables)**
- `countries` (PK: code)
- `currencies` (PK: code)
- `silos` (PK: id)

**Legal Entities (1 table, 2 types)**
- `legal_entities` (discriminator: entity_type)

**Accounts (2 tables)**
- `bank_accounts` (PK: id, unique: iban)
- `customer_accounts` (PK: id)

**Beneficial Owners/Join Tables (2 tables)**
- `bank_account_beneficial_owners` (PK: bank_account_id, legal_entity_id)
- `customer_account_beneficial_owners` (PK: customer_account_id, legal_entity_id)

**Audit Trail (3 tables)**
- `bank_account_audits`
- `corporation_audits`
- `person_audits`

### Key Constraints
- Foreign keys on entity_type values
- Composite PKs on join tables
- Unique constraints on: countries.numeric_code, countries.alpha3_code, bank_accounts.iban
- Indexes on: entity_type, country codes, account types/states, resource_id in audits

## Flyway Migrations

1. **V1__init_schema.sql**: All tables, PKs, FKs, indexes
2. **V2__insert_reference_data.sql**: Countries & currencies seed data
3. **V3__audit_tables.sql**: Audit trail table creation

## Implementation Checklist

### Phase 1: Reference Data
- [ ] Entity classes (Country, Currency, Silo)
- [ ] Repositories
- [ ] DTOs
- [ ] Mappers
- [ ] Services
- [ ] Controller
- [ ] Flyway V1 & V2

### Phase 2: Legal Entities
- [ ] Base LegalEntityEntity
- [ ] PersonEntity & CorporationEntity
- [ ] Repositories (all 3)
- [ ] Audit entities & repositories
- [ ] All DTOs (request/response/audit)
- [ ] Mappers
- [ ] Services
- [ ] Controllers (2)
- [ ] Exception handling

### Phase 3: Bank Accounts
- [ ] BankAccountEntity & join entity
- [ ] Repositories
- [ ] Audit entity & repository
- [ ] DTOs
- [ ] Mappers
- [ ] Services
- [ ] Controller
- [ ] Flyway schema update

### Phase 4: Customer Accounts
- [ ] CustomerAccountEntity & join entity
- [ ] All DTOs
- [ ] Repositories
- [ ] Mapper
- [ ] Service
- [ ] Controller

### Phase 5: Testing & Polish
- [ ] Unit tests (services)
- [ ] Integration tests (controllers)
- [ ] Exception handler tests
- [ ] Audit trail verification
- [ ] application.yml

## Key Technical Details

### Single-Table Inheritance Setup
```sql
entity_type VARCHAR(20) NOT NULL
-- Values: "people", "corporations"
```

### Optimistic Locking
- `@Version` on: LegalEntityEntity, BankAccountEntity, CustomerAccountEntity
- Prevents concurrent modification issues

### UUID Generation
```java
@GeneratedValue(strategy = GenerationType.UUID)
```

### Audit Recording Pattern
```
On Entity Create/Update:
1. Call service method (POST/PATCH)
2. Persist entity to database
3. AuditService.recordXxxAudit(entity)
4. Audit entity persisted with version snapshot
```

### Beneficial Owner Retrieval
- Query join table by primary key: BankAccountBeneficialOwner, CustomerAccountBeneficialOwner
- Fetch LegalEntity and determine type via @DiscriminatorValue
- Map to LegalEntityResponse (polymorphic)

## Validation Rules by DTO

| DTO | Field | Rule |
|-----|-------|------|
| CorporationRequest | name | @NotBlank, @Size(1-255) |
| CorporationRequest | code | @NotBlank, @Size(1-100) |
| CorporationRequest | incorporationDate | @PastOrPresent |
| CorporationRequest | incorporationCountry | @Pattern("^[A-Z]{2}$") |
| CorporationRequest | duplicates | @Null (creation) |
| PersonRequest | firstName | @NotBlank, @Size(1-255) |
| PersonRequest | lastName | @NotBlank, @Size(1-255) |
| BankAccountRequest | country | @NotNull, @Pattern("^[A-Z]{2}$") |
| BankAccountRequest | iban | @Pattern(ISO13616 regex) |
| BankAccountRequest | bic | @Pattern(ISO9362 regex) |
| BankAccountRequest | currency | @Pattern("^[A-Z]{3}$") |

## Dependency Injection Pattern (Required)

```java
// Use constructor injection with @RequiredArgsConstructor (or explicit constructor)
public class XxxServiceImpl implements XxxService {
    private final XxxRepository xxxRepository;
    private final XxxMapper xxxMapper;
    private final AuditService auditService;
    
    public XxxServiceImpl(XxxRepository r, XxxMapper m, AuditService a) {
        this.xxxRepository = r;
        this.xxxMapper = m;
        this.auditService = a;
    }
}
```

## Response HTTP Status Codes

| Operation | Status | Note |
|-----------|--------|------|
| GET (found) | 200 OK | |
| GET (not found) | 404 Not Found | |
| POST (success) | 200 OK | OpenAPI returns 200, not 201 |
| PATCH (success) | 200 OK | |
| DELETE (success) | 204 No Content | |
| Validation error | 400 Bad Request | MethodArgumentNotValidException |
| Duplicate/Conflict | 409 Conflict | ConflictException |
| Server error | 5XX | |

## No Lombok Rule

- **Cannot use**: @Getter, @Setter, @RequiredArgsConstructor, @Data, @NoArgsConstructor
- **Instead**:
  - Write explicit getters/setters
  - Use Java records for DTOs (no getters/setters needed)
  - Write explicit constructors where needed
  - MapStruct will generate mapper implementations (not Lombok)

---

This quick reference should guide developers through implementation. For detailed specifications, see ARCHITECTURE_DESIGN.md.

