package org.example.ChargingPointSteps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import org.example.entities.ChargingPoint;
import org.example.entities.Location;
import org.example.enums.Mode;
import org.example.managers.ChargingPointManager;
import org.example.managers.LocationManager;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

public class ChargingPointSystemSteps {

    private final LocationManager locationManager = LocationManager.getInstance();
    private final ChargingPointManager chargingPointManager = ChargingPointManager.getInstance();

    @Given("a new Charging Point Manager")
    public void aNewChargingPointManager() {
        chargingPointManager.clearChargingPoints();
    }

    @Given("the Location has charging Points")
    public void the_location_has_charging_points(DataTable dataTable) {

        // 1. There must be exactly one location in Background
        List<Location> locations = locationManager.getAllLocations();
        Assertions.assertEquals(1, locations.size(),
                "Expected exactly one Location in context");

        Location location = locations.get(0);

        // 2. Create charging points THROUGH the manager
        for (Map<String, String> row : dataTable.asMaps()) {

            String chargingPointID = row.get("Charging Point ID");
            Mode mode = Mode.valueOf(row.get("Mode"));

            chargingPointManager.createChargingPoint(
                    location,
                    chargingPointID,
                    mode
            );
        }
    }
}
