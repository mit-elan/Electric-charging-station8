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
      | LOC-300     | New Road 300    | North Station   |

    And the Location "LOC-200" has charging Points
      | Charging Point ID | Mode |
      | CP-1              | AC   |
      | CP-2              | DC   |

    And the Operator updates the pricing of Location "LOC-200" to:
      | AC Price | DC Price |
      | 0.35     | 0.60     |

    And the Charging Point "CP-1" is InOperationFree

  Scenario: Successfully start a charging session
    When the Customer "CUST-1" physically connects their car to Charging Point "CP-1"
    Then the Customer "CUST-1" starts a charging session at Charging Point "CP-1"
    And a charging session exists for Customer "CUST-1" at Charging Point "CP-1"
    And the Charging Point "CP-1" is marked as Occupied
