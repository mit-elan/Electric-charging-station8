Feature: Read Invoices
  In order to understand charging costs and top-up information
  As a customer or operator
  I want to view invoice information

  Background:
    Given a new InvoiceManager
    And a new Account Manager
    And a new Credit Manager
    And a new Charging Session Manager

  Scenario: Customer views invoice overview with sessions and top-ups sorted chronologically
    Given the following accounts exist:
      | Customer ID | Name        | Email           | Password |
      | CUST-1       | Clara White | clara@mail.com  | pw123    |

    And the customer with ID "CUST-1" has made the following credit top ups:
      | Amount | Date of Update      |
      | 30.50  | 03-12-2025 11:30:00 |
      | 50.00  | 01-11-2025 14:42:00 |

    And the following invoices exist for Customer "CUST-1":
      | Item No | Start Time          | Location     | Charging Point | Mode | Duration | Energy Used | Price |
      | 1       | 03-12-2025 10:00:00 | Highway Hub  | CP-DC-1        | DC   | 20       | 30.00       | 15.00 |
      | 2       | 02-01-2026 08:30:00 | Main Station | CP-AC-1        | AC   | 60       | 11.00       | 5.50  |

    When the customer with ID "CUST-1" views their invoice overview
    Then the invoice overview shows:
    """
    Invoice Overview for Customer: Clara White
    Customer ID:CUST-1

    01-11-2025 14:42:00 | Top-up | Amount: 50,00
    03-12-2025 10:00:00 | Charging Session | Invoice Item No: 1 | Location Name: Highway Hub | Charging Point Name: CP-DC-1 | Mode: DC | Duration: 20 minutes | Energy used: 30,00 kWh | Price: 15,00
    03-12-2025 11:30:00 | Top-up | Amount: 30,50
    02-01-2026 08:30:00 | Charging Session | Invoice Item No: 2 | Location Name: Main Station | Charging Point Name: CP-AC-1 | Mode: AC | Duration: 60 minutes | Energy used: 11,00 kWh | Price: 5,50

    Outstanding Balance: 60,00
    """

  Scenario: Operator views all invoices for multiple customers
    Given each customer has topped up credit:
      | Customer ID | Amount | Date of Update      |
      | CUST-1      | 50.00  | 01-11-2025 10:00:00 |
      | CUST-1      | 30.50  | 03-12-2025 10:00:00 |
      | CUST-3      | 30.00  | 24-12-2025 10:00:00 |
      | CUST-2      | 80.00  | 24-12-2025 10:00:00 |

    And the following invoices exist:
      | Customer ID | Item No | Start Time          | Location        | Charging Point | Mode | Duration | Energy Used | Price |
      | CUST-1      | 1       | 01-11-2025 10:00:00 | Main Station    | CP-AC-1        | AC   | 60       | 12.0        | 6.00  |
      | CUST-1      | 2       | 03-12-2025 14:00:00 | Highway Hub     | CP-DC-1        | DC   | 30       | 25.0        | 12.50 |
      | CUST-2      | 1       | 24-12-2025 09:00:00 | Downtown Hub    | CP-AC-2        | AC   | 45       | 10.0        | 5.00  |
      | CUST-2      | 2       | 24-12-2025 11:30:00 | Airport Station | CP-DC-2        | DC   | 90       | 50.0        | 25.00 |
      | CUST-3      | 1       | 24-12-2025 12:00:00 | Central Plaza   | CP-AC-3        | AC   | 20       | 5.0         | 2.50  |

    When the operator views all invoices
    Then a list of charging sessions for all customers is shown sorted by start time
    And each invoice entry contains charging and pricing details
    And top-up transactions are displayed
    And the current outstanding balance is displayed

  Scenario: Error - View invoices for non-existing customer
    When the customer with ID "NON-EXISTENT" attempts to view their invoice overview
    Then an empty invoice list is returned

  Scenario: Edge Case - View invoices with no charging sessions
    Given the following accounts exist:
      | Customer ID | Name      | Email          | Password |
      | CUST-EMPTY  | Empty User| empty@mail.com | pw123    |
    And the customer with ID "CUST-EMPTY" has made the following credit top ups:
      | Amount | Date of Update      |
      | 100.00 | 01-01-2026 10:00:00 |
    When the customer with ID "CUST-EMPTY" views their invoice overview
    Then the invoice overview shows only top-up information