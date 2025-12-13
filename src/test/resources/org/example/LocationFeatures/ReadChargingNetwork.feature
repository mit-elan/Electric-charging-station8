Feature: Read Charging Network
  As a customer
  I want to see a list of the currently valid prices and the operating status
  of the individual charging points for each location
  so that I can find out about the available charging options in my area

  Scenario: Customer views charging network overview
    Given a Charging Network with Locations
    And each Location has Charging Points
    When the customer reads the Charging Network
    Then a list of Locations is shown
    And each Location shows its Charging Points
    And each Charging Point shows its price
    And each Charging Point shows its Operating Status

