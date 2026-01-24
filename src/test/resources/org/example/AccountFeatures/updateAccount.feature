Feature: Update Account
  As a customer
  I want to update my account information
  so that my personal data stays correct.

  Background:
    Given a new Account Manager

  Scenario: Update an existing account's information
    Given an account exists with the following details:
      | Customer ID | 123456               |
      | Name        | Jane Doe             |
      | Email       | jane.doe@example.com |
      | Password    | secret123            |
    When the customer updates the account with Customer ID "123456" to:
      | Name     | Jane A. Doe            |
      | Email    | jane.adams@example.com |
      | Password | updated456             |
    Then the account with Customer ID "123456" has Name "Jane A. Doe"
    And the account with Customer ID "123456" has Email "jane.adams@example.com"
    And the account with Customer ID "123456" has Password "updated456"

  Scenario: Error - Update non-existing account
    When the customer updates the account with Customer ID "NON-EXISTENT" to:
      | Name     | New Name        |
      | Email    | new@example.com |
      | Password | newpass         |
    Then an exception is thrown indicating no account was found for update

  Scenario: Edge Case - Update only password, leave others unchanged
    Given an account exists with the following details:
      | Customer ID | 234567               |
      | Name        | Original Name        |
      | Email       | original@example.com |
      | Password    | oldpass              |
    When the customer updates only the password for Customer ID "234567" to "newpass123"
    Then the account with Customer ID "234567" has Name "Original Name"
    And the account with Customer ID "234567" has Email "original@example.com"
    And the account with Customer ID "234567" has Password "newpass123"