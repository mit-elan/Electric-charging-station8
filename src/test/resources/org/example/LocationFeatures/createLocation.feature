Feature: Add New Location
  As an Operator
  I want to add a new Location
  so that I can expand the Charging Network.

  Background:
    Given a new Location Manager

  Scenario: Successfully creating a new location
    When the Operator creates a new Location with the following details:
      | Location ID | Address             | Name           |
      | LOC-001     | 123 Energy Lane     | Central Station|
    Then the Location with Location ID "LOC-001" exists in the Charging Network
    And the Location with Location ID "LOC-001" has Name "Central Station"
    And the Location with Location ID "LOC-001" has Address "123 Energy Lane"