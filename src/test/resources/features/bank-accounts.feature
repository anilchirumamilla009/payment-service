Feature: Bank Accounts API
  As an authenticated user
  I want to manage bank accounts
  So that I can track settlement accounts for payment processing

  # ── Create Bank Account (PUT) ──────────────────────────────────────

  Scenario: Create a bank account with valid payload
    Given I am an authenticated user
    When I send a PUT request to "/bank-accounts" with body:
      """
      {
        "beneficiary": "Test Ltd",
        "beneficiaryAddress": "123 Test St",
        "currency": "USD",
        "country": "US"
      }
      """
    Then the response status should be 200
    And the response body "resourceType" should be "bank-accounts"
    And the response body should contain an "id"

  Scenario: Create a bank account without beneficiary returns 400
    Given I am an authenticated user
    When I send a PUT request to "/bank-accounts" with body:
      """
      { "currency": "USD", "country": "US" }
      """
    Then the response status should be 400

  Scenario: Create a bank account without currency returns 400
    Given I am an authenticated user
    When I send a PUT request to "/bank-accounts" with body:
      """
      { "beneficiary": "Test", "country": "US" }
      """
    Then the response status should be 400

  Scenario: Create a bank account without country returns 400
    Given I am an authenticated user
    When I send a PUT request to "/bank-accounts" with body:
      """
      { "beneficiary": "Test", "currency": "USD" }
      """
    Then the response status should be 400

  # ── Get Bank Account ───────────────────────────────────────────────

  Scenario: Retrieve existing bank account by UUID
    Given I am an authenticated user
    When I send a GET request to "/bank-accounts/33333333-3333-3333-3333-333333333333"
    Then the response status should be 200
    And the response body "beneficiary" should be "Cornerstone FX Ltd"
    And the response body "currency" should be "EUR"

  Scenario: Retrieve non-existing bank account returns 404
    Given I am an authenticated user
    When I send a GET request to "/bank-accounts/99999999-9999-9999-9999-999999999999"
    Then the response status should be 404

  # ── Audit Trail ────────────────────────────────────────────────────

  Scenario: Retrieve bank account audit trail
    Given I am an authenticated user
    When I send a GET request to "/bank-accounts/33333333-3333-3333-3333-333333333333/audit-trail"
    Then the response status should be 200
    And the response body should be a non-empty JSON array

  Scenario: Audit trail for non-existing bank account returns 404
    Given I am an authenticated user
    When I send a GET request to "/bank-accounts/99999999-9999-9999-9999-999999999999/audit-trail"
    Then the response status should be 404

  # ── Beneficial Owners ──────────────────────────────────────────────

  Scenario: Retrieve bank account beneficial owners
    Given I am an authenticated user
    When I send a GET request to "/bank-accounts/33333333-3333-3333-3333-333333333333/beneficial-owners"
    Then the response status should be 200
    And the response body should be a non-empty JSON array

  Scenario: Beneficial owners for non-existing bank account returns 404
    Given I am an authenticated user
    When I send a GET request to "/bank-accounts/99999999-9999-9999-9999-999999999999/beneficial-owners"
    Then the response status should be 404

  # ── Security ───────────────────────────────────────────────────────

  Scenario: Unauthenticated access to bank accounts returns 401
    Given I am not authenticated
    When I send a GET request to "/bank-accounts/33333333-3333-3333-3333-333333333333"
    Then the response status should be 401

