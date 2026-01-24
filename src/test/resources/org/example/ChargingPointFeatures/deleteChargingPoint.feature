Feature: Delete charging point from location
  As an operator
  I want to delete charging points from locations
  So that the system map and charging network information stay up to date

  Background:
    Given a new Location Manager
    And a new Charging Point Manager
    And a Location with ID "LOC-1" and name "Main Location" and address "Main Address"
    And the Operator creates a Charging Point with ID "CP-001", mode "DC", at Location "LOC-1"

  Scenario: Operator deletes a charging point from a location
    When the operator deletes the Charging Point with ID "CP-001" from Location "LOC-1"
    Then the Charging Point with ID "CP-001" does not exist at Location "LOC-1"

  Scenario: Deleted charging point is no longer visible in the charging network
    When the operator deletes the Charging Point with ID "CP-001" from Location "LOC-1"
    Then the Charging Point with ID "CP-001" is not shown in the Charging Network

  Scenario: Error - Delete non-existing charging point
    When the operator attempts to delete the Charging Point with ID "CP-999" from Location "LOC-1"
    Then no exception is thrown and the location remains unchanged

  Scenario: Edge Case - Delete charging point from location with no other charging points
    Given the Location "LOC-1" has only one Charging Point "CP-001"
    When the operator deletes the Charging Point with ID "CP-001" from Location "LOC-1"
    Then the Location "LOC-1" has no charging points