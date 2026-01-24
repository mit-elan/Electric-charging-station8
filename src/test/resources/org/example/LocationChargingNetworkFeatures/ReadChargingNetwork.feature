#Customers and owners can find out about the current status of the charging stations in
#the station network at any time. For this purpose, a list is to be issued that contains the
#currently valid prices and the operating status of the individual charging points for each
#location.

Feature: Read Charging Network
  As a customer
  I want to see a list of the currently valid prices and the Operating Status
  of the individual Charging Points for each Location
  so that I can find out about the available charging options in my area

  Background:
    Given a new Location Manager
    And a new Charging Point Manager

  Scenario: Customer views charging network overview
    Given a new Charging Network with Locations
      | locationID | name          | address          |
      | LOC-1      | Airport       | Airport Street 1 |
      | LOC-2      | Train Station | Train Street 2   |

    And the following tariffs exists:
      | locationID | AC price/kWh | AC price/min | DC price/kWh | DC price/min |
      | LOC-1      | 0.35         | 0.05         | 0.55         | 0.10         |
      | LOC-2      | 0.30         | 0.04         | 0.60         | 0.12         |


    And the Locations have Charging Points
      | locationID | chargingPointID | mode |
      | LOC-1      | CP-AC-1         | AC   |
      | LOC-1      | CP-DC-1         | DC   |
      | LOC-2      | CP-DC-2         | DC   |
      | LOC-2      | CP-DC-3         | DC   |

    And the operator sets the status of charging point "CP-AC-1" to "OUT_OF_ORDER"
    And a charging session exists at Charging Point "CP-DC-2"

    When the customer reads the Charging Network
    Then a list of all Locations is shown
    And each Location shows its Charging Points
    And each Location shows its current tariffs
    And each Charging Point shows its Operating Status

  Scenario: Error - Read charging network when no locations exist
    Given a new Location Manager
    When the customer reads the Charging Network
    Then an empty location list is shown

  Scenario: Edge Case - Read charging network with location having no charging points
    Given a new Charging Network with Locations
      | locationID | name          | address          |
      | LOC-EMPTY  | Empty Station | Empty Street 1   |
    And the following tariffs exists:
      | locationID | AC price/kWh | AC price/min | DC price/kWh | DC price/min |
      | LOC-EMPTY  | 0.35         | 0.05         | 0.55         | 0.10         |
    When the customer reads the Charging Network
    Then the Location "LOC-EMPTY" shows message "Coming soon..."