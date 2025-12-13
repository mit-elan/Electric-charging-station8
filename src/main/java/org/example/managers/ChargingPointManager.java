package org.example.managers;

import org.example.entities.ChargingPoint;
import org.example.entities.Location;
import org.example.enums.Mode;
import org.example.enums.OperatingStatus;

import java.util.ArrayList;
import java.util.List;

public class ChargingPointManager {

    private static final ChargingPointManager INSTANCE = new ChargingPointManager();
    private final List<ChargingPoint> chargingPoints;

    public ChargingPointManager() {
        chargingPoints = new ArrayList<>();
    }

    public static ChargingPointManager getInstance() {
        return INSTANCE;
    }

    public void clearChargingPoints() {
        chargingPoints.clear();
    }

    public ChargingPoint createChargingPoint(Location location, String chargingPointID, Mode mode) {
        if (location == null) throw new IllegalArgumentException("Location cannot be null");
        ChargingPoint cp = new ChargingPoint(location, chargingPointID, mode);
        location.addChargingPoint(cp);
        chargingPoints.add(cp);
        return cp;
    }

    public void updateChargingPoint(String chargingPointID, OperatingStatus operatingStatus) {
        for (ChargingPoint chargingPoint : chargingPoints) {
            if (chargingPoint.getChargingPointID().equals(chargingPointID)) {
                chargingPoint.setOperatingStatus(operatingStatus);
                return;
            }
        }
    }

    public void deleteChargingPoint(String chargingPointID) {
        chargingPoints.removeIf(cp -> cp.getChargingPointID().equals(chargingPointID));
    }

    public List<ChargingPoint> getChargingPoints() {
        return chargingPoints;
    }
}
