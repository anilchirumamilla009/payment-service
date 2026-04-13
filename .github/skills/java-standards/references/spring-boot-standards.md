# Spring Boot Coding Standards

## Controller Standards

### C01: Use Specific Request Mapping Annotations
```java
// GOOD
@GetMapping("/{uuid}")
@PostMapping
@PatchMapping("/{uuid}")

// AVOID
@RequestMapping(method = RequestMethod.GET, path = "/{uuid}")
```

### C02: Always Use @Valid on Request Bodies
```java
// GOOD
@PostMapping
public ResponseEntity<PersonResponse> createPerson(@Valid @RequestBody PersonRequest request) { ... }

// BAD — no validation triggered
@PostMapping
public ResponseEntity<PersonResponse> createPerson(@RequestBody PersonRequest request) { ... }
```

### C03: Return ResponseEntity with Proper Status Codes
```java
// GOOD
return ResponseEntity.ok(response);
return ResponseEntity.status(HttpStatus.CREATED).body(response);
return ResponseEntity.notFound().build();

// AVOID — loses control over status code
return response;
```

### C04: Controllers Should Be Thin
```java
// GOOD — delegate to service
@GetMapping("/{uuid}")
public ResponseEntity<PersonResponse> getPerson(@PathVariable UUID uuid) {
    return ResponseEntity.ok(personService.getByUuid(uuid));
}

// BAD — business logic in controller
@GetMapping("/{uuid}")
public ResponseEntity<PersonResponse> getPerson(@PathVariable UUID uuid) {
    var entity = personRepository.findByUuid(uuid)
        .orElseThrow(() -> new ResourceNotFoundException("..."));
    var dto = personMapper.toDto(entity);
    return ResponseEntity.ok(dto);
}
```

### C05: Use @PathVariable and @RequestParam with Explicit Names
```java
// GOOD
@GetMapping("/{uuid}")
public ResponseEntity<?> get(@PathVariable("uuid") UUID uuid) { ... }

// OK when parameter name matches
@GetMapping("/{uuid}")
public ResponseEntity<?> get(@PathVariable UUID uuid) { ... }
```

## Service Standards

### S01: Interface + Implementation Pattern
```java
// Interface in service/
public interface PersonService {
    PersonResponse getByUuid(UUID uuid);
    PersonResponse create(PersonRequest request);
}

// Implementation in service/impl/
@Service
public class PersonServiceImpl implements PersonService {
    // ...
}
```

### S02: Constructor Injection (No @Autowired)
```java
// GOOD
@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonServiceImpl(PersonRepository personRepository, PersonMapper personMapper) {
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }
}

// AVOID
@Autowired
private PersonRepository personRepository;
```

### S03: Throw Domain Exceptions
```java
// GOOD
throw new ResourceNotFoundException("Person not found with UUID: " + uuid);
throw new BadRequestException("Invalid country code: " + code);

// BAD — generic exceptions
throw new RuntimeException("Not found");
throw new Exception("Bad input");
```

### S04: Services Must Be Transactional Where Needed
```java
// GOOD — write operations
@Transactional
public PersonResponse create(PersonRequest request) { ... }

// Read operations — optional but explicit
@Transactional(readOnly = true)
public PersonResponse getByUuid(UUID uuid) { ... }
```

## Repository Standards

### R01: Extend JpaRepository
```java
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
    Optional<PersonEntity> findByUuid(UUID uuid);
}
```

### R02: Use Derived Query Methods When Possible
```java
// GOOD — Spring Data derives the query
Optional<PersonEntity> findByUuid(UUID uuid);
List<BankAccountEntity> findByLegalEntityUuid(UUID legalEntityUuid);

// Use @Query only when derived method names become unwieldy
@Query("SELECT c FROM CorporationEntity c WHERE c.country.id = :country AND c.code = :code")
Optional<CorporationEntity> findByCountryAndCode(@Param("country") String country, @Param("code") String code);
```

### R03: Never Use String Concatenation in Queries
```java
// GOOD
@Query("SELECT e FROM PersonEntity e WHERE e.name = :name")
List<PersonEntity> findByName(@Param("name") String name);

// CRITICAL — SQL injection risk
@Query("SELECT e FROM PersonEntity e WHERE e.name = '" + name + "'")
```

## Entity Standards

### E01: Use @Entity with @Table
```java
@Entity
@Table(name = "people")
public class PersonEntity { ... }
```

### E02: Use UUID Fields for External IDs
```java
@Column(name = "uuid", nullable = false, unique = true, updatable = false)
private UUID uuid;

@PrePersist
public void prePersist() {
    if (uuid == null) uuid = UUID.randomUUID();
}
```

### E03: Audit Fields on Mutable Entities
```java
@Column(name = "created_at", nullable = false, updatable = false)
private LocalDateTime createdAt;

@Column(name = "updated_at")
private LocalDateTime updatedAt;
```

## DTO Standards

### D01: Jakarta Validation on All Input DTOs
```java
public class PersonRequest {
    @NotBlank(message = "First name is required")
    @Size(max = 255, message = "First name must not exceed 255 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 255, message = "Last name must not exceed 255 characters")
    private String lastName;

    @NotNull(message = "Country is required")
    private String country;
}
```

### D02: No Entity Suffix on DTOs
```java
// GOOD
PersonRequest, PersonResponse, BankAccountResponse

// BAD
PersonEntityDto, PersonEntityRequest
```

### D03: Separate Request and Response DTOs
```java
// GOOD — different shapes for input vs output
public class PersonRequest { ... }   // what the client sends
public class PersonResponse { ... }  // what the client receives
```

## Mapper Standards

### M01: MapStruct with Spring Component Model
```java
@Mapper(componentModel = "spring")
public interface PersonMapper {
    PersonResponse toDto(PersonEntity entity);
    PersonEntity toEntity(PersonRequest request);
}
```

### M02: Use @Mapping for Non-trivial Field Maps
```java
@Mapper(componentModel = "spring")
public interface BankAccountMapper {
    @Mapping(source = "legalEntity.uuid", target = "legalEntityUuid")
    BankAccountResponse toDto(BankAccountEntity entity);
}
```

## Exception Handling Standards

### X01: Use Global Exception Handler
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        // collect field errors
    }
}
```

### X02: Never Catch and Swallow
```java
// BAD
catch (Exception e) { }

// BAD — log and swallow
catch (Exception e) { log.error("Error", e); }

// GOOD — log and rethrow or translate
catch (DataIntegrityViolationException e) {
    throw new BadRequestException("Duplicate entry: " + e.getMessage());
}
```
