package org.example.entities;

import java.util.ArrayList;
import java.util.List;

// Note: This class is simplified for US 1.1; prices and charging points are added later.
public class Location {
    private final String locationID;
    private String name;
    private String address;
    private double acPrice;
    private double dcPrice;
    private List<ChargingPoint> chargingPoints = new ArrayList<>();

    public Location(String locationID, String name, String address) {
        if (locationID == null || name == null || address == null) {
            throw new IllegalArgumentException("Location fields cannot be null.");
        }
        this.locationID = locationID;
        this.name = name;
        this.address = address;
    }

    public String getLocationID() { return locationID; }
    public String getName() { return name; }
    public String getAddress() { return address; }

    public double getAcPrice() { return acPrice; }
    public double getDcPrice() { return dcPrice; }

    public void setAcPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("AC price cannot be negative.");
        this.acPrice = price;
    }

    public void setDcPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("DC price cannot be negative.");
        this.dcPrice = price;
    }


    // Placeholder setters for future updates (US 1.2)
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }

    public void updatePricing(String locationID, double acPrice, double dcPrice){
    }

    public void addChargingPoint(ChargingPoint cp){
        chargingPoints.add(cp);
    }

    public List<ChargingPoint> getChargingPoints() {
        return chargingPoints;
    }

    public void setChargingPoints(List<ChargingPoint> chargingPoints) {
        this.chargingPoints = chargingPoints;
    }
}