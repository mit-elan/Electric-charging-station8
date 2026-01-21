Feature: End Charging Session
  As a customer
  I want to end a charging session at a charging point
  so that I can use my vehicle again

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

    And an active charging session with ID "CS-1" exists for Customer "CUST-1" at Charging Point "CP-1"

  Scenario: Successfully end a charging session
    When the customer disconnects the vehicle from Charging Point "CP-1"
    Then the charging session with ID "CS-1" is stopped using 50.00 kWh over 20 minutes
    And the charging session is stored in the ChargingSessionManager
    And the price of the charging session "CS-1" is 18.50
    And an invoice is created for the charging session "CS-1"
    And the Charging Point "CP-1" is marked as InOperationFree
    And the remaining credit of Customer "CUST-1" is 1.50
