# Implementation Checklist - Subabul Trade Book Application

## Phase 1: User Authentication & Authorization

### Authentication Layer
- [ ] Create User entity with fields: userId, username, email, passwordHash, role, clientId, status
- [ ] Create Role enum: SUPER_ADMIN, CLIENT_ADMIN, OPERATOR
- [ ] Implement JWT token generation (30 min expiry)
- [ ] Implement JWT refresh token (7 days expiry)
- [ ] Implement BCrypt password hashing
- [ ] Create UserRepository
- [ ] Create AuthenticationController with endpoints:
  - [ ] POST /api/v1/auth/login
  - [ ] POST /api/v1/auth/logout
  - [ ] POST /api/v1/auth/refresh-token
  - [ ] GET /api/v1/auth/profile

### Authorization Layer (Spring Security)
- [ ] Configure SecurityFilterChain
- [ ] Implement JwtAuthenticationFilter
- [ ] Implement @PreAuthorize annotations for role-based access
- [ ] Implement clientId extraction from JWT token
- [ ] Implement tenant isolation logic (filter queries by clientId)
- [ ] Create custom AuthenticationProvider

### User Service
- [ ] Create UserService interface
- [ ] Create UserServiceImpl with methods:
  - [ ] createUser(UserDTO) - for Super Admin
  - [ ] getUserByUsername(username)
  - [ ] getUserByEmail(email)
  - [ ] updateUser(userId, UserDTO)
  - [ ] deleteUser(userId)
  - [ ] validatePassword(username, password)

---

## Phase 2: Client Management (Super Admin Operations)

### Database
- [ ] Create Client entity with required fields
- [ ] Create Flyway migration V1__create_client_table.sql
- [ ] Create indexes on clientName, email

### Repository
- [ ] Create ClientRepository extending JpaRepository
- [ ] Add custom queries:
  - [ ] findByEmail(email)
  - [ ] findByTaxId(taxId)
  - [ ] findByStatus(status)

### Service Layer
- [ ] Create ClientService interface
- [ ] Create ClientServiceImpl with methods:
  - [ ] createClient(ClientDTO) - creates client + creates empty trade book
  - [ ] getClient(clientId)
  - [ ] getAllClients(pageable)
  - [ ] updateClient(clientId, ClientDTO)
  - [ ] deleteClient(clientId) - soft delete
  - [ ] searchClients(searchCriteria)

### Controller
- [ ] Create SuperAdminClientController with endpoints:
  - [ ] POST /api/v1/super-admin/clients
  - [ ] GET /api/v1/super-admin/clients
  - [ ] GET /api/v1/super-admin/clients/{clientId}
  - [ ] PUT /api/v1/super-admin/clients/{clientId}
  - [ ] DELETE /api/v1/super-admin/clients/{clientId}
  - [ ] GET /api/v1/super-admin/clients/search
  - [ ] POST /api/v1/super-admin/clients/{clientId}/admin-user

### DTOs
- [ ] Create ClientDTO with validation annotations
- [ ] Create ClientResponseDTO for API responses

---

## Phase 3: Farmer Management (Client Admin Operations)

### Database
- [ ] Create Farmer entity
- [ ] Create Flyway migration V2__create_farmer_table.sql
- [ ] Add foreign key to Client
- [ ] Create indexes on clientId, farmerName, email

### Repository
- [ ] Create FarmerRepository
- [ ] Add queries:
  - [ ] findByClientId(clientId)
  - [ ] findByClientIdAndEmail(clientId, email)
  - [ ] findByClientIdAndStatus(clientId, status)

### Service Layer
- [ ] Create FarmerService interface
- [ ] Create FarmerServiceImpl with methods:
  - [ ] createFarmer(clientId, FarmerDTO)
  - [ ] getFarmer(clientId, farmerId)
  - [ ] getFarmersByClient(clientId, pageable)
  - [ ] updateFarmer(clientId, farmerId, FarmerDTO)
  - [ ] deleteFarmer(clientId, farmerId)
  - [ ] searchFarmers(clientId, searchCriteria)

### Controller
- [ ] Create FarmerController with endpoints:
  - [ ] POST /api/v1/clients/{clientId}/farmers
  - [ ] GET /api/v1/clients/{clientId}/farmers
  - [ ] GET /api/v1/clients/{clientId}/farmers/{farmerId}
  - [ ] PUT /api/v1/clients/{clientId}/farmers/{farmerId}
  - [ ] DELETE /api/v1/clients/{clientId}/farmers/{farmerId}
  - [ ] GET /api/v1/clients/{clientId}/farmers/{farmerId}/audit-trail

### DTOs
- [ ] Create FarmerDTO with validation
- [ ] Create FarmerResponseDTO

---

## Phase 4: Team Management (Cutting & Transportation)

### Database
- [ ] Create CuttingTeam entity
- [ ] Create TransportationTeam entity
- [ ] Create Flyway migrations
- [ ] Add foreign keys to Client
- [ ] Create indexes

### Repositories
- [ ] Create CuttingTeamRepository
- [ ] Create TransportationTeamRepository
- [ ] Add standard queries (findByClientId, findByStatus, etc.)

### Services
- [ ] Create CuttingTeamService & CuttingTeamServiceImpl
- [ ] Create TransportationTeamService & TransportationTeamServiceImpl
- [ ] Implement CRUD operations for both

### Controllers
- [ ] Create CuttingTeamController
- [ ] Create TransportationTeamController
- [ ] Implement endpoints for both

### DTOs
- [ ] Create CuttingTeamDTO & ResponseDTO
- [ ] Create TransportationTeamDTO & ResponseDTO

---

## Phase 5: Rate Management with History

### Database
- [ ] Create RateHistory entity
- [ ] Create Flyway migration V3__create_rate_history_table.sql
- [ ] Create indexes on (entityType, entityId), (effectiveFromDate, effectiveToDate)
- [ ] Add foreign key to Client

### Repository
- [ ] Create RateHistoryRepository
- [ ] Add custom queries:
  - [ ] findActiveRateForEntity(entityType, entityId, clientId, date)
  - [ ] findRateHistoryForEntity(entityId, fromDate, toDate)
  - [ ] findActiveRates(clientId)
  - [ ] findRateOnDate(entityId, date)

### Service Layer
- [ ] Create RateService interface
- [ ] Create RateServiceImpl with methods:
  - [ ] createRate(clientId, RateDTO) - handles rate transitions
  - [ ] getActiveRate(entityType, entityId, clientId)
  - [ ] getRateHistoryForEntity(entityId, fromDate, toDate)
  - [ ] getRateEffectiveOnDate(entityId, date) - for invoice generation
  - [ ] updateRate(clientId, entityId, newRate) - marks old as inactive
  - [ ] getRateAuditTrail(clientId)

### Controller
- [ ] Create RateController with endpoints:
  - [ ] POST /api/v1/rates
  - [ ] GET /api/v1/rates/active
  - [ ] GET /api/v1/rates/entity/{entityId}
  - [ ] GET /api/v1/rates/entity/{entityId}/date/{date}
  - [ ] GET /api/v1/rates/audit-trail
  - [ ] PUT /api/v1/rates/{rateId}

### DTOs
- [ ] Create RateDTO
- [ ] Create RateHistoryDTO

---

## Phase 6: Harvest & Weighing Management

### Database
- [ ] Create Harvest entity (daily inlet records)
- [ ] Create Weighing entity
- [ ] Create Flyway migrations
- [ ] Create indexes on (clientId, harvestDate), (harvestId)

### Repositories
- [ ] Create HarvestRepository
- [ ] Create WeighingRepository
- [ ] Add queries for filtering by client, date range, status

### Services
- [ ] Create HarvestService with methods:
  - [ ] recordHarvest(clientId, HarvestDTO)
  - [ ] getHarvests(clientId, pageable, filters)
  - [ ] updateHarvestStatus(clientId, harvestId, newStatus)
  - [ ] deleteHarvest(clientId, harvestId) - with validations
  - [ ] getHarvestsByFarmer(clientId, farmerId)

- [ ] Create WeighingService with methods:
  - [ ] recordWeighing(clientId, WeighingDTO) - calculates net weight
  - [ ] getWeighingsByHarvest(clientId, harvestId)
  - [ ] updateWeighing(clientId, weighingId, WeighingDTO)

### Controllers
- [ ] Create HarvestController
- [ ] Create WeighingController
- [ ] Implement endpoints with @PreAuthorize("hasRole('OPERATOR') or hasRole('CLIENT_ADMIN')")

### DTOs
- [ ] Create HarvestDTO & ResponseDTO
- [ ] Create WeighingDTO & ResponseDTO

---

## Phase 7: Transaction Management

### Database
- [ ] Create Transaction entity
- [ ] Create Flyway migration
- [ ] Create indexes on (clientId, entityId, transactionDate, status)
- [ ] Total amount should be generated column

### Repository
- [ ] Create TransactionRepository
- [ ] Add queries:
  - [ ] findByClientIdAndEntityId(clientId, entityId, pageable)
  - [ ] findByClientIdAndDateRange(clientId, fromDate, toDate)
  - [ ] findByClientIdAndStatus(clientId, status)

### Service Layer
- [ ] Create TransactionService with methods:
  - [ ] createTransaction(clientId, TransactionDTO) - fetches current rate
  - [ ] getTransactions(clientId, filters, pageable)
  - [ ] getTransactionsByEntity(clientId, entityId, dateRange)
  - [ ] calculateTotalAmount(quantity, rate)

### Controller
- [ ] Create TransactionController (read-only for most users)
- [ ] Endpoints:
  - [ ] GET /api/v1/clients/{clientId}/transactions
  - [ ] GET /api/v1/clients/{clientId}/transactions/{transactionId}
  - [ ] GET /api/v1/clients/{clientId}/entities/{entityId}/transactions
  - [ ] GET /api/v1/clients/{clientId}/transactions/search

### DTOs
- [ ] Create TransactionDTO

---

## Phase 8: Invoice Generation & Management

### Database
- [ ] Create Invoice entity
- [ ] Create InvoiceLineItem entity
- [ ] Create Flyway migrations
- [ ] Create indexes

### Repositories
- [ ] Create InvoiceRepository
- [ ] Create InvoiceLineItemRepository
- [ ] Add queries for filtering

### Service Layer
- [ ] Create InvoiceService with methods:
  - [ ] generateInvoice(clientId, InvoiceGenerationRequest)
    - Fetch transactions for date range
    - For each transaction, get historical rate
    - Create line items with historical rates
    - Calculate totals
    - Create invoice
  - [ ] getInvoice(clientId, invoiceId)
  - [ ] getInvoicesByClient(clientId, pageable)
  - [ ] getInvoicesByEntity(clientId, entityId)
  - [ ] updateInvoiceStatus(clientId, invoiceId, newStatus)
  - [ ] generatePDF(clientId, invoiceId) - external PDF generation service

### Controller
- [ ] Create InvoiceController
- [ ] Endpoints:
  - [ ] POST /api/v1/clients/{clientId}/invoices/generate
  - [ ] GET /api/v1/clients/{clientId}/invoices
  - [ ] GET /api/v1/clients/{clientId}/invoices/{invoiceId}
  - [ ] GET /api/v1/clients/{clientId}/invoices/{invoiceId}/pdf
  - [ ] PUT /api/v1/clients/{clientId}/invoices/{invoiceId}/status
  - [ ] GET /api/v1/clients/{clientId}/entities/{entityId}/invoices

### DTOs
- [ ] Create InvoiceGenerationRequest
- [ ] Create InvoiceDTO
- [ ] Create InvoiceLineItemDTO

---

## Phase 9: Payment Management

### Database
- [ ] Create Payment entity
- [ ] Create Flyway migration
- [ ] Create indexes

### Repository
- [ ] Create PaymentRepository
- [ ] Add queries

### Service Layer
- [ ] Create PaymentService with methods:
  - [ ] recordPayment(clientId, PaymentDTO)
  - [ ] getPayments(clientId, filters, pageable)
  - [ ] getPaymentsByInvoice(clientId, invoiceId)
  - [ ] updatePaymentStatus(clientId, paymentId, status)
  - [ ] calculateRemainingBalance(invoiceId)

### Controller
- [ ] Create PaymentController
- [ ] Endpoints:
  - [ ] POST /api/v1/clients/{clientId}/payments
  - [ ] GET /api/v1/clients/{clientId}/payments
  - [ ] GET /api/v1/clients/{clientId}/payments/{paymentId}
  - [ ] PUT /api/v1/clients/{clientId}/payments/{paymentId}
  - [ ] GET /api/v1/clients/{clientId}/invoices/{invoiceId}/payments

### DTOs
- [ ] Create PaymentDTO
- [ ] Create PaymentResponseDTO

---

## Phase 10: Ledger Book Management

### Database
- [ ] Create LedgerBook entity
- [ ] Create Flyway migration
- [ ] Create index on (clientId, ledgerMonth)

### Repository
- [ ] Create LedgerBookRepository
- [ ] Add queries for month retrieval

### Service Layer
- [ ] Create LedgerService with methods:
  - [ ] createMonthlyLedger(clientId, month) - automated
  - [ ] getLedger(clientId, month)
  - [ ] calculateClosingBalance(clientId, month)
  - [ ] getLedgerSummary(clientId, dateRange)

### Controller
- [ ] Create LedgerController
- [ ] Endpoints:
  - [ ] GET /api/v1/clients/{clientId}/ledger
  - [ ] GET /api/v1/clients/{clientId}/ledger/month/{month}
  - [ ] GET /api/v1/clients/{clientId}/ledger/summary

### DTOs
- [ ] Create LedgerDTO
- [ ] Create LedgerSummaryDTO

---

## Phase 11: Audit Trail & Logging

### Database
- [ ] Create AuditLog entity
- [ ] Create Flyway migration

### Service Layer
- [ ] Create AuditService with methods:
  - [ ] logChange(entityType, entityId, action, fieldName, oldValue, newValue, changedBy, reason)
  - [ ] getAuditTrail(entityType, entityId)
  - [ ] getRateChangeHistory(clientId)

### Aspect/Interceptor
- [ ] Create AuditAspect to automatically log all @Auditable methods
- [ ] Capture method parameters and return values
- [ ] Log changes automatically on service methods

### Controller
- [ ] Create AuditController
- [ ] Endpoints:
  - [ ] GET /api/v1/clients/{clientId}/{entityType}/{entityId}/audit-trail
  - [ ] GET /api/v1/rates/audit-trail

---

## Phase 12: Dashboard & Reporting

### Service Layer
- [ ] Create DashboardService with methods:
  - [ ] getClientDashboard(clientId) - summary of transactions, invoices, payments
  - [ ] getTransactionReport(clientId, dateRange)
  - [ ] getInvoiceReport(clientId, dateRange)
  - [ ] getPaymentReport(clientId, dateRange)
  - [ ] getLedgerSummaryReport(clientId, dateRange)

### Controller
- [ ] Create DashboardController
- [ ] Endpoints:
  - [ ] GET /api/v1/clients/{clientId}/dashboard
  - [ ] GET /api/v1/clients/{clientId}/reports/transactions
  - [ ] GET /api/v1/clients/{clientId}/reports/invoices
  - [ ] GET /api/v1/clients/{clientId}/reports/payments
  - [ ] GET /api/v1/clients/{clientId}/reports/ledger-summary

### DTOs
- [ ] Create DashboardDTO
- [ ] Create ReportDTO variants

---

## Phase 13: Search & Filters

### Service Layer
- [ ] Create SearchService with methods:
  - [ ] globalSearch(clientId, query)
  - [ ] searchFarmers(clientId, criteria)
  - [ ] searchTransactions(clientId, criteria)
  - [ ] searchInvoices(clientId, criteria)

### Controller
- [ ] Create SearchController
- [ ] Endpoints:
  - [ ] GET /api/v1/clients/{clientId}/search/global
  - [ ] GET /api/v1/clients/{clientId}/search/{entityType}

---

## Phase 14: MapStruct Mappers

- [ ] Create ClientMapper
- [ ] Create FarmerMapper
- [ ] Create CuttingTeamMapper
- [ ] Create TransportationTeamMapper
- [ ] Create RateHistoryMapper
- [ ] Create HarvestMapper
- [ ] Create WeighingMapper
- [ ] Create TransactionMapper
- [ ] Create InvoiceMapper
- [ ] Create PaymentMapper
- [ ] Create LedgerMapper
- [ ] Create UserMapper

**Mapper Configuration**:
```java
@Mapper(componentModel = "spring")
public interface EntityMapper {
    EntityDTO toDTO(Entity entity);
    Entity toEntity(EntityDTO dto);
    List<EntityDTO> toDTOList(List<Entity> entities);
}
```

---

## Phase 15: Exception Handling

### Custom Exceptions
- [ ] Create ResourceNotFoundException
- [ ] Create BadRequestException
- [ ] Create UnauthorizedException
- [ ] Create TenantIsolationException
- [ ] Create InvoiceGenerationException

### Global Exception Handler
- [ ] Create GlobalExceptionHandler with @RestControllerAdvice
- [ ] Handle all custom exceptions
- [ ] Handle validation errors
- [ ] Return consistent error response format

### Error Response DTO
- [ ] Create ErrorResponseDTO with fields: errorCode, message, timestamp, details

---

## Phase 16: Validation

### Validation Annotations
- [ ] Use @NotNull, @NotBlank, @Min, @Max
- [ ] Use @Email for email validation
- [ ] Use @Pattern for phone number validation
- [ ] Use @PastOrPresent for dates

### Custom Validators
- [ ] Create @ValidUUID validator
- [ ] Create @ValidBankAccount validator
- [ ] Create @ValidPricePerTon validator
- [ ] Create @UniqueEmail validator

### Validation in Controllers
- [ ] Add @Valid on all request parameters
- [ ] Return 400 Bad Request with validation errors

---

## Phase 17: Testing

### Unit Tests (Mockito)
- [ ] Test all Service methods with mocked repositories
- [ ] Test business logic
- [ ] Test edge cases
- [ ] Aim for > 80% code coverage

### Integration Tests (MockMvc)
- [ ] Test all API endpoints
- [ ] Test role-based access control
- [ ] Test tenant isolation
- [ ] Test error scenarios
- [ ] Test pagination and filtering

### Test Database
- [ ] Use H2 in-memory for tests
- [ ] Use Flyway for test migrations
- [ ] Create test data fixtures

---

## Phase 18: Flyway Migrations

- [ ] V1__init_schema.sql - Base tables (clients, users)
- [ ] V2__create_farmers.sql
- [ ] V3__create_teams.sql
- [ ] V4__create_rate_history.sql
- [ ] V5__create_harvests_and_weighing.sql
- [ ] V6__create_transactions.sql
- [ ] V7__create_invoices.sql
- [ ] V8__create_payments.sql
- [ ] V9__create_ledger_books.sql
- [ ] V10__create_audit_logs.sql
- [ ] V11__insert_reference_data.sql (if any)

---

## Phase 19: Configuration

### Application Properties
- [ ] Set database URL (H2 or MySQL)
- [ ] Set JWT secret key
- [ ] Set JWT expiry times
- [ ] Set file upload path
- [ ] Set PDF generation settings
- [ ] Set page sizes

### Security Configuration
- [ ] Configure CORS if needed
- [ ] Configure SSL/HTTPS
- [ ] Configure rate limiting
- [ ] Configure CSRF tokens

---

## Phase 20: Documentation

- [ ] Create OpenAPI 3.0 specification
- [ ] Generate Swagger UI documentation
- [ ] Create architecture diagrams
- [ ] Create database schema diagrams
- [ ] Create API documentation
- [ ] Create deployment guide
- [ ] Create troubleshooting guide

---

## Phase 21: DevOps & Deployment

- [ ] Create Docker image
- [ ] Create docker-compose.yml
- [ ] Create Kubernetes manifests (optional)
- [ ] Setup CI/CD pipeline
- [ ] Setup monitoring and logging
- [ ] Setup backup strategy

---

## Key Constraints & Validations Checklist

- [ ] All queries filter by clientId (tenant isolation)
- [ ] Cannot delete farmers with active invoices
- [ ] Cannot delete harvests with weighing records
- [ ] Invoice generation uses historical rates
- [ ] Rate changes are immutable (no editing, only viewing)
- [ ] Audit logs are immutable
- [ ] Transactions are immutable
- [ ] Operators cannot modify farmer/team data
- [ ] Operators cannot access other clients' data
- [ ] Super Admin cannot manage client-specific data directly

---

## Success Criteria

✅ All endpoints return appropriate HTTP status codes
✅ All endpoints include proper authorization checks
✅ All queries properly filter by clientId
✅ All business rules are enforced
✅ All edge cases are handled
✅ Code coverage > 80%
✅ All APIs documented in Swagger
✅ System can support 100,000+ clients
✅ Performance: API response < 500ms for reads
✅ Zero data loss on transaction creation

