# Payment Service — Project Conventions

These conventions are specific to payment-service and supplement the general Java/Spring standards.

## Package Layout

| Package | Purpose | Naming |
|---------|---------|--------|
| `controller/` | REST controllers | `{Domain}Controller` |
| `service/` | Business logic interfaces | `{Domain}Service` |
| `service/impl/` | Service implementations | `{Domain}ServiceImpl` |
| `repository/` | Spring Data JPA interfaces | `{Domain}Repository` |
| `entity/` | JPA entities | `{Domain}Entity` |
| `dto/` | Request/response objects | `{Domain}Request`, `{Domain}Response` |
| `mapper/` | MapStruct mappers | `{Domain}Mapper` |
| `exception/` | Exception classes + handler | `{Type}Exception`, `GlobalExceptionHandler` |

All classes under `com.techwave.paymentservice`.

## P01: No Lombok
This project does not use Lombok. Write explicit getters/setters or use Java records.

```java
// GOOD
public class PersonEntity {
    private String firstName;
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
}

// BAD
@Data
public class PersonEntity { ... }
```

## P02: Flyway Migration Naming
```
V{n}__description.sql
```
- Sequential version numbers (`V1__`, `V2__`, `V3__`)
- Double underscore separator
- Lowercase description with underscores
- Check existing migrations before choosing the next version number

## P03: OpenAPI Is the Source of Truth
- All controller endpoints must match `src/main/resources/openapi.yaml`
- Request/response shapes must conform to OpenAPI schemas
- HTTP methods and paths must be exact matches
- Read the spec before implementing any controller

## P04: Audit Trail Pattern
Every mutable entity must support audit trail endpoints:
```
GET /{resource}/{uuid}/audit-trail
```

## P05: Beneficial Owner Pattern
Bank accounts and customer accounts support:
```
GET /{resource}/{uuid}/beneficial-owners
```

## P06: Single-Table Inheritance for Legal Entities
`PersonEntity` and `CorporationEntity` extend a base `LegalEntityBase` using JPA single-table inheritance with discriminator column.

## P07: Test Organization
| Type | Location | Naming |
|------|----------|--------|
| Unit tests | `src/test/java/.../service/` | `{Service}Test.java` |
| Integration tests | `src/test/java/.../integration/` | `{Feature}IntegrationTest.java` |
| Mapper tests | `src/test/java/.../mapper/` | `{Mapper}Test.java` |
