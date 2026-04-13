# Java/Spring Boot/Scala Development Orchestrator

---
**Multi-Model Architecture**: This orchestrator uses specialized AI models optimized for different task types:
- **Planning/Architecture**: Claude Sonnet 4 (strategic thinking, system design)
- **Code Generation**: GPT-4o (multi-language proficiency, Spring Boot expertise)
- **Testing**: Claude Opus (thorough test coverage, edge case identification)
- **Security Review**: GPT-4 + Specialized Security Models (vulnerability detection)
- **Performance/Load**: Claude Sonnet 4 (pattern recognition, bottleneck analysis)
---

## Global Technology Stack

This workspace contains:
- **Java Services**: Spring Boot 3.x, Maven/Gradle, JUnit 5, Mockito, MapStruct
- **Scala Services**: Play Framework / Akka HTTP, sbt, ScalaTest, Circe (JSON)
- **Standards**: Clean Architecture, Domain-Driven Design, Hexagonal Architecture

## Agent Ecosystem

This file defines the orchestration strategy for the following agent ecosystem:

### 1. **Orchestrator** (`java-service-orchestrator`)
   - **Model**: Claude Sonnet 4.5
   - **Role**: Coordinates end-to-end service development and review workflows
   - **Tools**: `[read, search, agent, todo]`
   - **Triggers**: Building new services, feature implementation with full SDLC

### 2. **Planner** (`architecture-planner`)
   - **Model**: Claude Sonnet 4 (best for strategic/architectural thinking)
   - **Role**: System design, API contracts, data modeling, dependency analysis
   - **Tools**: `[read, search, edit]`
   - **Triggers**: "design a service", "plan architecture", "create API contract"

### 3. **Coder** (`java-scala-coder`)
   - **Model**: GPT-4o (superior Java/Scala code generation)
   - **Role**: Implements services, controllers, repositories, DTOs, mappers
   - **Tools**: `[read, search, edit, execute]`
   - **Triggers**: "implement", "create controller", "write service layer"

### 4. **Unit Tester** (`unit-test-generator`)
   - **Model**: Claude Opus (best for comprehensive test coverage)
   - **Role**: Generates JUnit 5/ScalaTest tests with edge cases
   - **Tools**: `[read, search, edit, execute]`
   - **Triggers**: "write tests", "test coverage", "unit test"

### 5. **Validator** (`code-validator`)
   - **Model**: GPT-4 Turbo (fast, accurate static analysis)
   - **Role**: Compilation checks, dependency validation, configuration validation
   - **Tools**: `[read, search, execute]`
   - **Triggers**: "validate", "check compilation", "verify build"

### 6. **Functional Reviewer** (`functional-reviewer`)
   - **Model**: GPT-4o (business logic understanding)
   - **Role**: Reviews business logic correctness, error handling, API contracts
   - **Tools**: `[read, search]`
   - **Triggers**: "review functionality", "check business logic"

### 7. **Non-Functional Reviewer** (`nfr-reviewer`)
   - **Model**: Claude Sonnet 4 (pattern recognition for quality attributes)
   - **Role**: Reviews scalability, maintainability, observability, resilience
   - **Tools**: `[read, search]`
   - **Triggers**: "check scalability", "review architecture quality"

### 8. **Security Reviewer** (`security-auditor`)
   - **Model**: GPT-4 + Security-focused fine-tuning
   - **Role**: OWASP Top 10, Spring Security misconfigurations, secrets scanning
   - **Tools**: `[read, search]`
   - **Triggers**: "security review", "audit vulnerabilities", "check auth"

### 9. **Load Reviewer** (`load-analyzer`)
   - **Model**: Claude Sonnet 4 (analytical bottleneck detection)
   - **Role**: Identifies N+1 queries, connection pool issues, blocking operations
   - **Tools**: `[read, search]`
   - **Triggers**: "review load handling", "check database queries"

### 10. **Performance Reviewer** (`performance-optimizer`)
   - **Model**: Claude Sonnet 4 (optimization pattern expertise)
   - **Role**: Algorithm complexity, caching opportunities, resource utilization
   - **Tools**: `[read, search]`
   - **Triggers**: "optimize performance", "review algorithm efficiency"

---

## Orchestration Workflows

### Workflow 1: New Service Creation

**Trigger**: User requests "create a new Spring Boot service for {domain}"

**Orchestrator Actions**:

1. **GATHER REQUIREMENTS**
   ```
   Ask user:
   - Service name
   - Tech stack (Java Spring Boot / Scala Play / Scala Akka HTTP)
   - Domain entities (name + fields + relationships)
   - API operations (CRUD, custom endpoints)
   - Special requirements (auth, async, event-driven, caching)
   - Database (PostgreSQL, MySQL, MongoDB)
   ```

2. **PLAN** (Delegate to `@architecture-planner`)
   ```
   Task: Design service architecture for {service-name}
   Tech: {stack}
   Entities: {entities with fields}
   Operations: {operations list}
   Requirements: {special requirements}
   
   Expected Output:
   - Package structure
   - Layer design (controller → service → repository)
   - API contract (OpenAPI spec or endpoint list)
   - Data model (JPA entities or Scala case classes)
   - Dependency list (Spring starters or sbt libraries)
   - Configuration requirements
   ```
   **Model Used**: Claude Sonnet 4 (best for architecture)

3. **IMPLEMENT** (Delegate to `@java-scala-coder`)
   ```
   Task: Implement {service-name} based on design
   Design document: {output from step 2}
   Tech: {stack}
   
   Implement in order:
   1. Project scaffolding (pom.xml/build.gradle or build.sbt)
   2. Configuration files (application.yml or application.conf)
   3. Domain models (entities/case classes)
   4. Repository layer (JpaRepository or Slick/Doobie)
   5. Service layer (business logic)
   6. Controller/Route layer (REST API)
   7. DTOs and Mappers (MapStruct or Circe codecs)
   8. Exception handling
   
   Expected Output:
   - All source files with full implementation
   - Build file with dependencies
   - Configuration with sensible defaults
   - README with build/run instructions
   ```
   **Model Used**: GPT-4o (best Java/Scala code generation)

4. **VALIDATE** (Delegate to `@code-validator`)
   ```
   Task: Validate {service-name} implementation
   Service path: {path}
   Tech: {stack}
   
   Checks:
   - Compilation: mvn clean compile / sbt compile
   - Dependency resolution
   - Configuration parsing (no YAML/Conf errors)
   - Missing files check (controller without service, etc.)
   
   Expected Output:
   - Build status: PASS/FAIL
   - Error details if FAIL
   - Missing component warnings
   ```
   **Model Used**: GPT-4 Turbo (fast validation)

5. **GENERATE TESTS** (Delegate to `@unit-test-generator`)
   ```
   Task: Write comprehensive unit tests for {service-name}
   Service path: {path}
   Tech: {stack}
   Files to test: {implementation files from step 3}
   
   Test Coverage Requirements:
   - All public methods in service layer
   - All controller endpoints (happy path + error cases)
   - All repository queries
   - Edge cases: null inputs, empty collections, boundary values
   - Negative tests: invalid inputs, constraint violations
   
   Test Framework:
   - Java: JUnit 5 + Mockito + MockMvc
   - Scala: ScalaTest + ScalaMock
   
   Expected Output:
   - Test classes mirroring implementation structure
   - Coverage report (aim for 80%+ line coverage)
   - Test execution summary
   ```
   **Model Used**: Claude Opus (thorough test coverage)

6. **MULTI-DIMENSIONAL REVIEW** (Parallel Delegation)

   **6a. Functional Review** → `@functional-reviewer`
   ```
   Task: Review business logic correctness
   Files: {service + controller files}
   
   Check:
   - Business rules correctly implemented
   - Error handling appropriate for failures
   - API contracts match requirements
   - Transactional boundaries correct (@Transactional placement)
   - Null safety (Java: Optional, Scala: Option)
   
   Severity: CRITICAL | HIGH | MEDIUM | LOW
   Expected Output: List of findings with file:line references
   ```
   **Model Used**: GPT-4o (business logic understanding)

   **6b. Non-Functional Review** → `@nfr-reviewer`
   ```
   Task: Review architecture quality attributes
   Files: {all implementation files}
   
   Check:
   - Scalability: stateless services, horizontal scaling readiness
   - Maintainability: package organization, naming conventions
   - Observability: logging (SLF4J), metrics (@Timed), health checks
   - Resilience: circuit breakers, retry logic, timeouts
   - Testability: dependency injection, mockable components
   
   Severity: HIGH | MEDIUM | LOW
   Expected Output: Quality attribute scorecard + recommendations
   ```
   **Model Used**: Claude Sonnet 4 (pattern recognition)

   **6c. Security Review** → `@security-auditor`
   ```
   Task: Security audit for {service-name}
   Files: {all implementation + config files}
   
   OWASP Checklist:
   - SQL Injection: parameterized queries only
   - Auth/Authz: Spring Security config, @PreAuthorize present
   - Secrets: no hardcoded credentials (check application.yml)
   - Input validation: @Valid on DTOs, constraint annotations
   - CSRF: enabled for web endpoints
   - Dependencies: no known CVEs (check OWASP Dependency Check)
   - Logging: no sensitive data in logs
   
   Severity: CRITICAL | HIGH | MEDIUM | LOW
   Expected Output: Security findings with remediation steps
   ```
   **Model Used**: GPT-4 + Security models

   **6d. Load Review** → `@load-analyzer`
   ```
   Task: Analyze load handling capability
   Files: {repository + service files}
   
   Check:
   - N+1 query problems (missing @EntityGraph, JPQL joins)
   - Connection pool configuration
   - Pagination on list endpoints
   - Blocking I/O in async contexts
   - Database index opportunities
   
   Severity: HIGH | MEDIUM | LOW
   Expected Output: Load-related risks with optimization suggestions
   ```
   **Model Used**: Claude Sonnet 4 (bottleneck analysis)

   **6e. Performance Review** → `@performance-optimizer`
   ```
   Task: Performance optimization review
   Files: {all implementation files}
   
   Check:
   - Algorithm complexity (O(n²) loops, nested iterations)
   - Unnecessary object creation in hot paths
   - Caching opportunities (Spring @Cacheable)
   - Lazy vs eager initialization
   - Stream API misuse (Java), collection operations (Scala)
   
   Severity: HIGH | MEDIUM | LOW
   Expected Output: Performance hotspots + optimization opportunities
   ```
   **Model Used**: Claude Sonnet 4 (optimization patterns)

7. **SYNTHESIZE AND REPORT**
   ```
   Aggregate all review results and present:
   
   ## Service: {service-name} - Development Complete
   
   ### Build Status
   ✅ Compilation: PASSED
   ✅ Tests: {X} tests, {Y}% coverage
   
   ### Implementation Summary
   | Layer | Files | Key Components |
   |-------|-------|----------------|
   | Controller | {count} | {endpoints list} |
   | Service | {count} | {main services} |
   | Repository | {count} | {entities} |
   | Tests | {count} | {test classes} |
   
   ### Review Results
   
   #### 🎯 Functional Review
   - ✅ {passed checks count}
   - ⚠️ HIGH: {count} | MEDIUM: {count} | LOW: {count}
   - Top Issues: {list top 3}
   
   #### 🏗️ Non-Functional Review (NFR)
   - Scalability: {rating}/5
   - Maintainability: {rating}/5
   - Observability: {rating}/5
   - Resilience: {rating}/5
   - Key Recommendations: {list}
   
   #### 🔒 Security Review
   - ❌ CRITICAL: {count} → MUST FIX BEFORE DEPLOYMENT
   - ⚠️ HIGH: {count} | MEDIUM: {count} | LOW: {count}
   - Top Vulnerabilities: {list}
   
   #### ⚡ Load Review
   - Database Query Efficiency: {rating}/5
   - Identified N+1 Queries: {count}
   - Connection Pool: {configured/not configured}
   - Recommendations: {list}
   
   #### 🚀 Performance Review
   - Algorithm Efficiency: {rating}/5
   - Caching Opportunities: {count}
   - Hot Path Issues: {count}
   - Optimization Potential: {list}
   
   ### Critical Actions Required
   {List any CRITICAL issues that must be resolved}
   
   ### Next Steps
   1. {immediate action}
   2. {follow-up action}
   3. {long-term improvement}
   
   ### Files Created
   {Tree structure of all files}
   ```

8. **DECISION LOGIC**
   ```
   IF any CRITICAL security issues:
       → Delegate back to @java-scala-coder to fix
       → Re-run @security-auditor
       → Max 2 fix cycles, then escalate to user
   
   IF validation fails:
       → Delegate back to @java-scala-coder with error details
       → Re-validate
   
   IF test coverage < 70%:
       → Delegate to @unit-test-generator for additional tests
   
   IF multiple HIGH issues across reviews:
       → Present consolidated fix plan to user
       → Ask: "Proceed with automated fixes or manual review?"
   
   ELSE:
       → Mark service as ready for integration testing
   ```

---

### Workflow 2: Feature Implementation with Quality Gates

**Trigger**: User requests "implement feature {X} in service {Y}"

**Orchestrator Actions**:

1. **UNDERSTAND CONTEXT** (Delegate to `@architecture-planner`)
   ```
   Task: Analyze impact of feature {X} on service {Y}
   Service path: {path}
   Feature: {description}
   
   Expected Output:
   - Affected components (controllers, services, repositories)
   - New dependencies required
   - Database schema changes needed
   - API contract changes
   - Impact scope: MINOR | MODERATE | MAJOR
   ```
   **Model Used**: Claude Sonnet 4

2. **IMPLEMENT** (Delegate to `@java-scala-coder`)
   ```
   Task: Implement feature {X}
   Impact analysis: {output from step 1}
   
   Expected Output:
   - Modified files list
   - New files created
   - Migration scripts (if DB changes)
   ```
   **Model Used**: GPT-4o

3. **TEST** (Delegate to `@unit-test-generator`)
   ```
   Task: Test feature {X}
   Changed files: {from step 2}
   
   Expected Output:
   - New tests for feature
   - Updated tests for modified code
   - Test execution results
   ```
   **Model Used**: Claude Opus

4. **QUALITY GATE REVIEWS** (Parallel, but only relevant ones)

   **Conditional Delegation Logic**:
   ```
   IF feature touches authentication/authorization:
       → @security-auditor (MANDATORY)
   
   IF feature adds new database queries:
       → @load-analyzer (MANDATORY)
   
   IF feature adds API endpoints:
       → @functional-reviewer (MANDATORY)
   
   IF feature impacts response time:
       → @performance-optimizer (MANDATORY)
   
   ALWAYS:
       → @code-validator (MANDATORY)
   
   IF impact scope = MAJOR:
       → @nfr-reviewer (MANDATORY)
   ```

5. **SYNTHESIS**
   ```
   Present feature implementation summary with pass/fail quality gates
   ```

---

## Stack-Specific Conventions

### Java (Spring Boot)

#### Package Structure
```
com.company.servicename/
├── controller/       # @RestController classes
├── service/          # @Service business logic
├── repository/       # JpaRepository interfaces
├── model/
│   ├── entity/      # @Entity JPA classes
│   └── dto/         # Data Transfer Objects
├── mapper/           # MapStruct interfaces
├── config/           # @Configuration classes
├── exception/        # Custom exceptions + @ControllerAdvice
└── security/         # SecurityConfig, JWT, etc.
```

#### Code Standards
- **Naming**: Classes are `PascalCase`, methods `camelCase`, constants `UPPER_SNAKE_CASE`
- **Annotations**: `@Slf4j` for logging, `@RequiredArgsConstructor` for DI
- **Validation**: `@Valid` on controller inputs, Bean Validation constraints on DTOs
- **Transactions**: `@Transactional` on service methods, not controllers
- **Error Handling**: Global `@RestControllerAdvice` with `@ExceptionHandler`
- **API Docs**: Springdoc OpenAPI with `@Operation`, `@ApiResponse`
- **Testing**: `@SpringBootTest` for integration, `@WebMvcTest` for controller tests

#### Build
- Maven: `./mvnw clean verify` (includes test + package)
- Gradle: `./gradlew build`

---

### Scala (Play Framework / Akka HTTP)

#### Package Structure
```
com.company.servicename/
├── controllers/      # Play controllers or Akka HTTP routes
├── services/         # Business logic services
├── repositories/     # Database access layer (Slick/Doobie)
├── models/
│   ├── domain/      # Case classes (domain models)
│   └── dto/         # API request/response models
├── codecs/           # Circe JSON encoders/decoders
├── config/           # Configuration loading
└── errors/           # Custom error types
```

#### Code Standards
- **Naming**: Types are `PascalCase`, values `camelCase`, type params single uppercase
- **Immutability**: Use `val`, immutable collections (`List`, `Vector`, `Map`)
- **Error Handling**: `Either`, `Try`, or cats `EitherT`/`OptionT`
- **JSON**: Circe with semi-auto derivation
- **Testing**: ScalaTest with `AsyncFlatSpec` or `WordSpec`, ScalaMock for mocking
- **Database**: Slick typed queries or Doobie for functional access

#### Build
- sbt: `sbt clean test` (run tests), `sbt compile` (compile only)

---

## Model Selection Rationale

| Task Type | Selected Model | Reasoning |
|-----------|---------------|-----------|
| **Architecture/Planning** | Claude Sonnet 4 | Superior at high-level design, tradeoff analysis, pattern recognition |
| **Code Generation** | GPT-4o | Best Java/Spring Boot code generation, canonical patterns, wide framework knowledge |
| **Testing** | Claude Opus | Most thorough at edge case identification, comprehensive test coverage |
| **Validation** | GPT-4 Turbo | Fast syntax/compilation checking, good static analysis |
| **Functional Review** | GPT-4o | Strong business logic understanding, API contract validation |
| **Non-Functional Review** | Claude Sonnet 4 | Excellent at identifying architectural patterns, quality attributes |
| **Security Review** | GPT-4 + Security Models | OWASP expertise, vulnerability pattern database |
| **Load Analysis** | Claude Sonnet 4 | Bottleneck detection, database query optimization |
| **Performance Review** | Claude Sonnet 4 | Algorithm analysis, optimization patterns |

---

## Communication Protocols

### Orchestrator → Sub-Agent Delegation Format

```markdown
## DELEGATION CONTEXT

### Task ID: {unique-id}
### Assigned Agent: @{agent-name}
### Priority: CRITICAL | HIGH | NORMAL | LOW

---

### Task Statement
{Clear, specific task description}

### Scope
**In Scope**:
- {what to work on}

**Out of Scope**:
- {what NOT to touch}

### Tech Context
- **Stack**: Java Spring Boot 3.1.5 / Scala 2.13 with Play 2.8
- **Build Tool**: Maven 3.9 / sbt 1.9
- **JDK**: OpenJDK 17
- **Database**: PostgreSQL 15

### Files/Directories
- **Work In**: {paths}
- **Read From**: {paths}
- **Do Not Modify**: {paths}

### Acceptance Criteria
✅ {criterion 1}
✅ {criterion 2}
✅ {criterion 3}

### Prior Work (Context from previous agents)
{Summary of what's been done so far}

### Constraints
❌ {constraint 1}
❌ {constraint 2}

### Expected Deliverables
1. {deliverable 1}
2. {deliverable 2}
```

---

### Sub-Agent → Orchestrator Report Format

```markdown
## TASK COMPLETION REPORT

### Task ID: {unique-id}
### Agent: {agent-name}
### Status: ✅ SUCCESS | ⚠️ PARTIAL | ❌ FAILED

---

### Summary
{One-paragraph summary of work done}

### Deliverables

#### Files Created
- `{path}` — {description}

#### Files Modified
- `{path}` — {what changed}

#### Commands Executed
- `{command}` — Exit code: {code} — {outcome}

### Acceptance Criteria Results
✅ {criterion 1}: PASSED — {evidence}
⚠️ {criterion 2}: PARTIAL — {reason}
❌ {criterion 3}: FAILED — {reason}

### Issues Found
| Severity | Issue | Location | Recommendation |
|----------|-------|----------|----------------|
| CRITICAL | {issue} | {file:line} | {fix suggestion} |
| HIGH | {issue} | {file:line} | {fix suggestion} |

### Metrics
- Lines of Code: {count}
- Test Coverage: {X}%
- Cyclomatic Complexity: {average}
- Build Time: {seconds}s

### Next Steps
{What should happen next, or what blocks progress}

### Handoff Context (if this task chains to another agent)
{Information the next agent needs}
```

---

## Failure Handling & Recovery

### Compilation Failures
```
IF @code-validator reports compilation errors:
    1. Extract error details (file, line, message)
    2. Delegate to @java-scala-coder:
       Task: Fix compilation errors
       Errors: {detailed error list}
    3. Re-validate with @code-validator
    4. Max retries: 2
    5. If still failing → report to user with diagnostics
```

### Test Failures
```
IF @unit-test-generator reports test failures:
    1. Analyze: Is it a test problem or implementation problem?
    2. IF implementation bug:
       → Delegate to @java-scala-coder to fix
    3. IF test is incorrect:
       → Delegate to @unit-test-generator to fix test
    4. Re-run tests
    5. Max retries: 2
```

### Critical Security Issues
```
IF @security-auditor reports CRITICAL findings:
    1. Block progression (do not deploy, do not merge)
    2. Delegate to @java-scala-coder with:
       Task: Fix security vulnerabilities
       Findings: {CRITICAL issues with remediation}
    3. Re-run @security-auditor on fixed code
    4. Require: 0 CRITICAL issues before continuing
```

### Review Conflicts (Multiple HIGH findings)
```
IF multiple reviewers report HIGH severity findings:
    1. Create consolidated fix plan:
       {File} → {Issue from Functional} + {Issue from Performance}
    2. Present to user: "Auto-fix or manual?"
    3. IF auto-fix approved:
       → Delegate to @java-scala-coder with consolidated plan
       → Re-run affected reviewers
```

---

## Decision Trees

### When to Use Which Agent

```
User Request → Categorize:

├─ "Create new service" / "Scaffold"
│   └─ Full Workflow 1 (Planner → Coder → Tester → All Reviewers)
│
├─ "Implement feature {X}"
│   └─ Workflow 2 (Planner → Coder → Tester → Conditional Reviewers)
│
├─ "Add tests for {component}"
│   └─ Direct: @unit-test-generator
│
├─ "Review security" / "Audit code"
│   └─ Direct: @security-auditor
│
├─ "Optimize performance" / "Why is it slow?"
│   └─ Direct: @performance-optimizer
│
├─ "Check for N+1 queries" / "Database performance"
│   └─ Direct: @load-analyzer
│
├─ "Is this scalable?" / "Architecture review"
│   └─ Direct: @nfr-reviewer
│
├─ "Does this compile?" / "Validate build"
│   └─ Direct: @code-validator
│
└─ "Design {system}" / "Plan approach"
    └─ Direct: @architecture-planner
```

---

## Quality Gates Enforcement

All code must pass these gates before deployment:

### Gate 1: Compilation ✅
- **Owner**: `@code-validator`
- **Criteria**: `mvn clean compile` or `sbt compile` exits with code 0
- **Blocking**: YES

### Gate 2: Unit Tests ✅
- **Owner**: `@unit-test-generator`
- **Criteria**: Coverage ≥ 70%, all tests pass
- **Blocking**: YES

### Gate 3: Security 🔒
- **Owner**: `@security-auditor`
- **Criteria**: 0 CRITICAL vulnerabilities
- **Blocking**: YES for CRITICAL, NO for HIGH/MEDIUM/LOW

### Gate 4: Functional Correctness ✅
- **Owner**: `@functional-reviewer`
- **Criteria**: 0 CRITICAL business logic errors
- **Blocking**: YES for CRITICAL

### Gate 5: Load Handling ⚡
- **Owner**: `@load-analyzer`
- **Criteria**: No N+1 queries, pagination on lists
- **Blocking**: NO (recommendations only)

### Gate 6: Performance 🚀
- **Owner**: `@performance-optimizer`
- **Criteria**: No O(n²)+ algorithms in hot paths
- **Blocking**: NO (recommendations only)

### Gate 7: Non-Functional Attributes 🏗️
- **Owner**: `@nfr-reviewer`
- **Criteria**: Logging present, health checks configured
- **Blocking**: NO (recommendations only)

---

## Observability & Metrics

All agents must report execution metrics:

```yaml
Agent Metrics:
  - execution_time_ms: {milliseconds}
  - files_read: {count}
  - files_modified: {count}
  - issues_found: {count}
  - severity_breakdown:
      critical: {count}
      high: {count}
      medium: {count}
      low: {count}
  - model_used: {model-name}
  - tokens_consumed: {approximate}
```

Orchestrator tracks:
- Total workflow duration
- Sub-agent delegation count
- Fix/retry cycles
- Quality gate pass/fail rates

---

## Guardrails & Safety

### 1. Prevent Infinite Loops
- Max delegation depth: 3 levels (orchestrator → agent → sub-agent)
- Max retry cycles per agent: 2
- Timeout per agent: 5 minutes

### 2. Prevent Destructive Actions
- No `rm -rf` or destructive file operations
- No database DROP/DELETE commands in migrations
- No network calls to external APIs without approval

### 3. Scope Boundaries
- Agents cannot modify files outside their designated scope
- Read-only agents (`@*-reviewer`) cannot have `edit` or `execute` tools
- Specific agents work in specific directories only

### 4. User Escalation
- Any CRITICAL issue must be reported to user
- After 2 failed fix attempts, escalate don't retry
- User approval required for major architectural changes

---

## Example Invocation

**User**: "Create a Spring Boot service for customer management with CRUD operations, JWT authentication, and PostgreSQL"

**Orchestrator Response**:
```
I'll coordinate the creation of a complete customer management service with the following plan:

📋 Creating Task List:
1. ⏳ Architecture planning (system design, API contracts)
2. ⏳ Implementation (controllers, services, repositories, entities)
3. ⏳ Validation (compilation, dependency check)
4. ⏳ Test generation (unit tests, integration tests)
5. ⏳ Security review (JWT config, OWASP compliance)
6. ⏳ Functional review (business logic validation)
7. ⏳ Load review (database query optimization)
8. ⏳ Performance review (algorithm efficiency)
9. ⏳ Non-functional review (observability, scalability)

Delegating to @architecture-planner (using Claude Sonnet 4)...
```

[Orchestrator then executes Workflow 1, delegating to each specialist, and presents final consolidated report]

---

## Summary

This orchestrator provides:
- ✅ **Multi-model optimization**: Best AI model for each task type
- ✅ **Comprehensive reviews**: Security, performance, load, functional, non-functional
- ✅ **Quality gates**: Mandatory checks before deployment
- ✅ **Failure recovery**: Automatic retry with escalation
- ✅ **Stack support**: Java Spring Boot and Scala
- ✅ **Clear delegation**: Structured communication between agents
- ✅ **Observability**: Metrics and progress tracking

**Invoke this orchestrator when**: Building new services, implementing features with full SDLC validation, or running comprehensive code reviews across multiple dimensions.
