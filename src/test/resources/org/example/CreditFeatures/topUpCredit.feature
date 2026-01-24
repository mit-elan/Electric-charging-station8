Feature: Top Up Prepaid Credit
  As a customer
  I want to top up my prepaid credit
  so that I can start charging sessions

  Background:
    Given a new Account Manager
    And a new Credit Manager

    And the following Customers exist:
      | Customer ID | Initial Credit |
      | CUST-1      | 10.00          |
      | CUST-2      | 5.00           |

  Scenario: Customer successfully tops up prepaid credit
    When the customer with ID "CUST-1" tops up their credit with 20.00
    Then the credit balance of Customer "CUST-1" is 30.00
    And the payment is processed successfully
    And the credit last updated date is stored

  Scenario: Error - Top up with negative amount
    When the customer with ID "CUST-1" attempts to top up their credit with -10.00
    Then an exception is thrown indicating cannot add negative credit

  Scenario: Edge Case - Top up with very large amount
    When the customer with ID "CUST-1" tops up their credit with 999999.99
    Then the credit balance of Customer "CUST-1" is 1000009.99
    And the payment is processed successfully