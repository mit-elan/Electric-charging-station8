Feature: CreateAccount
  As a customer
  I want to create an account with my personal information
  so that I can access and use the charging network.

  Background:
    Given a new Account Manager

  Scenario: Create a single customer account
    When customer creates an account with name "Alice", email "alice@example.com", and password "pass123"
    Then the account exists with name "Alice" and email "alice@example.com"
    And a unique customer ID is generated

  Scenario: Error - Create account with null name
    When customer creates an account with name "", email "test@example.com", and password "pass123"
    Then an exception is thrown indicating account fields cannot be null

  Scenario: Edge Case - Create account with very long name
    When customer creates an account with name "ThisIsAVeryLongNameThatExceedsNormalExpectationsForACustomerNameButShouldStillBeHandledProperly", email "long@example.com", and password "pass123"
    Then the account exists with name "ThisIsAVeryLongNameThatExceedsNormalExpectationsForACustomerNameButShouldStillBeHandledProperly" and email "long@example.com"
    And a unique customer ID is generated