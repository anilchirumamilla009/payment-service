# OWASP Top 10 (2021) — Checklist for payment-service

## A01: Broken Access Control
- [ ] All endpoints behind authentication (or explicitly public)
- [ ] No IDOR — cannot access other users' resources by changing UUID
- [ ] UUID path parameters validated as proper UUID format
- [ ] `@PreAuthorize` or equivalent on sensitive endpoints
- [ ] Beneficial owner endpoints restrict access to authorized callers

## A02: Cryptographic Failures
- [ ] No sensitive data (passwords, tokens) in plain text config
- [ ] No PII in log output
- [ ] No financial data (IBANs, account numbers) in error responses
- [ ] Secrets injected via environment variables, not hardcoded

## A03: Injection
- [ ] No string concatenation in JPQL/HQL/native queries
- [ ] All queries use parameterized binding (`@Param`, `?1`)
- [ ] No `@Query` with user-controlled strings
- [ ] Input length limits on all string DTO fields

## A04: Insecure Design
- [ ] Rate limiting on write endpoints (POST, PUT, PATCH)
- [ ] Input length/size limits on all DTOs
- [ ] Audit trail endpoints exist for mutable entities
- [ ] Business logic validates state transitions

## A05: Security Misconfiguration
- [ ] H2 console disabled in production (`spring.h2.console.enabled=false`)
- [ ] Actuator endpoints restricted (only `/health` public)
- [ ] Debug mode disabled in production
- [ ] CORS not using wildcard `*` origin
- [ ] Error responses don't leak stack traces or internal paths
- [ ] Default Spring Boot error page customized

## A06: Vulnerable and Outdated Components
- [ ] Spring Boot version has no known CVEs
- [ ] Hibernate version has no known CVEs
- [ ] Flyway version has no known CVEs
- [ ] MapStruct version has no known CVEs
- [ ] No unnecessary dependencies in `pom.xml`

## A07: Identification and Authentication Failures
- [ ] Authentication mechanism in place (Spring Security)
- [ ] No hardcoded credentials
- [ ] Session management configured properly
- [ ] Failed auth attempts logged

## A08: Software and Data Integrity Failures
- [ ] Flyway migrations checksummed (default behavior)
- [ ] No untrusted deserialization of user input
- [ ] Migration scripts are append-only (no edits to applied scripts)

## A09: Security Logging and Monitoring Failures
- [ ] Security events logged (auth failures, access denials)
- [ ] No sensitive data in log statements
- [ ] Audit trail endpoints functional for all mutable entities
- [ ] Log output does not contain stack traces for client errors

## A10: Server-Side Request Forgery (SSRF)
- [ ] No user-controlled URLs used in server-side requests
- [ ] External service calls use allowlists
- [ ] No open redirects
