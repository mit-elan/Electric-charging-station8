package org.example.LocationSteps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Location;
import org.example.managers.LocationManager;
import org.junit.jupiter.api.Assertions;
import org.example.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateLocationAddressSteps {

    private final LocationManager locationManager = LocationManager.getInstance();

    //private Exception caughtException;

    @When("the Operator updates the address of Location {string} to {string}")
    public void the_operator_updates_the_address_of_location(
            String locationID,
            String newAddress) {

        locationManager.updateLocationAddress(locationID, newAddress);
    }

    @When("the Operator attempts to update address of Location {string} to {string}")
    public void theOperatorAttemptsToUpdateAddressOfLocation(String locationId, String newAddress) {
        try {
            locationManager.updateLocationAddress(locationId, newAddress);
        } catch (IllegalArgumentException e) {
            ScenarioContext.lastException = e;
        }
    }

    @Then("an exception is thrown indicating address cannot be empty")
    public void anExceptionIsThrownIndicatingAddressCannotBeEmpty() {
        assertNotNull(ScenarioContext.lastException);
        assertInstanceOf(IllegalArgumentException.class, ScenarioContext.lastException);
        assertTrue(ScenarioContext.lastException.getMessage().contains("cannot be null or empty"));
    }
}
