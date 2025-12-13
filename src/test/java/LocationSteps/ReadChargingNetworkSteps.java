package org.example.steps;

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

import static org.junit.jupiter.api.Assertions.*;

public class ReadChargingNetworkSteps {

    private LocationManager locationManager;
    private ChargingPointManager chargingPointManager;
    private List<Location> locations;

    @Given("a Charging Network with Locations")
    public void a_charging_network_with_locations() {
        locationManager = LocationManager.getInstance();
        chargingPointManager = new ChargingPointManager();

        Location location = locationManager.createLocation(
                "LOC-1",
                "Main Location",
                "Main Address"
        );
    }

    @Given("each Location has Charging Points")
    public void each_location_has_charging_points() {
        Location location = locationManager.getLocationByID("LOC-1");

        chargingPointManager.createChargingPoint(
                location,
                "CP-AC-1",
                Mode.AC
        );

        chargingPointManager.createChargingPoint(
                location,
                "CP-DC-1",
                Mode.DC
        );
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

    @Then("each Location shows its Charging Points")
    public void each_location_shows_its_charging_points() {
        for (Location location : locations) {
            assertNotNull(location.getChargingPoints());
            assertFalse(location.getChargingPoints().isEmpty());
        }
    }

    @Then("each Charging Point shows its price")
    public void each_charging_point_shows_its_price() {
        for (Location location : locations) {
            for (ChargingPoint chargingPoint : location.getChargingPoints()) {
                assertTrue(chargingPoint.getPrice() >= 0);
            }
        }
    }

    @Then("each Charging Point shows its Operating Status")
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
