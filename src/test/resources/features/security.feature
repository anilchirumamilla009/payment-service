Feature: OWASP Security Compliance
  As a security-conscious platform
  I want all API responses to include standard security headers
  So that the system meets OWASP Top-10 protection requirements

  Scenario: X-Content-Type-Options header present
    Given I am an authenticated user
    When I send a GET request to "/countries"
    Then the response status should be 200
    And the response header "X-Content-Type-Options" should be "nosniff"

  Scenario: X-XSS-Protection header present
    Given I am an authenticated user
    When I send a GET request to "/countries"
    Then the response status should be 200
    And the response header "X-XSS-Protection" should exist

  Scenario: Content-Security-Policy header present
    Given I am an authenticated user
    When I send a GET request to "/countries"
    Then the response status should be 200
    And the response header "Content-Security-Policy" should exist

  Scenario: Referrer-Policy header present
    Given I am an authenticated user
    When I send a GET request to "/countries"
    Then the response status should be 200
    And the response header "Referrer-Policy" should exist

  Scenario: Cache-Control header present on authenticated responses
    Given I am an authenticated user
    When I send a GET request to "/countries"
    Then the response status should be 200
    And the response header "Cache-Control" should exist

  Scenario: Protected endpoint denies unauthenticated access
    Given I am not authenticated
    When I send a GET request to "/countries"
    Then the response status should be 401

  Scenario: Public health endpoint is accessible without credentials
    Given I am not authenticated
    When I send a GET request to "/api/health"
    Then the response status should be 200

  Scenario: Error responses do not leak stack traces
    Given I am an authenticated user
    When I send a GET request to "/countries/ZZ"
    Then the response status should be 404
    And the response body should not contain "stackTrace"
    And the response body should not contain "java."

