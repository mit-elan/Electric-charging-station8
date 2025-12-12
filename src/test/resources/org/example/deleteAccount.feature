Feature: Delete Account
  As a system administrator
  I want to delete customer accounts
  so that outdated or invalid accounts are removed.

  Background:
    Given a new Account Manager

  Scenario: Delete an existing account
    Given the following accounts exist:
      | Customer ID | Name       | Email            | Password |
      | 1001        | Alice Doe  | alice@mail.com   | a1       |
      | 1002        | Bob Smith  | bob@mail.com     | b2       |
    When the admin deletes the account with Customer ID "1002"
    Then the total number of existing accounts is 1
    And the account with Customer ID "1002" does not exist

  Scenario: Attempting to delete a non-existing account
    Given the following accounts exist:
      | Customer ID | Name        | Email           | Password |
      | 2001        | Clara White | clara@mail.com  | pw123    |
    When the admin deletes the account with Customer ID "9999"
    Then the total number of existing accounts is 1
