package org.example.entities;

import org.example.enums.Mode;

import java.util.ArrayList;
import java.util.List;

public class Location {
    private final String locationID;
    private String name;
    private String address;
    private double acPrice;
    private double dcPrice;
    private static final List<ChargingPoint> chargingPoints = new ArrayList<>();

    public Location(String locationID, String name, String address) {
        if (locationID == null || name == null || address == null) {
            throw new IllegalArgumentException("Location fields cannot be null.");
        }
        this.locationID = locationID;
        this.name = name;
        this.address = address;
    }

    public String getLocationID() {
        return locationID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getAcPrice() {
        return acPrice;
    }

    public double getDcPrice() {
        return dcPrice;
    }

    public void setAcPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("AC price cannot be negative.");
        this.acPrice = price;
    }

    public void setDcPrice(double price) {
        if (price < 0) throw new IllegalArgumentException("DC price cannot be negative.");
        this.dcPrice = price;
    }


    // Placeholder setters for future updates (US 1.2)
    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPricing(double acPrice, double dcPrice) {
        setAcPrice(acPrice);
        setDcPrice(dcPrice);

        for (ChargingPoint chargingPoint : chargingPoints) {
            if (chargingPoint.getMode() == Mode.AC) {
                chargingPoint.setPrice(acPrice);
            } else if (chargingPoint.getMode() == Mode.DC) {
                chargingPoint.setPrice(dcPrice);
            }
        }
    }

    public static void addChargingPoint(ChargingPoint cp) {
        chargingPoints.add(cp);
    }

    public List<ChargingPoint> getChargingPoints() {
        return chargingPoints;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Location ")
                .append(locationID)
                .append(" | ")
                .append(name)
                .append(" | ")
                .append(address)
                .append("\n");

        sb.append("  Charging Points:\n");

        if (chargingPoints.isEmpty()) {
            sb.append("    Coming soon...\n");
        } else {
            for (ChargingPoint cp : chargingPoints) {

                sb.append("    ")
                        .append(cp.toString())
                        .append("\n");
            }
        }
        return sb.toString();
    }
}
