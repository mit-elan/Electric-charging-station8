package LocationSteps;

import io.cucumber.java.en.*;
import org.example.entities.Location;
import org.example.managers.LocationManager;
import org.junit.jupiter.api.Assertions;
import java.util.Map;

public class DeleteLocationSteps {
    private final LocationManager locationManager = LocationManager.getInstance();
    private Exception capturedException;

    @Given("the following Locations exist:")
    public void the_following_locations_exist(io.cucumber.datatable.DataTable dataTable) {
        for (Map<String, String> row : dataTable.asMaps()) {
            String ID = row.get("Location ID");
            String name = row.get("Name");
            String address = row.get("Address");

            Location location = new Location(ID, name, address);
            locationManager.addLocation(location);
        }
    }

    @When("the Operator deletes the Location with Location ID {string}")
    public void the_operator_deletes_the_location_with_ID(String locationID) {
        try {
            locationManager.deleteLocation(locationID);
        } catch (Exception e) {
            capturedException = e;
        }
    }

    @Then("the Location with Location ID {string} does not exist in the Charging Network anymore")
    public void the_location_does_not_exist(String locationID) {
        Location location = locationManager.getLocationByID(locationID);
        Assertions.assertNull(location, "Expected location " + locationID + " to be deleted, but it still exists.");
    }

}
