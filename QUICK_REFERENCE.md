# Quick Reference Card - Subabul Trade Book Application

## 📋 Three User Roles

| Role | Scope | Key Responsibility | Creates | Cannot Do |
|------|-------|-------------------|---------|-----------|
| **Super Admin** | Application | Onboard clients, create client admins | Clients, Admin accounts | Manage client-specific data |
| **Client Admin** | Organization | Manage all operations for their client | Farmers, Teams, Operators, Invoices | Manage other clients, modify client profile |
| **Operator** | Staff | Record daily operations | Harvests, Weighing records | Create farmers/teams, manage rates |

---

## 🔐 Three Security Layers

1. **JWT Token** - User authentication with clientId embedded
2. **@PreAuthorize** - Role-based access control
3. **Query Filter** - WHERE clientId = currentUser.clientId (automatic)

---

## 📱 Key API Endpoints

### Super Admin
```
POST   /api/v1/super-admin/clients                - Create client (ONBOARDING)
POST   /api/v1/super-admin/clients/{id}/admin-user - Create admin account
```

### Client Admin
```
POST   /api/v1/clients/{clientId}/farmers        - Add farmer
POST   /api/v1/clients/{clientId}/operators       - Create operator
POST   /api/v1/rates                             - Set/update rates
POST   /api/v1/clients/{clientId}/invoices/generate - Generate invoice
POST   /api/v1/clients/{clientId}/payments       - Record payment
```

### Operator
```
POST   /api/v1/clients/{clientId}/harvests       - Record harvest
POST   /api/v1/clients/{clientId}/weighings      - Record weighing
GET    /api/v1/clients/{clientId}/transactions   - View transactions
GET    /api/v1/clients/{clientId}/invoices       - View invoices
```

---

## 📊 Data Entities (12 total)

1. **Client** - Business client (multi-tenant isolation point)
2. **Farmer** - Suppliers of Subabul crop
3. **Cutting Team** - Harvest team
4. **Transportation Team** - Transport service
5. **Rate History** - Historical prices (immutable audit trail)
6. **Harvest** - Daily inlet records
7. **Weighing** - Gross/Tare/Net weight records
8. **Transaction** - Purchase/payment records
9. **Invoice** - Generated invoices with line items
10. **Payment** - Payment records
11. **Ledger Book** - Monthly financial summary
12. **Audit Log** - Change history (immutable)

---

## 🎯 Business Processes (10 Total)

| # | Process | Actor | Output |
|---|---------|-------|--------|
| 1 | Client Onboarding | Super Admin | Client created + Admin account created |
| 2 | Farmer Onboarding | Client Admin | Farmer record + Initial rate set |
| 3 | Operator Creation | Client Admin | Operator credentials created |
| 4 | Add Cutting/Transport Team | Client Admin | Team record + Rate set |
| 5 | Update Rate | Client Admin | New rate active, old rate marked inactive |
| 6 | Record Daily Harvest | Operator | Harvest + Transaction created using current rate |
| 7 | Record Weighing | Operator | Weighing record with calculated net weight |
| 8 | Generate Invoice | Client Admin | Invoice with historical rates per transaction date |
| 9 | Send Invoice & Record Payment | Admin/Operator | Payment linked to invoice, status updated |
| 10 | Update Ledger Book | System (auto) | Monthly ledger created/updated |

---

## 🗄️ Database Schema

**18 Tables** (all with clientId except users/system):
- users, clients, farmers, cutting_teams, transportation_teams
- rate_history, harvests, weighings, transactions
- invoices, invoice_line_items, payments, ledger_books, audit_logs

**Multi-tenancy Rule**: Every data table has `client_id` column
**Query Rule**: Every SELECT filters `WHERE client_id = :currentClientId`

---

## 🔄 Transaction Flow

```
1. Farmer supplies crop
   ↓
2. Operator records harvest (current rate used)
   ↓
3. Operator records weighing (net weight calculated)
   ↓
4. Invoice generated (uses rates active on transaction dates)
   ↓
5. Invoice sent to farmer
   ↓
6. Farmer settles payment
   ↓
7. Operator records payment
   ↓
8. Invoice marked PAID
   ↓
9. Ledger book updated (monthly)
```

---

## 💰 Rate Management Key Concept

```
April 1-10:  Rate = 5000/ton
April 10:    Rate CHANGED to 5500/ton
April 11-30: Rate = 5500/ton

INVOICING FOR APRIL 1-30:
- Transactions Apr 1-10 use 5000/ton (historical)
- Transactions Apr 11-30 use 5500/ton (historical)

Each transaction billed with rate active on its date!
```

---

## 🛡️ Security Best Practices

✅ **DO**:
- Filter all queries by clientId
- Validate clientId in URL parameters
- Use parameterized queries
- Include @PreAuthorize on every endpoint
- Log all changes in audit table
- Validate all user inputs

❌ **DON'T**:
- Allow operators to see other clients' data
- Allow editing of immutable data (transactions, rates, audit logs)
- Hardcode role permissions
- Trust clientId from request body
- Create cross-tenant reports

---

## 📈 Scalability Notes

- Supports 100,000+ clients in single instance
- Supports 1,000,000+ transactions per client
- Page size: 50 records (configurable)
- Index on (clientId, entity_id) for performance
- Partition tables by clientId if needed for massive scale

---

## 🚀 Implementation Priority

**Phase 1 (Week 1)**
- [ ] Create users/authentication
- [ ] Setup Spring Security
- [ ] Create client management (Super Admin)

**Phase 2 (Week 2)**
- [ ] Create farmers, teams, rates
- [ ] Client admin operations
- [ ] Operator account creation

**Phase 3 (Week 3)**
- [ ] Harvest & weighing
- [ ] Transactions
- [ ] Basic invoicing

**Phase 4 (Week 4)**
- [ ] Historical rates in invoices
- [ ] Payments
- [ ] Ledger books
- [ ] Audit trails

**Phase 5 (Week 5)**
- [ ] Dashboard & reports
- [ ] Search & filters
- [ ] API documentation

---

## 🧪 Testing Checklist

### Unit Tests
- [ ] Service logic with mocked repos
- [ ] Business rules validation
- [ ] Rate calculations

### Integration Tests
- [ ] API endpoints (all roles)
- [ ] Multi-client isolation
- [ ] Cross-tenant access attempts (should fail)
- [ ] Historical rate retrieval
- [ ] Invoice generation with rate changes

### Security Tests
- [ ] JWT validation
- [ ] Role authorization
- [ ] ClientId validation
- [ ] SQL injection prevention

### Load Tests
- [ ] 100,000 clients
- [ ] 1,000,000 transactions
- [ ] Concurrent invoice generation

---

## 📝 Important Validations

| Field | Validation | Impact |
|-------|-----------|--------|
| **Quantity** | Must be > 0 | Cannot record 0 harvest |
| **Rate** | Must be > 0, 4 decimals | Price validity |
| **Harvest Date** | Cannot be future | Real-time operations |
| **Email** | RFC 5322 format | Contact validity |
| **Gross Weight** | Must be > Tare Weight | Physical constraint |
| **Net Weight** | Auto-calculated: Gross - Tare | Accuracy |
| **Invoice Amount** | Locked when GENERATED | Prevent data tampering |
| **Transaction** | Immutable after creation | Audit trail integrity |
| **Rate History** | Immutable after creation | Audit trail integrity |

---

## 🔧 Configuration Defaults

```
JWT Token Expiry: 30 minutes
Refresh Token: 7 days
Page Size: 50 records
Max Clients: 100,000
Currency: INR (configurable)
Tax Rate: 0% (configurable)
Invoice Number Format: INV-YYYY-NNNN
```

---

## 📞 Key Contacts & Responsibilities

| Component | Owner | Tech Stack |
|-----------|-------|-----------|
| Authentication | Security Team | Spring Security, JWT |
| Client Mgmt | Super Admin Feature | REST API |
| Farmer Mgmt | Client Admin Feature | REST API |
| Rate History | Business Logic | Service Layer |
| Invoice Engine | Core Logic | Service Layer, Historical Rates |
| Audit Trail | Compliance Team | AOP, Event Logging |
| Reporting | BI Team | Dashboard Service |
| DevOps | Operations Team | Docker, Kubernetes |

---

## 🎓 Learning Resources

1. **REQUIREMENT.md** - Complete specifications (1422 lines)
2. **ROLE_CLARIFICATION.md** - Detailed role guide (1000+ lines)
3. **IMPLEMENTATION_CHECKLIST.md** - Phase-by-phase tasks (1200+ lines)
4. **VISUAL_ARCHITECTURE_GUIDE.md** - ASCII diagrams & flows
5. **CHANGES_SUMMARY.md** - What changed & why
6. **This File** - Quick reference (this page)

---

## ⚡ Quick Start (First 3 Steps)

1. **Create User Table**
   ```sql
   CREATE TABLE users (
     user_id UUID PRIMARY KEY,
     username VARCHAR UNIQUE,
     role VARCHAR (SUPER_ADMIN, CLIENT_ADMIN, OPERATOR),
     client_id UUID (NULL for SUPER_ADMIN)
   );
   ```

2. **Create Spring Security Config**
   ```java
   @EnableWebSecurity
   public class SecurityConfig {
     @Bean
     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
       http.authorizeRequests()
         .antMatchers("/api/v1/super-admin/**").hasRole("SUPER_ADMIN")
         .antMatchers("/api/v1/clients/**").authenticated()
       return http.build();
     }
   }
   ```

3. **Extract ClientId in Every Service Query**
   ```java
   UUID currentClientId = SecurityContextHolder.getContext()
     .getAuthentication().getPrincipal().getClientId();
   return repo.findByClientId(currentClientId);
   ```

**DONE! Multi-tenancy secured!**

---

## 🆘 Troubleshooting

| Issue | Cause | Solution |
|-------|-------|----------|
| 403 Forbidden | Wrong role | Check @PreAuthorize |
| 403 Forbidden | Wrong clientId | Check URL clientId == token.clientId |
| 401 Unauthorized | Token expired | Use refresh token endpoint |
| 404 Not Found | Data from other client | Query filters by clientId correctly |
| 400 Bad Request | Invalid rate format | Use 4 decimal places |
| Invoice calc wrong | Old rate used | Check rate history query uses transaction date |
| Can't delete farmer | Active invoices exist | Delete/cancel invoices first |
| Operator sees other client | Code missing clientId filter | Add WHERE client_id = :clientId |

---

## 📞 Support

For issues or clarifications, refer to:
- **Architecture**: REQUIREMENT.md + VISUAL_ARCHITECTURE_GUIDE.md
- **Roles**: ROLE_CLARIFICATION.md
- **Implementation**: IMPLEMENTATION_CHECKLIST.md
- **Changes**: CHANGES_SUMMARY.md

---

**Last Updated**: April 14, 2026
**Version**: 2.0 (Multi-Tenant Architecture)
**Status**: Ready for Implementation

