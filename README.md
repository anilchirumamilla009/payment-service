# Payment Service

A production-grade Spring Boot 3.3.5 REST API for managing core payment data entities including countries, currencies, silos, corporations, people, bank accounts, and customer accounts.

---

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Swagger Documentation](#swagger-documentation)
- [Database](#database)
- [Security (OWASP)](#security-owasp)
- [Testing](#testing)
- [Feature Files (BDD)](#feature-files-bdd)
- [Code Coverage](#code-coverage)
- [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)

---

## Overview

The **payment-service** manages state and access to core data entities used in a payment processing ecosystem:

| Domain             | Capabilities                                      |
|--------------------|---------------------------------------------------|
| **Core**           | Countries, Currencies, Silos (read-only reference) |
| **Legal Entities** | Corporations & People (CRUD + audit trail)         |
| **Bank Accounts**  | Bank accounts (create/read + audit + beneficial owners) |
| **Customer Accounts** | Customer accounts (read + beneficial owners)    |

---

## Architecture

```
┌─────────────┐     ┌──────────────┐     ┌──────────────┐     ┌──────────┐
│  Controller  │────▶│   Service    │────▶│  Repository  │────▶│  H2 DB   │
│  (REST API)  │     │  (Business)  │     │  (JPA/CRUD)  │     │ (In-Mem) │
└─────────────┘     └──────────────┘     └──────────────┘     └──────────┘
       │                                        │
       │            ┌──────────────┐            │
       └───────────▶│    Mapper    │◀───────────┘
                    │ (Entity↔DTO) │
                    └──────────────┘
```

- **API Interfaces** — Hand-crafted contracts per domain (+ OpenAPI code generation)
- **Controllers** — Thin REST layer implementing API interfaces
- **Services** — Business logic, validation, transactional boundaries
- **Repositories** — Spring Data JPA interfaces for each entity
- **Entities** — JPA `@Entity` classes mapped to H2 tables
- **Models (DTOs)** — Plain Java objects for API request/response payloads
- **Mapper** — Centralised `EntityMapper` for entity ↔ DTO conversion

---

## Tech Stack

| Technology              | Version  | Purpose                                  |
|-------------------------|----------|------------------------------------------|
| Java                    | 17       | Language                                  |
| Spring Boot             | 3.3.5    | Framework                                 |
| Spring Data JPA         | –        | ORM / data access                         |
| H2 Database             | –        | In-memory relational database             |
| Spring Security         | –        | Authentication, OWASP security headers    |
| SpringDoc OpenAPI       | 2.8.9    | Swagger UI / OpenAPI 3 documentation      |
| Jakarta Validation      | –        | Bean validation                           |
| Spring Boot Actuator    | –        | Health & info endpoints                   |
| JUnit 5                 | –        | Unit & integration testing                |
| Cucumber                | 7.18.0   | BDD feature testing                       |
| JaCoCo                  | 0.8.12   | Code coverage reporting                   |
| OpenAPI Generator       | 7.14.0   | Contract-first code generation            |

---

## Project Structure

```
payment-service/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/techwave/paymentservice/
    │   │   ├── PaymentServiceApplication.java
    │   │   ├── api/                    # API interface contracts
    │   │   │   ├── BankAccountsApi.java
    │   │   │   ├── CoreApi.java
    │   │   │   ├── CustomerAccountsApi.java
    │   │   │   └── LegalEntitiesApi.java
    │   │   ├── config/                 # Configuration classes
    │   │   │   ├── OpenApiConfig.java
    │   │   │   ├── SecurityConfig.java
    │   │   │   └── WebConfig.java
    │   │   ├── controller/             # REST controllers
    │   │   │   ├── BankAccountsController.java
    │   │   │   ├── CoreController.java
    │   │   │   ├── CustomerAccountsController.java
    │   │   │   ├── HealthController.java
    │   │   │   └── LegalEntitiesController.java
    │   │   ├── entity/                 # JPA entities
    │   │   │   ├── BankAccountAuditEntity.java
    │   │   │   ├── BankAccountEntity.java
    │   │   │   ├── BeneficialOwnerEntity.java
    │   │   │   ├── CorporationAuditEntity.java
    │   │   │   ├── CorporationEntity.java
    │   │   │   ├── CountryEntity.java
    │   │   │   ├── CurrencyEntity.java
    │   │   │   ├── CustomerAccountEntity.java
    │   │   │   ├── PersonAuditEntity.java
    │   │   │   ├── PersonEntity.java
    │   │   │   └── SiloEntity.java
    │   │   ├── exception/              # Exception handling
    │   │   │   ├── BadRequestException.java
    │   │   │   ├── ForbiddenOperationException.java
    │   │   │   ├── GlobalExceptionHandler.java
    │   │   │   ├── ResourceNotFoundException.java
    │   │   │   └── UnauthorizedException.java
    │   │   ├── mapper/                 # Entity ↔ DTO mappers
    │   │   │   └── EntityMapper.java
    │   │   ├── model/                  # API DTOs
    │   │   │   ├── BankAccount.java
    │   │   │   ├── BankAccountAudit.java
    │   │   │   ├── Corporation.java
    │   │   │   ├── CorporationAudit.java
    │   │   │   ├── Country.java
    │   │   │   ├── Currency.java
    │   │   │   ├── CustomerAccount.java
    │   │   │   ├── ExceptionDetail.java
    │   │   │   ├── LegalEntity.java
    │   │   │   ├── Person.java
    │   │   │   ├── PersonAudit.java
    │   │   │   └── Silo.java
    │   │   ├── repository/             # Spring Data JPA repositories
    │   │   │   ├── BankAccountAuditRepository.java
    │   │   │   ├── BankAccountRepository.java
    │   │   │   ├── BeneficialOwnerRepository.java
    │   │   │   ├── CorporationAuditRepository.java
    │   │   │   ├── CorporationRepository.java
    │   │   │   ├── CountryRepository.java
    │   │   │   ├── CurrencyRepository.java
    │   │   │   ├── CustomerAccountRepository.java
    │   │   │   ├── PersonAuditRepository.java
    │   │   │   ├── PersonRepository.java
    │   │   │   └── SiloRepository.java
    │   │   └── service/                # Business services
    │   │       ├── BankAccountsService.java
    │   │       ├── CoreService.java
    │   │       ├── CustomerAccountsService.java
    │   │       └── LegalEntitiesService.java
    │   └── resources/
    │       ├── application.yml
    │       ├── data.sql                # Seed data
    │       ├── openapi.yaml            # OpenAPI 3.0 spec
    │       └── schema.sql              # DDL
    └── test/
        ├── java/com/techwave/paymentservice/
        │   ├── PaymentServiceApplicationTests.java
        │   ├── config/
        │   │   └── SecurityHeadersTest.java
        │   ├── controller/
        │   │   ├── BankAccountsControllerTest.java
        │   │   ├── CoreControllerTest.java
        │   │   ├── CustomerAccountsControllerTest.java
        │   │   ├── HealthControllerTest.java
        │   │   └── LegalEntitiesControllerTest.java
        │   ├── cucumber/
        │   │   ├── CucumberRunnerTest.java
        │   │   ├── CucumberSpringConfig.java
        │   │   └── PaymentServiceStepDefs.java
        │   ├── exception/
        │   │   └── GlobalExceptionHandlerTest.java
        │   ├── mapper/
        │   │   └── EntityMapperTest.java
        │   └── service/
        │       ├── BankAccountsServiceTest.java
        │       ├── CoreServiceTest.java
        │       ├── CustomerAccountsServiceTest.java
        │       └── LegalEntitiesServiceTest.java
        └── resources/
            ├── application.yml
            ├── data.sql
            ├── schema.sql
            └── features/
                ├── bank-accounts.feature
                ├── core.feature
                ├── customer-accounts.feature
                ├── legal-entities.feature
                └── security.feature
```

---

## Getting Started

### Prerequisites

- **Java 17+** (JDK)
- **Maven 3.9+**

### Build

```powershell
cd payment-service
mvn clean install
```

### Run Locally

```powershell
mvn spring-boot:run
```

The service starts on **http://localhost:8080**.

Default credentials (HTTP Basic):
- Username: `admin`
- Password: `admin`

### Smoke Checks

```powershell
# Public endpoints (no auth required)
Invoke-RestMethod http://localhost:8080/api/health
Invoke-RestMethod http://localhost:8080/actuator/health

# Authenticated endpoints
$cred = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("admin:admin"))
$headers = @{ Authorization = "Basic $cred" }
Invoke-RestMethod http://localhost:8080/countries -Headers $headers
Invoke-RestMethod http://localhost:8080/currencies -Headers $headers
Invoke-RestMethod http://localhost:8080/silos -Headers $headers
Invoke-RestMethod http://localhost:8080/corporations/11111111-1111-1111-1111-111111111111 -Headers $headers
Invoke-RestMethod http://localhost:8080/people/22222222-2222-2222-2222-222222222222 -Headers $headers
Invoke-RestMethod http://localhost:8080/bank-accounts/33333333-3333-3333-3333-333333333333 -Headers $headers
Invoke-RestMethod http://localhost:8080/customer-accounts/44444444-4444-4444-4444-444444444444 -Headers $headers
```

---

## API Endpoints

### Core (Reference Data)

| Method | Path                | Description                    | Auth Required |
|--------|---------------------|--------------------------------|:------------:|
| GET    | `/countries`        | Retrieve all countries         | ✅           |
| GET    | `/countries/{id}`   | Retrieve single country        | ✅           |
| GET    | `/currencies`       | Retrieve all currencies        | ✅           |
| GET    | `/currencies/{id}`  | Retrieve single currency       | ✅           |
| GET    | `/silos`            | Retrieve all silos             | ✅           |
| GET    | `/silos/{id}`       | Retrieve single silo           | ✅           |

### Legal Entities

| Method | Path                                   | Description                          | Auth Required |
|--------|----------------------------------------|--------------------------------------|:------------:|
| POST   | `/corporations`                        | Create corporation                   | ✅           |
| GET    | `/corporations/{uuid}`                 | Retrieve corporation by UUID         | ✅           |
| PATCH  | `/corporations/{uuid}`                 | Update corporation                   | ✅           |
| GET    | `/corporations/{uuid}/audit-trail`     | Corporation audit trail              | ✅           |
| GET    | `/corporations/{country}/{code}`       | Retrieve corporation by country/code | ✅           |
| POST   | `/people`                              | Create person                        | ✅           |
| GET    | `/people/{uuid}`                       | Retrieve person by UUID              | ✅           |
| PATCH  | `/people/{uuid}`                       | Update person                        | ✅           |
| GET    | `/people/{uuid}/audit-trail`           | Person audit trail                   | ✅           |

### Bank Accounts

| Method | Path                                          | Description                    | Auth Required |
|--------|-----------------------------------------------|--------------------------------|:------------:|
| PUT    | `/bank-accounts`                              | Create/locate bank account     | ✅           |
| GET    | `/bank-accounts/{uuid}`                       | Retrieve bank account          | ✅           |
| GET    | `/bank-accounts/{uuid}/audit-trail`           | Bank account audit trail       | ✅           |
| GET    | `/bank-accounts/{uuid}/beneficial-owners`     | Bank account beneficial owners | ✅           |

### Customer Accounts

| Method | Path                                              | Description                        | Auth Required |
|--------|---------------------------------------------------|------------------------------------|:------------:|
| GET    | `/customer-accounts/{uuid}`                       | Retrieve customer account          | ✅           |
| GET    | `/customer-accounts/{uuid}/beneficial-owners`     | Customer account beneficial owners | ✅           |

### System

| Method | Path               | Description      | Auth Required |
|--------|--------------------|------------------|:------------:|
| GET    | `/api/health`      | Custom health    | ❌           |
| GET    | `/actuator/health` | Actuator health  | ❌           |

---

## Swagger Documentation

Once the application is running, access the interactive API docs at:

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **OpenAPI JSON**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

---

## Database

The service uses an **H2 in-memory database** (`jdbc:h2:mem:paymentdb`).

- Schema is defined in `src/main/resources/schema.sql`
- Seed data is loaded from `src/main/resources/data.sql`
- The H2 Console is available at [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:paymentdb`, user: `sa`, no password)

### Tables

| Table                   | Description                          |
|-------------------------|--------------------------------------|
| `countries`             | ISO country reference data           |
| `currencies`            | ISO currency reference data          |
| `silos`                 | Business unit / treasury silos       |
| `corporations`          | Corporation legal entities           |
| `people`                | Person legal entities                |
| `bank_accounts`         | Bank account records                 |
| `customer_accounts`     | Customer account records             |
| `corporation_audits`    | Corporation change audit trail       |
| `person_audits`         | Person change audit trail            |
| `bank_account_audits`   | Bank account change audit trail      |
| `beneficial_owners`     | Links accounts to legal entity owners|

---

## Security (OWASP)

The service follows OWASP Top-10 security best practices:

| OWASP Concern                         | Implementation                                                                 |
|---------------------------------------|--------------------------------------------------------------------------------|
| **A01 – Broken Access Control**       | All API endpoints require HTTP Basic authentication (except health/swagger)    |
| **A02 – Cryptographic Failures**      | HSTS header enforced (`Strict-Transport-Security: max-age=31536000`)           |
| **A03 – Injection**                   | Spring Data JPA parameterized queries; Bean Validation on inputs               |
| **A04 – Insecure Design**             | DTO/Entity separation; input validation at service layer                       |
| **A05 – Security Misconfiguration**   | Restrictive CORS; error responses never leak stack traces or internals         |
| **A07 – XSS**                         | `X-XSS-Protection`, `Content-Security-Policy`, `X-Content-Type-Options`        |
| **A08 – Software Integrity**          | Dependency versions pinned; JaCoCo coverage gate                               |
| **A09 – Logging & Monitoring**        | SLF4J structured logging; Actuator health/info endpoints                       |

### Security Headers Applied

- `X-Content-Type-Options: nosniff`
- `X-Frame-Options: DENY` (SAMEORIGIN for H2 console)
- `X-XSS-Protection: 1; mode=block`
- `Strict-Transport-Security: max-age=31536000; includeSubDomains`
- `Content-Security-Policy: default-src 'self'; frame-ancestors 'none'`
- `Referrer-Policy: strict-origin-when-cross-origin`
- `Permissions-Policy: geolocation=(), camera=(), microphone=()`
- `Cache-Control: no-cache, no-store, max-age=0, must-revalidate`

---

## Testing

### Run All Tests

```powershell
mvn clean test
```

### Test Categories

| Category              | Location                                     | Count | Description                           |
|-----------------------|----------------------------------------------|:-----:|---------------------------------------|
| Application Tests     | `PaymentServiceApplicationTests`             |   3   | Context load, bean verification       |
| Controller Tests      | `controller/*ControllerTest`                 |  30+  | MockMvc integration tests             |
| Service Tests         | `service/*ServiceTest`                       |  30+  | Service-layer unit tests              |
| Exception Tests       | `exception/GlobalExceptionHandlerTest`       |   5   | Exception handler mapping             |
| Mapper Tests          | `mapper/EntityMapperTest`                    |  12   | Entity ↔ DTO mapping correctness      |
| Security Tests        | `config/SecurityHeadersTest`                 |   8   | OWASP header verification             |
| Cucumber BDD          | `cucumber/` + `features/*.feature`           |  40+  | End-to-end BDD scenarios              |

---

## Feature Files (BDD)

Cucumber feature files cover all functional and security requirements:

| Feature File                | Scenarios | Covers                                    |
|-----------------------------|:---------:|-------------------------------------------|
| `core.feature`              |    12     | Countries, currencies, silos CRUD + auth  |
| `bank-accounts.feature`     |    12     | Bank account create/read/audit/owners     |
| `legal-entities.feature`    |    18     | Corporation & person CRUD/audit + auth    |
| `customer-accounts.feature` |     5     | Customer account read + owners + auth     |
| `security.feature`          |     8     | OWASP headers, auth enforcement, no leaks |

Run Cucumber tests independently:

```powershell
mvn test -Dtest=CucumberRunnerTest
```

---

## Code Coverage

JaCoCo is configured to generate coverage reports during the `test` phase.

```powershell
mvn clean test
```

View the HTML report at: `target/site/jacoco/index.html`

---

## Configuration

### `application.yml` Key Properties

| Property                           | Default                                          | Description                    |
|------------------------------------|--------------------------------------------------|--------------------------------|
| `server.port`                      | `8080`                                           | HTTP port                      |
| `spring.datasource.url`           | `jdbc:h2:mem:paymentdb`                          | H2 JDBC URL                   |
| `spring.security.user.name`       | `admin`                                          | HTTP Basic username            |
| `spring.security.user.password`   | `admin`                                          | HTTP Basic password            |
| `spring.h2.console.enabled`       | `true`                                           | Enable H2 web console          |
| `springdoc.swagger-ui.path`       | `/swagger-ui.html`                               | Swagger UI path                |

---

## Troubleshooting

| Issue                           | Solution                                                          |
|---------------------------------|-------------------------------------------------------------------|
| Port 8080 in use                | Change `server.port` in `application.yml`                         |
| 401 on API calls                | Include `Authorization: Basic <base64>` header                    |
| H2 console not loading          | Ensure `spring.h2.console.enabled=true`; use SAMEORIGIN frames    |
| OpenAPI gen compilation errors  | Run `mvn clean generate-sources` first                            |
| Tests failing on fresh clone    | Ensure Java 17+ and Maven 3.9+ are installed                     |

---

## License

Internal – TechWave © 2024-2026
