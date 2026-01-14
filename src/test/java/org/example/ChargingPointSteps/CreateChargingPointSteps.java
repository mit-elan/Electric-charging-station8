package org.example.ChargingPointSteps;

import io.cucumber.java.en.*;
import org.example.entities.ChargingPoint;
import org.example.entities.Location;
import org.example.enums.ChargingMode;
import org.example.managers.ChargingPointManager;
import org.example.managers.LocationManager;

import static org.junit.jupiter.api.Assertions.*;

public class CreateChargingPointSteps {
    private final LocationManager locationManager;
    private final ChargingPointManager chargingPointManager;

    // Background:
    //    Given a new Location Manager
    //    And a new Charging Point Manager
    public CreateChargingPointSteps() {
        locationManager = LocationManager.getInstance();        // <-- initialize singleton for Location Manager
        chargingPointManager = ChargingPointManager.getInstance(); // <-- singleton for CP manager
    }


    @And("a Location with ID {string} and name {string} and address {string}")
    public void a_location_with_id_and_name_and_address(String locationID, String name, String address) {
        Location location = locationManager.createLocation(locationID, name, address);
    }

    @When("the Operator creates a Charging Point with ID {string}, mode {string}, at Location {string}")
    public void the_operator_creates_a_charging_point(String chargingPointID, String modeStr, String locationID) {
        ChargingMode chargingMode = ChargingMode.valueOf(modeStr);
        Location targetLocation = locationManager.getLocationByID(locationID);
        chargingPointManager.createChargingPoint(targetLocation, chargingPointID, chargingMode);
    }

    @Then("the Charging Point with ID {string} exists at Location {string}")
    public void the_charging_point_exists_at_location(String chargingPointID, String locationID) {
        Location targetLocation = locationManager.getLocationByID(locationID);
        boolean exists = targetLocation.getChargingPoints().stream()
                .anyMatch(cp -> cp.getChargingPointID().equals(chargingPointID));
        assertTrue(exists, "Charging Point " + chargingPointID + " does not exist at Location " + locationID);
    }

    @And("the Charging Point with ID {string} has mode {string}")
    public void the_charging_point_has_mode(String chargingPointID, String modeStr) {
        ChargingMode chargingMode = ChargingMode.valueOf(modeStr);
        ChargingPoint cp = chargingPointManager.getChargingPoints().stream()
                .filter(c -> c.getChargingPointID().equals(chargingPointID))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Charging Point " + chargingPointID + " not found"));
        assertEquals(chargingMode, cp.getMode(), "Charging Point mode does not match");
    }
}
