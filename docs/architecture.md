# Payment Service — Architecture Documentation

## 1. Overview

Payment-service is a Spring Boot 3.2.5 microservice (Java 17) built and maintained through an **agent-driven development workflow**. Four specialized AI agents collaborate in a pipeline — each with focused responsibilities, tool access, and shared knowledge — to scaffold, implement, test, and review the codebase.

---

## 2. Agent-Centric Architecture (Master View)

```mermaid
graph TB
    DEV["👤 Developer"]

    subgraph KNOWLEDGE["📚 Shared Knowledge Layer"]
        AGENTS_MD["AGENTS.md<br/>12 conventions, tech stack,<br/>package structure, domain concepts"]
        OPENAPI["openapi.yaml<br/>API source of truth<br/>21 endpoints"]
    end

    subgraph AGENT_PIPELINE["🤖 Agent Pipeline"]
        direction LR
        SCAFFOLD["@scaffold<br/>🔧 read, edit, search, execute<br/>─────────────<br/>Creates: Entity, Repo, DTO,<br/>Mapper, Service, Controller,<br/>Migration, Test stubs"]
        IMPLEMENT["@implementer<br/>🔧 read, edit, search, execute<br/>─────────────<br/>Writes: Business logic,<br/>Mappings, Queries,<br/>Validation, Exceptions"]
        TESTER["@tester<br/>🔧 read, edit, search, execute<br/>─────────────<br/>Writes: Unit tests (Mockito),<br/>Integration tests (MockMvc),<br/>Edge cases, Assertions"]
        REVIEWER["@reviewer<br/>🔧 read, search<br/>─────────────<br/>Checks: Conventions,<br/>OpenAPI conformance,<br/>Security, Test quality"]
    end

    subgraph SKILLS["🎯 On-Demand Skills"]
        JS["/java-standards<br/>G01–G10 · C01–X02 · P01–P07<br/>Java 17, Spring Boot, Project rules"]
        SA["/security-audit<br/>OWASP A01–A10 · CVE scanning<br/>Spring hardening, PII protection"]
    end

    EXPLORE["@explore<br/>🔍 read, search<br/>Codebase Q&A (anytime)"]

    subgraph APP["💻 payment-service Codebase"]
        direction TB
        subgraph LAYERS["Application Layers"]
            CTRL["controller/"]
            SVC["service/ + impl/"]
            MAP["mapper/"]
            REPO["repository/"]
            ENT["entity/"]
            DTO["dto/"]
            EXC["exception/"]
        end
        MIGRATION["db/migration/"]
        TESTS["test/ (unit + integration)"]
    end

    DEV -->|"@scaffold"| SCAFFOLD
    DEV -->|"@implementer"| IMPLEMENT
    DEV -->|"@tester"| TESTER
    DEV -->|"@reviewer"| REVIEWER
    DEV -->|"@explore"| EXPLORE

    KNOWLEDGE -->|"auto-loaded into all agents"| AGENT_PIPELINE
    KNOWLEDGE -->|"auto-loaded"| EXPLORE

    SCAFFOLD -->|"scaffolded files"| IMPLEMENT
    IMPLEMENT -->|"implemented code"| TESTER
    TESTER -->|"tested code"| REVIEWER
    REVIEWER -->|"fix findings"| IMPLEMENT

    IMPLEMENT -.->|"loads"| JS
    IMPLEMENT -.->|"loads"| SA
    REVIEWER -.->|"loads"| JS
    REVIEWER -.->|"loads"| SA

    SCAFFOLD -->|"creates"| APP
    IMPLEMENT -->|"modifies"| APP
    TESTER -->|"writes"| TESTS
    REVIEWER -->|"reads"| APP
    EXPLORE -->|"reads"| APP

    style KNOWLEDGE fill:#e3f2fd,stroke:#1565c0,stroke-width:2px
    style AGENT_PIPELINE fill:#e8f5e9,stroke:#2e7d32,stroke-width:2px
    style SKILLS fill:#fff8e1,stroke:#f9a825,stroke-width:2px
    style APP fill:#f3e5f5,stroke:#6a1b9a,stroke-width:2px
```

---

## 3. Agent Pipeline — Detailed Workflow

```mermaid
sequenceDiagram
    participant DEV as 👤 Developer
    participant SC as @scaffold
    participant IM as @implementer
    participant TE as @tester
    participant RE as @reviewer
    participant EX as @explore
    participant CODE as 💻 Codebase

    Note over DEV,CODE: Phase 1 — Scaffold
    DEV->>SC: "Scaffold the Person feature"
    SC->>CODE: Reads AGENTS.md + openapi.yaml
    SC->>CODE: Creates Entity, Repo, DTO, Mapper, Service, Controller, Migration, Test stubs
    SC-->>DEV: File list + TODOs for implementer

    Note over DEV,CODE: Phase 2 — Implement
    DEV->>IM: "Implement Person feature"
    IM->>CODE: Reads scaffolded files + openapi.yaml
    Note right of IM: Loads /java-standards<br/>Loads /security-audit
    IM->>CODE: Fills in service logic, mappings, validation, controller handlers
    IM->>CODE: Runs mvn compile
    IM-->>DEV: Implementation summary + decisions

    Note over DEV,CODE: Phase 3 — Test
    DEV->>TE: "Write tests for Person feature"
    TE->>CODE: Reads implementation + openapi.yaml
    TE->>CODE: Writes unit tests (Mockito) + integration tests (MockMvc)
    TE->>CODE: Runs mvn test
    TE-->>DEV: Test results + coverage report

    Note over DEV,CODE: Phase 4 — Review
    DEV->>RE: "Review Person feature"
    RE->>CODE: Reads all Person-related files
    Note right of RE: Loads /java-standards<br/>Loads /security-audit
    RE-->>DEV: Review report (CRITICAL / WARNING / INFO)

    alt Findings exist
        DEV->>IM: "Fix review findings"
        IM->>CODE: Fixes issues
        IM-->>DEV: Fixed
    end

    Note over DEV,CODE: Anytime — Explore
    DEV->>EX: "How does audit trail work?"
    EX->>CODE: Searches + reads codebase
    EX-->>DEV: Explanation with file references
```

---

## 4. Agent Responsibilities Matrix

```mermaid
graph LR
    subgraph SCAFFOLD_SCOPE["@scaffold — Creates Structure"]
        S_ENT["entity/<br/>*Entity.java"]
        S_REPO["repository/<br/>*Repository.java"]
        S_DTO["dto/<br/>*Request.java<br/>*Response.java"]
        S_MAP["mapper/<br/>*Mapper.java"]
        S_SVC["service/<br/>*Service.java<br/>impl/*ServiceImpl.java"]
        S_CTRL["controller/<br/>*Controller.java"]
        S_MIG["db/migration/<br/>V{n}__*.sql"]
        S_TEST["test/<br/>*Test.java stubs"]
    end

    subgraph IMPL_SCOPE["@implementer — Fills Logic"]
        I_SVC["Service methods<br/>Business rules"]
        I_MAP["Mapper methods<br/>@Mapping annotations"]
        I_REPO["Custom queries<br/>@Query methods"]
        I_CTRL["Controller handlers<br/>HTTP status codes"]
        I_DTO["Validation annotations<br/>@NotBlank, @Size"]
        I_EXC["Exception throwing<br/>ResourceNotFoundException"]
    end

    subgraph TEST_SCOPE["@tester — Writes Tests"]
        T_UNIT["Unit tests<br/>service/ (Mockito)"]
        T_INT["Integration tests<br/>integration/ (MockMvc)"]
        T_EDGE["Edge cases<br/>Nulls, 404s, validation"]
    end

    subgraph REVIEW_SCOPE["@reviewer — Validates"]
        R_CONV["Convention compliance<br/>AGENTS.md rules 1–12"]
        R_API["OpenAPI conformance<br/>Paths, shapes, status codes"]
        R_SEC["Security check<br/>OWASP, injection, PII"]
        R_TEST["Test quality<br/>Coverage, assertions"]
    end

    style SCAFFOLD_SCOPE fill:#e3f2fd,stroke:#1565c0
    style IMPL_SCOPE fill:#e8f5e9,stroke:#2e7d32
    style TEST_SCOPE fill:#fff3e0,stroke:#e65100
    style REVIEW_SCOPE fill:#fce4ec,stroke:#c62828
```

---

## 5. Knowledge & Skill Flow

```mermaid
graph TB
    subgraph ALWAYS["Always Loaded — Every Agent Gets This"]
        AGENTS_MD["AGENTS.md"]
        AGENTS_MD_CONTENT["• Spring Boot 3.2.5, Java 17, H2, Flyway, MapStruct<br/>• Package structure: controller → service → repository → entity<br/>• 12 conventions: naming, validation, no Lombok, etc.<br/>• Domain: countries, currencies, silos, legal entities, accounts<br/>• openapi.yaml is the single source of truth"]
    end

    subgraph ON_DEMAND["Loaded On Demand — Only When Relevant"]
        subgraph JS_SKILL["/java-standards"]
            JS_REF1["references/java17-standards.md<br/>Records, sealed classes, switch expressions,<br/>Optional, streams, naming (G01–G10)"]
            JS_REF2["references/spring-boot-standards.md<br/>Controllers (C01–C05), Services (S01–S04),<br/>Repos (R01–R03), Entities (E01–E03),<br/>DTOs (D01–D03), Mappers (M01–M02),<br/>Exceptions (X01–X02)"]
            JS_REF3["references/project-conventions.md<br/>No Lombok (P01), Flyway naming (P02),<br/>OpenAPI truth (P03), Audit trail (P04),<br/>Beneficial owners (P05), Inheritance (P06),<br/>Test organization (P07)"]
        end

        subgraph SA_SKILL["/security-audit"]
            SA_REF1["references/owasp-top10.md<br/>A01 Access Control → A10 SSRF<br/>Checklist per category"]
            SA_REF2["references/report-template.md<br/>CRITICAL / HIGH / MEDIUM / LOW / INFO<br/>CVE table, remediation format"]
        end
    end

    AGENTS_MD --> AGENTS_MD_CONTENT

    IMPL["@implementer"] -->|"auto"| ALWAYS
    IMPL -->|"loads"| JS_SKILL
    IMPL -->|"loads"| SA_SKILL

    REV["@reviewer"] -->|"auto"| ALWAYS
    REV -->|"loads"| JS_SKILL
    REV -->|"loads"| SA_SKILL

    SCAF["@scaffold"] -->|"auto"| ALWAYS
    TEST["@tester"] -->|"auto"| ALWAYS
    EXPL["@explore"] -->|"auto"| ALWAYS

    style ALWAYS fill:#e3f2fd,stroke:#1565c0,stroke-width:2px
    style ON_DEMAND fill:#fff8e1,stroke:#f9a825,stroke-width:2px
    style JS_SKILL fill:#fff9c4,stroke:#f9a825
    style SA_SKILL fill:#ffccbc,stroke:#bf360c
```

---

## 6. What Agents Build — Application Architecture

```mermaid
graph TB
    CLIENT["🌐 API Clients"]

    subgraph SPRING_BOOT["Spring Boot 3.2.5 — payment-service"]
        subgraph CONTROLLER["Controller Layer (created by @scaffold, implemented by @implementer)"]
            CC["CoreController<br/>/countries, /currencies, /silos"]
            LEC["LegalEntitiesController<br/>/people, /corporations"]
            BAC["BankAccountsController<br/>/bank-accounts"]
            CAC["CustomerAccountsController<br/>/customer-accounts"]
        end

        GEH["GlobalExceptionHandler<br/>@RestControllerAdvice"]

        subgraph SERVICE["Service Layer (created by @scaffold, implemented by @implementer)"]
            CS["CountryService"]
            CUS["CurrencyService"]
            SS["SiloService"]
            PS["PersonService"]
            COS["CorporationService"]
            BAS["BankAccountService"]
            CAS["CustomerAccountService"]
        end

        subgraph MAPPER["MapStruct Mappers (created by @scaffold, mapped by @implementer)"]
            M["Entity ↔ DTO<br/>@Mapper(componentModel = spring)"]
        end

        subgraph REPO["Repository Layer (created by @scaffold, queries by @implementer)"]
            R["Spring Data JPA<br/>JpaRepository interfaces"]
        end
    end

    subgraph DATABASE["Database (migration by @scaffold)"]
        H2["H2 In-Memory DB"]
        FW["Flyway Migrations"]
    end

    subgraph TESTING["Tests (written by @tester, verified by @reviewer)"]
        UT["Unit Tests<br/>Mockito"]
        IT["Integration Tests<br/>MockMvc"]
    end

    CLIENT -->|"HTTP REST"| CONTROLLER
    CONTROLLER -->|"delegates"| SERVICE
    CONTROLLER ---|"errors"| GEH
    SERVICE -->|"uses"| MAPPER
    SERVICE -->|"queries"| REPO
    REPO -->|"JPA/Hibernate"| H2
    FW -->|"manages schema"| H2
    UT -.->|"tests"| SERVICE
    IT -.->|"tests"| CONTROLLER

    style SPRING_BOOT fill:#e8f5e9,stroke:#2e7d32,stroke-width:2px
    style CONTROLLER fill:#bbdefb,stroke:#1565c0
    style SERVICE fill:#c8e6c9,stroke:#2e7d32
    style MAPPER fill:#fff9c4,stroke:#f9a825
    style REPO fill:#ffe0b2,stroke:#e65100
    style DATABASE fill:#f3e5f5,stroke:#6a1b9a
    style TESTING fill:#ffccbc,stroke:#bf360c
```

---

## 7. Domain Model

```mermaid
erDiagram
    countries {
        VARCHAR id PK "Alpha-2 code (e.g., US, IE)"
        VARCHAR name
        VARCHAR numeric_code
        VARCHAR alpha3_code
        BOOLEAN eurozone
        BOOLEAN sepa
    }

    currencies {
        VARCHAR id PK "Currency code (e.g., EUR, USD)"
        VARCHAR name
    }

    silos {
        VARCHAR id PK
        VARCHAR name
        VARCHAR description
        VARCHAR email
        VARCHAR default_base_currency FK
        DECIMAL default_credit_limit
        DECIMAL default_profit_share
        VARCHAR type
    }

    legal_entities {
        UUID id PK
        VARCHAR resource_type "Discriminator: people | corporations"
        VARCHAR first_name "Person only"
        VARCHAR last_name "Person only"
        VARCHAR name "Corporation only"
        VARCHAR code "Corporation only"
        DATE incorporation_date "Corporation only"
        VARCHAR incorporation_country "Corporation only"
        VARCHAR type "Corporation only"
        UUID duplicates
        INTEGER version
    }

    bank_accounts {
        UUID id PK
        VARCHAR beneficiary
        VARCHAR iban
        VARCHAR bic
        VARCHAR currency FK
        VARCHAR country FK
        INTEGER version
    }

    customer_accounts {
        UUID id PK
        VARCHAR name
        VARCHAR account_type
        VARCHAR account_state
        UUID account_manager
        VARCHAR silo FK
    }

    person_audits {
        BIGINT id PK
        UUID resource FK
        INTEGER version
        VARCHAR first_name
        VARCHAR last_name
    }

    corporation_audits {
        BIGINT id PK
        UUID resource FK
        INTEGER version
        VARCHAR name
        VARCHAR code
    }

    bank_account_audits {
        BIGINT id PK
        UUID resource FK
        INTEGER version
        VARCHAR iban
        VARCHAR bic
    }

    bank_account_beneficial_owners {
        UUID bank_account_id FK
        UUID legal_entity_id FK
    }

    customer_account_beneficial_owners {
        UUID customer_account_id FK
        UUID legal_entity_id FK
    }

    silos ||--o{ currencies : "default_base_currency"
    legal_entities ||--o{ person_audits : "has audits"
    legal_entities ||--o{ corporation_audits : "has audits"
    bank_accounts ||--o{ bank_account_audits : "has audits"
    bank_accounts }o--o{ legal_entities : "beneficial_owners"
    customer_accounts }o--o{ legal_entities : "beneficial_owners"
    customer_accounts }o--|| silos : "belongs to"
    bank_accounts }o--|| currencies : "denominated in"
    bank_accounts }o--|| countries : "domiciled in"
```

---

## 8. Entity Inheritance (Legal Entities)

```mermaid
classDiagram
    class LegalEntityBase {
        <<Abstract>>
        -Long internalId
        -UUID uuid
        -UUID duplicates
        -int version
        +prePersist()
    }

    class PersonEntity {
        -String firstName
        -String lastName
    }

    class CorporationEntity {
        -String name
        -String code
        -LocalDate incorporationDate
        -String incorporationCountry
        -String type
    }

    LegalEntityBase <|-- PersonEntity : resource_type = "people"
    LegalEntityBase <|-- CorporationEntity : resource_type = "corporations"

    note for LegalEntityBase "Single-table inheritance\n@DiscriminatorColumn(resource_type)\nTable: legal_entities"
```

---

## 9. API Surface

```mermaid
graph LR
    subgraph CORE["Core — Read Only (6 endpoints)"]
        G1["GET /countries"]
        G2["GET /countries/{id}"]
        G3["GET /currencies"]
        G4["GET /currencies/{id}"]
        G5["GET /silos"]
        G6["GET /silos/{id}"]
    end

    subgraph LEGAL["Legal Entities — CRUD + Audit (9 endpoints)"]
        L1["POST /people"]
        L2["GET /people/{uuid}"]
        L3["PATCH /people/{uuid}"]
        L4["GET /people/{uuid}/audit-trail"]
        L5["POST /corporations"]
        L6["GET /corporations/{uuid}"]
        L7["PATCH /corporations/{uuid}"]
        L8["GET /corporations/{uuid}/audit-trail"]
        L9["GET /corporations/{country}/{code}"]
    end

    subgraph BANK["Bank Accounts — CRUD + Audit + Owners (4 endpoints)"]
        B1["PUT /bank-accounts"]
        B2["GET /bank-accounts/{uuid}"]
        B3["GET /bank-accounts/{uuid}/audit-trail"]
        B4["GET /bank-accounts/{uuid}/beneficial-owners"]
    end

    subgraph CUST["Customer Accounts — Read + Owners (2 endpoints)"]
        CA1["GET /customer-accounts/{uuid}"]
        CA2["GET /customer-accounts/{uuid}/beneficial-owners"]
    end

    style CORE fill:#e3f2fd,stroke:#1565c0
    style LEGAL fill:#e8f5e9,stroke:#2e7d32
    style BANK fill:#fff3e0,stroke:#e65100
    style CUST fill:#fce4ec,stroke:#c62828
```

**Total: 21 endpoints** across 4 controllers.

---

## 10. Request Flow (Sequence)

```mermaid
sequenceDiagram
    participant C as Client
    participant CT as Controller
    participant V as Jakarta Validation
    participant S as ServiceImpl
    participant M as MapStruct Mapper
    participant R as Repository
    participant DB as H2 Database

    C->>CT: HTTP Request (e.g., POST /people)
    CT->>V: @Valid on @RequestBody
    alt Validation Fails
        V-->>CT: MethodArgumentNotValidException
        CT-->>C: 400 Bad Request (ExceptionDetail)
    end
    CT->>S: service.create(request)
    S->>M: mapper.toEntity(request)
    M-->>S: PersonEntity
    S->>R: repository.save(entity)
    R->>DB: INSERT
    DB-->>R: Persisted entity
    R-->>S: PersonEntity (with ID)
    S->>M: mapper.toDto(entity)
    M-->>S: PersonResponse
    S-->>CT: PersonResponse
    CT-->>C: 200 OK (JSON)
```

---

## 11. Error Handling Flow

```mermaid
flowchart TD
    REQ["Incoming Request"] --> CTRL["Controller"]
    CTRL --> VAL{"@Valid<br/>passes?"}
    VAL -->|No| MAVE["MethodArgumentNotValidException"]
    VAL -->|Yes| SVC["Service Layer"]
    SVC --> FOUND{"Resource<br/>found?"}
    FOUND -->|No| RNFE["ResourceNotFoundException"]
    FOUND -->|Yes| LOGIC{"Business<br/>rule OK?"}
    LOGIC -->|No| BRE["BadRequestException"]
    LOGIC -->|Yes| OK["200 OK Response"]
    SVC --> UNEXP["Unexpected Exception"]

    MAVE --> GEH["GlobalExceptionHandler<br/>@RestControllerAdvice"]
    RNFE --> GEH
    BRE --> GEH
    UNEXP --> GEH

    GEH --> R400["400 Bad Request<br/>ExceptionDetail"]
    GEH --> R404["404 Not Found<br/>ExceptionDetail"]
    GEH --> R500["500 Internal Error<br/>ExceptionDetail"]

    style GEH fill:#ffcdd2,stroke:#c62828,stroke-width:2px
    style OK fill:#c8e6c9,stroke:#2e7d32
    style R400 fill:#fff9c4,stroke:#f9a825
    style R404 fill:#ffe0b2,stroke:#e65100
    style R500 fill:#ffcdd2,stroke:#c62828
```

---

## 12. File Structure Summary

```
payment-service/
├── AGENTS.md                               # Global agent instructions (loaded by ALL agents)
├── pom.xml                                 # Maven build + dependencies
├── README.md                               # Project documentation
├── docs/
│   └── architecture.md                     # This file
├── .github/
│   ├── agents/                             # Copilot custom agents
│   │   ├── scaffold.agent.md               # Phase 1: Boilerplate generator
│   │   ├── implementer.agent.md            # Phase 2: Business logic writer
│   │   ├── tester.agent.md                 # Phase 3: Test writer
│   │   └── reviewer.agent.md              # Phase 4: Code reviewer
│   └── skills/                             # On-demand knowledge
│       ├── java-standards/                 # Java 17 + Spring Boot standards
│       │   ├── SKILL.md
│       │   └── references/
│       │       ├── java17-standards.md     # G01–G10
│       │       ├── spring-boot-standards.md # C01–X02
│       │       └── project-conventions.md  # P01–P07
│       └── security-audit/                 # OWASP + CVE audit
│           ├── SKILL.md
│           └── references/
│               ├── owasp-top10.md          # A01–A10 checklist
│               └── report-template.md      # Severity format
├── src/
│   ├── main/
│   │   ├── java/com/techwave/paymentservice/
│   │   │   ├── PaymentServiceApplication.java
│   │   │   ├── controller/                 # 4 REST controllers
│   │   │   ├── service/                    # 7 service interfaces
│   │   │   │   └── impl/                   # 7 service implementations
│   │   │   ├── repository/                 # JPA repositories
│   │   │   ├── entity/                     # JPA entities
│   │   │   ├── dto/                        # Request/Response DTOs
│   │   │   ├── mapper/                     # MapStruct mappers
│   │   │   └── exception/                  # Error handling
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── openapi.yaml                # API source of truth (21 endpoints)
│   │       └── db/migration/               # Flyway SQL scripts
│   └── test/java/.../
│       ├── service/                        # Unit tests (Mockito)
│       └── integration/                    # Integration tests (MockMvc)
```
