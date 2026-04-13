# Quick Agent Reference

## 🚀 Quick Start

```bash
# For complete new service
@java-service-orchestrator create a Spring Boot service for order management

# For specific tasks
@architecture-planner design API for user service
@java-scala-coder implement UserController with CRUD
@unit-test-generator write tests for OrderService
@security-auditor review for OWASP compliance
```

## 📋 All Agents At-a-Glance

| Agent | Trigger Keywords | Model | Purpose |
|-------|-----------------|-------|---------|
| **@java-service-orchestrator** | create service, build microservice, full SDLC | Claude Sonnet 4.5 | **Coordinates everything** |
| **@architecture-planner** | design, plan, API contract, architecture | Claude Sonnet 4 | **Strategic design** |
| **@java-scala-coder** | implement, create, write code | GPT-4o | **Write all code** |
| **@unit-test-generator** | test, coverage, unit test | Claude Opus | **Complete testing** |
| **@code-validator** | validate, compile, check build | GPT-4 Turbo | **Build verification** |
| **@functional-reviewer** | review logic, business rules | GPT-4o | **Functional correctness** |
| **@security-auditor** | security, OWASP, audit | GPT-4 Security | **Find vulnerabilities** |
| **@load-analyzer** | N+1 query, database load | Claude Sonnet 4 | **DB optimization** |
| **@performance-optimizer** | optimize, performance, cache | Claude Sonnet 4 | **Speed improvements** |
| **@nfr-reviewer** | scalability, observability | Claude Sonnet 4 | **Quality attributes** |

## 🎯 Use Cases

### I want to... → Use this agent

| Task | Agent |
|------|-------|
| Create a brand new service from scratch | `@java-service-orchestrator` |
| Add a feature with full quality checks | `@java-service-orchestrator` |
| Design system architecture | `@architecture-planner` |
| Write Spring Boot controllers | `@java-scala-coder` |
| Add unit tests | `@unit-test-generator` |
| Check if code compiles | `@code-validator` |
| Review business logic | `@functional-reviewer` |
| Find security vulnerabilities | `@security-auditor` |
| Fix N+1 queries | `@load-analyzer` |
| Speed up slow code | `@performance-optimizer` |
| Check if service can scale | `@nfr-reviewer` |

## ⚡ Common Commands

### New Service Creation

```
@java-service-orchestrator create a Spring Boot 3 service for customer management 
with PostgreSQL, JWT auth, and these entities:
- Customer (id, email, firstName, lastName, phone)
- Address (id, street, city, country, customerId)

Operations needed:
- CRUD for customers
- Add/update/delete addresses for a customer
- Search customers by email
- List customers with pagination
```

### Feature Implementation

```
@java-service-orchestrator implement a feature to apply discount codes 
to orders in the order-service. 

Requirements:
- DiscountCode entity (code, percentage, expiryDate)
- Validate code before applying
- Calculate discounted price
- Store applied discount with order
```

### Quick Reviews

```
# Security check
@security-auditor review this UserController for vulnerabilities

# Performance check
@performance-optimizer analyze OrderService.processOrders() for bottlenecks

# Load check
@load-analyzer check for N+1 queries in this repository layer

# Functional check
@functional-reviewer validate business logic in PricingService
```

## 📊 Quality Gates Cheatsheet

| Gate | Severity | Blocks Deployment? | Owner |
|------|----------|-------------------|-------|
| **Compilation** | ✅ Mandatory | YES | @code-validator |
| **Tests (≥70%)** | ✅ Mandatory | YES | @unit-test-generator |
| **Security (0 CRITICAL)** | ✅ Mandatory | YES | @security-auditor |
| **Functional (0 CRITICAL)** | ✅ Mandatory | YES | @functional-reviewer |
| **Load (no N+1)** | ⚠️ Advisory | NO | @load-analyzer |
| **Performance (no O(n²))** | ⚠️ Advisory | NO | @performance-optimizer |
| **NFR (observability)** | ⚠️ Advisory | NO | @nfr-reviewer |

## 🔧 Tech Stack Support

### Java Spring Boot
- ✅ Spring Boot 3.x
- ✅ JPA/Hibernate
- ✅ Maven/Gradle
- ✅ JUnit 5 + Mockito
- ✅ Spring Security
- ✅ MapStruct

### Scala
- ✅ Play Framework 2.8+
- ✅ Akka HTTP
- ✅ Slick/Doobie
- ✅ sbt
- ✅ ScalaTest + ScalaMock
- ✅ Circe (JSON)

## 🔍 Review Checklists

### Security (@security-auditor)
- SQL injection (parameterized queries)
- Authentication (Spring Security, JWT)
- Secrets (no hardcoded credentials)
- Input validation (@Valid annotations)
- OWASP Top 10 compliance

### Load (@load-analyzer)
- N+1 query detection
- Pagination on list endpoints
- Connection pool configuration
- Blocking I/O in async contexts

### Performance (@performance-optimizer)
- Algorithm complexity (O(n²) → O(n log n))
- Caching opportunities (@Cacheable)
- Object allocation in loops
- Collection efficiency

### NFR (@nfr-reviewer)
- Scalability (stateless, horizontal scaling)
- Observability (logging, metrics, health checks)
- Resilience (circuit breakers, retries)
- Maintainability (SRP, low coupling)

## 🚨 Severity Levels

| Level | Meaning | Action |
|-------|---------|--------|
| **CRITICAL** | 🔴 Data breach / corruption risk | **BLOCK** deployment, must fix |
| **HIGH** | 🟠 Exploitable / major bug | Fix before deployment |
| **MEDIUM** | 🟡 Suboptimal, best practice | Fix in next sprint |
| **LOW** | 🔵 Code quality improvement | Plan to fix |

## 📁 File Locations

```
/home/chandan/projects/AIOPs/
├── agents.md                    # Orchestrator documentation
├── AGENTS_README.md             # Full documentation
├── AGENTS_QUICK_REF.md          # This file
└── .github/agents/
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

## 💡 Tips

1. **Start with orchestrator** for complete workflows
2. **Use direct agents** for focused tasks
3. **Check quality gates** before deployment
4. **Review agent reports** for detailed findings
5. **Escalate after 2 retries** if stuck

## 🔗 Related Files

- [Full Documentation](AGENTS_README.md)
- [Orchestrator Guide](agents.md)
- [Training Guide](docs/AGENTS_MD_TRAINING_GUIDE.md)

---

**Quick help**: 
- "I need X" → Check "Use Cases" table
- "What does agent Y do?" → Check "All Agents" table
- "Can I deploy?" → Check "Quality Gates"
