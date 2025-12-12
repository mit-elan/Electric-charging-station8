Feature: Delete Location
  As an Operator
  I want to delete a Location
  so that the Charging Network map and information stays up to date.

  Background:
    Given a new Location Manager
    And the following Locations exist:
      | Location ID | Name            |
      | LOC-001     | Central Station |
      | LOC-002     | North Station   |

  Scenario: Delete an existing location
    When the Operator deletes the Location with Location ID "LOC-001"
    Then the Location with Location ID "LOC-001" does not exist in the Charging Network
    And the Location with Location ID "LOC-002" exists in the Charging Network