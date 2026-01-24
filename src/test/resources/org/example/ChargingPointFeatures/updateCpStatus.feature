Feature: Update charging point status
  As an operator
  I want to update the status of the charging point
  To inform users about available charging points in the area

  Background:
    Given a new Location Manager
    And a new Charging Point Manager
    And a Location with ID "LOC-1" and name "Main Location" and address "Main Address"
    And the Operator sets the pricing for Location "LOC-1" valid from "01-01-2026 08:00":
      | Mode | Price per kWh | Price per minute |
      | AC   | 0.35          | 0.05             |
      | DC   | 0.60          | 0.10             |
    And the Operator creates a Charging Point with ID "CP-001", mode "DC", at Location "LOC-1"
    And the following Customers exist:
      | Customer ID | Initial Credit |
      | CUST-1      | 20.00          |

  Scenario: Charging point is InOperationFree upon creation
    When the Operator creates a Charging Point with ID "CP-002", mode "AC", at Location "LOC-1"
    Then the Charging Point with ID "CP-002" exists at Location "LOC-1"
    And the status of charging point "CP-002" is "IN_OPERATION_FREE"

  Scenario: Operator sets a charging point to Out of Order
    When the operator sets the status of charging point "CP-001" to "OUT_OF_ORDER"
    Then the status of charging point "CP-001" is "OUT_OF_ORDER"

  Scenario: Charging point is Occupied during an active charging session
    When the Customer "CUST-1" physically connects their car to Charging Point "CP-001"
    Then the Customer "CUST-1" starts a charging session at Charging Point "CP-001"
    And a charging session exists for Customer "CUST-1" at Charging Point "CP-001"
    Then the status of charging point "CP-001" is "OCCUPIED"

  Scenario: Error - Update status of non-existing charging point
    When the operator attempts to set status of non-existing charging point "CP-999" to "OUT_OF_ORDER"
    Then an exception is thrown or status update fails gracefully

  Scenario: Edge Case - Set charging point to same status it already has
    Given the status of charging point "CP-001" is "IN_OPERATION_FREE"
    When the operator sets the status of charging point "CP-001" to "IN_OPERATION_FREE"
    Then the status of charging point "CP-001" is "IN_OPERATION_FREE"