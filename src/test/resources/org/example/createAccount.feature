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