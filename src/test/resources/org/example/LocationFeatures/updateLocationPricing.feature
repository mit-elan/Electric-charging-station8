Feature: Update Location Pricing
  As an Operator
  I want to update the price of services for a specific Location
  so that I can adjust for market demand over time.

  Background:
    Given a new Location Manager
    And a new Charging Point Manager
    And the following Locations exist:
      | Location ID | Address       | Name          |
      | LOC-200     | Energy Road 5 | East Station  |
      | LOC-300     | New Road 300  | North Station |

    And the Location "LOC-200" has charging Points
      | Charging Point ID | Mode |
      | CP-1              | AC   |
      | CP-2              | DC   |

  Scenario: Successfully updating AC and DC pricing for a location with validity timestamp
    When the Operator sets the pricing for Location "LOC-200" valid from "01-03-2026 08:00":
      | Mode | Price per kWh | Price per minute |
      | AC   | 0.35          | 0.05             |
      | DC   | 0.60          | 0.10             |

    Then Location "LOC-200" has the following active tariffs:
      | Mode | Price per kWh | Price per minute | Valid from         |
      | AC   | 0.35          | 0.05             | 01-03-2026 08:00     |
      | DC   | 0.60          | 0.10             | 01-03-2026 08:00     |

  Scenario: Error - Set negative pricing values
    When the Operator attempts to set pricing for Location "LOC-200" with negative values
    Then an exception is thrown indicating prices cannot be negative

  Scenario: Edge Case - Set pricing with zero values
    When the Operator sets the pricing for Location "LOC-200" valid from "01-04-2026 08:00":
      | Mode | Price per kWh | Price per minute |
      | AC   | 0.00          | 0.00             |
    Then Location "LOC-200" has the following active tariffs:
      | Mode | Price per kWh | Price per minute | Valid from       |
      | AC   | 0.00          | 0.00             | 01-04-2026 08:00 |