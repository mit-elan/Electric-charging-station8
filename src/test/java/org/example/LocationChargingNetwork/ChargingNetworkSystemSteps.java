package org.example.LocationChargingNetwork;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import org.example.managers.*;

import java.util.Map;

public class ChargingNetworkSystemSteps {

    // Managers
    private final AccountManager accountManager = AccountManager.getInstance();
    private final CreditManager creditManager = CreditManager.getInstance();
    private final ChargingSessionManager sessionManager = ChargingSessionManager.getInstance();
    private final LocationManager locationManager = LocationManager.getInstance();
    private final ChargingPointManager chargingPointManager = ChargingPointManager.getInstance();

    @Given("a new Charging Session Manager")
    public void aNewChargingSessionManager() {
        ChargingSessionManager.getInstance().clearChargingSessions();
    }

    @Given("a new Credit Manager")
    public void aNewCreditManager() {
        CreditManager.getInstance().clearCredits();
    }

    @Given("a new Invoice Manager")
    public void aNewInvoiceManager() {
        InvoiceManager.getInstance().clearInvoices();
    }

}