Feature: Update Location Address
  As an Operator
  I want to update the address of a Location
  so that the Charging Network information stays accurate.

  Background:
    Given a new Location Manager
    And the following Locations exist:
      | Location ID | Address        | Name           |
      | LOC-300     | Old Street 10  | South Station  |

  Scenario: Successfully updating a Location address
    When the Operator updates the address of Location "LOC-300" to "New Avenue 25"
    Then the Location with Location ID "LOC-300" has Address "New Avenue 25"

  Scenario: Error - Update address of non-existing location
    When the Operator attempts to update address of Location "LOC-999" to "New Street"
    Then an exception is thrown indicating location not found

  Scenario: Edge Case - Update address to empty string
    When the Operator attempts to update the address of Location "LOC-300" to ""
    Then an exception is thrown indicating address cannot be empty