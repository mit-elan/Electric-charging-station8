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