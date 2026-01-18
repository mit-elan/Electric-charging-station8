package org.example.ChargingPointSteps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Location;
import org.example.managers.ChargingPointManager;
import org.example.managers.LocationManager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DeleteChargingPointSteps {

    private final LocationManager locationManager =
            LocationManager.getInstance();

    private final ChargingPointManager chargingPointManager =
            ChargingPointManager.getInstance();

    // -------------------------
    // WHEN: delete charging point from location
    // -------------------------
    @When("the operator deletes the Charging Point with ID {string} from Location {string}")
    public void the_operator_deletes_the_charging_point_from_location(
            String chargingPointId,
            String locationId
    ) {
        Location location = locationManager.getLocationByID(locationId);

        chargingPointManager.deleteChargingPoint(
                chargingPointId,
                location
        );
    }

    // -------------------------
    // THEN: charging point no longer exists at location
    // -------------------------
    @Then("the Charging Point with ID {string} does not exist at Location {string}")
    public void the_charging_point_does_not_exist_at_location(
            String chargingPointId,
            String locationId
    ) {
        Location location = locationManager.getLocationByID(locationId);

        boolean existsAtLocation = location.getChargingPoints().stream()
                .anyMatch(cp -> cp.getChargingPointID().equals(chargingPointId));

        assertFalse(
                existsAtLocation,
                "Charging Point " + chargingPointId +
                        " should not exist at Location " + locationId
        );
    }

    // -------------------------
    // THEN: charging point not visible in charging network
    // -------------------------
    @Then("the Charging Point with ID {string} is not shown in the Charging Network")
    public void the_charging_point_is_not_shown_in_the_charging_network(
            String chargingPointId
    ) {
        assertNull(
                chargingPointManager.getChargingPointById(chargingPointId),
                "Charging Point should not be visible in the Charging Network"
        );
    }
}
