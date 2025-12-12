package LocationSteps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Location;
import org.example.managers.LocationManager;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

public class CreateLocationSteps {
    private final LocationManager locationManager = LocationManager.getInstance();

    // Use an instance variable to store any expected exceptions
    private Exception caughtException = null;


    @When("the Operator creates a new Location with the following details:")
    public void the_operator_creates_a_new_location_with_the_following_details(DataTable dataTable) {
        // Use asMaps to safely handle the vertical table structure
        List<Map<String, String>> dataList = dataTable.asMaps(String.class, String.class);

        // Assuming only one row of data for "a new Location"
        if (dataList.isEmpty()) {
            throw new IllegalArgumentException("Location details table cannot be empty.");
        }

        Map<String, String> data = dataList.get(0); // Get the first (and only) row

        try {
            locationManager.createLocation(
                    data.get("Location ID"),
                    data.get("Name"),
                    data.get("Address")
            );
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Then("the Location with Location ID {string} exists in the Charging Network")
    public void the_location_with_location_id_exists_in_the_charging_network(String locationID) {
        Location location = locationManager.getLocation(locationID);
        Assertions.assertNotNull(location, "Location with ID " + locationID + " should exist.");
    }

    // Dynamic Then step to check name and address
    @Then("the Location with Location ID {string} has {word} {string}")
    public void the_location_with_location_id_has_field_value(String locationID, String field, String expectedValue) {
        Location location = locationManager.getLocation(locationID);
        Assertions.assertNotNull(location, "Location should exist before checking details.");

        switch (field) {
            case "Name":
                Assertions.assertEquals(expectedValue, location.getName(), "Name mismatch.");
                break;
            case "Address":
                Assertions.assertEquals(expectedValue, location.getAddress(), "Address mismatch.");
                break;
            default:
                Assertions.fail("Unknown field: " + field);
        }
    }
}


