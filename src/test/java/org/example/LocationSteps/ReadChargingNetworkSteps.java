package org.example.LocationSteps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.ChargingPoint;
import org.example.entities.Location;
import org.example.enums.Mode;
import org.example.enums.OperatingStatus;
import org.example.managers.ChargingPointManager;
import org.example.managers.LocationManager;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ReadChargingNetworkSteps {
    private final LocationManager locationManager = LocationManager.getInstance();
    private final ChargingPointManager chargingPointManager = ChargingPointManager.getInstance();
    private List<Location> locations;


    @Given("a Charging Network with Locations")
    public void a_charging_network_with_locations(DataTable dataTable) {

        for (Map<String, String> row : dataTable.asMaps()) {
            locationManager.createLocation(
                    row.get("locationID"),
                    row.get("name"),
                    row.get("address")
            );
        }
    }

    @And("the Locations have Charging Points")
    public void the_locations_have_charging_points(DataTable dataTable) {

        for (Map<String, String> row : dataTable.asMaps()) {
            Location location = locationManager.getLocationByID(row.get("locationID"));

            chargingPointManager.createChargingPoint(
                    location,
                    row.get("chargingPointID"),
                    Mode.valueOf(row.get("mode"))
            );
        }
    }


    @When("the customer reads the Charging Network")
    public void the_customer_reads_the_charging_network() {
        locations = locationManager.getAllLocations();
    }

    @Then("a list of Locations is shown")
    public void a_list_of_locations_is_shown() {
        assertNotNull(locations);
        assertFalse(locations.isEmpty());
    }

    @And("each Location shows its Charging Points")
    public void each_location_shows_its_charging_points() {
        for (Location location : locations) {
            assertNotNull(location.getChargingPoints());
            assertFalse(location.getChargingPoints().isEmpty());
        }
    }

    @And("each Charging Point shows its price")
    public void each_charging_point_shows_its_price() {
        for (Location location : locations) {
            for (ChargingPoint chargingPoint : location.getChargingPoints()) {
                assertTrue(chargingPoint.getPrice() >= 0);
            }
        }
    }

    @And("each Charging Point shows its Operating Status")
    public void each_charging_point_shows_its_operating_status() {
        for (Location location : locations) {
            for (ChargingPoint chargingPoint : location.getChargingPoints()) {
                assertNotNull(chargingPoint.getOperatingStatus());
                assertTrue(
                        chargingPoint.getOperatingStatus() == OperatingStatus.IN_OPERATION_FREE
                                || chargingPoint.getOperatingStatus() == OperatingStatus.OCCUPIED
                                || chargingPoint.getOperatingStatus() == OperatingStatus.OUT_OF_ORDER
                );
            }
        }
    }
}
