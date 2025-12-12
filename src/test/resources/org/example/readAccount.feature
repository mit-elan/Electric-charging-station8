Feature: Read Account
  As a customer
  I want to view my account information
  so that I can see my personal data at any time.

  Background:
    Given a new Account Manager

  # Scenario for reading a single account
  Scenario: View a single account's information
    Given an account exists with:
      | Field    | Value                |
      | Name     | Jane Doe             |
      | Email    | jane.doe@example.com |
      | Password | secret123            |
    When the customer requests the account with the generated Customer ID
    Then the displayed Name is "Jane Doe"
    And the displayed Email is "jane.doe@example.com"
    And the displayed Password is "secret123"

  # Scenario for multiple accounts (admin view)
  Scenario: View multiple accounts' information
    When the following accounts are created:
      | Name          | Email             | Password |
      | Alice Smith   | alice@mail.com    | pw1      |
      | Bob Johnson   | bob@mail.com      | pw2      |
      | Charlie Brown | charlie@mail.com  | pw3      |
    Then reading the account list shows the following output:
      """
      Account Overview:
      Name: Alice Smith, Email: alice@mail.com, Password: pw1
      Name: Bob Johnson, Email: bob@mail.com, Password: pw2
      Name: Charlie Brown, Email: charlie@mail.com, Password: pw3
      """
