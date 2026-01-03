package org.example.LocationSteps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.example.entities.Location;
import org.example.managers.LocationManager;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocationSystemSteps {

    @Given("a new Location Manager")
    public void aNewLocationManager() {
        LocationManager.getInstance().clearLocations();
    }

    private final LocationManager locationManager = LocationManager.getInstance();

    @Then("the Location with Location ID {string} exists in the Charging Network")
    public void the_location_with_location_id_exists_in_the_charging_network(String locationID) {
        Location location = locationManager.getLocation(locationID);
        Assertions.assertNotNull(location, "Location with ID " + locationID + " should exist.");
    }

    // Dynamic Then step to check name and address
    @And("the Location with Location ID {string} has Name {string}")
    public void location_has_name(String locationID, String expectedName) {
        Location location = locationManager.getLocation(locationID);
        Assertions.assertEquals(expectedName, location.getName());
    }

    @And("the Location with Location ID {string} has Address {string}")
    public void location_has_address(String locationID, String expectedAddress) {
        Location location = locationManager.getLocation(locationID);
        Assertions.assertEquals(expectedAddress, location.getAddress());
    }

}
