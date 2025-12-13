Feature: Create Charging Point
  As an Operator
  I want to create a new Charging Point
  so that I can expand the Charging Network at a Location

  Background:
    Given a new Location Manager
    And a new Charging Point Manager
    And a Location with ID "LOC-1" and name "Main Location" and address "Main Address"

  Scenario: Create an AC Charging Point at a Location
    When the Operator creates a Charging Point with ID "CP-001", mode "AC", at Location "LOC-1"
    Then the Charging Point with ID "CP-001" exists at Location "LOC-1"
    And the Charging Point with ID "CP-001" has mode "AC"

  Scenario: Create a DC Charging Point at a Location
    When the Operator creates a Charging Point with ID "CP-002", mode "DC", at Location "LOC-1"
    Then the Charging Point with ID "CP-002" exists at Location "LOC-1"
    And the Charging Point with ID "CP-002" has mode "DC"
