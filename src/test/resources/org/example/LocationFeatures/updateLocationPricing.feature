Feature: Update Location Pricing
  As an Operator
  I want to update the price of services for a specific Location
  so that I can adjust for market demand.

  Background:
    Given a new Location Manager
    And a new Charging Point Manager
    And the following Locations exist:
      | Location ID | Address         | Name            |
      | LOC-200     | Energy Road 5   | East Station    |
    And the Location has charging Points
      | Charging Point ID | Mode |
      | CP-1              | AC   |
      | CP-2              | DC   |

  Scenario: Successfully updating AC and DC pricing for a location
    When the Operator updates the pricing of Location "LOC-200" to:
      | AC Price | DC Price |
      | 0.35     | 0.60     |
    Then the AC price of Location "LOC-200" is 0.35
    And the DC price of Location "LOC-200" is 0.60
    And all AC Charging Points at Location "LOC-200" have price 0.35
    And all DC Charging Points at Location "LOC-200" have price 0.60
