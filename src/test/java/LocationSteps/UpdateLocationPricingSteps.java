package LocationSteps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import org.example.entities.ChargingPoint;
import org.example.entities.Location;
import org.example.managers.LocationManager;
import org.example.enums.Mode;


import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateLocationPricingSteps {
    private final LocationManager locationManager = LocationManager.getInstance();


    @Given("a location exists with ID {string} and the following pricing:")
    public void a_location_exists_with_pricing(String locationID, DataTable table) {

        Location location = locationManager.createLocation(locationID, "TestLocation", "123 Test Street");

        for (Map<String, String> row : table.asMaps()) {
            String mode = row.get("Mode");
            double price = Double.parseDouble(row.get("Price"));

            if (mode.equalsIgnoreCase("AC")) {
                location.setAcPrice(price);
            } else if (mode.equalsIgnoreCase("DC")) {
                location.setDcPrice(price);
            }
        }
    }

    @Given("the location {string} has the following charging points:")
    public void location_has_charging_points(String locationID, DataTable table) {
        Location location = locationManager.getLocation(locationID);
        assertNotNull(location, "Location must exist before adding charging points.");

        for (Map<String, String> row : table.asMaps()) {
            String cpID = row.get("ChargingPoint ID");
            Mode mode = Mode.valueOf(row.get("Mode"));

            ChargingPoint cp = new ChargingPoint(cpID, mode);

            location.addChargingPoint(cp);
        }
    }

    @When("the operator updates the pricing for location {string} to:")
    public void operator_updates_pricing(String locationID, DataTable table) {
        double newAC = 0;
        double newDC = 0;

        for (Map<String, String> row : table.asMaps()) {
            String mode = row.get("Mode");
            double price = Double.parseDouble(row.get("Price"));

            if (mode.equalsIgnoreCase("AC")) newAC = price;
            if (mode.equalsIgnoreCase("DC")) newDC = price;
        }

        locationManager.updatePricing(locationID, newAC, newDC);
    }

    @Then("the pricing for location {string} should be:")
    public void pricing_should_be(String locationID, DataTable table) {
        Location location = locationManager.getLocation(locationID);
        assertNotNull(location);

        for (Map<String, String> row : table.asMaps()) {
            String mode = row.get("Mode");
            double expected = Double.parseDouble(row.get("Price"));

            if (mode.equalsIgnoreCase("AC")) {
                assertEquals(expected, location.getAcPrice());
            } else if (mode.equalsIgnoreCase("DC")) {
                assertEquals(expected, location.getDcPrice());
            }
        }
    }

    @Then("all AC charging points at location {string} should have price {double}")
    public void all_ac_points_should_have_price(String locationID, double expectedPrice) {
        Location location = locationManager.getLocation(locationID);

        for (ChargingPoint cp : location.getChargingPoints()) {
            if (cp.getMode() == Mode.AC) {
                assertEquals(expectedPrice, cp.getPrice());
            }
        }
    }

    @Then("all DC charging points at location {string} should have price {double}")
    public void all_dc_points_should_have_price(String locationID, double expectedPrice) {
        Location location = locationManager.getLocation(locationID);

        for (ChargingPoint cp : location.getChargingPoints()) {
            if (cp.getMode() == Mode.AC) {
                assertEquals(expectedPrice, cp.getPrice());
            }
        }
    }
}
