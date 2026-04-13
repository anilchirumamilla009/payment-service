---
name: java-standards
description: "Use when: checking Java coding standards, enforcing naming conventions, reviewing code style, validating Spring Boot patterns, checking JPA best practices, reviewing MapStruct usage, enforcing DTO conventions, checking exception handling patterns, validating Jakarta validation usage, reviewing Java 17 features."
argument-hint: "Describe what to check (e.g., 'review service layer', 'check all DTOs', 'full standards audit')"
---

# Java Coding Standards Skill

Enforce Java 17, Spring Boot 3.x, and project-specific coding standards for payment-service.

## When to Use

- Before committing code — verify standards compliance
- When reviewing scaffolded or implemented code
- When onboarding to understand project conventions
- When unsure about the correct pattern for a class

## Procedure

### 1. Gather Context
- Read `AGENTS.md` for project-specific conventions
- Read the files under review
- Check existing code for established patterns

### 2. Apply Standards
Check the code against the standards in the references below:
- [Java 17 standards](./references/java17-standards.md)
- [Spring Boot standards](./references/spring-boot-standards.md)
- [Project conventions](./references/project-conventions.md)

### 3. Report Findings
For each violation, report:
- **File and line number**
- **Rule violated** (with rule ID)
- **What's wrong**
- **How to fix it** (with code example)

## Constraints

- DO NOT auto-fix code unless explicitly asked — report findings only by default
- ALWAYS reference the specific standard rule being violated
- ALWAYS provide a corrected code example for each finding
- Categorize findings: **ERROR** (must fix) / **WARNING** (should fix) / **SUGGESTION** (nice to have)
