package org.example.LocationSteps;

import io.cucumber.java.en.Given;
import org.example.managers.LocationManager;

public class LocationSystemSteps {

    @Given("a new Location Manager")
    public void aNewLocationManager() {
        LocationManager.getInstance().clearLocations();
    }
}
