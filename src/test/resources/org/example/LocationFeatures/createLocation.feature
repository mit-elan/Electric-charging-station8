Feature: Add New Location
  As an Operator
  I want to create a new Location
  so that I can expand the Charging Network.

  Background:
    Given a new Location Manager

  Scenario: Successfully creating a new location
    When the Operator creates new Locations with the following details:
      | Location ID | Address             | Name           |
      | LOC-001     | 123 Energy Lane     | Central Station|
    Then the Location with Location ID "LOC-001" exists in the Charging Network
    And the Location with Location ID "LOC-001" has Name "Central Station"
    And the Location with Location ID "LOC-001" has Address "123 Energy Lane"

  Scenario: Successfully creating multiple new locations
    When the Operator creates new Locations with the following details:
      | Location ID | Address         | Name            |
      | LOC-001     | 123 Energy Lane | Central Station |
      | LOC-002     | 455 Main Road   | West Station    |

    Then the Location with Location ID "LOC-001" exists in the Charging Network
    And the Location with Location ID "LOC-001" has Name "Central Station"
    And the Location with Location ID "LOC-001" has Address "123 Energy Lane"

    And the Location with Location ID "LOC-002" exists in the Charging Network
    And the Location with Location ID "LOC-002" has Name "West Station"
    And the Location with Location ID "LOC-002" has Address "455 Main Road"
