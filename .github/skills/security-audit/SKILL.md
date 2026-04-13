---
name: security-audit
description: "Use when: auditing security vulnerabilities, checking OWASP Top 10 compliance, scanning for CVE risks, reviewing authentication/authorization, validating input sanitization, checking dependency vulnerabilities, reviewing SQL injection risks, auditing sensitive data exposure, checking CSRF/XSS protections."
argument-hint: "Describe what to audit (e.g., 'full audit', 'check controllers', 'scan dependencies')"
---

# Security Audit Skill

Perform security audits on the payment-service codebase following OWASP Top 10 (2021) and CVE standards.

## When to Use

- Before a release — full security sweep
- After adding new endpoints or entities
- When updating dependencies
- When handling PII or financial data
- Ad-hoc audit of specific files or layers

## Procedure

### 1. Gather Context
- Read `AGENTS.md` for project conventions
- Read `pom.xml` for dependency versions
- Read `application.yml` for configuration

### 2. Audit Dependencies
- Check `pom.xml` dependency versions against known CVEs
- Run `mvn dependency:tree` if transitive dependency inspection is needed
- Reference the [OWASP Top 10 checklist](./references/owasp-top10.md) for category details

### 3. Audit Configuration
- Check `application.yml` for security misconfigurations
- Verify actuator endpoint exposure
- Verify H2 console is disabled in non-dev profiles
- Check CORS configuration

### 4. Audit Code
- **Controllers**: Input validation (`@Valid`), auth annotations, error response leakage
- **Services**: Injection risks, sensitive data in logs
- **Entities**: Sensitive field exposure
- **DTOs**: Jakarta validation annotations present
- **OpenAPI spec**: Security scheme definitions

### 5. Audit Payment Domain
- PII not logged or exposed in error responses
- Financial data (IBANs, account numbers) properly protected
- UUIDs are v4 (random), not v1 (time-based)
- Beneficial owner access properly secured

### 6. Report Findings
Format the report using the [report template](./references/report-template.md).

## Constraints

- DO NOT modify production code — report findings only
- ALWAYS reference specific files and line numbers
- ALWAYS provide a remediation suggestion for each finding
- ALWAYS classify severity: CRITICAL / HIGH / MEDIUM / LOW / INFO
