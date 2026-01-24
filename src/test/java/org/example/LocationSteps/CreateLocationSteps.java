package org.example.LocationSteps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Location;
import org.example.managers.LocationManager;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateLocationSteps {
    private final LocationManager locationManager = LocationManager.getInstance();

    // Use an instance variable to store any expected exceptions
    private Exception caughtException = null;


    @When("the Operator creates new Locations with the following details:")
    public void the_operator_creates_new_locations_with_the_following_details(DataTable dataTable) {

        List<Map<String, String>> dataList = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> data : dataList) {
            try {
                locationManager.createLocation(
                        data.get("Location ID"),
                        data.get("Name"),
                        data.get("Address")
                );
            } catch (Exception e) {
                caughtException = e;
                // Stop immediately so we know WHICH row failed
                throw e;
            }
        }
    }

    @When("the Operator attempts to create a Location with duplicate ID {string}")
    public void theOperatorAttemptsToCreateLocationWithDuplicateId(String locationId) {
        try {
            locationManager.createLocation(locationId, "Duplicate Name", "Duplicate Address");
        } catch (IllegalArgumentException e) {
            caughtException = e;
        }
    }

    @Then("an exception is thrown indicating location already exists")
    public void anExceptionIsThrownIndicatingLocationAlreadyExists() {
        assertNotNull(caughtException);
        assertInstanceOf(IllegalArgumentException.class, caughtException);
        assertTrue(caughtException.getMessage().contains("already exists"));
    }
}



