Feature: Core Reference Data API
  As an authenticated user
  I want to retrieve reference data (countries, currencies, silos)
  So that I can use them in payment processing

  # ── Countries ──────────────────────────────────────────────────────

  Scenario: Retrieve all countries
    Given I am an authenticated user
    When I send a GET request to "/countries"
    Then the response status should be 200
    And the response body should be a non-empty JSON array
    And each item should have a "resourceType" of "countries"

  Scenario: Retrieve a single country by ID
    Given I am an authenticated user
    When I send a GET request to "/countries/GB"
    Then the response status should be 200
    And the response body "id" should be "GB"
    And the response body "name" should be "United Kingdom"

  Scenario: Retrieve a non-existing country returns 404
    Given I am an authenticated user
    When I send a GET request to "/countries/ZZ"
    Then the response status should be 404

  Scenario: Unauthenticated access to countries returns 401
    Given I am not authenticated
    When I send a GET request to "/countries"
    Then the response status should be 401

  # ── Currencies ─────────────────────────────────────────────────────

  Scenario: Retrieve all currencies
    Given I am an authenticated user
    When I send a GET request to "/currencies"
    Then the response status should be 200
    And the response body should be a non-empty JSON array

  Scenario: Retrieve a single currency by ID
    Given I am an authenticated user
    When I send a GET request to "/currencies/EUR"
    Then the response status should be 200
    And the response body "id" should be "EUR"

  Scenario: Retrieve a non-existing currency returns 404
    Given I am an authenticated user
    When I send a GET request to "/currencies/XYZ"
    Then the response status should be 404

  # ── Silos ──────────────────────────────────────────────────────────

  Scenario: Retrieve all silos
    Given I am an authenticated user
    When I send a GET request to "/silos"
    Then the response status should be 200
    And the response body should be a non-empty JSON array

  Scenario: Retrieve a single silo by ID
    Given I am an authenticated user
    When I send a GET request to "/silos/treasury-core"
    Then the response status should be 200
    And the response body "id" should be "treasury-core"

  Scenario: Retrieve a non-existing silo returns 404
    Given I am an authenticated user
    When I send a GET request to "/silos/nonexistent"
    Then the response status should be 404

