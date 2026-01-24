package org.example.LocationChargingNetwork;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.*;
import org.example.enums.ChargingMode;
import org.example.enums.OperatingStatus;
import org.example.managers.ChargingPointManager;
import org.example.managers.LocationManager;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ReadChargingNetworkSteps {
    private final LocationManager locationManager = LocationManager.getInstance();
    private final ChargingPointManager chargingPointManager = ChargingPointManager.getInstance();
    private List<Location> locations;

    @Given("a new Charging Network with Locations")
    public void a_new_charging_network_with_locations(DataTable dataTable) {
        // Clear any existing locations if needed
        locationManager.getAllLocations().clear();

        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            String locationID = row.get("locationID");
            String name = row.get("name");
            String address = row.get("address");

            // Create the location using LocationManager
            Location location = locationManager.createLocation(locationID, name, address);

            // Optionally print for debug
            System.out.println("Created Location: " + location.getLocationID() + " | " + location.getName());
        }
    }

    @And("the Locations have Charging Points")
    public void the_locations_have_charging_points(DataTable dataTable) {

        for (Map<String, String> row : dataTable.asMaps()) {
            Location location = locationManager.getLocationByID(row.get("locationID"));

            chargingPointManager.createChargingPoint(
                    location,
                    row.get("chargingPointID"),
                    ChargingMode.valueOf(row.get("mode"))
            );
        }
    }

    @And("the following tariffs exists:")
    public void the_following_tariffs_exists(DataTable dataTable) {

        for (Map<String, String> row : dataTable.asMaps()) {
            Location location = locationManager.getLocationByID(row.get("locationID"));
            assertNotNull(location, "Location must exist");

            // AC tariff
            location.addTariff(new Tariff(
                    ChargingMode.AC,
                    Double.parseDouble(row.get("AC price/kWh")),
                    Double.parseDouble(row.get("AC price/min")),
                    java.time.LocalDateTime.now()
            ));

            // DC tariff
            location.addTariff(new Tariff(
                    ChargingMode.DC,
                    Double.parseDouble(row.get("DC price/kWh")),
                    Double.parseDouble(row.get("DC price/min")),
                    java.time.LocalDateTime.now()
            ));
        }
    }

    @And("a charging session exists at Charging Point {string}")
    public void a_charging_session_exists_at_charging_point(String chargingPointId) {

        ChargingPoint chargingPoint =
                chargingPointManager.getChargingPointById(chargingPointId);

        assertNotNull(chargingPoint, "Charging Point must exist");

        // Fake account (internal only)
        Account fakeAccount = new Account(
                "FAKE-CUST",
                "Fake Customer",
                "fake@mail.com",
                "password"
        );

        // Create session
        ChargingSession session = new ChargingSession(
                "FAKE-SESSION-" + chargingPointId,
                java.time.LocalDateTime.now(),
                chargingPoint
        );

        session.setAccount(fakeAccount);
    }




    @When("the customer reads the Charging Network")
    public void the_customer_reads_the_charging_network() {
        locations = locationManager.getAllLocations();
    }

    @Then("a list of all Locations is shown")
    public void a_list_of_all_locations_is_shown() {
        assertNotNull(locations);
        assertFalse(locations.isEmpty());
    }

    @And("each Location shows its current tariffs")
    public void each_location_shows_its_current_tariffs() {

        for (Location location : locations) {
            assertNotNull(location.getTariffs());
            assertFalse(location.getTariffs().isEmpty());

            boolean hasAC = location.getTariffs().stream()
                    .anyMatch(t -> t.mode() == ChargingMode.AC);

            boolean hasDC = location.getTariffs().stream()
                    .anyMatch(t -> t.mode() == ChargingMode.DC);

            assertTrue(hasAC, "Location must have AC tariff");
            assertTrue(hasDC, "Location must have DC tariff");
        }
    }


    @And("each Location shows its Charging Points")
    public void each_location_shows_its_charging_points() {
        for (Location location : locations) {
            assertNotNull(location.getChargingPoints());
            assertFalse(location.getChargingPoints().isEmpty());
        }
    }

    @And("each Charging Point shows its Operating Status")
    public void each_charging_point_shows_its_operating_status() {
        for (Location location : locations) {
            for (ChargingPoint chargingPoint : location.getChargingPoints()) {
                assertNotNull(chargingPoint.getOperatingStatus());
                assertTrue(
                        chargingPoint.getOperatingStatus() == OperatingStatus.IN_OPERATION_FREE
                                || chargingPoint.getOperatingStatus() == OperatingStatus.OCCUPIED
                                || chargingPoint.getOperatingStatus() == OperatingStatus.OUT_OF_ORDER
                );
            }
        }
    }

    @Then("an empty location list is shown")
    public void anEmptyLocationListIsShown() {
        assertTrue(locations == null || locations.isEmpty());
    }

    @Then("the Location {string} shows message {string}")
    public void theLocationShowsMessage(String locationId, String expectedMessage) {
        Location location = locationManager.getLocationByID(locationId);
        String output = location.toString();
        assertTrue(output.contains(expectedMessage));
    }
}
