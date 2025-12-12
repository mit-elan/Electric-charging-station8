package org.example.entities;

import org.example.enums.Mode;

public class ChargingPoint {

    private final String chargingPointID;
    private final Mode mode;   // AC or DC
    private double price;
    private boolean isOperational;

    public ChargingPoint(String chargingPointID, Mode mode) {
        if (chargingPointID == null || mode == null) {
            throw new IllegalArgumentException("ChargingPoint fields cannot be null.");
        }

        this.chargingPointID = chargingPointID;
        this.mode = mode;
        this.isOperational = true;
    }

    public String getChargingPointID() {
        return chargingPointID;
    }

    public Mode getMode(){
        return mode;
    }

    public double getPrice() {
        return price;
    }

    public boolean isOperational() {
        return isOperational;
    }

    public void setPrice(double price) {
        if (price < 0)
            throw new IllegalArgumentException("Price cannot be negative.");
        this.price = price;
    }

    public void setOperational(boolean operational) {
        this.isOperational = operational;
    }
}
