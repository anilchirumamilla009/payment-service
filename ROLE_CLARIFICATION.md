# Role Clarification Summary - Subabul Trade Book Application

## Quick Reference: Who Does What

### Scenario 1: New Client Wants to Use the System

**Who**: Super Admin (Application Maintainer)

**What They Do**:
1. Create new Client record in the application
2. Create initial **Client Admin** account
3. Send login credentials to the client
4. Client Admin can now manage their organization's data

**Database Role**: `SUPER_ADMIN`
**Example Endpoints**: `/api/v1/super-admin/clients` (create, read, update, delete clients)

---

### Scenario 2: Client Has Onboarded and Now Wants to Add Their Farmers

**Who**: Client Admin (Admin for their specific client)

**What They Do**:
1. Log in with credentials provided by Super Admin
2. Add farmers to their account (one by one or bulk import)
3. Set initial rate per ton for each farmer
4. Create operator accounts for their staff (e.g., field workers, office staff)
5. Manage cutting teams and transportation teams
6. Create invoices and record payments

**Database Role**: `CLIENT_ADMIN` (assigned to a specific `clientId`)
**Example Endpoints**: `/api/v1/clients/{clientId}/farmers` (create, read, update, delete)

**Permissions**:
- ✅ Full control of farmers, teams, rates, invoices, payments
- ✅ Can create and manage operators
- ❌ Cannot see other clients' data
- ❌ Cannot modify the client profile (only Super Admin)

---

### Scenario 3: Client Admin Wants to Add Staff to Record Daily Work

**Who**: Client Admin (creates accounts), Operators (use accounts)

**What Client Admin Does**:
1. Create operator account with username/email
2. Set temporary password
3. Send credentials to operator
4. Operator logs in and changes password

**What Operator Does**:
1. Records daily harvest details (date, farmer, quantity, teams)
2. Records weighing information (gross weight, tare weight)
3. Views transactions and invoices
4. Marks tasks as complete

**Database Role for Operator**: `OPERATOR` (assigned to a specific `clientId`)
**Example Endpoints**: 
- Client Admin: `/api/v1/clients/{clientId}/operators` (create, read, update, delete)
- Operator: `/api/v1/clients/{clientId}/harvests` (create), `/api/v1/clients/{clientId}/weighings` (create)

**Permissions**:
- ✅ Can record daily operations (harvests, weighings)
- ✅ Can view all data
- ❌ Cannot modify farmer/team rates
- ❌ Cannot create invoices
- ❌ Cannot see other clients' data

---

## Role Hierarchy

```
┌─────────────────────────────────────────────────────────────────┐
│ SUPER_ADMIN (Application Level)                                 │
│ ├─ Onboards new clients                                          │
│ ├─ Creates initial client admin accounts                         │
│ ├─ Manages system-wide settings                                  │
│ └─ Views audit trails across all clients                         │
└─────────────────────────────────────────────────────────────────┘
                            │
         ┌──────────────────┼──────────────────┐
         ↓                  ↓                  ↓
   ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
   │ CLIENT_ADMIN │  │ CLIENT_ADMIN  │  │ CLIENT_ADMIN │
   │ (Client 1)   │  │ (Client 2)    │  │ (Client N)   │
   │              │  │               │  │              │
   │ ├─ Farmers   │  │ ├─ Farmers    │  │ ├─ Farmers   │
   │ ├─ Teams     │  │ ├─ Teams      │  │ ├─ Teams     │
   │ ├─ Rates     │  │ ├─ Rates      │  │ ├─ Rates     │
   │ ├─ Invoices  │  │ ├─ Invoices   │  │ ├─ Invoices  │
   │ └─ Operators │  │ └─ Operators  │  │ └─ Operators │
   └────┬─────────┘  └────┬──────────┘  └────┬─────────┘
        │                 │                   │
        ├─ OPERATOR       ├─ OPERATOR        ├─ OPERATOR
        ├─ OPERATOR       ├─ OPERATOR        └─ OPERATOR
        └─ OPERATOR       └─ OPERATOR
```

---

## Key Differences: Admin vs Operator

| Feature | Client Admin | Operator |
|---------|--------------|----------|
| **Can Create Clients** | ❌ No | ❌ No |
| **Can Add Farmers** | ✅ Yes | ❌ No (read-only) |
| **Can Set Rates** | ✅ Yes | ❌ No (read-only) |
| **Can Record Harvests** | ✅ Yes | ✅ Yes |
| **Can Record Weighing** | ✅ Yes | ✅ Yes |
| **Can Create Invoices** | ✅ Yes | ❌ No (read-only) |
| **Can Record Payments** | ✅ Yes | ❌ No (read-only) |
| **Can Create Operators** | ✅ Yes | ❌ No |
| **Can See Other Clients' Data** | ❌ No | ❌ No |

---

## Spring Security Implementation Notes

### Authentication Endpoints
```java
// All users login here
POST /api/v1/auth/login
    Input: { username, password }
    Output: { token, role, clientId }
```

### Authorization by Role
```java
// Super Admin only
@PreAuthorize("hasRole('SUPER_ADMIN')")
public ResponseEntity<?> createClient(@RequestBody ClientDTO client) {
    // Create new client
}

// Client Admin for their specific client
@PreAuthorize("hasRole('CLIENT_ADMIN') and #clientId == authentication.principal.clientId")
public ResponseEntity<?> addFarmer(@PathVariable UUID clientId, @RequestBody FarmerDTO farmer) {
    // Add farmer for this client
}

// Operator for their specific client
@PreAuthorize("hasRole('OPERATOR') and #clientId == authentication.principal.clientId")
public ResponseEntity<?> recordHarvest(@PathVariable UUID clientId, @RequestBody HarvestDTO harvest) {
    // Record harvest
}
```

### Tenant Isolation Query Filter
```java
// For Client Admin and Operator endpoints:
// Automatically add filter: WHERE client_id = currentUser.clientId

@Repository
public interface FarmerRepository extends JpaRepository<Farmer, UUID> {
    // This ensures only farmers for the current client are returned
    List<Farmer> findByClientId(UUID clientId);
}

// Service layer applies the filter automatically
public List<FarmerDTO> getFarmersForCurrentClient() {
    UUID currentClientId = SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal().getClientId();
    return farmerRepository.findByClientId(currentClientId);
}
```

---

## User Registration Flow

### Step 1: Super Admin Onboards New Client
```
Super Admin → Creates Client → Creates Admin Account → Sends Credentials to Client
```

### Step 2: Client Admin Creates Operators
```
Client Admin → Logs In → Creates Operator Accounts → Sends Credentials to Operators
```

### Step 3: Operators Log In and Start Working
```
Operator → Logs In → Records Harvests → Records Weighing → Views Reports
```

---

## Database User Tables

### Users Table Structure
```sql
CREATE TABLE users (
  user_id UUID PRIMARY KEY,
  username VARCHAR(255) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL,  -- SUPER_ADMIN, CLIENT_ADMIN, OPERATOR
  client_id UUID,             -- NULL for SUPER_ADMIN, required for CLIENT_ADMIN and OPERATOR
  status VARCHAR(50) DEFAULT 'ACTIVE',
  created_date TIMESTAMP,
  modified_date TIMESTAMP,
  FOREIGN KEY (client_id) REFERENCES clients(client_id)
);
```

**Important Constraint**: 
- `role = 'SUPER_ADMIN'` → `client_id` must be NULL
- `role = 'CLIENT_ADMIN'` or `'OPERATOR'` → `client_id` must be populated

---

## Examples

### Example 1: Three Clients Using the System

**Client A**
- Super Admin created account on 2026-01-15
- Client Admin: "Ravi Kumar" (ravi@clientA.com)
- Operators: "Prakash", "Suresh"
- Farmers: "Farmer1", "Farmer2", "Farmer3"

**Client B**
- Super Admin created account on 2026-02-01
- Client Admin: "Arjun Patel" (arjun@clientB.com)
- Operators: "Karthik", "Rajesh", "Vikram"
- Farmers: "FarmerX", "FarmerY"

**Client C**
- Super Admin created account on 2026-03-10
- Client Admin: "Sharma" (sharma@clientC.com)
- Operators: "Worker1"
- Farmers: "FarmerAlpha", "FarmerBeta"

**Key Point**: 
- Ravi (Client A Admin) cannot see Client B or C data
- Prakash (Client A Operator) can only record harvests for Client A
- Each client has completely separate farmers, teams, rates, invoices, etc.

---

## API Examples by Role

### Super Admin Example
```
POST /api/v1/super-admin/clients
Body: {
  "clientName": "Fresh Harvest Ltd",
  "email": "admin@freshharvest.com",
  "businessType": "AGRICULTURAL",
  ...
}
Response: Client created with clientId = xyz123

POST /api/v1/super-admin/clients/xyz123/admin-user
Body: {
  "username": "ravi.kumar",
  "email": "ravi@freshharvest.com"
}
Response: Admin account created (temporary password sent)
```

### Client Admin Example
```
POST /api/v1/clients/xyz123/farmers
Header: Authorization: Bearer <token>
Body: {
  "farmerName": "Rajesh Kumar",
  "email": "rajesh@farm.com",
  ...
}
Response: Farmer created

POST /api/v1/clients/xyz123/operators
Header: Authorization: Bearer <token>
Body: {
  "username": "prakash.op",
  "email": "prakash@freshharvest.com"
}
Response: Operator account created
```

### Operator Example
```
POST /api/v1/clients/xyz123/harvests
Header: Authorization: Bearer <token>
Body: {
  "farmerId": "farmer123",
  "harvestDate": "2026-04-14",
  "quantity": 50.5,
  ...
}
Response: Harvest recorded
```

---

## Summary

✅ **Client Onboarding**: Super Admin Only
- Creates client account
- Creates initial admin account for the client

✅ **Farmer/Team Onboarding**: Client Admin Only
- Each client manages their own farmers and teams
- Each client has independent rates

✅ **Operator Creation**: Client Admin Only
- Client admin creates operators for their staff
- Operators record daily operations

✅ **Data Isolation**: Complete
- Operators cannot see other clients' data
- Each client has completely separate workspace

