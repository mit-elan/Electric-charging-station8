package org.example.ChargingPointSteps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import org.example.entities.Location;
import org.example.enums.ChargingMode;
import org.example.managers.ChargingPointManager;
import org.example.managers.LocationManager;

import java.util.Map;

public class ChargingPointSystemSteps {

    private final LocationManager locationManager = LocationManager.getInstance();
    private final ChargingPointManager chargingPointManager = ChargingPointManager.getInstance();

    @Given("a new Charging Point Manager")
    public void aNewChargingPointManager() {
        chargingPointManager.clearChargingPoints();
    }

    @Given("the Location {string} has charging Points")
    public void the_location_has_charging_points(String locationID, DataTable dataTable) {

        Location location = locationManager.getLocation(locationID);
        if (location == null) {
            throw new IllegalArgumentException("Location not found: " + locationID);
        }
        // Create charging points for that location
        for (Map<String, String> row : dataTable.asMaps()) {
            String chargingPointID = row.get("Charging Point ID");
            ChargingMode chargingMode = ChargingMode.valueOf(row.get("Mode"));
            chargingPointManager.createChargingPoint(location, chargingPointID, chargingMode);
        }
    }
}
