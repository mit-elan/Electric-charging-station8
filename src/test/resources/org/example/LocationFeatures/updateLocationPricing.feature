Feature: Update Location Pricing
  As an operator
  I want to update the price of services for a specific location
  so that I can adjust for market demand.

  Background:
    Given a new Location Manager
    And a location exists with ID "LOC-001" and the following pricing:
      | Mode | Price |
      | AC   | 0.30  |
      | DC   | 0.45  |
    And the location "LOC-001" has the following charging points:
      | ChargingPoint ID | Mode |
      | CP-01            | AC   |
      | CP-02            | AC   |
      | CP-03            | DC   |

  Scenario: Update AC and DC pricing for a location
    When the operator updates the pricing for location "LOC-001" to:
      | Mode | Price |
      | AC   | 0.35  |
      | DC   | 0.50  |
    Then the pricing for location "LOC-001" should be:
      | Mode | Price |
      | AC   | 0.35  |
      | DC   | 0.50  |
    And all AC charging points at location "LOC-001" should have price 0.35
    And all DC charging points at location "LOC-001" should have price 0.50

  Scenario: Update pricing multiple times per day
    When the operator updates the pricing for location "LOC-001" to:
      | Mode | Price |
      | AC   | 0.40  |
      | DC   | 0.55  |
    And the operator updates the pricing for location "LOC-001" to:
      | Mode | Price |
      | AC   | 0.45  |
      | DC   | 0.60  |
    Then the pricing for location "LOC-001" should be:
      | Mode | Price |
      | AC   | 0.45  |
      | DC   | 0.60  |
