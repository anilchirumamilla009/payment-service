# Visual Guide - Subabul Trade Book Application Architecture

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     SUBABUL TRADE BOOK APPLICATION                          │
│                    Multi-Tenant SaaS Architecture                           │
└─────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────┐
│                          APPLICATION LAYER                                  │
│                   (Super Admin Dashboard & Controls)                         │
├──────────────────────────────────────────────────────────────────────────────┤
│  • Onboard New Clients                                                       │
│  • Create Client Admin Accounts                                              │
│  • Manage System Settings                                                    │
│  • View System-Wide Audit Trails                                             │
│  • Monitor Application Health                                                │
└───────┬─────────────────────────────────────────────────────────────┬────────┘
        │                                                             │
        │                                                             │
        ↓                                                             ↓
┌──────────────────────────────┐                         ┌──────────────────────────────┐
│   CLIENT 1 WORKSPACE         │                         │   CLIENT 2 WORKSPACE         │
│   (Independent Instance)     │                         │   (Independent Instance)     │
├──────────────────────────────┤                         ├──────────────────────────────┤
│ Admin: Ravi Kumar            │                         │ Admin: Arjun Patel           │
│                              │                         │                              │
│ ┌──────────────────────────┐ │                         │ ┌──────────────────────────┐ │
│ │    Admin Dashboard       │ │                         │ │    Admin Dashboard       │ │
│ │ • Manage Farmers         │ │                         │ │ • Manage Farmers         │ │
│ │ • Manage Teams           │ │                         │ │ • Manage Teams           │ │
│ │ • Set Rates              │ │                         │ │ • Set Rates              │ │
│ │ • Create Invoices        │ │                         │ │ • Create Invoices        │ │
│ │ • Record Payments        │ │                         │ │ • Record Payments        │ │
│ │ • Create Operators       │ │                         │ │ • Create Operators       │ │
│ └─────────┬────────────────┘ │                         │ └─────────┬────────────────┘ │
│           │                  │                         │           │                  │
│      ┌────┴────┐             │                         │      ┌────┴────┐             │
│      ↓         ↓             │                         │      ↓         ↓             │
│  ┌────────┐ ┌────────┐       │                         │  ┌────────┐ ┌────────┐       │
│  │Operator│ │Operator│  ... │                         │  │Operator│ │Operator│  ... │
│  │(Field) │ │(Office)│       │                         │  │(Field) │ │(Office)│       │
│  └────────┘ └────────┘       │                         │  └────────┘ └────────┘       │
│                              │                         │                              │
│  Data:                       │                         │  Data:                       │
│  • Farmer1, Farmer2, ...     │                         │  • FarmerA, FarmerB, ...     │
│  • Transactions              │                         │  • Transactions              │
│  • Invoices                  │                         │  • Invoices                  │
│  • Payments                  │                         │  • Payments                  │
│  • Ledger                    │                         │  • Ledger                    │
└──────────────────────────────┘                         └──────────────────────────────┘
        (Complete Data Isolation)                                (Complete Data Isolation)
```

---

## User Role Hierarchy

```
                          ┌─────────────────────┐
                          │   SUPER_ADMIN       │
                          │ (System Maintainer) │
                          │                     │
                          │ • Onboard Clients   │
                          │ • Create Admins     │
                          │ • System Settings   │
                          └──────────┬──────────┘
                                     │
                    ┌────────────────┼────────────────┐
                    │                │                │
                    ↓                ↓                ↓
            ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
            │ CLIENT_ADMIN │ │ CLIENT_ADMIN  │ │ CLIENT_ADMIN │
            │ (Client 1)   │ │ (Client 2)    │ │ (Client N)   │
            │              │ │               │ │              │
            │ Scope:       │ │ Scope:        │ │ Scope:       │
            │ Client 1     │ │ Client 2      │ │ Client N     │
            │ Data Only    │ │ Data Only     │ │ Data Only    │
            └──────┬───────┘ └──────┬────────┘ └──────┬───────┘
                   │                │                │
          ┌────────┴────────┐       │        ┌───────┴──────────┐
          ↓                 ↓       ↓        ↓                  ↓
      ┌────────┐       ┌────────┐ ┌────────┐ ┌────────┐    ┌────────┐
      │Operator│       │Operator│ │Operator│ │Operator│    │Operator│
      │(Client1)       │(Client1) │(Client2)│ │(Client2│    │(ClientN)
      │                │        │ │        │ │        │    │        │
      │ Can:           │        │ │ Can:   │ │ Can:   │    │ Can:   │
      │ • Record       │        │ │ • View │ │ • View │    │ • View │
      │   Harvests     │        │ │ • View │ │ • View │    │ • View │
      │ • Record       │        │ │ • View │ │ • View │    │ • View │
      │   Weighing     │        │ │ • View │ │ • View │    │ • View │
      │ • View Data    │        │ │        │ │        │    │        │
      └────────┘       └────────┘ └────────┘ └────────┘    └────────┘
        (Scope: Client 1 Data Only)
```

---

## Request Flow with Multi-Tenancy

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           API REQUEST FLOW                                  │
└─────────────────────────────────────────────────────────────────────────────┘

1. USER LOGIN
   ┌──────────────┐
   │   User       │
   │  username    │
   │  password    │
   └──────┬───────┘
          ↓
   ┌──────────────────────────┐
   │  POST /auth/login        │
   │  - Validate credentials  │
   │  - Check role            │
   │  - Extract clientId      │
   └──────┬───────────────────┘
          ↓
   ┌──────────────────────────┐
   │  JWT Token Generated     │
   │  {                       │
   │    sub: userId,          │
   │    role: CLIENT_ADMIN,   │
   │    clientId: 123-xyz,    │
   │    exp: timestamp        │
   │  }                       │
   └──────┬───────────────────┘
          ↓
   ┌──────────────────────────┐
   │  Token Returned to User  │
   └──────────────────────────┘

2. AUTHENTICATED REQUEST
   ┌──────────────────────────┐
   │   GET /clients/123-xyz/  │
   │   /farmers               │
   │                          │
   │   Header:                │
   │   Authorization: Bearer  │
   │   {token}                │
   └──────┬───────────────────┘
          ↓
   ┌──────────────────────────────────────┐
   │  JwtAuthenticationFilter             │
   │  - Extract token from header         │
   │  - Validate token signature          │
   │  - Extract userId, role, clientId    │
   │  - Store in SecurityContext          │
   └──────┬───────────────────────────────┘
          ↓
   ┌──────────────────────────────────────┐
   │  @PreAuthorize Check                 │
   │  - Verify role: hasRole('CLIENT_ADMIN')  │
   │  - Verify tenant: clientId == 123-xyz    │
   │  - Allow or deny                     │
   └──────┬───────────────────────────────┘
          ↓
   ┌──────────────────────────────────────┐
   │  Service Layer                       │
   │  - Extract current user's clientId   │
   │  - Query: SELECT * FROM farmers      │
   │           WHERE client_id = 123-xyz  │
   │  - Only returns Client 1's farmers   │
   └──────┬───────────────────────────────┘
          ↓
   ┌──────────────────────────────────────┐
   │  Response: 200 OK                    │
   │  [                                   │
   │    { farmerId: 1, name: Farmer1 },   │
   │    { farmerId: 2, name: Farmer2 }    │
   │  ]                                   │
   └──────────────────────────────────────┘

3. UNAUTHORIZED REQUEST (Cross-Tenant Attack)
   ┌──────────────────────────────────────┐
   │   GET /clients/999-abc/              │
   │   /farmers                           │
   │   (Client 1 trying to access Client 2)  │
   │                                      │
   │   Header:                            │
   │   Authorization: Bearer              │
   │   {token with clientId: 123-xyz}     │
   └──────┬───────────────────────────────┘
          ↓
   ┌──────────────────────────────────────┐
   │  @PreAuthorize Check                 │
   │  - clientId from token: 123-xyz      │
   │  - clientId in URL: 999-abc          │
   │  - MISMATCH!                         │
   │  - DENY ACCESS                       │
   └──────┬───────────────────────────────┘
          ↓
   ┌──────────────────────────────────────┐
   │  Response: 403 Forbidden             │
   │  {                                   │
   │    error: "Access Denied",           │
   │    message: "Cannot access other     │
   │    client's data"                    │
   │  }                                   │
   └──────────────────────────────────────┘
```

---

## Client Onboarding Workflow

```
┌─────────────────────────────────────────────────────────────────┐
│            CLIENT ONBOARDING PROCESS (Super Admin)              │
└─────────────────────────────────────────────────────────────────┘

STEP 1: NEW CLIENT REQUESTS ACCESS
┌─────────────────┐
│ New Business    │
│ "Fresh Harvest" │
│ Contact Info    │
└────────┬────────┘
         ↓
STEP 2: SUPER ADMIN ONBOARDS
┌──────────────────────────────────────────┐
│ POST /api/v1/super-admin/clients         │
│ {                                        │
│   clientName: "Fresh Harvest Ltd",       │
│   email: "admin@freshharvest.com",       │
│   businessType: "AGRICULTURAL",          │
│   taxId: "TAX123456",                    │
│   address: "...",                        │
│   ...                                    │
│ }                                        │
└────────┬─────────────────────────────────┘
         ↓
STEP 3: SYSTEM CREATES
┌────────────────────────────────────────────────┐
│ • Client Record (clientId: xyz-123)            │
│ • Empty Trade Book                             │
│ • Empty Ledger Book                            │
│ • Audit Log Entry                              │
└────────┬───────────────────────────────────────┘
         ↓
STEP 4: SUPER ADMIN CREATES ADMIN ACCOUNT
┌──────────────────────────────────────────────┐
│ POST /api/v1/super-admin/clients/xyz-123/    │
│      admin-user                              │
│ {                                            │
│   username: "ravi.kumar",                    │
│   email: "ravi@freshharvest.com"             │
│ }                                            │
└────────┬───────────────────────────────────────┘
         ↓
STEP 5: SYSTEM CREATES
┌────────────────────────────────────────────┐
│ • User Record                              │
│   - username: ravi.kumar                   │
│   - role: CLIENT_ADMIN                     │
│   - clientId: xyz-123                      │
│   - passwordHash: bcrypt(temp_password)    │
│   - status: ACTIVE                         │
└────────┬───────────────────────────────────┘
         ↓
STEP 6: SEND CREDENTIALS TO CLIENT
┌──────────────────────────────────────┐
│ Email: ravi@freshharvest.com        │
│ Subject: Your Fresh Harvest Account  │
│                                      │
│ Username: ravi.kumar                 │
│ Temporary Password: Temp@123!        │
│ Login URL: https://app.../login      │
│ Please change password on first login│
└──────────┬───────────────────────────┘
           ↓
STEP 7: CLIENT ADMIN LOGS IN
┌─────────────────────────────────────┐
│ POST /api/v1/auth/login             │
│ {                                   │
│   username: "ravi.kumar",           │
│   password: "Temp@123!"             │
│ }                                   │
└────────┬────────────────────────────┘
         ↓
STEP 8: FORCE PASSWORD CHANGE
┌────────────────────────────────────────┐
│ PUT /api/v1/auth/change-password       │
│ {                                      │
│   oldPassword: "Temp@123!",            │
│   newPassword: "MySecret@2024"         │
│ }                                      │
└────────┬───────────────────────────────┘
         ↓
STEP 9: CLIENT ADMIN DASHBOARD
┌─────────────────────────────────────────────┐
│ Welcome, Ravi Kumar                         │
│ Your Dashboard:                             │
│ ┌─────────────────────────────────────────┐ │
│ │ Quick Actions                           │ │
│ │ • Add Farmer                            │ │
│ │ • Add Cutting Team                      │ │
│ │ • Add Transportation Team               │ │
│ │ • Create Operator                       │ │
│ │ • Manage Rates                          │ │
│ │ • Record Harvest (via operators)        │ │
│ │ • Generate Invoices                     │ │
│ │ • Record Payments                       │ │
│ └─────────────────────────────────────────┘ │
└─────────────────────────────────────────────┘

COMPLETED: Client is now ready to use the system!
```

---

## Farmer Addition Workflow (by Client Admin)

```
┌─────────────────────────────────────────────────────────┐
│    FARMER ADDITION PROCESS (Client Admin)               │
└─────────────────────────────────────────────────────────┘

STEP 1: CLIENT ADMIN ADDS FARMER
┌──────────────────────────────────────────┐
│ POST /api/v1/clients/xyz-123/farmers     │
│ {                                        │
│   farmerName: "Rajesh Kumar",            │
│   email: "rajesh@farm.com",              │
│   phoneNumber: "9876543210",             │
│   address: "Village A, District B",      │
│   bankAccountNumber: "123456789",        │
│   bankName: "HDFC Bank",                 │
│   ifscCode: "HDFC0001",                  │
│   status: "ACTIVE"                       │
│ }                                        │
│                                          │
│ Authorization Header:                    │
│ Authorization: Bearer {jwt_token         │
│   with clientId: xyz-123}                │
└────────┬─────────────────────────────────┘
         ↓
STEP 2: SYSTEM VALIDATES
┌──────────────────────────────────────────┐
│ ✓ Is user CLIENT_ADMIN? YES              │
│ ✓ Is clientId in token == xyz-123? YES   │
│ ✓ All required fields present? YES       │
│ ✓ Email format valid? YES                │
│ ✓ Phone valid? YES                       │
│ ✓ Farmer with same email already exists? NO │
└────────┬─────────────────────────────────┘
         ↓
STEP 3: CREATE FARMER RECORD
┌──────────────────────────────────────────┐
│ INSERT INTO farmers (                    │
│   farmer_id,                             │
│   client_id = xyz-123,                   │
│   farmer_name = "Rajesh Kumar",          │
│   email = "rajesh@farm.com",             │
│   ...                                    │
│ )                                        │
└────────┬─────────────────────────────────┘
         ↓
STEP 4: CLIENT ADMIN SETS RATE
┌──────────────────────────────────────────┐
│ POST /api/v1/rates                       │
│ {                                        │
│   entityType: "FARMER",                  │
│   entityId: farmer_id_123,               │
│   clientId: xyz-123,                     │
│   pricePerTon: 5000.00,                  │
│   currency: "INR",                       │
│   effectiveFromDate: "2026-04-14",       │
│   effectiveToDate: null,                 │
│   rateChangeReason: "Initial rate"       │
│ }                                        │
└────────┬─────────────────────────────────┘
         ↓
STEP 5: SYSTEM CREATES RATE HISTORY
┌──────────────────────────────────────────────┐
│ INSERT INTO rate_history (                   │
│   entity_type = "FARMER",                    │
│   entity_id = farmer_id_123,                 │
│   client_id = xyz-123,                       │
│   price_per_ton = 5000.00,                   │
│   is_active = true,                          │
│   effective_from_date = "2026-04-14",        │
│   effective_to_date = null                   │
│ )                                            │
└────────┬───────────────────────────────────┘
         ↓
STEP 6: SUCCESS RESPONSE
┌────────────────────────────────────────────┐
│ 201 Created                                │
│ {                                          │
│   farmerId: "farmer_id_123",               │
│   farmerName: "Rajesh Kumar",              │
│   email: "rajesh@farm.com",                │
│   status: "ACTIVE",                        │
│   ratePerTon: 5000.00,                     │
│   effectiveFromDate: "2026-04-14",         │
│   message: "Farmer added successfully"    │
│ }                                          │
└────────────────────────────────────────────┘

NOW OPERATORS CAN RECORD HARVESTS FROM THIS FARMER!
```

---

## Operator Creating Harvest

```
┌──────────────────────────────────────────────────────┐
│    HARVEST RECORDING (Operator)                      │
└──────────────────────────────────────────────────────┘

OPERATOR DASHBOARD
┌───────────────────────────────────────────────────────┐
│ Welcome, Prakash (Operator @ Fresh Harvest)           │
│                                                       │
│ Today's Farmers: [Rajesh Kumar, Sharma, Patel]       │
│                                                       │
│ Record Harvest Button                                │
└────────────────┬────────────────────────────────────┘
                 ↓
STEP 1: OPERATOR FILLS HARVEST FORM
┌──────────────────────────────────────────┐
│ POST /api/v1/clients/xyz-123/harvests    │
│ {                                        │
│   farmerId: "farmer_id_123",             │
│   harvestDate: "2026-04-14T10:30:00",    │
│   quantity: 50.5,                        │
│   cuttingTeamId: "team_cut_001",         │
│   transportationTeamId: "team_trans_001",│
│   notes: "Good quality crop"             │
│ }                                        │
│                                          │
│ Authorization Header:                    │
│ Bearer {token with clientId: xyz-123}    │
└────────┬─────────────────────────────────┘
         ↓
STEP 2: SYSTEM VALIDATES
┌──────────────────────────────────────────┐
│ ✓ Is user OPERATOR? YES                  │
│ ✓ clientId in token == xyz-123? YES      │
│ ✓ Farmer exists for this client? YES     │
│ ✓ Quantity > 0? YES                      │
│ ✓ Date not in future? YES                │
└────────┬─────────────────────────────────┘
         ↓
STEP 3: GET CURRENT RATE FOR FARMER
┌─────────────────────────────────────────────────┐
│ SELECT price_per_ton FROM rate_history          │
│ WHERE entity_id = farmer_id_123                 │
│ AND entity_type = 'FARMER'                      │
│ AND client_id = xyz-123                         │
│ AND is_active = true                            │
│ AND effective_from_date <= NOW()               │
│                                                 │
│ Result: 5000.00 INR per ton                     │
└────────┬────────────────────────────────────────┘
         ↓
STEP 4: CREATE HARVEST RECORD
┌──────────────────────────────────────────┐
│ INSERT INTO harvests (                   │
│   harvest_id = 'harv_001',               │
│   client_id = xyz-123,                   │
│   farmer_id = farmer_id_123,             │
│   harvest_date = '2026-04-14T10:30',     │
│   quantity = 50.5,                       │
│   status = 'RECORDED'                    │
│ )                                        │
└────────┬─────────────────────────────────┘
         ↓
STEP 5: CREATE TRANSACTION RECORD
┌──────────────────────────────────────────┐
│ INSERT INTO transactions (               │
│   transaction_id = 'txn_001',            │
│   client_id = xyz-123,                   │
│   transaction_type = 'FARMER_PURCHASE',  │
│   entity_id = farmer_id_123,             │
│   harvest_id = harv_001,                 │
│   quantity = 50.5,                       │
│   price_per_unit = 5000.00,              │
│   total_amount = 252500.00,              │
│   status = 'RECORDED'                    │
│ )                                        │
│                                          │
│ Calculation: 50.5 × 5000.00 = 252,500   │
└────────┬─────────────────────────────────┘
         ↓
STEP 6: SUCCESS RESPONSE
┌──────────────────────────────────────────┐
│ 201 Created                              │
│ {                                        │
│   harvestId: "harv_001",                 │
│   farmerId: "farmer_id_123",             │
│   farmerName: "Rajesh Kumar",            │
│   harvestDate: "2026-04-14T10:30",       │
│   quantity: 50.5,                        │
│   transactionId: "txn_001",              │
│   totalAmount: 252500.00,                │
│   status: "RECORDED",                    │
│   message: "Harvest recorded successfully" │
│ }                                        │
└──────────────────────────────────────────┘

HARVEST RECORDED! Next, Operator will Record Weighing...
```

---

## Invoicing with Historical Rates

```
┌──────────────────────────────────────────────────────┐
│    INVOICE GENERATION (with Historical Rates)        │
└──────────────────────────────────────────────────────┘

SCENARIO: Rate changed during invoice period
┌───────────────────────────────────────────────────────┐
│ April 1-10: Rate = 5000/ton (Farmer Rajesh)          │
│ April 10:   Rate changed to 5500/ton                 │
│ April 11-30: Rate = 5500/ton                         │
│                                                       │
│ Generate invoice for April 1-30                      │
└───────────────────────────────────────────────────────┘

STEP 1: CLIENT ADMIN REQUESTS INVOICE
┌──────────────────────────────────────────┐
│ POST /api/v1/clients/xyz-123/invoices/   │
│      generate                            │
│ {                                        │
│   invoiceType: "FARMER_INVOICE",         │
│   entityId: "farmer_id_123",             │
│   fromDate: "2026-04-01",                │
│   toDate: "2026-04-30"                   │
│ }                                        │
└────────┬─────────────────────────────────┘
         ↓
STEP 2: FETCH TRANSACTIONS IN PERIOD
┌─────────────────────────────────────────────────┐
│ SELECT * FROM transactions                      │
│ WHERE entity_id = farmer_id_123                 │
│ AND client_id = xyz-123                         │
│ AND transaction_date BETWEEN '2026-04-01'      │
│   AND '2026-04-30'                             │
│ AND status IN ('RECORDED', 'WEIGHED')          │
│                                                 │
│ Results:                                        │
│ • Txn 001: Apr 5, 50 tons                       │
│ • Txn 002: Apr 8, 30 tons                       │
│ • Txn 003: Apr 15, 40 tons                      │
│ • Txn 004: Apr 20, 25 tons                      │
└────────┬────────────────────────────────────────┘
         ↓
STEP 3: FOR EACH TRANSACTION, GET HISTORICAL RATE
┌──────────────────────────────────────────────────────┐
│ For Txn 001 (Apr 5):                                │
│   SELECT price_per_ton FROM rate_history           │
│   WHERE entity_id = farmer_id_123                  │
│   AND transaction_date >= effective_from_date      │
│   AND (effective_to_date IS NULL                  │
│         OR transaction_date <= effective_to_date)  │
│   Result: 5000.00/ton                              │
│                                                    │
│ For Txn 002 (Apr 8):                               │
│   Result: 5000.00/ton                              │
│                                                    │
│ For Txn 003 (Apr 15):                              │
│   Result: 5500.00/ton  (rate changed on Apr 10)    │
│                                                    │
│ For Txn 004 (Apr 20):                              │
│   Result: 5500.00/ton                              │
└────────┬───────────────────────────────────────────┘
         ↓
STEP 4: CREATE INVOICE LINE ITEMS
┌─────────────────────────────────────────┐
│ Line Item 1:                            │
│   Date: Apr 5, Qty: 50, Rate: 5000      │
│   Amount: 250,000                       │
│                                         │
│ Line Item 2:                            │
│   Date: Apr 8, Qty: 30, Rate: 5000      │
│   Amount: 150,000                       │
│                                         │
│ Line Item 3:                            │
│   Date: Apr 15, Qty: 40, Rate: 5500     │
│   Amount: 220,000                       │
│                                         │
│ Line Item 4:                            │
│   Date: Apr 20, Qty: 25, Rate: 5500     │
│   Amount: 137,500                       │
│                                         │
│ Subtotal: 757,500                       │
└────────┬──────────────────────────────────┘
         ↓
STEP 5: CREATE INVOICE
┌──────────────────────────────────────────┐
│ INSERT INTO invoices (                   │
│   invoice_number = 'INV-2026-0001',      │
│   invoice_type = 'FARMER_INVOICE',       │
│   entity_id = farmer_id_123,             │
│   client_id = xyz-123,                   │
│   from_date = '2026-04-01',              │
│   to_date = '2026-04-30',                │
│   subtotal = 757500,                     │
│   tax_amount = 0,                        │
│   total_amount = 757500,                 │
│   status = 'DRAFT'                       │
│ )                                        │
│                                          │
│ INSERT INTO invoice_line_items (4 rows)  │
└────────┬─────────────────────────────────┘
         ↓
STEP 6: GENERATE PDF
┌────────────────────────────────────────────┐
│ INVOICE INV-2026-0001                      │
│ ────────────────────────────────────────── │
│ Fresh Harvest Ltd                          │
│ Farmer: Rajesh Kumar                       │
│ Period: Apr 1-30, 2026                     │
│                                            │
│ Date    Qty    Rate      Amount             │
│ Apr 5   50     5000      250,000           │
│ Apr 8   30     5000      150,000           │
│ Apr 15  40     5500      220,000           │
│ Apr 20  25     5500      137,500           │
│ ────────────────────────────────────────── │
│ TOTAL                    757,500 INR       │
│ ────────────────────────────────────────── │
│                                            │
│ PDF saved to: /invoices/INV-2026-0001.pdf │
└────────┬───────────────────────────────────┘
         ↓
STEP 7: UPDATE INVOICE STATUS
┌────────────────────────────────┐
│ Invoice status: DRAFT → GENERATED │
│                                │
│ Response: 201 Created          │
│ {                              │
│   invoiceId: "inv_001",        │
│   invoiceNumber: "INV-2026-0001", │
│   status: "GENERATED",         │
│   totalAmount: 757500.00,      │
│   pdfPath: "/invoices/...",    │
│   message: "Invoice generated" │
│ }                              │
└────────────────────────────────┘

INVOICE READY FOR DOWNLOAD AND SHARING WITH FARMER!
Each transaction used the rate active on that date.
```

---

## Data Isolation Security

```
┌─────────────────────────────────────────────────────────┐
│         TENANT ISOLATION SECURITY LAYERS                │
└─────────────────────────────────────────────────────────┘

LAYER 1: JWT TOKEN AUTHENTICATION
┌─────────────────────────────────────┐
│ Token Contains:                     │
│ • userId                            │
│ • role (SUPER_ADMIN, CLIENT_ADMIN,  │
│         OPERATOR)                   │
│ • clientId (NULL for SUPER_ADMIN)   │
│ • expiry                            │
└─────────────────────────────────────┘

LAYER 2: @PreAuthorize ROLE CHECK
┌──────────────────────────────────────────────┐
│ @PreAuthorize("hasRole('CLIENT_ADMIN')")     │
│ public ResponseEntity addFarmer(...) { }     │
│                                              │
│ Checks:                                      │
│ ✓ User has CLIENT_ADMIN role?               │
│ ✓ If YES → proceed to Layer 3               │
│ ✓ If NO → return 403 Forbidden              │
└──────────────────────────────────────────────┘

LAYER 3: URL PARAMETER VALIDATION
┌──────────────────────────────────────────────┐
│ @PreAuthorize("#clientId ==                  │
│   authentication.principal.clientId")        │
│                                              │
│ Checks:                                      │
│ ✓ clientId in URL matches token clientId?   │
│ ✓ If YES → proceed to Layer 4               │
│ ✓ If NO → return 403 Forbidden              │
│                                              │
│ Example:                                     │
│ POST /clients/xyz-123/farmers               │
│ Token.clientId = xyz-123  ✓ ALLOWED         │
│ Token.clientId = abc-789  ✗ DENIED          │
└──────────────────────────────────────────────┘

LAYER 4: QUERY FILTERING
┌──────────────────────────────────────────────┐
│ Service Layer Method:                        │
│ public List<Farmer> getFarmers() {           │
│   UUID currentClientId =                     │
│     SecurityContextHolder.getContext()       │
│     .getAuthentication()                     │
│     .getPrincipal().getClientId();           │
│   return farmerRepository                    │
│     .findByClientId(currentClientId);        │
│ }                                            │
│                                              │
│ SQL Executed:                                │
│ SELECT * FROM farmers                        │
│ WHERE client_id = <currentClientId>          │
│                                              │
│ Result: Only farmers for this client         │
│ Even if database had other clients'          │
│ data, this query wouldn't return it          │
└──────────────────────────────────────────────┘

EXAMPLE ATTACK SCENARIOS & PROTECTION

Scenario 1: Operator tries to access other client's farmers
┌──────────────────────────────────────────────────────────┐
│ URL: GET /clients/OTHER-CLIENT-ID/farmers               │
│ Token: Bearer {clientId: MY-CLIENT-ID, role: OPERATOR}  │
│                                                          │
│ Layer 3 Check:                                           │
│ OTHER-CLIENT-ID != MY-CLIENT-ID                         │
│ → 403 FORBIDDEN                                          │
│                                                          │
│ Hacker never reaches database!                          │
└──────────────────────────────────────────────────────────┘

Scenario 2: Token is stolen/forged
┌──────────────────────────────────────────────────────────┐
│ Layer 1: Token signature verification                   │
│ If token tampered with:                                 │
│ → 401 UNAUTHORIZED                                      │
│                                                          │
│ If expiry passed:                                       │
│ → 401 UNAUTHORIZED (request refresh)                    │
│                                                          │
│ Attack blocked immediately!                             │
└──────────────────────────────────────────────────────────┘

Scenario 3: SQL Injection attempt
┌──────────────────────────────────────────────────────────┐
│ Attack URL:                                              │
│ GET /clients/'; DROP TABLE farmers; --/farmers          │
│                                                          │
│ Layer 3 Check FIRST:                                    │
│ '; DROP TABLE farmers; -- != MY-CLIENT-ID              │
│ → 403 FORBIDDEN                                         │
│                                                          │
│ Additionally, we use parameterized queries:             │
│ .findByClientId(currentClientId)                        │
│ So even if Layer 3 failed, SQL injection is prevented   │
└──────────────────────────────────────────────────────────┘

4 LAYERS OF DEFENSE = SECURE MULTI-TENANCY
```

---

## Database Multi-Tenancy Pattern

```
┌──────────────────────────────────────────────────────────┐
│      SINGLE-DATABASE MULTI-TENANT SCHEMA                │
└──────────────────────────────────────────────────────────┘

USERS TABLE (Stores credentials + tenant assignment)
┌─────────────────────────────────────────────┐
│ user_id │ username│ role          │ clientId│
├─────────┼─────────┼───────────────┼─────────┤
│ U1      │ ravi    │ CLIENT_ADMIN   │ C1     │
│ U2      │ prakash │ OPERATOR       │ C1     │
│ U3      │ arjun   │ CLIENT_ADMIN   │ C2     │
│ U4      │ karthik │ OPERATOR       │ C2     │
│ U5      │ admin   │ SUPER_ADMIN    │ NULL   │
└─────────┴─────────┴───────────────┴─────────┘

FARMERS TABLE (Scoped by clientId)
┌───────────┬──────────┬──────────┐
│ farmer_id │ clientId │ name     │
├───────────┼──────────┼──────────┤
│ F1        │ C1       │ Rajesh   │  } Client 1
│ F2        │ C1       │ Sharma   │  } data
│ F3        │ C2       │ FarmerA  │  } only
│ F4        │ C2       │ FarmerB  │  }
└───────────┴──────────┴──────────┘

TRANSACTIONS TABLE (Scoped by clientId)
┌────────────┬──────────┬────────┐
│ txn_id     │ clientId │ amount │
├────────────┼──────────┼────────┤
│ TXN1       │ C1       │ 252500 │  } Client 1
│ TXN2       │ C1       │ 150000 │  } transactions
│ TXN3       │ C2       │ 300000 │  } only
│ TXN4       │ C2       │ 200000 │  }
└────────────┴──────────┴────────┘

INVOICES TABLE (Scoped by clientId)
┌──────────┬──────────┬─────────────┐
│ invoice_id │ clientId │ total_amount│
├──────────┼──────────┼─────────────┤
│ INV1     │ C1       │ 757500      │  } Client 1
│ INV2     │ C1       │ 450000      │  } invoices
│ INV3     │ C2       │ 500000      │  } only
│ INV4     │ C2       │ 350000      │  }
└──────────┴──────────┴─────────────┘

RATE_HISTORY TABLE (Scoped by clientId)
┌─────┬──────────┬────────┬──────┐
│ id  │ clientId │ entity_id │ rate │
├─────┼──────────┼────────┼──────┤
│ R1  │ C1       │ F1     │ 5000 │  } Client 1
│ R2  │ C1       │ F1     │ 5500 │  } rates
│ R3  │ C2       │ F3     │ 6000 │  } only
│ R4  │ C2       │ F3     │ 6200 │  }
└─────┴──────────┴────────┴──────┘

KEY PRINCIPLE:
Every table has clientId column
Every query filters: WHERE clientId = :currentClientId
Every user is tied to a clientId
= COMPLETE DATA ISOLATION
```

---

This visual guide demonstrates the complete multi-tenant architecture with security layers, workflows, and data isolation patterns.

