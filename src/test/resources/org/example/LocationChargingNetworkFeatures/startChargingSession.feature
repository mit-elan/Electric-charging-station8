Feature: Start Charging Session
  As a Customer
  I want to start a charging session at a charging point
  so that I can charge my electric vehicle

  Background:
    Given a new Account Manager
    And a new Credit Manager
    And a new Location Manager
    And a new Charging Point Manager
    And a new Charging Session Manager

    And the following Customers exist:
      | Customer ID | Initial Credit |
      | CUST-1      | 20.00          |

    And the following Locations exist:
      | Location ID | Address         | Name            |
      | LOC-200     | Energy Road 5   | East Station    |

    And the Location "LOC-200" has charging Points
      | Charging Point ID | Mode |
      | CP-1              | AC   |
      | CP-2              | DC   |

    And the Operator sets the pricing for Location "LOC-200" valid from "01-01-2026 08:00":
      | Mode | Price per kWh | Price per minute |
      | AC   | 0.35          | 0.05             |
      | DC   | 0.60          | 0.10             |

    And the Charging Point "CP-1" is InOperationFree

  Scenario: Successfully start a charging session
    When the Customer "CUST-1" physically connects their car to Charging Point "CP-1"
    Then the Customer "CUST-1" starts a charging session at Charging Point "CP-1"
    And a charging session exists for Customer "CUST-1" at Charging Point "CP-1"
    And the Charging Point "CP-1" is marked as Occupied

  Scenario: Error - Start session with insufficient credit
    Given the following Customers exist:
      | Customer ID | Initial Credit |
      | CUST-POOR   | 0.00           |
    And the Charging Point "CP-1" is InOperationFree
    When the Customer "CUST-POOR" physically connects their car to Charging Point "CP-1"
    And the Customer "CUST-POOR" attempts to start a charging session at Charging Point "CP-1"
    Then an exception is thrown indicating insufficient credit

  Scenario: Edge Case - Start session at charging point that is out of order
    Given the operator sets the status of charging point "CP-2" to "OUT_OF_ORDER"
    When the Customer "CUST-1" physically connects their car to Charging Point "CP-2"
    And the Customer "CUST-1" attempts to start a charging session at Charging Point "CP-2"
    Then an exception is thrown indicating charging point not available