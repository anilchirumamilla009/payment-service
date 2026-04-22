# Subabul Trade Book Application - Detailed Requirements

> **⚠️ IMPORTANT CLARIFICATIONS**:
> 1. **Client Onboarding** is performed by **Super Admin** (application maintainer) only
> 2. **Farmer/Team Onboarding** is performed by each **Client Admin** for their own organization
> 3. **Operator Credentials** are created by **Client Admin** for their staff
> 4. This is a **Multi-Tenant SaaS** application with complete data isolation per client

---

## Table of Contents
1. [System Overview](#system-overview)
2. [Core Entities](#core-entities)
3. [Business Processes](#business-processes)
4. [API Endpoints](#api-endpoints)
5. [Database Schema](#database-schema)
6. [User Roles & Permissions](#user-roles--permissions)
7. [Business Rules & Validations](#business-rules--validations)
8. [Edge Cases & Special Handling](#edge-cases--special-handling)
9. [Non-Functional Requirements](#non-functional-requirements)

---

## System Overview

**Application**: Trade Book Management System for Subabul Crop Business
**Primary Purpose**: Automate manual trade book management for Subabul crop transactions

### Multi-Tenant Architecture
This is a **multi-tenant SaaS application** where:
- **Super Admin** (application maintainer) onboards multiple business clients
- Each **Client** operates independently with their own data
- Each client can have multiple **Admins** and **Operators**
- Complete data isolation: Operators at Client A cannot see Client B's data
- Each client has independent farmers, teams, rates, transactions, invoices, etc.

### Data Isolation Strategy
```
Application (1 Super Admin)
  ├── Client 1 (Admin + Operators) → Farmers, Teams, Rates, Transactions, Invoices
  ├── Client 2 (Admin + Operators) → Farmers, Teams, Rates, Transactions, Invoices
  ├── Client 3 (Admin + Operators) → Farmers, Teams, Rates, Transactions, Invoices
  └── Client N (Admin + Operators) → Farmers, Teams, Rates, Transactions, Invoices
```

**Important**: All queries must filter by `clientId` to ensure tenant isolation

### Core Business Flow
```
Client Onboarding 
  ↓
Create Trade Book & Ledger Book
  ↓
Buy Crop from Farmers (Record Transactions)
  ↓
Record Harvest Details (With Cutting & Transportation Teams)
  ↓
Record Weighing Details (Gross/Tare/Net Weight)
  ↓
Generate Invoices (Based on Active Rates)
  ↓
Record Payments & Receipts
```

---

## Core Entities

### 1. Client Entity
**Description**: Business clients who purchase Subabul crop from farmers

**Attributes**:
- `clientId` (UUID) - Primary Key
- `clientName` (String) - Full name/business name
- `email` (String) - Email address
- `phoneNumber` (String) - Contact number
- `address` (String) - Business address
- `city` (String) - City
- `state` (String) - State
- `zipCode` (String) - ZIP/Postal code
- `country` (String) - Country
- `businessType` (Enum) - Type of business
- `taxId` (String) - Tax/GST ID (if applicable)
- `status` (Enum) - ACTIVE, INACTIVE, SUSPENDED
- `createdDate` (DateTime) - Record creation date
- `modifiedDate` (DateTime) - Last modification date
- `createdBy` (String) - User who created the record
- `modifiedBy` (String) - User who last modified

**Operations**: 
- CRUD operations (Create, Read, Update, Delete)
- List all clients with pagination & filtering
- Search clients by name, email, phone

---

### 2. Farmer Entity
**Description**: Farmers from whom the client purchases Subabul crops

**Attributes**:
- `farmerId` (UUID) - Primary Key
- `clientId` (UUID) - Foreign Key (Relationship to Client)
- `farmerName` (String) - Full name
- `email` (String) - Email address
- `phoneNumber` (String) - Contact number
- `address` (String) - Farm address
- `city` (String) - City
- `state` (String) - State
- `zipCode` (String) - ZIP/Postal code
- `bankAccountNumber` (String) - For payments
- `bankName` (String) - Bank details
- `ifscCode` (String) - IFSC code
- `status` (Enum) - ACTIVE, INACTIVE
- `createdDate` (DateTime)
- `modifiedDate` (DateTime)

**Operations**:
- CRUD operations per client
- List all farmers for a specific client
- Search farmers by name, phone
- Bulk farmer import

---

### 3. Cutting Team Entity
**Description**: Teams responsible for cutting/harvesting Subabul crops

**Attributes**:
- `cuttingTeamId` (UUID) - Primary Key
- `clientId` (UUID) - Foreign Key (Relationship to Client)
- `teamName` (String) - Team name
- `teamLeadName` (String) - Lead person name
- `email` (String) - Email address
- `phoneNumber` (String) - Contact number
- `address` (String) - Team address
- `bankAccountNumber` (String) - For payments
- `bankName` (String) - Bank details
- `ifscCode` (String) - IFSC code
- `status` (Enum) - ACTIVE, INACTIVE
- `createdDate` (DateTime)
- `modifiedDate` (DateTime)

**Operations**:
- CRUD operations per client
- List all cutting teams for a specific client
- Search cutting teams by name

---

### 4. Transportation Team Entity
**Description**: Teams/Owners responsible for transportation of Subabul crops

**Attributes**:
- `transportationTeamId` (UUID) - Primary Key
- `clientId` (UUID) - Foreign Key (Relationship to Client)
- `teamName` (String) - Team/Tractor owner name
- `ownerName` (String) - Owner's name
- `email` (String) - Email address
- `phoneNumber` (String) - Contact number
- `address` (String) - Address
- `vehicleCount` (Integer) - Number of vehicles
- `vehicleDetails` (String) - Vehicle registration numbers/details
- `bankAccountNumber` (String) - For payments
- `bankName` (String) - Bank details
- `ifscCode` (String) - IFSC code
- `status` (Enum) - ACTIVE, INACTIVE
- `createdDate` (DateTime)
- `modifiedDate` (DateTime)

**Operations**:
- CRUD operations per client
- List all transportation teams for a specific client
- Search transportation teams by name

---

### 5. Rate History Entity
**Description**: Track price changes over time for farmers, cutting teams, and transportation teams

**Attributes**:
- `rateHistoryId` (UUID) - Primary Key
- `entityType` (Enum) - FARMER, CUTTING_TEAM, TRANSPORTATION_TEAM
- `entityId` (UUID) - Foreign Key (Reference to Farmer/CuttingTeam/TransportationTeam)
- `clientId` (UUID) - Foreign Key (Relationship to Client)
- `pricePerTon` (Decimal) - Rate per ton (market-based)
- `currencyCode` (String) - Currency (e.g., INR, USD)
- `effectiveFromDate` (DateTime) - When this rate became active
- `effectiveToDate` (DateTime) - When this rate expired (NULL if current)
- `isActive` (Boolean) - Is this the current active rate
- `createdDate` (DateTime)
- `createdBy` (String) - User who created this rate
- `rateChangeReason` (String) - Reason for rate change (e.g., market update, negotiation)

**Operations**:
- Create rate history entry
- Fetch active rates for an entity
- Fetch rate history for a date range
- Fetch historical rate for a specific date (for invoice generation)

---

### 6. Harvest/Daily Inlet Record Entity
**Description**: Records daily harvest intake details

**Attributes**:
- `harvestId` (UUID) - Primary Key
- `clientId` (UUID) - Foreign Key (Relationship to Client)
- `farmerId` (UUID) - Foreign Key (Farmer providing the crop)
- `harvestDate` (DateTime) - Date of harvest
- `quantity` (Decimal) - Quantity in tons
- `cuttingTeamId` (UUID) - Foreign Key (Cutting team involved)
- `transportationTeamId` (UUID) - Foreign Key (Transportation team involved)
- `notes` (String) - Additional notes
- `status` (Enum) - RECORDED, WEIGHED, INVOICED, PAID
- `createdDate` (DateTime)
- `modifiedDate` (DateTime)
- `createdBy` (String)

**Operations**:
- Create daily inlet record
- Update harvest record
- List all harvests for a client
- List all harvests for a farmer
- Calculate total harvest quantity for a date range

---

### 7. Weighing Record Entity
**Description**: Records weight details for each harvest entry (two-stage weighing)

**Attributes**:
- `weighingId` (UUID) - Primary Key
- `harvestId` (UUID) - Foreign Key (Harvest record)
- `clientId` (UUID) - Foreign Key (Relationship to Client)
- `weighingDateTime` (DateTime) - Date and time of weighing
- `grossWeight` (Decimal) - Gross weight (in kg/tons)
- `tareWeight` (Decimal) - Tare weight (empty container weight)
- `netWeight` (Decimal) - Calculated as Gross - Tare
- `weighingLocation` (String) - Where weighing was done
- `weighingOperator` (String) - Person who performed weighing
- `cuttingTeamId` (UUID) - Foreign Key (Cutting team at time of weighing)
- `transportationTeamId` (UUID) - Foreign Key (Transportation team at time of weighing)
- `notes` (String) - Additional notes
- `createdDate` (DateTime)

**Operations**:
- Create weighing record
- Calculate net weight automatically
- Update weighing record
- List all weighings for a harvest
- List all weighings for a client (with date range)

---

### 8. Transaction Entity
**Description**: Records all transactions (for each category: Farmer, Cutting Team, Transportation Team)

**Attributes**:
- `transactionId` (UUID) - Primary Key
- `clientId` (UUID) - Foreign Key (Relationship to Client)
- `transactionType` (Enum) - FARMER_PURCHASE, CUTTING_TEAM_PAYMENT, TRANSPORTATION_PAYMENT
- `entityId` (UUID) - Foreign Key (Reference to Farmer/CuttingTeam/TransportationTeam)
- `harvestId` (UUID) - Foreign Key (Optional, reference to harvest if applicable)
- `transactionDate` (DateTime) - Date of transaction
- `quantity` (Decimal) - Quantity (in tons)
- `pricePerUnit` (Decimal) - Rate applied at time of transaction
- `totalAmount` (Decimal) - Quantity × PricePerUnit
- `currency` (String) - Currency code
- `description` (String) - Transaction description
- `status` (Enum) - PENDING, INVOICED, PAID, CANCELLED
- `createdDate` (DateTime)
- `modifiedDate` (DateTime)

**Operations**:
- Create transaction
- Update transaction
- List all transactions for a client
- List all transactions for a specific entity (farmer/team) with date range
- Calculate total transaction amount for a date range

---

### 9. Invoice Entity
**Description**: Generated invoices for Farmer, Cutting Team, or Transportation Team

**Attributes**:
- `invoiceId` (UUID) - Primary Key
- `clientId` (UUID) - Foreign Key (Relationship to Client)
- `invoiceType` (Enum) - FARMER_INVOICE, CUTTING_TEAM_INVOICE, TRANSPORTATION_INVOICE
- `entityId` (UUID) - Foreign Key (Farmer/CuttingTeam/TransportationTeam)
- `invoiceNumber` (String) - Unique invoice number (e.g., INV-2026-001)
- `invoiceDate` (DateTime) - Date invoice was generated
- `fromDate` (DateTime) - Period start
- `toDate` (DateTime) - Period end
- `lineItems` (List) - List of transaction line items included
- `subtotal` (Decimal) - Sum of all line items
- `taxAmount` (Decimal) - Tax (if applicable)
- `totalAmount` (Decimal) - Final invoice amount
- `currency` (String) - Currency code
- `status` (Enum) - DRAFT, GENERATED, SENT, PAID, PARTIALLY_PAID, CANCELLED
- `paymentDueDate` (DateTime) - Due date for payment
- `pdfFilePath` (String) - Path to generated PDF file
- `createdDate` (DateTime)
- `modifiedDate` (DateTime)

**Operations**:
- Generate invoice from transactions (batch)
- Retrieve invoice by ID
- List all invoices for a client
- List all invoices for an entity (farmer/team)
- Download invoice as PDF
- Update invoice status

---

### 10. Payment Record Entity
**Description**: Records payments made to Farmers, Cutting Teams, and Transportation Teams

**Attributes**:
- `paymentId` (UUID) - Primary Key
- `clientId` (UUID) - Foreign Key (Relationship to Client)
- `invoiceId` (UUID) - Foreign Key (Reference to Invoice)
- `entityId` (UUID) - Foreign Key (Farmer/CuttingTeam/TransportationTeam)
- `paymentDate` (DateTime) - Date of payment
- `amountPaid` (Decimal) - Amount paid
- `paymentMode` (Enum) - CASH, BANK_TRANSFER, CHEQUE, OTHER
- `referenceNumber` (String) - Check number, transaction ID, etc.
- `bankName` (String) - Bank details (if bank transfer)
- `notes` (String) - Payment notes
- `status` (Enum) - PENDING, COMPLETED, FAILED, REVERSED
- `createdDate` (DateTime)
- `modifiedDate` (DateTime)

**Operations**:
- Create payment record
- Update payment status
- List all payments for a client
- List all payments for an invoice
- Calculate total amount paid for a date range

---

### 11. Ledger Book Entity
**Description**: Master ledger for a client - tracks all receipts and payments

**Attributes**:
- `ledgerId` (UUID) - Primary Key
- `clientId` (UUID) - Foreign Key (Relationship to Client)
- `ledgerMonth` (String) - Month identifier (e.g., "2026-04")
- `openingBalance` (Decimal) - Opening balance
- `totalReceipts` (Decimal) - Total money received from crop sales
- `totalPayments` (Decimal) - Total payments made to farmers/teams
- `closingBalance` (Decimal) - Calculated as Opening + Receipts - Payments
- `notes` (String) - Ledger notes
- `createdDate` (DateTime)
- `modifiedDate` (DateTime)

**Operations**:
- Create monthly ledger
- Calculate ledger summary for a month
- Update ledger with transactions
- Retrieve ledger for a specific month/date range

---

### 12. Audit Log Entity
**Description**: Track all changes to rate and transactional data

**Attributes**:
- `auditId` (UUID) - Primary Key
- `clientId` (UUID) - Foreign Key (Relationship to Client)
- `entityType` (String) - Entity being audited (Farmer, Rate, Transaction, etc.)
- `entityId` (UUID) - ID of the entity being audited
- `action` (Enum) - CREATE, UPDATE, DELETE
- `fieldName` (String) - Field that was changed
- `oldValue` (String) - Previous value
- `newValue` (String) - New value
- `changedBy` (String) - User who made the change
- `changedDate` (DateTime) - When the change was made
- `reason` (String) - Reason for change (for rate changes)

**Operations**:
- Log all changes automatically
- Retrieve audit history for an entity
- Retrieve rate change history for auditing
- Generate audit trail report

---

## Business Processes

### Process 1: Client Onboarding (Super Admin Only)
**Actor**: Super Admin (Application Maintainer)
**Trigger**: New business client wants to use the application

**Steps**:
1. Super Admin receives client request/onboarding form
2. Super Admin creates new Client record with:
   - Client name
   - Business type
   - Contact details
   - Tax ID
   - Address
3. System automatically creates:
   - Empty Trade Book workspace for this client
   - Empty Ledger Book for this client
4. Super Admin creates initial **Client Admin** account:
   - Email/username for the client's admin
   - Temporary password (client will change on first login)
   - Assigns ADMIN role
5. Super Admin sends login credentials to client
6. Client (Admin) logs in and changes password
7. Client (Admin) can now onboard their team:
   - Add farmers
   - Add cutting teams
   - Add transportation teams
   - Create operators

**Permissions During This Process**: Super Admin only

---

### Process 2: Farmer Onboarding (Client Admin)
**Actor**: Client Admin (Each client manages their own farmers)
**Trigger**: Client wants to start purchasing from a new farmer

**Steps**:
1. Client Admin adds a new Farmer with:
   - Farmer name
   - Contact details (phone, email)
   - Address
   - Bank account details (for payments)
2. Client Admin sets initial **Rate per ton** for the farmer:
   - Price per ton (market-based)
   - Effective date
   - Currency
3. System records rate in RateHistory table with:
   - Entity Type: FARMER
   - Entity ID: farmer_id
   - Client ID: client_id
   - Effective From Date: today
   - Effective To Date: NULL (currently active)
   - Is Active: TRUE
4. Farmer record is now active and ready for transactions
5. Operators can now record harvests from this farmer

**Important Notes**:
- Each client manages their own farmers independently
- Rates are client-specific (different clients can have different rates for same farmer)
- Farmers cannot see the system (they receive invoices generated by the system)

---

### Process 3: Operator Account Creation (Client Admin)
**Actor**: Client Admin
**Trigger**: Need to add staff to record daily operations

**Steps**:
1. Client Admin creates Operator account:
   - Username/email
   - Temporary password
   - Assign OPERATOR role
2. System creates operator record linked to:
   - Client ID (tenant isolation)
   - OPERATOR role
3. Operator receives login credentials
4. Operator logs in and changes password
5. Operator can now:
   - Record daily harvests
   - Record weighing data
   - View transactions and invoices

---

### Process 4: Add Cutting Team/Transportation Team
**Actor**: Client Admin
**Trigger**: Need to add new cutting/transportation service provider

**Steps**:
1. Client Admin adds new team member with:
   - Team name
   - Lead/Owner name
   - Contact details
   - Bank account details
2. System stores information linked to client
3. Client Admin sets initial rate per ton:
   - Price per ton
   - Effective date
   - Currency
4. System records rate in RateHistory table with effective date
5. Team is now active for use in harvest records

---

### Process 5: Update Rate (Market Changes)
**Actor**: Client Admin
**Trigger**: Market prices change or negotiated rates change

**Steps**:
1. Admin updates rate for a farmer/team
2. System:
   - Marks previous rate as inactive (sets effectiveToDate)
   - Creates new RateHistory entry with new rate and effectiveFromDate
   - Logs the change in Audit table with reason
   - Sends notification to stakeholders (if enabled)
3. All future transactions use the new rate automatically
4. Historical invoices remain based on rates active at time of transaction

---

### Process 6: Record Daily Harvest Intake
**Actor**: Operator
**Trigger**: Daily crop harvest needs to be recorded

**Steps**:
1. Operator records daily harvest details:
   - Date
   - Farmer
   - Quantity (in tons)
   - Cutting team involved
   - Transportation team involved
2. System creates Harvest record
3. System creates corresponding Transaction record using current active rate
4. Status set to "RECORDED"

---

### Process 7: Record Weighing
**Actor**: Operator
**Trigger**: Harvest needs to be weighed

**Steps**:
1. Operator records weighing details:
   - Harvest ID
   - Gross weight
   - Tare weight
   - Weighing date/time
   - Teams involved
2. System:
   - Calculates net weight (Gross - Tare)
   - Creates Weighing record
   - Updates Harvest status to "WEIGHED"
   - Updates Transaction with accurate net weight if needed

---

### Process 8: Generate Invoice
**Actor**: Client Admin
**Trigger**: Time to generate invoice for a farmer/team (end of month, quarterly, or as needed)

**Steps**:
1. Admin/Operator selects:
   - Invoice type (Farmer / Cutting Team / Transportation)
   - Entity (specific farmer/team)
   - Date range
2. System:
   - Fetches all transactions for the entity within the date range
   - For each transaction, retrieves the rate that was active on transaction date
   - Recalculates amounts using historical rates
   - Groups transactions into line items
   - Creates Invoice record in DRAFT status
3. Operator reviews and approves invoice
4. System generates PDF and stores it
5. Invoice status changes to GENERATED

---

### Process 9: Send Invoice & Record Payment
**Actor**: Client Admin (send), Operator (record payment)
**Trigger**: Invoice ready to send and payment received from farmer/team

**Steps**:
1. Admin sends generated invoice to farmer/team (PDF download)
2. Farmer/Team receives and settles payment
3. Operator records payment:
   - Amount paid
   - Payment mode (cash/bank transfer/check)
   - Reference number
   - Date
4. System:
   - Creates Payment record
   - Updates Invoice status to PAID or PARTIALLY_PAID
   - Updates Ledger book with receipt/payment
   - Updates Transaction status to PAID

---

### Process 10: Update Ledger Book
**Actor**: System (automatic) / Client Admin (review)
**Trigger**: End of month or manual ledger generation

**Steps**:
1. At end of month, system auto-generates ledger for the client
2. Ledger includes:
   - Opening balance from previous month
   - All payments received (from crop sales)
   - All payments made (to farmers/teams)
   - Closing balance
3. Admin can view monthly ledger summaries

---

## API Endpoints

### Authentication & User Management
```
POST   /api/v1/auth/login              - User login
POST   /api/v1/auth/logout             - User logout
GET    /api/v1/auth/profile            - Get current user
POST   /api/v1/auth/refresh-token      - Refresh authentication token
```

### Client Management (Super Admin Only)
```
POST   /api/v1/super-admin/clients                 - Create client (Onboarding)
GET    /api/v1/super-admin/clients                 - List all clients (paginated)
GET    /api/v1/super-admin/clients/{clientId}      - Get client details
PUT    /api/v1/super-admin/clients/{clientId}      - Update client
DELETE /api/v1/super-admin/clients/{clientId}      - Delete client (soft delete)
GET    /api/v1/super-admin/clients/search          - Search clients (by name, email, phone)
POST   /api/v1/super-admin/clients/{clientId}/admin-user - Create client admin account
```

### Client Self-Service (Client Admin)
```
GET    /api/v1/clients/{clientId}      - Get my client details
PUT    /api/v1/clients/{clientId}      - Update my client profile
GET    /api/v1/clients/{clientId}/dashboard - Get client dashboard summary
GET    /api/v1/clients/{clientId}/audit-trail - Get client audit trail
POST   /api/v1/clients/{clientId}/operators      - Create operator account
GET    /api/v1/clients/{clientId}/operators      - List operators
PUT    /api/v1/clients/{clientId}/operators/{operatorId} - Update operator
DELETE /api/v1/clients/{clientId}/operators/{operatorId} - Delete operator
```

### Farmer Management
```
POST   /api/v1/clients/{clientId}/farmers                  - Create farmer
GET    /api/v1/clients/{clientId}/farmers                  - List farmers for client
GET    /api/v1/clients/{clientId}/farmers/{farmerId}       - Get farmer details
PUT    /api/v1/clients/{clientId}/farmers/{farmerId}       - Update farmer
DELETE /api/v1/clients/{clientId}/farmers/{farmerId}       - Delete farmer
GET    /api/v1/clients/{clientId}/farmers/{farmerId}/audit-trail - Farmer audit trail
```

### Cutting Team Management
```
POST   /api/v1/clients/{clientId}/cutting-teams                  - Create cutting team
GET    /api/v1/clients/{clientId}/cutting-teams                  - List cutting teams
GET    /api/v1/clients/{clientId}/cutting-teams/{teamId}         - Get team details
PUT    /api/v1/clients/{clientId}/cutting-teams/{teamId}         - Update team
DELETE /api/v1/clients/{clientId}/cutting-teams/{teamId}         - Delete team
GET    /api/v1/clients/{clientId}/cutting-teams/{teamId}/audit-trail - Team audit trail
```

### Transportation Team Management
```
POST   /api/v1/clients/{clientId}/transportation-teams                  - Create team
GET    /api/v1/clients/{clientId}/transportation-teams                  - List teams
GET    /api/v1/clients/{clientId}/transportation-teams/{teamId}         - Get team details
PUT    /api/v1/clients/{clientId}/transportation-teams/{teamId}         - Update team
DELETE /api/v1/clients/{clientId}/transportation-teams/{teamId}         - Delete team
GET    /api/v1/clients/{clientId}/transportation-teams/{teamId}/audit-trail - Team audit trail
```

### Rate Management
```
POST   /api/v1/rates                                        - Create/Update rate
GET    /api/v1/rates/active                                 - Get all active rates
GET    /api/v1/rates/entity/{entityId}                      - Get rate history for entity
GET    /api/v1/rates/entity/{entityId}/date/{date}          - Get rate effective on specific date
GET    /api/v1/rates/audit-trail                            - Get rate change audit trail
PUT    /api/v1/rates/{rateId}                               - Update rate
GET    /api/v1/rates/{rateId}/audit-trail                   - Rate audit trail
```

### Harvest Management
```
POST   /api/v1/clients/{clientId}/harvests                  - Create daily inlet record
GET    /api/v1/clients/{clientId}/harvests                  - List all harvests for client
GET    /api/v1/clients/{clientId}/harvests/{harvestId}      - Get harvest details
PUT    /api/v1/clients/{clientId}/harvests/{harvestId}      - Update harvest
DELETE /api/v1/clients/{clientId}/harvests/{harvestId}      - Delete harvest
GET    /api/v1/clients/{clientId}/farmers/{farmerId}/harvests - Get harvests for farmer
GET    /api/v1/clients/{clientId}/harvests/audit-trail      - Harvest audit trail
```

### Weighing Management
```
POST   /api/v1/clients/{clientId}/weighings                 - Create weighing record
GET    /api/v1/clients/{clientId}/weighings                 - List weighings for client
GET    /api/v1/clients/{clientId}/weighings/{weighingId}    - Get weighing details
PUT    /api/v1/clients/{clientId}/weighings/{weighingId}    - Update weighing
GET    /api/v1/clients/{clientId}/harvests/{harvestId}/weighings - Get weighings for harvest
GET    /api/v1/clients/{clientId}/weighings/audit-trail     - Weighing audit trail
```

### Transaction Management
```
GET    /api/v1/clients/{clientId}/transactions              - List transactions for client
GET    /api/v1/clients/{clientId}/transactions/{transactionId} - Get transaction details
GET    /api/v1/clients/{clientId}/entities/{entityId}/transactions - Get transactions for entity
GET    /api/v1/clients/{clientId}/transactions/search       - Search transactions (by date, type, entity)
GET    /api/v1/clients/{clientId}/transactions/audit-trail  - Transaction audit trail
```

### Invoice Management
```
POST   /api/v1/clients/{clientId}/invoices/generate         - Generate new invoice
GET    /api/v1/clients/{clientId}/invoices                  - List invoices for client
GET    /api/v1/clients/{clientId}/invoices/{invoiceId}      - Get invoice details
GET    /api/v1/clients/{clientId}/invoices/{invoiceId}/pdf  - Download invoice as PDF
PUT    /api/v1/clients/{clientId}/invoices/{invoiceId}/status - Update invoice status
GET    /api/v1/clients/{clientId}/entities/{entityId}/invoices - Get invoices for entity
GET    /api/v1/clients/{clientId}/invoices/audit-trail      - Invoice audit trail
POST   /api/v1/clients/{clientId}/invoices/{invoiceId}/send  - Send invoice (email/notification)
```

### Payment Management
```
POST   /api/v1/clients/{clientId}/payments                  - Create payment record
GET    /api/v1/clients/{clientId}/payments                  - List payments for client
GET    /api/v1/clients/{clientId}/payments/{paymentId}      - Get payment details
PUT    /api/v1/clients/{clientId}/payments/{paymentId}      - Update payment
GET    /api/v1/clients/{clientId}/invoices/{invoiceId}/payments - Get payments for invoice
GET    /api/v1/clients/{clientId}/payments/audit-trail      - Payment audit trail
```

### Ledger Book
```
GET    /api/v1/clients/{clientId}/ledger                    - Get current ledger
GET    /api/v1/clients/{clientId}/ledger/month/{month}      - Get ledger for specific month
GET    /api/v1/clients/{clientId}/ledger/summary            - Get summary
GET    /api/v1/clients/{clientId}/ledger/audit-trail        - Ledger audit trail
```

### Dashboard & Reports
```
GET    /api/v1/clients/{clientId}/dashboard                 - Dashboard summary
GET    /api/v1/clients/{clientId}/reports/transactions      - Transaction report
GET    /api/v1/clients/{clientId}/reports/invoices          - Invoice report
GET    /api/v1/clients/{clientId}/reports/payments          - Payment report
GET    /api/v1/clients/{clientId}/reports/ledger-summary    - Ledger summary report
```

### Search & Filter
```
GET    /api/v1/search/global                                - Global search
GET    /api/v1/clients/{clientId}/search                    - Search within client data
```

---

## Database Schema

### Schema Design Principles
- All entities have UUID primary keys
- Timestamp fields: createdDate, modifiedDate (automatic)
- Soft deletes: use `isActive` or `status` field
- Audit trail: separate audit tables or audit column

### Table Structure

```sql
-- CLIENTS
CREATE TABLE clients (
  client_id UUID PRIMARY KEY,
  client_name VARCHAR(255) NOT NULL,
  email VARCHAR(255),
  phone_number VARCHAR(20),
  address TEXT,
  city VARCHAR(100),
  state VARCHAR(100),
  zip_code VARCHAR(20),
  country VARCHAR(100),
  business_type VARCHAR(100),
  tax_id VARCHAR(100),
  status VARCHAR(50) DEFAULT 'ACTIVE',
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(255),
  modified_by VARCHAR(255),
  INDEX idx_client_name (client_name),
  INDEX idx_email (email)
);

-- FARMERS
CREATE TABLE farmers (
  farmer_id UUID PRIMARY KEY,
  client_id UUID NOT NULL,
  farmer_name VARCHAR(255) NOT NULL,
  email VARCHAR(255),
  phone_number VARCHAR(20),
  address TEXT,
  city VARCHAR(100),
  state VARCHAR(100),
  zip_code VARCHAR(20),
  bank_account_number VARCHAR(50),
  bank_name VARCHAR(100),
  ifsc_code VARCHAR(20),
  status VARCHAR(50) DEFAULT 'ACTIVE',
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (client_id) REFERENCES clients(client_id),
  INDEX idx_client_farmer (client_id),
  INDEX idx_farmer_name (farmer_name)
);

-- CUTTING_TEAMS
CREATE TABLE cutting_teams (
  cutting_team_id UUID PRIMARY KEY,
  client_id UUID NOT NULL,
  team_name VARCHAR(255) NOT NULL,
  team_lead_name VARCHAR(255),
  email VARCHAR(255),
  phone_number VARCHAR(20),
  address TEXT,
  bank_account_number VARCHAR(50),
  bank_name VARCHAR(100),
  ifsc_code VARCHAR(20),
  status VARCHAR(50) DEFAULT 'ACTIVE',
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (client_id) REFERENCES clients(client_id),
  INDEX idx_client_team (client_id),
  INDEX idx_team_name (team_name)
);

-- TRANSPORTATION_TEAMS
CREATE TABLE transportation_teams (
  transportation_team_id UUID PRIMARY KEY,
  client_id UUID NOT NULL,
  team_name VARCHAR(255) NOT NULL,
  owner_name VARCHAR(255),
  email VARCHAR(255),
  phone_number VARCHAR(20),
  address TEXT,
  vehicle_count INT DEFAULT 0,
  vehicle_details TEXT,
  bank_account_number VARCHAR(50),
  bank_name VARCHAR(100),
  ifsc_code VARCHAR(20),
  status VARCHAR(50) DEFAULT 'ACTIVE',
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (client_id) REFERENCES clients(client_id),
  INDEX idx_client_transport (client_id),
  INDEX idx_team_name (team_name)
);

-- RATE_HISTORY
CREATE TABLE rate_history (
  rate_history_id UUID PRIMARY KEY,
  entity_type VARCHAR(50) NOT NULL,
  entity_id UUID NOT NULL,
  client_id UUID NOT NULL,
  price_per_ton DECIMAL(15, 4) NOT NULL,
  currency_code VARCHAR(10) DEFAULT 'INR',
  effective_from_date TIMESTAMP NOT NULL,
  effective_to_date TIMESTAMP,
  is_active BOOLEAN DEFAULT true,
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(255),
  rate_change_reason VARCHAR(500),
  FOREIGN KEY (client_id) REFERENCES clients(client_id),
  INDEX idx_entity (entity_type, entity_id),
  INDEX idx_effective_dates (effective_from_date, effective_to_date),
  INDEX idx_client_rates (client_id)
);

-- HARVESTS (Daily Inlet Records)
CREATE TABLE harvests (
  harvest_id UUID PRIMARY KEY,
  client_id UUID NOT NULL,
  farmer_id UUID NOT NULL,
  harvest_date TIMESTAMP NOT NULL,
  quantity DECIMAL(15, 4) NOT NULL,
  cutting_team_id UUID,
  transportation_team_id UUID,
  notes TEXT,
  status VARCHAR(50) DEFAULT 'RECORDED',
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(255),
  FOREIGN KEY (client_id) REFERENCES clients(client_id),
  FOREIGN KEY (farmer_id) REFERENCES farmers(farmer_id),
  FOREIGN KEY (cutting_team_id) REFERENCES cutting_teams(cutting_team_id),
  FOREIGN KEY (transportation_team_id) REFERENCES transportation_teams(transportation_team_id),
  INDEX idx_client_harvests (client_id),
  INDEX idx_farmer_harvests (farmer_id),
  INDEX idx_harvest_date (harvest_date)
);

-- WEIGHINGS
CREATE TABLE weighings (
  weighing_id UUID PRIMARY KEY,
  harvest_id UUID NOT NULL,
  client_id UUID NOT NULL,
  weighing_date_time TIMESTAMP NOT NULL,
  gross_weight DECIMAL(15, 4) NOT NULL,
  tare_weight DECIMAL(15, 4) NOT NULL,
  net_weight DECIMAL(15, 4) GENERATED ALWAYS AS (gross_weight - tare_weight) STORED,
  weighing_location VARCHAR(255),
  weighing_operator VARCHAR(255),
  cutting_team_id UUID,
  transportation_team_id UUID,
  notes TEXT,
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (client_id) REFERENCES clients(client_id),
  FOREIGN KEY (harvest_id) REFERENCES harvests(harvest_id),
  FOREIGN KEY (cutting_team_id) REFERENCES cutting_teams(cutting_team_id),
  FOREIGN KEY (transportation_team_id) REFERENCES transportation_teams(transportation_team_id),
  INDEX idx_harvest_weighings (harvest_id),
  INDEX idx_weighing_date (weighing_date_time)
);

-- TRANSACTIONS
CREATE TABLE transactions (
  transaction_id UUID PRIMARY KEY,
  client_id UUID NOT NULL,
  transaction_type VARCHAR(50) NOT NULL,
  entity_id UUID NOT NULL,
  harvest_id UUID,
  transaction_date TIMESTAMP NOT NULL,
  quantity DECIMAL(15, 4) NOT NULL,
  price_per_unit DECIMAL(15, 4) NOT NULL,
  total_amount DECIMAL(18, 2) GENERATED ALWAYS AS (quantity * price_per_unit) STORED,
  currency VARCHAR(10) DEFAULT 'INR',
  description TEXT,
  status VARCHAR(50) DEFAULT 'PENDING',
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (client_id) REFERENCES clients(client_id),
  INDEX idx_client_txns (client_id),
  INDEX idx_entity_txns (entity_id),
  INDEX idx_txn_date (transaction_date),
  INDEX idx_txn_status (status)
);

-- INVOICES
CREATE TABLE invoices (
  invoice_id UUID PRIMARY KEY,
  client_id UUID NOT NULL,
  invoice_type VARCHAR(50) NOT NULL,
  entity_id UUID NOT NULL,
  invoice_number VARCHAR(50) NOT NULL UNIQUE,
  invoice_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  from_date DATE NOT NULL,
  to_date DATE NOT NULL,
  subtotal DECIMAL(18, 2) NOT NULL,
  tax_amount DECIMAL(18, 2) DEFAULT 0,
  total_amount DECIMAL(18, 2) NOT NULL,
  currency VARCHAR(10) DEFAULT 'INR',
  status VARCHAR(50) DEFAULT 'DRAFT',
  payment_due_date DATE,
  pdf_file_path VARCHAR(500),
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (client_id) REFERENCES clients(client_id),
  INDEX idx_client_invoices (client_id),
  INDEX idx_entity_invoices (entity_id),
  INDEX idx_invoice_number (invoice_number),
  INDEX idx_invoice_status (status)
);

-- INVOICE_LINE_ITEMS
CREATE TABLE invoice_line_items (
  line_item_id UUID PRIMARY KEY,
  invoice_id UUID NOT NULL,
  transaction_id UUID,
  description VARCHAR(500),
  quantity DECIMAL(15, 4),
  unit_price DECIMAL(15, 4),
  line_amount DECIMAL(18, 2),
  FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id),
  FOREIGN KEY (transaction_id) REFERENCES transactions(transaction_id)
);

-- PAYMENTS
CREATE TABLE payments (
  payment_id UUID PRIMARY KEY,
  client_id UUID NOT NULL,
  invoice_id UUID,
  entity_id UUID NOT NULL,
  payment_date TIMESTAMP NOT NULL,
  amount_paid DECIMAL(18, 2) NOT NULL,
  payment_mode VARCHAR(50) NOT NULL,
  reference_number VARCHAR(100),
  bank_name VARCHAR(100),
  notes TEXT,
  status VARCHAR(50) DEFAULT 'PENDING',
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (client_id) REFERENCES clients(client_id),
  FOREIGN KEY (invoice_id) REFERENCES invoices(invoice_id),
  INDEX idx_client_payments (client_id),
  INDEX idx_invoice_payments (invoice_id),
  INDEX idx_payment_date (payment_date)
);

-- LEDGER_BOOKS
CREATE TABLE ledger_books (
  ledger_id UUID PRIMARY KEY,
  client_id UUID NOT NULL UNIQUE,
  ledger_month VARCHAR(10) NOT NULL,
  opening_balance DECIMAL(18, 2) DEFAULT 0,
  total_receipts DECIMAL(18, 2) DEFAULT 0,
  total_payments DECIMAL(18, 2) DEFAULT 0,
  closing_balance DECIMAL(18, 2) GENERATED ALWAYS AS (opening_balance + total_receipts - total_payments) STORED,
  notes TEXT,
  created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (client_id) REFERENCES clients(client_id),
  INDEX idx_client_ledger (client_id),
  INDEX idx_ledger_month (ledger_month)
);

-- AUDIT_LOGS
CREATE TABLE audit_logs (
  audit_id UUID PRIMARY KEY,
  client_id UUID NOT NULL,
  entity_type VARCHAR(100) NOT NULL,
  entity_id UUID NOT NULL,
  action VARCHAR(50) NOT NULL,
  field_name VARCHAR(255),
  old_value TEXT,
  new_value TEXT,
  changed_by VARCHAR(255),
  changed_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  reason TEXT,
  INDEX idx_entity_audit (entity_type, entity_id),
  INDEX idx_changed_date (changed_date),
  INDEX idx_client_audit (client_id)
);
```

---

## User Roles & Permissions

### Role 1: Super Admin (Application Level)
**Scope**: Manages the entire application and onboards new clients

**Permissions**:
- ✅ Create, Read, Update, Delete Clients (application-level client management)
- ✅ View all client data (for troubleshooting/support)
- ✅ Create Client Admin accounts (for each client's admin)
- ✅ Manage System Settings & Configuration
- ✅ View System-wide Audit Trails
- ✅ View System Dashboard & Analytics
- ✅ Manage System Users & Roles
- ✅ Database & Backup Management

**Restrictions**:
- ❌ Cannot manage client-specific data (farmers, teams, rates, transactions)
- ❌ Cannot create operators (only client admins can)
- ❌ Cannot record harvests or transactions

**Key Responsibility**: 
- Onboard new clients to the application
- Create initial admin credentials for each client
- Provide technical support & system maintenance

---

### Role 2: Admin (Client Level)
**Scope**: Manages all operations for their specific client organization

**Parent Role**: Assigned by Super Admin during client onboarding

**Permissions**:
- ✅ Create, Read, Update, Delete Farmers (for their client only)
- ✅ Create, Read, Update, Delete Cutting Teams (for their client only)
- ✅ Create, Read, Update, Delete Transportation Teams (for their client only)
- ✅ Create, Read, Update Rates (manages price changes per entity)
- ✅ Create, Read, Update, Delete Harvests
- ✅ Create, Read, Update Weighings
- ✅ Generate Invoices
- ✅ View, Create, Update Payments
- ✅ View Transactions
- ✅ View Ledger Books
- ✅ View Audit Trails (for their client)
- ✅ View Dashboard
- ✅ Create Operators (create operator accounts for their organization)
- ✅ View all data within their client workspace

**Restrictions**:
- ❌ Cannot manage other clients' data
- ❌ Cannot access system-level settings
- ❌ Cannot create other admins
- ❌ Cannot modify client profile (only Super Admin)
- ❌ Cannot create system users

---

### Role 3: Operator (Client Level)
**Scope**: Records daily operations for their specific client

**Parent Role**: Created by Client Admin

**Permissions**:
- ✅ View Client Details (read-only)
- ✅ View Farmers (read-only)
- ✅ View Cutting Teams (read-only)
- ✅ View Transportation Teams (read-only)
- ✅ View Rates (read-only)
- ✅ Create Harvests (daily inlet records)
- ✅ Update Harvests (own records only)
- ✅ Create Weighings
- ✅ View Transactions
- ✅ View Invoices (read-only)
- ✅ View Payments (read-only)
- ✅ View Ledger Books (read-only)
- ✅ View Audit Trails (read-only)
- ✅ View Dashboard

**Restrictions**:
- ❌ Cannot Create/Edit/Delete Clients, Farmers, Teams, Rates
- ❌ Cannot Delete Harvests
- ❌ Cannot Create/Modify Invoices
- ❌ Cannot Modify Payments
- ❌ Cannot Create/Delete Users or Operators
- ❌ Cannot access data from other clients

---

### User Hierarchy Diagram
```
┌─────────────────────────────────────────────┐
│         Super Admin (Application)           │
│  - Onboards clients                         │
│  - Creates client admin accounts            │
│  - Manages system settings                  │
└─────────────────┬───────────────────────────┘
                  │
                  │ Onboards
                  ↓
┌─────────────────────────────────────────────┐
│    Client 1: Admin (Organization Level)    │
│  - Manages farmers, teams, rates            │
│  - Creates operators                        │
│  - Generates invoices & manages payments    │
└─────────────────┬───────────────────────────┘
         ┌────────┴────────┐
         ↓                 ↓
    ┌─────────┐       ┌─────────┐
    │Operator1│       │Operator2│
    │Records  │       │Records  │
    │Harvests │       │Harvests │
    │& Weights│       │& Weights│
    └─────────┘       └─────────┘
```

### Implementation Notes:
- Use Spring Security with role-based access control
- Annotations for Super Admin: `@PreAuthorize("hasRole('SUPER_ADMIN')")`
- Annotations for Client Admin: `@PreAuthorize("hasRole('CLIENT_ADMIN')")`
- Annotations for Operator: `@PreAuthorize("hasRole('OPERATOR')")`
- Include `clientId` in all endpoints to ensure clients cannot access other clients' data
- Implement tenant isolation: filter all queries by current user's clientId

---

## Business Rules & Validations

### Rule 1: Client Uniqueness
- Client email and tax ID must be unique
- Cannot create duplicate client records

### Rule 2: Farmer Management
- Each farmer must belong to exactly one client
- Farmer email and phone must be valid
- Cannot delete farmer if active invoices/payments exist

### Rule 3: Rate Management
- Rate per ton must be > 0 and must be decimal (4 places)
- New rate must have effectiveFromDate
- Previous rate must be marked inactive when new rate is created
- Cannot have overlapping effective dates for same entity
- Rate history is immutable (cannot edit, only view)

### Rule 4: Harvest Recording
- Harvest quantity must be > 0
- Harvest date cannot be in the future
- Must specify farmer and client
- Cutting team and transportation team are optional but recommended
- Harvest cannot be deleted if weighing records exist

### Rule 5: Weighing Records
- Gross weight > Tare weight (Net weight must be positive)
- Net weight auto-calculated as Gross - Tare
- Weighing date/time cannot be before harvest date
- Must reference a valid harvest
- Cannot create multiple weighings for same harvest at same time

### Rule 6: Transactions
- Transaction must reference valid entity (farmer/cutting team/transportation team)
- Quantity must be > 0
- Price per unit fetched from rate history for transaction date
- Total amount auto-calculated (Quantity × Price)
- Transaction is immutable (cannot edit, only view audit trail)

### Rule 7: Invoice Generation
- Can only generate invoice for transactions with status RECORDED/WEIGHED
- Date range must be valid (fromDate ≤ toDate)
- Must use rates effective on transaction date (historical rates)
- Invoice number must be unique and auto-generated
- Invoice cannot be generated if no transactions exist for entity in date range

### Rule 8: Invoice Recalculation
- Historical rates must be retrieved for each transaction
- If rate changed during invoice period, use correct rate for each transaction date
- Invoice must show line items with quantities and rates as they were

### Rule 9: Payment Processing
- Payment amount must be > 0 and ≤ invoice total
- Payment date cannot be in the future
- Payment mode must be valid (CASH, BANK_TRANSFER, CHEQUE, OTHER)
- Payment can only be made for invoices with status GENERATED/PAID/PARTIALLY_PAID
- Multiple payments allowed for single invoice (partial payments)

### Rule 10: Ledger Book
- Ledger auto-generated monthly (one ledger per client per month)
- Opening balance = closing balance of previous month
- Total receipts = sum of all payment received amounts
- Total payments = sum of all payments made
- Closing balance auto-calculated
- Ledger is read-only once month ends

### Rule 11: Audit Trail
- All changes automatically logged
- Cannot delete audit logs
- Rate changes must include reason
- User who made change is tracked
- Timestamps are immutable

### Rule 12: Data Validation
- Email format validation (RFC 5322)
- Phone number validation (10-15 digits)
- Bank account validation (if provided)
- Currency code must be valid (ISO 4217)
- UUID validation for all IDs

---

## Edge Cases & Special Handling

### Edge Case 1: Rate Change During Invoice Period
**Scenario**: Generate invoice for Jan 1-31. Rate changed on Jan 15.

**Expected Behavior**:
- Transactions Jan 1-14 use old rate
- Transactions Jan 15-31 use new rate
- Invoice line items show correct rates per transaction date

**Implementation**:
```
For each transaction in date range:
  Get rate active on transaction date
  Recalculate amount using historical rate
  Add to invoice line items
```

### Edge Case 2: Partial Payment
**Scenario**: Invoice amount is $10,000. Customer pays $6,000 first, then $4,000 later.

**Expected Behavior**:
- First payment: Invoice status = PARTIALLY_PAID
- Second payment: Invoice status = PAID
- Both payment records linked to same invoice

**Implementation**:
- Track remaining balance per invoice
- Allow multiple payments for same invoice
- Update status based on total paid vs. invoice total

### Edge Case 3: Zero Quantity Harvest
**Scenario**: Operator records harvest with 0 tons.

**Expected Behavior**: Validation error - quantity must be > 0

**Implementation**:
```java
@Min(value = 1, message = "Quantity must be greater than 0")
private BigDecimal quantity;
```

### Edge Case 4: Future Transactions
**Scenario**: User tries to record harvest dated 5 days in future.

**Expected Behavior**: Validation error - cannot record future transactions

**Implementation**:
```java
@PastOrPresent(message = "Harvest date cannot be in future")
private LocalDateTime harvestDate;
```

### Edge Case 5: Negative Net Weight
**Scenario**: Gross weight 100 kg, Tare weight 150 kg (scenario: data entry error).

**Expected Behavior**: Validation error - Gross weight must be ≥ Tare weight

**Implementation**:
```
if (grossWeight < tareWeight) {
    throw new ValidationException("Gross weight must be >= Tare weight");
}
```

### Edge Case 6: Rate Effective Date Overlap
**Scenario**: Old rate effective 2026-01-01 to null. User tries to add new rate 2026-01-15 to null.

**Expected Behavior**: System auto-closes old rate (sets effectiveToDate = 2026-01-14)

**Implementation**:
```
Before saving new rate:
  Find active rate for entity
  If exists: Set old_rate.effectiveToDate = new_rate.effectiveFromDate - 1 day
  Mark old rate as inactive
  Create new rate
```

### Edge Case 7: Delete Entity with Active References
**Scenario**: Admin tries to delete farmer who has unpaid invoices.

**Expected Behavior**: Validation error - cannot delete farmer with active invoices/payments

**Implementation**:
```
Before deletion:
  Check for active invoices/payments/transactions
  If any exist: throw ValidationException
  Else: Perform soft delete
```

### Edge Case 8: Duplicate Invoice Generation
**Scenario**: User clicks "Generate Invoice" twice for same entity/date range.

**Expected Behavior**: System returns existing invoice or validates no duplicate

**Implementation**:
```
Check existing invoices for:
  Same entity
  Same date range
  Status not CANCELLED
  If exists: return error or return existing invoice
```

### Edge Case 9: Ledger with No Transactions
**Scenario**: Client added but no harvests recorded in a month.

**Expected Behavior**: Ledger shows opening balance, 0 receipts, 0 payments, closing = opening

**Implementation**:
- Still create ledger for the month
- Show $0 for receipts/payments
- Closing balance = opening balance

### Edge Case 10: Concurrent Modifications
**Scenario**: Two admins editing same rate/harvest simultaneously.

**Expected Behavior**: Last write wins (optimistic locking)

**Implementation**:
```java
@Version
private Long version;  // For optimistic locking
```

---

## Non-Functional Requirements

### Performance
- API response time < 500ms for read operations
- Batch invoice generation < 2s for 1000 transactions
- Page size: 50 records (configurable)
- Implement pagination for all list endpoints

### Scalability
- Support up to 100,000 clients
- Support up to 1,000,000 transactions
- Support up to 10,000 farmers per client

### Reliability
- Data backup daily
- Audit trail immutable
- No data loss on payment/invoice creation
- Transaction rollback on error

### Security
- HTTPS only
- JWT authentication tokens (30 min expiry)
- Refresh tokens (7 days)
- Password encryption (BCrypt)
- SQL injection prevention (use parameterized queries)
- OWASP Top 10 compliance

### Usability
- Responsive UI (web + mobile)
- Search/filter on all list pages
- Bulk operations (CSV import/export)
- PDF invoice download

### Maintainability
- Code coverage > 80%
- API documentation (Swagger/OpenAPI)
- Database migrations (Flyway)
- Logging (Slf4j)
- Error messages in user-friendly format

### Compliance
- Audit trail for all data changes
- Rate change history with reason
- Invoice retention for 7 years
- Payment records immutable
- Data privacy (GDPR compliance)

