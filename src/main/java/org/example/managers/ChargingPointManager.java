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

    private ChargingPointManager() {
        chargingPoints = new ArrayList<>();
    }

    public static ChargingPointManager getInstance() {
        return INSTANCE;
    }

    public void clearChargingPoints() {
        chargingPoints.clear();
    }

    public ChargingPoint createChargingPoint(
            Location location,
            String chargingPointID,
            Mode mode) {

        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null");
        }

        ChargingPoint cp = new ChargingPoint(location, chargingPointID, mode);

        // Correct ownership
        location.addChargingPoint(cp);

        // Manager bookkeeping
        chargingPoints.add(cp);

        return cp;
    }


    public List<ChargingPoint> getChargingPoints() {
        return chargingPoints;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Charging Point Overview:\n");
        for (ChargingPoint cp : chargingPoints) {
            sb.append(cp.toString()).append("\n");
        }
        return sb.toString();
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

    public void addChargingPoint(ChargingPoint cp) {
        chargingPoints.add(cp);
    }
}
