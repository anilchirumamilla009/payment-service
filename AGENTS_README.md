# Java/Scala Multi-Model Agent Orchestrator

A comprehensive AI agent ecosystem for Java (Spring Boot) and Scala (Play/Akka HTTP) development with multi-model optimization and specialized sub-agents.

## 📋 Overview

This agent system provides **end-to-end software development lifecycle automation** for JVM-based microservices, using best-fit AI models for each specialized task.

### Key Features

- ✅ **Multi-Model Architecture**: Different AI models optimized for specific tasks (planning, coding, testing, reviews)
- ✅ **Complete SDLC Coverage**: From architecture design to deployment-ready code
- ✅ **Multi-Dimensional Reviews**: Security, performance, load, functional, and non-functional reviews
- ✅ **Quality Gates**: Automatic validation with mandatory and advisory gates
- ✅ **Failure Recovery**: Intelligent retry logic with escalation
- ✅ **Stack Support**: Java Spring Boot 3.x and Scala Play/Akka HTTP

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                   ORCHESTRATOR                              │
│            (Claude Sonnet 4.5)                              │
│   • Workflow coordination                                   │
│   • Quality gate enforcement                                │
│   • Multi-dimensional review synthesis                      │
└───────────────┬─────────────────────────────────────────────┘
                │
    ┌───────────┴───────────┐
    │                       │
    ▼                       ▼
┌─────────────────┐   ┌─────────────────┐
│    PLANNER      │   │     CODER       │
│ Claude Sonnet 4 │   │    GPT-4o       │
│ Architecture    │   │ Implementation  │
└─────────────────┘   └─────────────────┘
    │                       │
    ▼                       ▼
┌─────────────────┐   ┌─────────────────┐
│     TESTER      │   │   VALIDATOR     │
│  Claude Opus    │   │  GPT-4 Turbo    │
│ Comprehensive   │   │ Compilation     │
└─────────────────┘   └─────────────────┘
              │
    ┌─────────┴─────────┐
    │    REVIEWERS      │
    ├───────────────────┤
    │ • Functional      │
    │ • Security        │
    │ • Load            │
    │ • Performance     │
    │ • NFR             │
    └───────────────────┘
```

## 🤖 Agent Ecosystem

### 1. Orchestrator: `@java-service-orchestrator`

**Model**: Claude Sonnet 4.5  
**Role**: Coordinates complete SDLC workflows

**Triggers**: 
- "Create a Spring Boot service for ..."
- "Build a new microservice ..."
- "Implement feature with full quality validation ..."

**Capabilities**:
- End-to-end service creation
- Feature implementation with quality gates
- Multi-agent coordination
- Failure recovery and retry logic

---

### 2. Planner: `@architecture-planner`

**Model**: Claude Sonnet 4 (best for strategic thinking)  
**Role**: System architecture and design

**Triggers**:
- "Design a service ..."
- "Plan architecture ..."
- "Create API contract ..."

**Deliverables**:
- Package structure
- API contracts (OpenAPI)
- Data models
- Dependency specifications
- Configuration requirements

---

### 3. Coder: `@java-scala-coder`

**Model**: GPT-4o (superior Java/Scala generation)  
**Role**: Full-stack implementation

**Triggers**:
- "Implement ..."
- "Create controller/service/repository ..."
- "Write business logic ..."

**Capabilities**:
- Spring Boot services (controllers, services, repositories)
- Scala Play/Akka HTTP services
- JPA entities and Slick repositories
- DTOs, mappers, exception handling
- Build files (Maven, Gradle, sbt)

---

### 4. Tester: `@unit-test-generator`

**Model**: Claude Opus (most thorough coverage)  
**Role**: Comprehensive test generation

**Triggers**:
- "Write tests ..."
- "Test coverage ..."
- "Generate unit tests ..."

**Coverage**:
- JUnit 5 + Mockito (`@WebMvcTest`, `@SpringBootTest`)
- ScalaTest + ScalaMock
- Edge cases and negative tests
- Target: ≥70% code coverage

---

### 5. Validator: `@code-validator`

**Model**: GPT-4 Turbo (fast, accurate)  
**Role**: Build and configuration validation

**Triggers**:
- "Validate build ..."
- "Check compilation ..."
- "Verify configuration ..."

**Checks**:
- Compilation (`mvn compile`, `sbt compile`)
- Dependency resolution
- Configuration file validation (YAML, HOCON)
- Missing component detection

---

### 6. Functional Reviewer: `@functional-reviewer`

**Model**: GPT-4o (business logic understanding)  
**Role**: Business logic correctness

**Triggers**:
- "Review business logic ..."
- "Check functional correctness ..."

**Checks**:
- Business rule implementation
- API contract compliance
- Error handling completeness
- Transaction boundaries
- Null safety

---

### 7. Security Auditor: `@security-auditor`

**Model**: GPT-4 + Security fine-tuning  
**Role**: OWASP compliance and vulnerability detection

**Triggers**:
- "Security review ..."
- "Audit vulnerabilities ..."
- "Check authentication ..."

**OWASP Top 10 Checks**:
- SQL injection prevention
- Authentication/authorization
- Sensitive data exposure
- CSRF, XSS, XXE
- Insecure deserialization
- Dependency vulnerabilities

---

### 8. Load Analyzer: `@load-analyzer`

**Model**: Claude Sonnet 4 (bottleneck detection)  
**Role**: Database performance and load handling

**Triggers**:
- "Review load handling ..."
- "Check database queries ..."
- "Detect N+1 queries ..."

**Checks**:
- N+1 query detection
- Pagination implementation
- Connection pool configuration
- Blocking I/O detection
- Database index recommendations

---

### 9. Performance Optimizer: `@performance-optimizer`

**Model**: Claude Sonnet 4 (optimization patterns)  
**Role**: Algorithm efficiency and caching

**Triggers**:
- "Optimize performance ..."
- "Review algorithm efficiency ..."

**Checks**:
- Algorithm complexity (Big-O)
- Caching opportunities
- Memory allocation reduction
- Collection usage optimization
- Stream API efficiency

---

### 10. NFR Reviewer: `@nfr-reviewer`

**Model**: Claude Sonnet 4 (pattern recognition)  
**Role**: Non-functional quality attributes

**Triggers**:
- "Check scalability ..."
- "Review architecture quality ..."

**Checks**:
- Scalability (horizontal/vertical)
- Maintainability (SRP, coupling)
- Observability (logging, metrics, health)
- Resilience (circuit breakers, retries)
- Testability (DI, mockability)

---

## 🚀 Usage Examples

### Example 1: Create New Service

```
User: Create a Spring Boot service for customer management with CRUD operations, 
      JWT authentication, and PostgreSQL database.

@java-service-orchestrator will:
1. Gather requirements (if needed)
2. Delegate to @architecture-planner → System design
3. Delegate to @java-scala-coder → Implementation
4. Delegate to @code-validator → Build verification
5. Delegate to @unit-test-generator → Test generation
6. Parallel reviews:
   - @security-auditor → OWASP compliance
   - @functional-reviewer → Business logic
   - @load-analyzer → Query efficiency
   - @performance-optimizer → Caching opportunities
   - @nfr-reviewer → Architecture quality
7. Synthesize → Comprehensive report with quality scores
```

### Example 2: Implement Feature with Quality Gates

```
User: Implement a feature to add discount codes to the order-service

@java-service-orchestrator will:
1. Delegate to @architecture-planner → Impact analysis
2. Delegate to @java-scala-coder → Implementation
3. Delegate to @unit-test-generator → Tests
4. Conditional reviews (based on feature):
   - @functional-reviewer (mandatory for API changes)
   - @security-auditor (if discount logic has security implications)
   - @load-analyzer (if new DB queries)
5. Report with pass/fail quality gates
```

### Example 3: Direct Agent Invocation

```
User: Review this code for security vulnerabilities

@security-auditor will:
- Scan for OWASP Top 10 issues
- Check authentication/authorization
- Detect hardcoded secrets
- Report severity-classified findings
```

---

## 📊 Quality Gates

### Mandatory (BLOCKING)

| Gate | Owner | Criteria | Action if Failed |
|------|-------|----------|------------------|
| **Compilation** | `@code-validator` | Exit code 0 | Fix + re-validate |
| **Tests** | `@unit-test-generator` | Coverage ≥ 70% | Add tests |
| **Security** | `@security-auditor` | 0 CRITICAL issues | Fix vulnerabilities |

### Advisory (NON-BLOCKING)

| Gate | Owner | Criteria | Action |
|------|-------|----------|--------|
| **Load** | `@load-analyzer` | No N+1 queries | Recommendations |
| **Performance** | `@performance-optimizer` | No O(n²) in hot paths | Optimization suggestions |
| **NFR** | `@nfr-reviewer` | Observability configured | Best practices |

---

## 🔄 Workflow Diagrams

### New Service Creation Flow

```
Requirements → Plan → Implement → Validate → Test → Multi-Review → Report
     │          │         │          │         │           │
     │          │         │          │         │           │
   User    @planner  @coder   @validator  @tester    All Reviewers
              ↓
        Architecture
         Document
```

### Quality Gate Flow

```
Code → Compile ✅ → Tests ✅ → Security ✅ → Deploy
         │            │          │
       FAIL         FAIL       FAIL
         ↓            ↓          ↓
      @coder       @tester    @coder
      (fix)        (add)      (fix)
```

---

## ⚙️ Configuration

### File Structure

```
/home/chandan/projects/AIOPs/
├── agents.md                           # Main orchestrator documentation
└── .github/
    └── agents/
        ├── java-service-orchestrator.agent.md
        ├── architecture-planner.agent.md
        ├── java-scala-coder.agent.md
        ├── unit-test-generator.agent.md
        ├── code-validator.agent.md
        ├── functional-reviewer.agent.md
        ├── security-auditor.agent.md
        ├── load-analyzer.agent.md
        ├── performance-optimizer.agent.md
        └── nfr-reviewer.agent.md
```

### Model Assignments

Edit individual agent files to change model assignments:

```yaml
---
name: "java-scala-coder"
model: gpt-4o  # Change to different model if needed
---
```

---

## 🎯 Best Practices

### When to Use the Orchestrator vs. Direct Agents

**Use `@java-service-orchestrator` when**:
- Creating new services from scratch
- Implementing features requiring full SDLC validation
- You need comprehensive multi-dimensional reviews

**Use direct agents when**:
- Single focused task (e.g., "add unit tests")
- Quick review (e.g., "security audit this file")
- Targeted optimization (e.g., "optimize this query")

### Customizing Workflows

The orchestrator follows these workflows (defined in [agents.md](agents.md)):

1. **Full Service Creation**: Planner → Coder → Validator → Tester → All Reviewers
2. **Feature Addition**: Impact Analysis → Implementation → Testing → Conditional Reviews

To modify workflows, edit [.github/agents/java-service-orchestrator.agent.md](.github/agents/java-service-orchestrator.agent.md).

---

## 🐛 Troubleshooting

### Issue: Compilation Failures

**Orchestrator Action**:
1. Extract error details
2. Delegate to `@java-scala-coder` with error context
3. Re-validate with `@code-validator`
4. Max 2 retries, then escalate to user

### Issue: Security CRITICAL Findings

**Orchestrator Action**:
1. **BLOCK** all other workflows
2. Delegate to `@java-scala-coder` with remediation steps
3. Re-run `@security-auditor`
4. Require 0 CRITICAL before continuing

### Issue: Test Coverage < 70%

**Orchestrator Action**:
1. Request additional tests from `@unit-test-generator`
2. Specify uncovered classes/methods
3. Re-run coverage report

---

## 📈 Performance Metrics

Agents report execution metrics:

```yaml
Agent Metrics:
  - execution_time_ms
  - files_read
  - files_modified
  - issues_found (by severity)
  - model_used
  - tokens_consumed (approximate)
```

Orchestrator tracks:
- Total workflow duration
- Delegation count
- Fix/retry cycles
- Quality gate pass/fail rates

---

## 🔒 Guardrails

### Safety Boundaries

1. **Max delegation depth**: 3 levels
2. **Max retry cycles**: 2 per agent
3. **Timeout per agent**: 5 minutes
4. **Read-only reviewers**: Cannot modify code

### Scope Restrictions

- Agents work only in designated directories
- No destructive file operations (`rm -rf`)
- No database DROP/DELETE in migrations
- No external API calls without approval

---

## 🤝 Contributing

To add new agents or modify workflows:

1. Create agent file in `.github/agents/`
2. Follow the agent template structure
3. Add to orchestrator's `agents:` list
4. Update this README

---

## 📚 References

- [AGENTS.md Training Guide](docs/AGENTS_MD_TRAINING_GUIDE.md)
- [Agent Testing Guide](docs/AGENT_TESTING_VALIDATION_GUIDE.md)
- [Spring Boot Best Practices](https://spring.io/projects/spring-boot)
- [Scala Best Practices](https://docs.scala-lang.org/style/)
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)

---

## 📝 License

Internal use - Company Proprietary

---

## 🙋 Support

For issues or questions:
- Check [Troubleshooting](#-troubleshooting) section
- Review agent-specific documentation in `.github/agents/`
- Escalate to team lead if unresolved after 2 retry cycles

---

**Last Updated**: 2026-04-09  
**Version**: 1.0.0  
**Maintained By**: AIOps Team
