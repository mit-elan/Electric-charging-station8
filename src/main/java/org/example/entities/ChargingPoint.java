package org.example.entities;

import org.example.enums.ChargingMode;
import org.example.enums.OperatingStatus;

public class ChargingPoint {
    private final Location location;
    private final String chargingPointID;
    private String chargingPointName = "";
    private final ChargingMode chargingMode;
    private OperatingStatus operatingStatus;
    private boolean isPhysicallyConnected = false;

    public ChargingPoint(Location location, String chargingPointID, ChargingMode chargingMode) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null.");
        }
        this.location = location;
        this.chargingPointID = chargingPointID;
        this.chargingMode = chargingMode;
        this.operatingStatus = OperatingStatus.IN_OPERATION_FREE;
    }

    public ChargingPoint(Location location, String chargingPointID, String name, ChargingMode mode) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null.");
        }
        this.location = location;
        this.chargingPointID = chargingPointID;
        this.chargingPointName = (name == null || name.isBlank()) ? chargingPointID : name;
        this.chargingMode = mode;
    }

    public Location getLocation() {
        return location;
    }

    public String getChargingPointID() {
        return chargingPointID;
    }

    public ChargingMode getMode() {
        return chargingMode;
    }

    public OperatingStatus getOperatingStatus() {
        return operatingStatus;
    }

    public void setOperatingStatus(OperatingStatus operatingStatus) {
        this.operatingStatus = operatingStatus;
    }

    public void connectVehicle() {
        if (isPhysicallyConnected) {
            throw new IllegalStateException("Vehicle already connected");
        }
        this.isPhysicallyConnected = true;
    }

    public void disconnectVehicle() {
        if (!isPhysicallyConnected) {
            throw new IllegalStateException("No vehicle connected");
        }
        this.isPhysicallyConnected = false;
    }

    public boolean isPhysicallyConnected() { return isPhysicallyConnected; }


    @Override
    public String toString() {
        return "Name: " + chargingPointName +
                " | Mode: " + chargingMode +
                " | Status: " + operatingStatus;
    }

    public String getChargingPointName() {
        return chargingPointName;
    }
}
