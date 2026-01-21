package org.example.LocationSteps;


import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Location;
import org.example.entities.Tariff;
import org.example.enums.ChargingMode;
import org.example.managers.LocationManager;
import org.junit.jupiter.api.Assertions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class UpdateLocationPricingSteps {

    private final LocationManager locationManager = LocationManager.getInstance();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    @When("the Operator sets the pricing for Location {string} valid from {string}:")
    public void the_operator_sets_pricing_with_valid_from(
            String locationID,
            String validFrom,
            DataTable table
    ) {
        Location location = locationManager.getLocation(locationID);
        Assertions.assertNotNull(location);

        LocalDateTime validFromTime = LocalDateTime.parse(validFrom, FORMATTER);

        for (Map<String, String> row : table.asMaps()) {
            ChargingMode mode = ChargingMode.valueOf(row.get("Mode"));
            double pricePerKwh = Double.parseDouble(row.get("Price per kWh"));
            double pricePerMinute = Double.parseDouble(row.get("Price per minute"));

            Tariff tariff = new Tariff(
                    mode,
                    pricePerKwh,
                    pricePerMinute,
                    validFromTime
            );

            location.addTariff(tariff);
        }
    }

    @Then("Location {string} has the following active tariffs:")
    public void location_has_active_tariffs(String locationID, DataTable table) {
        Location location = locationManager.getLocation(locationID);
        Assertions.assertNotNull(location);

        List<Tariff> tariffs = location.getTariffs();
        Assertions.assertEquals(table.asMaps().size(), tariffs.size());

        for (Map<String, String> row : table.asMaps()) {
            ChargingMode mode = ChargingMode.valueOf(row.get("Mode"));
            double expectedKwh = Double.parseDouble(row.get("Price per kWh"));
            double expectedMinute = Double.parseDouble(row.get("Price per minute"));
            LocalDateTime expectedValidFrom =
                    LocalDateTime.parse(row.get("Valid from"), FORMATTER);

            Tariff tariff = tariffs.stream()
                    .filter(t -> t.getMode() == mode)
                    .findFirst()
                    .orElseThrow();

            Assertions.assertEquals(expectedKwh, tariff.getPricePerKwh());
            Assertions.assertEquals(expectedMinute, tariff.getPricePerMinute());
            Assertions.assertEquals(expectedValidFrom, tariff.getValidFrom());
        }
    }
}
