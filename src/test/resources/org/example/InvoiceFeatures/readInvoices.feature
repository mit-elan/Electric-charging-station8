Feature: Read Invoices
  In order to understand charging costs and top-up information
  As a customer or operator
  I want to view invoice information

  Background:
    Given a new InvoiceManager
    Given a new Account Manager
    Given a new Credit Manager
    Given a new Charging Session Manager

  Scenario: Customer views invoice overview with sessions and top-ups sorted chronologically
    Given the following Customers exist:
      | Customer ID | Initial Credit |
      | CUST-1      | 30.00          |

    And the following invoices exist for Customer "CUST-1":
      | Item No | Start Time          | Location     | Charging Point | Mode | Duration | Energy Used | Price |
      | 1       | 03-12-2025 10:00:00 | Highway Hub  | CP-DC-1        | DC   | 20       | 30.00       | 15.00 |
      | 2       | 02-01-2026 08:30:00 | Main Station | CP-AC-1        | AC   | 60       | 11.00       | 5.50  |

    And the customer with ID "CUST-1" has made the following credit top ups:
      | Amount | Date of Update      |
      | 30.50  | 03-12-2025 11:30:00 |
      | 50.00  | 01-11-2025 14:42:00 |

    When the customer with ID "CUST-1" views their invoice overview
    Then the invoice overview shows:
  """
  Invoice Overview for Customer: CUST-1
  Item 2 | Start Time: 02-01-2026 08:30:00 | Location: Main Station | CP: CP-AC-1 | Mode: AC | Duration: 60 minutes | Energy used: 11,00 kWh | Price: 5,50
  Item 1 | Start Time: 03-12-2025 10:00:00 | Location: Highway Hub | CP: CP-DC-1 | Mode: DC | Duration: 20 minutes | Energy used: 30,00 kWh | Price: 15,00

  Credit Top-Ups:
  Top-up | Amount: 30,50 | Date: 03-12-2025 11:30:00
  Top-up | Amount: 50,00 | Date: 01-11-2025 14:42:00

  Outstanding Balance: 90,00
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
