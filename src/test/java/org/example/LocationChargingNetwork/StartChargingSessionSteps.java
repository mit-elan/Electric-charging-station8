package org.example.LocationChargingNetwork;

import io.cucumber.java.en.*;
import org.example.entities.*;
import org.example.managers.*;
import org.example.enums.OperatingStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StartChargingSessionSteps {

    // Managers
    private final AccountManager accountManager = AccountManager.getInstance();
    private final ChargingSessionManager sessionManager = ChargingSessionManager.getInstance();
    private ChargingPointManager chargingPointManager = ChargingPointManager.getInstance();

    private Exception caughtException;

    @Given("the Charging Point {string} is InOperationFree")
    public void the_charging_point_is_in_operation_free(String cpId) {
        ChargingPoint cp = chargingPointManager.getChargingPointById(cpId);
        cp.updateOperatingStatus(OperatingStatus.IN_OPERATION_FREE);
    }

    @When("the Customer {string} physically connects their car to Charging Point {string}")
    public void the_customer_physically_connects_their_car_to_charging_point(
            String customerId,
            String chargingPointId
    ) {
        ChargingPoint cp =
                chargingPointManager.getChargingPointById(chargingPointId);

        cp.connectVehicle();
    }


    @When("the Customer {string} starts a charging session at Charging Point {string}")
    public void start_session(String custId, String cpId) {
        Account account = accountManager.getAccount(custId);
        ChargingPoint cp = chargingPointManager.getChargingPointById(cpId);
        sessionManager.createChargingSession(account, cp);
    }


    @Then("a charging session exists for Customer {string} at Charging Point {string}")
    public void verify_session_exists(String custId, String cpId) {
        // 1. Get the list from the instance (not the class)
        List<ChargingSession> sessions = ChargingSessionManager.getInstance().getChargingSessions();

        boolean found = false;

        // 2. Use a simple loop - very clear and easy to explain in a presentation
        for (ChargingSession session : sessions) {
            String sessionCustId = session.getAccount().getCustomerID();
            String sessionCpId = session.getChargingPoint().getChargingPointID();

            if (sessionCustId.equals(custId) && sessionCpId.equals(cpId)) {
                found = true;
                break;
            }
        }

        assertTrue(found, "Could not find a Charging Session for Customer " + custId + " at Point " + cpId);
    }

    @Then("the Charging Point {string} is marked as Occupied")
    public void verify_occupied(String cpId) {
        ChargingPoint cp = chargingPointManager.getChargingPointById(cpId);
        assertEquals(OperatingStatus.OCCUPIED, cp.getOperatingStatus());
    }

    @When("the Customer {string} attempts to start a charging session at Charging Point {string}")
    public void theCustomerAttemptsToStartAChargingSession(String custId, String cpId) {
        try {
            Account account = accountManager.getAccount(custId);
            ChargingPoint cp = chargingPointManager.getChargingPointById(cpId);
            sessionManager.createChargingSession(account, cp);
        } catch (IllegalStateException e) {
            caughtException = e;
        }
    }

    @Then("an exception is thrown indicating insufficient credit")
    public void anExceptionIsThrownIndicatingInsufficientCredit() {
        assertNotNull(caughtException);
        assertInstanceOf(IllegalStateException.class, caughtException);
        assertTrue(caughtException.getMessage().contains("Insufficient credit"));
    }

    @Then("an exception is thrown indicating charging point not available")
    public void anExceptionIsThrownIndicatingChargingPointNotAvailable() {
        assertNotNull(caughtException);
        assertInstanceOf(IllegalStateException.class, caughtException);
        assertTrue(caughtException.getMessage().contains("not available"));
    }

}

