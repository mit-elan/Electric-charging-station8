package org.example.LocationSteps;


import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.ChargingPoint;
import org.example.entities.Location;
import org.example.enums.Mode;
import org.example.managers.LocationManager;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

public class UpdateLocationPricingSteps {

    private final LocationManager locationManager = LocationManager.getInstance();

    @When("the Operator updates the pricing of Location {string} to:")
    public void the_operator_updates_the_pricing_of_location(
            String locationID,
            DataTable dataTable) {

        Map<String, String> data = dataTable.asMaps().get(0);

        double acPrice = Double.parseDouble(data.get("AC Price"));
        double dcPrice = Double.parseDouble(data.get("DC Price"));

        locationManager.updateLocationPricing(locationID, acPrice, dcPrice);
    }

    @Then("the AC price of Location {string} is {double}")
    public void the_ac_price_of_location_is(String locationID, double expectedPrice) {
        Location location = locationManager.getLocation(locationID);
        Assertions.assertEquals(expectedPrice, location.getAcPrice());
    }

    @Then("the DC price of Location {string} is {double}")
    public void the_dc_price_of_location_is(String locationID, double expectedPrice) {
        Location location = locationManager.getLocation(locationID);
        Assertions.assertEquals(expectedPrice, location.getDcPrice());
    }

    @Then("all AC Charging Points at Location {string} have price {double}")
    public void all_ac_charging_points_have_price(String locationID, double expectedPrice) {
        Location location = locationManager.getLocation(locationID);

        for (ChargingPoint cp : location.getChargingPoints()) {
            if (cp.getMode() == Mode.AC) {
                Assertions.assertEquals(expectedPrice, cp.getPrice());
            }
        }
    }

    @Then("all DC Charging Points at Location {string} have price {double}")
    public void all_dc_charging_points_have_price(String locationID, double expectedPrice) {
        Location location = locationManager.getLocation(locationID);

        for (ChargingPoint cp : location.getChargingPoints()) {
            if (cp.getMode() == Mode.DC) {
                Assertions.assertEquals(expectedPrice, cp.getPrice());
            }
        }
    }
}
