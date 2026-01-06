package org.example.entities;

import org.example.enums.Mode;

import java.time.LocalDateTime;

public class ChargingSession {
    private final String sessionID;
    private final LocalDateTime startTime;
    private int duration; // in minutes
    private final Mode chargingMode;
    private final String chargingPointID;
    private double energyUsed;
    private double price;
    private Account account;
    private ChargingPoint chargingPoint;

    public ChargingSession(
            String sessionID,
            LocalDateTime startTime,
            ChargingPoint chargingPoint) {
        this.sessionID = sessionID;
        this.startTime = startTime;
        this.chargingPoint = chargingPoint;                 // reference
        this.chargingPointID = chargingPoint.getChargingPointID(); // snapshot
        this.chargingMode = chargingPoint.getMode();        // snapshot
    }


    public String getSessionID() {
        return sessionID;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return duration;
    }

    public Mode getChargingMode() {
        return chargingMode;
    }

    public String getChargingPointID() {
        return chargingPointID;
    }

    public Account getAccount() {
        return account;
    }

    public ChargingPoint getChargingPoint() {
        return chargingPoint;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setChargingPoint(ChargingPoint chargingPoint) {
        this.chargingPoint = chargingPoint;
    }

    public void setDuration(int duration) {
        if (duration < 0) {
            throw new IllegalArgumentException("Duration cannot be negative");
        }
        this.duration = duration;
    }

    public boolean isActive() {
        return duration == 0;
    }

    public double getEnergyUsed() {
        return energyUsed;
    }

    public void setEnergyUsed(double energyUsed) {
        this.energyUsed = energyUsed;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getLocationName() {
        if (chargingPoint == null || chargingPoint.getLocation() == null) {
            return "UNKNOWN LOCATION";
        }
        return chargingPoint.getLocation().getName();
    }
}


