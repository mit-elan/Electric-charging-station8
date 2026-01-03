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
