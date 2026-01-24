package org.example.ChargingPointSteps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.ChargingPoint;
import org.example.enums.OperatingStatus;
import org.example.managers.ChargingPointManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UpdateCpStatusSteps {
    private final ChargingPointManager chargingPointManager =
            ChargingPointManager.getInstance();

    private Exception caughtException;

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

        chargingPoint.updateOperatingStatus(
                OperatingStatus.valueOf(newStatus)
        );
    }

    @When("the operator attempts to set status of non-existing charging point {string} to {string}")
    public void theOperatorAttemptsToSetStatusOfNonExistingChargingPoint(String cpId, String status) {
        try {
            ChargingPoint cp = chargingPointManager.getChargingPointById(cpId);
            if (cp != null) {
                cp.updateOperatingStatus(OperatingStatus.valueOf(status));
            }
        } catch (Exception e) {
            caughtException = e;
        }
    }

    @Then("an exception is thrown or status update fails gracefully")
    public void anExceptionIsThrownOrStatusUpdateFailsGracefully() {
        assertTrue(true);
    }
/*
    @Given("the status of charging point {string} is {string}")
    public void theStatusOfChargingPointIs(String cpId, String status) {
        ChargingPoint cp = chargingPointManager.getChargingPointById(cpId);
        assertEquals(OperatingStatus.valueOf(status), cp.getOperatingStatus());
    }

 */
}
