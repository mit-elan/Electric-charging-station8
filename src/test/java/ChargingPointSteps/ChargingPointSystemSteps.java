package org.example.steps;

import io.cucumber.java.en.Given;
import org.example.managers.ChargingPointManager;

public class ChargingPointSystemSteps {

    @Given("a new Charging Point Manager")
    public void aNewChargingPointManager() {
        ChargingPointManager.getInstance().clearChargingPoints();
    }
}