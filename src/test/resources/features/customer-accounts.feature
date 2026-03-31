Feature: Customer Accounts API
  As an authenticated user
  I want to retrieve customer account information
  So that I can view account details and beneficial owners

  Scenario: Retrieve existing customer account by UUID
    Given I am an authenticated user
    When I send a GET request to "/customer-accounts/44444444-4444-4444-4444-444444444444"
    Then the response status should be 200
    And the response body "resourceType" should be "customer-accounts"
    And the response body "name" should be "Cornerstone Principal Account"
    And the response body "accountType" should be "CORPORATE"
    And the response body "accountState" should be "ACCEPTED"

  Scenario: Retrieve non-existing customer account returns 404
    Given I am an authenticated user
    When I send a GET request to "/customer-accounts/99999999-9999-9999-9999-999999999999"
    Then the response status should be 404

  Scenario: Retrieve customer account beneficial owners
    Given I am an authenticated user
    When I send a GET request to "/customer-accounts/44444444-4444-4444-4444-444444444444/beneficial-owners"
    Then the response status should be 200
    And the response body should be a non-empty JSON array

  Scenario: Beneficial owners for non-existing customer account returns 404
    Given I am an authenticated user
    When I send a GET request to "/customer-accounts/99999999-9999-9999-9999-999999999999/beneficial-owners"
    Then the response status should be 404

  Scenario: Unauthenticated access to customer accounts returns 401
    Given I am not authenticated
    When I send a GET request to "/customer-accounts/44444444-4444-4444-4444-444444444444"
    Then the response status should be 401

