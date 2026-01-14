package org.example.entities;

import org.example.enums.ChargingMode;
import org.example.enums.OperatingStatus;

public class ChargingPoint {
    private final Location location;
    private final String chargingPointID;
    private final ChargingMode chargingMode;
    private OperatingStatus operatingStatus;
    private double price;
    private boolean isPhysicallyConnected = false;

    public ChargingPoint(Location location, String chargingPointID, ChargingMode chargingMode) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null.");
        }
        this.location = location;
        this.chargingPointID = chargingPointID;
        this.chargingMode = chargingMode;
        this.operatingStatus = OperatingStatus.IN_OPERATION_FREE;

        // Initial price comes from Location
        if (chargingMode == ChargingMode.AC) {
            this.price = location.getAcPrice();
        } else {
            this.price = location.getDcPrice();
        }
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

    public double getPrice() {
        return price;
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

    /* Only Location may change pricing */
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

    @Override
    public String toString() {
        return "ChargingPoint " + chargingPointID +
                " | Mode: " + chargingMode +
                " | Price: " + price +
                " | Status: " + operatingStatus;
    }

}
