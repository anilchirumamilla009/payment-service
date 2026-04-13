# Security Audit Report Template

Use this format when reporting security audit findings.

```
## Security Audit Summary
- **Scope**: [full / controllers / dependencies / specific files]
- **Files audited**: N
- **Critical**: N | **High**: N | **Medium**: N | **Low**: N | **Info**: N

---

## CRITICAL
- **[OWASP Category]** [file.java:L##]
  - **Finding**: Description of the vulnerability
  - **Risk**: Impact if exploited
  - **Remediation**: How to fix it

## HIGH
- **[OWASP Category]** [file.java:L##]
  - **Finding**: ...
  - **Risk**: ...
  - **Remediation**: ...

## MEDIUM
...

## LOW
...

## INFO
...

---

## Dependency CVE Check
| Dependency | Version | Known CVE | Severity | Fixed In |
|------------|---------|-----------|----------|----------|
| example    | 1.0.0   | CVE-XXXX  | HIGH     | 1.0.1    |

---

## Recommendations (Priority Order)
1. Immediate fixes (Critical/High)
2. Configuration hardening
3. Dependencies to add (e.g., `spring-boot-starter-security`)
4. Backlog items (Low/Info)
```

## Severity Definitions

| Level | Meaning | Action |
|-------|---------|--------|
| **CRITICAL** | Exploitable vulnerability, data breach risk | Immediate fix required |
| **HIGH** | Significant security weakness | Fix before next release |
| **MEDIUM** | Defense-in-depth gap | Fix in current sprint |
| **LOW** | Minor hardening opportunity | Add to backlog |
| **INFO** | Best practice suggestion | Consider adopting |
