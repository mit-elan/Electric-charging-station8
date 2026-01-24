Feature: Delete Location
  As an Operator
  I want to delete a Location
  so that the Charging Network and information stays up to date.

  Background:
    Given a new Location Manager
    And the following Locations exist:
      | Location ID | Name            | Address           |
      | LOC-001     | Central Station | Central-Street 12 |
      | LOC-002     | North Station   | North-Street 44   |

  Scenario: Delete an existing location
    When the Operator deletes the Location with Location ID "LOC-001"
    Then the Location with Location ID "LOC-001" does not exist in the Charging Network anymore

  Scenario: Error - Delete non-existing location
    When the Operator attempts to delete the Location with Location ID "LOC-999"
    Then an exception is thrown indicating location not found

  Scenario: Edge Case - Delete location with active charging points
    Given the Location "LOC-002" has charging Points
      | Charging Point ID | Mode |
      | CP-ACTIVE         | AC   |
    When the Operator deletes the Location with Location ID "LOC-002"
    Then the Location with Location ID "LOC-002" does not exist in the Charging Network anymore
    And the Charging Point "CP-ACTIVE" is also removed from the system