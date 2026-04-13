# Payment Service — Global Agent Instructions

## Application Overview

This is **payment-service**, a Spring Boot 3.2.5 microservice (Java 17) managing state and access to core data entities: countries, currencies, silos, legal entities (people and corporations), bank accounts, and customer accounts.

## Technology Stack

| Layer         | Technology                                   |
|---------------|----------------------------------------------|
| Language      | Java 17                                      |
| Framework     | Spring Boot 3.2.5                            |
| Database      | H2 (in-memory)                               |
| ORM           | Spring Data JPA / Hibernate                  |
| Migrations    | Flyway                                       |
| DTO Mapping   | MapStruct 1.5.5                              |
| API Spec      | OpenAPI 3.0 (`src/main/resources/openapi.yaml`) |
| Testing       | JUnit 5, Mockito, Spring MockMvc             |
| Build         | Maven                                        |

## Architecture

The application follows a layered architecture:

```
REST Controllers → Service Layer → Repository Layer → Database (H2)
                     ↕
              MapStruct Mappers (Entity ↔ DTO)
```

## Package Structure

```
src/main/java/com/techwave/paymentservice/
├── PaymentServiceApplication.java          # Entry point
├── controller/                             # REST controllers
├── service/                                # Business logic interfaces
│   └── impl/                               # Service implementations
├── repository/                             # Spring Data JPA repositories
├── entity/                                 # JPA entities (single-table inheritance for legal entities)
├── dto/                                    # Data Transfer Objects
├── mapper/                                 # MapStruct mappers
└── exception/                              # Global exception handling
```

## Key Domain Concepts

- **Countries / Currencies / Silos**: Read-only reference data
- **Legal Entities**: Base entity with single-table inheritance — `PersonEntity` and `CorporationEntity`
- **Bank Accounts**: Financial accounts linked to legal entities, support beneficial owners
- **Customer Accounts**: Customer-facing accounts with beneficial owner relationships
- **Audit Trail**: Every mutable entity supports `/audit-trail` endpoints

## API Spec

The OpenAPI 3.0 specification at `src/main/resources/openapi.yaml` is the single source of truth for API contracts. All controllers must conform to this spec.

## Conventions All Agents Must Follow

1. **Package**: All classes under `com.techwave.paymentservice`
2. **Entities**: Annotated JPA entities in `entity/` package, class names end with `Entity`
3. **Repositories**: Spring Data JPA interfaces in `repository/`, names end with `Repository`
4. **Services**: Interface in `service/`, implementation in `service/impl/`, names end with `Service` / `ServiceImpl`
5. **Controllers**: REST controllers in `controller/`, names end with `Controller`
6. **DTOs**: Request/response objects in `dto/`, no `Entity` suffix
7. **Mappers**: MapStruct interfaces in `mapper/`, annotated with `@Mapper(componentModel = "spring")`
8. **Migrations**: Flyway SQL scripts in `src/main/resources/db/migration/` with `V{n}__description.sql` naming
9. **Validation**: Use `jakarta.validation` annotations on DTOs; `@Valid` on controller parameters
10. **Exception Handling**: Use `GlobalExceptionHandler` with `@RestControllerAdvice`; throw `ResourceNotFoundException` / `BadRequestException`
11. **Testing**: Unit tests with Mockito in `src/test/java/.../service/`; integration tests with MockMvc in `src/test/java/.../integration/`
12. **No Lombok**: Use MapStruct for mapping; write explicit getters/setters or use Java records for DTOs
