package org.example.LocationChargingNetwork;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Account;
import org.example.entities.ChargingPoint;
import org.example.entities.ChargingSession;
import org.example.entities.Invoice;
import org.example.managers.*;

import java.time.LocalDateTime;
import java.util.function.BooleanSupplier;

import static org.junit.jupiter.api.Assertions.*;

public class EndChargingSessionSteps {

    private final AccountManager accountManager = AccountManager.getInstance();
    private final ChargingPointManager chargingPointManager = ChargingPointManager.getInstance();
    private final ChargingSessionManager chargingSessionManager = ChargingSessionManager.getInstance();
    private final InvoiceManager invoiceManager = InvoiceManager.getInstance();

    private ChargingSession activeSession;
    private Exception caughtException;

    // ---------------------------------------------------
    // GIVEN
    // ---------------------------------------------------

    @And("an active charging session with ID {string} exists for Customer {string} at Charging Point {string}")
    public void an_active_charging_session_exists(
            String sessionId,
            String customerId,
            String chargingPointId
    ) {
        Account account = accountManager.getAccount(customerId);
        ChargingPoint chargingPoint = chargingPointManager.getChargingPointById(chargingPointId);

        assertNotNull(account, "Account must exist");
        assertNotNull(chargingPoint, "Charging Point must exist");

        LocalDateTime now = LocalDateTime.now();

        activeSession = chargingSessionManager.createChargingSessionWithId(
                sessionId,
                account,
                chargingPoint,
                now
        );

        assertNotNull(activeSession, "Charging session should be created");
        assertTrue(activeSession.isActive(), "Charging session should be active");
    }

    @When("the customer disconnects the vehicle from Charging Point {string}")
    public void the_customer_disconnects_the_vehicle(String chargingPointId) {
        ChargingPoint chargingPoint = chargingPointManager.getChargingPointById(chargingPointId);
    }


    @Then("the charging session with ID {string} is stopped using {double} kWh over {int} minutes")
    public void the_charging_session_is_stopped(String sessionId, double energyUsed, int duration) {
        chargingSessionManager.endChargingSession(sessionId, energyUsed, duration);

        ChargingSession session = chargingSessionManager.getChargingSessionById(sessionId);
        assertFalse(session.isActive(), "Charging session should be stopped");
    }

    @And("the energy used is {string} kWh")
    public void the_energy_used_is_kwh(String energyUsed) {
        assertEquals(
                Double.parseDouble(energyUsed),
                activeSession.getEnergyUsed(),
                0.001
        );
    }

    @And("the charging session is stored in the ChargingSessionManager")
    public void the_charging_session_is_stored() {
        assertTrue(
                chargingSessionManager.getChargingSessions().contains(activeSession),
                "Charging session should be stored"
        );
    }

    @And("the price of the charging session {string} is {double}")
    public void the_price_of_the_charging_session_is(String sessionId, double expectedPrice) {
        ChargingSession session = chargingSessionManager.getChargingSessionById(sessionId);
        assertEquals(expectedPrice, session.getPrice(), 0.001);
    }

    @And("an invoice is created for the charging session {string}")
    public void an_invoice_is_created(String sessionId) {
        Invoice invoice = invoiceManager.createInvoiceFromSession(activeSession);
        assertNotNull(invoice, "Invoice should exist for charging session: " + sessionId);
    }


    @And("the Charging Point {string} is marked as InOperationFree")
    public void the_charging_point_is_marked_free(String chargingPointId) {
        ChargingPoint chargingPoint = chargingPointManager.getChargingPointById(chargingPointId);
        assertEquals(
                "IN_OPERATION_FREE",
                chargingPoint.getOperatingStatus().name()
        );
    }

    @And("the remaining credit of Customer {string} is {double}")
    public void the_remaining_credit_is(String customerId, double expectedCredit) {
        Account account = accountManager.getAccount(customerId);
        assertEquals(
                expectedCredit,
                account.getCredit().getAmount(),
                0.001
        );
    }

    @When("the operator attempts to end charging session with ID {string}")
    public void theOperatorAttemptsToEndChargingSession(String sessionId) {
        try {
            chargingSessionManager.endChargingSession(sessionId, 10.0, 10);
        } catch (IllegalArgumentException e) {
            caughtException = e;
        }
    }

    @Then("an exception is thrown indicating session not found")
    public void anExceptionIsThrownIndicatingSessionNotFound() {
        assertNotNull(caughtException);
        assertInstanceOf(IllegalArgumentException.class, caughtException);
        assertTrue(caughtException.getMessage().contains("not found"));
    }
}
