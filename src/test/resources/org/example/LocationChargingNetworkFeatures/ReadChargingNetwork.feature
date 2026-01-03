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
    Given a Charging Network with Locations
      | locationID | name          | address        |
      | LOC-1      | Main Location | Main Address   |

    And the Locations have Charging Points
      | locationID | chargingPointID | mode |
      | LOC-1      | CP-AC-1         | AC   |
      | LOC-1      | CP-DC-1         | DC   |

    When the customer reads the Charging Network
    Then a list of Locations is shown
    And each Location shows its Charging Points
    And each Charging Point shows its price
    And each Charging Point shows its Operating Status
