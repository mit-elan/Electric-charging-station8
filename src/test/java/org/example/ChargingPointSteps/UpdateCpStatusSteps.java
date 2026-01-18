package org.example.ChargingPointSteps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.ChargingPoint;
import org.example.enums.OperatingStatus;
import org.example.managers.ChargingPointManager;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class UpdateCpStatusSteps {
    private final ChargingPointManager chargingPointManager =
            ChargingPointManager.getInstance();

    @Then("the status of charging point {string} is {string}")
    public void the_status_of_charging_point_is(String chargingPointId, String expectedStatus) {

        ChargingPoint chargingPoint =
                chargingPointManager.getChargingPointById(chargingPointId);

        assertEquals(
                OperatingStatus.valueOf(expectedStatus),
                chargingPoint.getOperatingStatus(),
                "Charging point status should match expected value"
        );
    }

    @When("the operator sets the status of charging point {string} to {string}")
    public void the_operator_sets_the_status_of_charging_point_to(
            String chargingPointId,
            String newStatus
    ) {

        ChargingPoint chargingPoint =
                chargingPointManager.getChargingPointById(chargingPointId);

        chargingPoint.setOperatingStatus(
                OperatingStatus.valueOf(newStatus)
        );
    }

}
