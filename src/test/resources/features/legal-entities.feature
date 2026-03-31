Feature: Legal Entities API
  As an authenticated user
  I want to manage corporations and people
  So that I can maintain legal entity records for the payment system

  # ── Corporation CRUD ───────────────────────────────────────────────

  Scenario: Create a corporation with valid payload
    Given I am an authenticated user
    When I send a POST request to "/corporations" with body:
      """
      {
        "name": "Acme Corp",
        "code": "ACME-US",
        "incorporationCountry": "US",
        "type": "FINTECH"
      }
      """
    Then the response status should be 200
    And the response body "resourceType" should be "corporations"
    And the response body should contain an "id"

  Scenario: Create a corporation without name returns 400
    Given I am an authenticated user
    When I send a POST request to "/corporations" with body:
      """
      { "code": "ACME-US" }
      """
    Then the response status should be 400

  Scenario: Retrieve existing corporation by UUID
    Given I am an authenticated user
    When I send a GET request to "/corporations/11111111-1111-1111-1111-111111111111"
    Then the response status should be 200
    And the response body "name" should be "Cornerstone FX Ltd"

  Scenario: Retrieve non-existing corporation returns 404
    Given I am an authenticated user
    When I send a GET request to "/corporations/99999999-9999-9999-9999-999999999999"
    Then the response status should be 404

  Scenario: Update an existing corporation (PATCH)
    Given I am an authenticated user
    When I send a PATCH request to "/corporations/11111111-1111-1111-1111-111111111111" with body:
      """
      { "name": "Cornerstone FX Updated" }
      """
    Then the response status should be 200
    And the response body "name" should be "Cornerstone FX Updated"

  Scenario: Update a non-existing corporation returns 404
    Given I am an authenticated user
    When I send a PATCH request to "/corporations/99999999-9999-9999-9999-999999999999" with body:
      """
      { "name": "X" }
      """
    Then the response status should be 404

  Scenario: Retrieve corporation by country and code
    Given I am an authenticated user
    When I send a GET request to "/corporations/GB/CFS-UK"
    Then the response status should be 200
    And the response body "code" should be "CFS-UK"

  Scenario: Retrieve corporation audit trail
    Given I am an authenticated user
    When I send a GET request to "/corporations/11111111-1111-1111-1111-111111111111/audit-trail"
    Then the response status should be 200
    And the response body should be a non-empty JSON array

  # ── Person CRUD ────────────────────────────────────────────────────

  Scenario: Create a person with valid payload
    Given I am an authenticated user
    When I send a POST request to "/people" with body:
      """
      { "firstName": "Jane", "lastName": "Doe" }
      """
    Then the response status should be 200
    And the response body "resourceType" should be "people"

  Scenario: Create a person without firstName returns 400
    Given I am an authenticated user
    When I send a POST request to "/people" with body:
      """
      { "lastName": "Doe" }
      """
    Then the response status should be 400

  Scenario: Create a person without lastName returns 400
    Given I am an authenticated user
    When I send a POST request to "/people" with body:
      """
      { "firstName": "Jane" }
      """
    Then the response status should be 400

  Scenario: Retrieve existing person by UUID
    Given I am an authenticated user
    When I send a GET request to "/people/22222222-2222-2222-2222-222222222222"
    Then the response status should be 200
    And the response body "firstName" should be "Stephen"

  Scenario: Retrieve non-existing person returns 404
    Given I am an authenticated user
    When I send a GET request to "/people/99999999-9999-9999-9999-999999999999"
    Then the response status should be 404

  Scenario: Update an existing person (PATCH)
    Given I am an authenticated user
    When I send a PATCH request to "/people/22222222-2222-2222-2222-222222222222" with body:
      """
      { "lastName": "Updated" }
      """
    Then the response status should be 200
    And the response body "lastName" should be "Updated"

  Scenario: Retrieve person audit trail
    Given I am an authenticated user
    When I send a GET request to "/people/22222222-2222-2222-2222-222222222222/audit-trail"
    Then the response status should be 200
    And the response body should be a non-empty JSON array

  # ── Security ───────────────────────────────────────────────────────

  Scenario: Unauthenticated access to corporations returns 401
    Given I am not authenticated
    When I send a GET request to "/corporations/11111111-1111-1111-1111-111111111111"
    Then the response status should be 401

  Scenario: Unauthenticated access to people returns 401
    Given I am not authenticated
    When I send a GET request to "/people/22222222-2222-2222-2222-222222222222"
    Then the response status should be 401

