package org.example.managers;

import org.example.entities.ChargingPoint;
import org.example.entities.Location;
import org.example.enums.ChargingMode;
import org.example.enums.OperatingStatus;

import java.util.ArrayList;
import java.util.List;

public class ChargingPointManager {

    private static ChargingPointManager instance;
    private final List<ChargingPoint> chargingPoints = new ArrayList<>();

    private ChargingPointManager() {}

    public static ChargingPointManager getInstance() {
        if (instance == null) {
            instance = new ChargingPointManager();
        }
        return instance;
    }

    public void clearChargingPoints() {
        chargingPoints.clear();
    }

    public void createChargingPoint(
            Location location,
            String chargingPointID,
            ChargingMode chargingMode
    ) {
        // 1. Validate input
        if (location == null) {
            throw new IllegalArgumentException("Location must not be null");
        }
        if (chargingPointID == null || chargingMode == null) {
            throw new IllegalArgumentException("Charging Point ID and Mode must not be null");
        }

        // 2. Prevent duplicate IDs
        boolean exists = chargingPoints.stream()
                .anyMatch(cp -> cp.getChargingPointID().equals(chargingPointID));
        if (exists) {
            throw new IllegalStateException(
                    "Charging Point with ID " + chargingPointID + " already exists"
            );
        }

        // 3. Create charging point
        ChargingPoint chargingPoint = new ChargingPoint(location, chargingPointID, chargingMode);

        // 4. Set initial operating status
        chargingPoint.setOperatingStatus(OperatingStatus.IN_OPERATION_FREE);

        // 5. Set price based on Location pricing
        if (chargingMode == ChargingMode.AC) {
            chargingPoint.setPrice(location.getAcPrice());
        } else if (chargingMode == ChargingMode.DC) {
            chargingPoint.setPrice(location.getDcPrice());
        }

        // 6. Attach to location (domain invariant)
        location.addChargingPoint(chargingPoint);

        // 7. Register globally
        chargingPoints.add(chargingPoint);

    }

    public ChargingPoint getChargingPointById(String id) {
        return chargingPoints.stream()
                .filter(cp -> cp.getChargingPointID().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<ChargingPoint> getChargingPoints() {
        return chargingPoints;
    }

    public void deleteChargingPoint(String chargingPointId, Location location) {
        if (location == null) {
            throw new IllegalArgumentException("Location must exist");
        }

        // Remove from location
        location.getChargingPoints()
                .removeIf(cp -> cp.getChargingPointID().equals(chargingPointId));

        // Remove from global charging network
        ChargingPoint cp = getChargingPointById(chargingPointId);
        chargingPoints.remove(cp);
    }

}
