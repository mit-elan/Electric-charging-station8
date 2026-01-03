package org.example.LocationSteps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Location;
import org.example.managers.LocationManager;
import org.junit.jupiter.api.Assertions;

public class UpdateLocationAddressSteps {

    private final LocationManager locationManager = LocationManager.getInstance();

    @When("the Operator updates the address of Location {string} to {string}")
    public void the_operator_updates_the_address_of_location(
            String locationID,
            String newAddress) {

        locationManager.updateLocationAddress(locationID, newAddress);
    }
}
