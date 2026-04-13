# Java 17 Coding Standards

## Naming Conventions

| Element | Convention | Example |
|---------|-----------|---------|
| Classes | PascalCase, nouns | `BankAccountService` |
| Interfaces | PascalCase, adjective or noun | `Serializable`, `AccountService` |
| Methods | camelCase, verbs | `getAccountById()`, `createPerson()` |
| Variables | camelCase, descriptive | `accountBalance`, `legalEntityUuid` |
| Constants | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT`, `DEFAULT_PAGE_SIZE` |
| Packages | all lowercase, no underscores | `com.techwave.paymentservice.service` |
| Type parameters | Single uppercase letter | `<T>`, `<E>`, `<K, V>` |
| Enums | PascalCase class, UPPER_SNAKE_CASE values | `AccountStatus.ACTIVE` |

## Java 17 Features to Prefer

### Records for Immutable Data
```java
// GOOD — use records for simple DTOs
public record CountryResponse(String id, String name, String alpha3Code) {}

// AVOID — verbose POJO for immutable data carriers
public class CountryResponse {
    private String id;
    private String name;
    // ... getters, equals, hashCode, toString
}
```

### Sealed Classes (where applicable)
```java
// GOOD — restrict hierarchy
public sealed class LegalEntityDto permits PersonDto, CorporationDto {}
```

### Pattern Matching for instanceof
```java
// GOOD
if (entity instanceof PersonEntity person) {
    return person.getFirstName();
}

// AVOID
if (entity instanceof PersonEntity) {
    PersonEntity person = (PersonEntity) entity;
    return person.getFirstName();
}
```

### Text Blocks for Multi-line Strings
```java
// GOOD
String query = """
    SELECT e FROM PersonEntity e
    WHERE e.country = :country
    """;

// AVOID
String query = "SELECT e FROM PersonEntity e " +
    "WHERE e.country = :country";
```

### Switch Expressions
```java
// GOOD
String label = switch (status) {
    case ACTIVE -> "Active";
    case INACTIVE -> "Inactive";
    case SUSPENDED -> "Suspended";
};

// AVOID
String label;
switch (status) {
    case ACTIVE: label = "Active"; break;
    case INACTIVE: label = "Inactive"; break;
    // ...
}
```

### var for Local Variables (when type is obvious)
```java
// GOOD — type is obvious from right side
var accounts = bankAccountRepository.findAll();
var uuid = UUID.randomUUID();

// AVOID var — type is not obvious
var result = processAccount(data);  // What type is result?
```

## General Rules

### G01: No Wildcard Imports
```java
// GOOD
import java.util.List;
import java.util.Optional;

// BAD
import java.util.*;
```

### G02: No Raw Types
```java
// GOOD
List<BankAccountEntity> accounts = repository.findAll();

// BAD
List accounts = repository.findAll();
```

### G03: Use Optional Correctly
```java
// GOOD — Optional as return type
public Optional<PersonEntity> findByUuid(UUID uuid) { ... }

// GOOD — handle Optional
var person = personRepository.findByUuid(uuid)
    .orElseThrow(() -> new ResourceNotFoundException("Person not found: " + uuid));

// BAD — Optional as field or parameter
private Optional<String> name;  // Don't do this
public void process(Optional<String> input) { }  // Don't do this
```

### G04: Prefer Immutability
```java
// GOOD
private final BankAccountRepository repository;
List.of("a", "b", "c");
Map.of("key", "value");

// AVOID
private BankAccountRepository repository;  // non-final field in service
new ArrayList<>(Arrays.asList("a", "b", "c"));
```

### G05: No Magic Numbers
```java
// GOOD
private static final int MAX_NAME_LENGTH = 255;

// BAD
if (name.length() > 255) { ... }
```

### G06: Use Meaningful Method Names
```java
// GOOD
findBankAccountByUuid(uuid)
createPersonFromRequest(request)
validateCorporationCode(country, code)

// BAD
process(uuid)
handle(request)
check(country, code)
```

### G07: No Empty Catch Blocks
```java
// GOOD
catch (DataIntegrityViolationException e) {
    throw new BadRequestException("Duplicate entry: " + e.getMessage());
}

// BAD
catch (Exception e) {
    // silently swallowed
}
```

### G08: Close Resources with try-with-resources
```java
// GOOD
try (var stream = Files.lines(path)) {
    stream.forEach(System.out::println);
}
```

### G09: Prefer Stream API for Collection Operations
```java
// GOOD
var activeAccounts = accounts.stream()
    .filter(a -> a.getStatus() == Status.ACTIVE)
    .map(mapper::toDto)
    .toList();

// AVOID
List<AccountDto> activeAccounts = new ArrayList<>();
for (BankAccountEntity a : accounts) {
    if (a.getStatus() == Status.ACTIVE) {
        activeAccounts.add(mapper.toDto(a));
    }
}
```

### G10: No Null Returns from Collections
```java
// GOOD — return empty collection
public List<AuditEntry> getAuditTrail(UUID uuid) {
    return auditRepository.findByEntityUuid(uuid);  // returns empty list, not null
}

// BAD
return null;
```
