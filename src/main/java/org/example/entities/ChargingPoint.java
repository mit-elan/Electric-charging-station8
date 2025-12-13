package org.example.entities;

import org.example.enums.Mode;
import org.example.enums.OperatingStatus;

public class ChargingPoint {

    private final Location location;
    private final String chargingPointID;
    private final Mode mode;
    private OperatingStatus operatingStatus;
    private double price;

    public ChargingPoint(Location location, String chargingPointID, Mode mode) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null.");
        }

        this.location = location;
        this.chargingPointID = chargingPointID;
        this.mode = mode;
        this.operatingStatus = OperatingStatus.IN_OPERATION_FREE;

        // Initial price comes from Location
        if (mode == Mode.AC) {
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

    public Mode getMode() {
        return mode;
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



    /* Only Location may change pricing */
    void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative.");
        }
        this.price = price;
    }

}
