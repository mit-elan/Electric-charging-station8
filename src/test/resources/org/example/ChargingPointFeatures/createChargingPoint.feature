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

  Scenario: Create multiple Charging Points with different modes at one Location
    When the Operator creates a Charging Point with ID "CP-101", mode "AC", at Location "LOC-1"
    And the Operator creates a Charging Point with ID "CP-102", mode "DC", at Location "LOC-1"
    And the Operator creates a Charging Point with ID "CP-103", mode "AC", at Location "LOC-1"
    And the Operator creates a Charging Point with ID "CP-104", mode "DC", at Location "LOC-1"

    Then the Charging Point with ID "CP-101" exists at Location "LOC-1"
    And the Charging Point with ID "CP-101" has mode "AC"

    And the Charging Point with ID "CP-102" exists at Location "LOC-1"
    And the Charging Point with ID "CP-102" has mode "DC"

    And the Charging Point with ID "CP-103" exists at Location "LOC-1"
    And the Charging Point with ID "CP-103" has mode "AC"

    And the Charging Point with ID "CP-104" exists at Location "LOC-1"
    And the Charging Point with ID "CP-104" has mode "DC"

  Scenario: Error - Create Charging Point at non-existing Location
    When the Operator attempts to create a Charging Point with ID "CP-ERROR" at non-existing Location "LOC-999"
    Then an exception is thrown because the location does not exist

  Scenario: Edge Case - Create Charging Point with duplicate ID
    Given the Operator creates a Charging Point with ID "CP-DUP", mode "AC", at Location "LOC-1"
    When the Operator attempts to create another Charging Point with ID "CP-DUP", mode "DC", at Location "LOC-1"
    Then an exception is thrown indicating duplicate charging point ID