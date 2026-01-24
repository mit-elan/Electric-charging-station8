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

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateLocationPricingSteps {

    private final LocationManager locationManager = LocationManager.getInstance();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    private Exception caughtException;

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
                    .filter(t -> t.mode() == mode)
                    .findFirst()
                    .orElseThrow();

            Assertions.assertEquals(expectedKwh, tariff.pricePerKwh());
            Assertions.assertEquals(expectedMinute, tariff.pricePerMinute());
            Assertions.assertEquals(expectedValidFrom, tariff.validFrom());
        }
    }

    @When("the Operator attempts to set pricing for Location {string} with negative values")
    public void theOperatorAttemptsToSetPricingWithNegativeValues(String locationId) {
        try {
            Location location = locationManager.getLocation(locationId);
            Tariff tariff = new Tariff(
                    ChargingMode.AC,
                    -0.50,  // negative price
                    0.05,
                    LocalDateTime.now()
            );
            location.addTariff(tariff);
        } catch (IllegalArgumentException e) {
            caughtException = e;
        }
    }

    @Then("an exception is thrown indicating prices cannot be negative")
    public void anExceptionIsThrownIndicatingPricesCannotBeNegative() {
        assertNotNull(caughtException);
        assertInstanceOf(IllegalArgumentException.class, caughtException);
        assertTrue(caughtException.getMessage().contains("must not be negative"));
    }
}
