package org.example.entities;

import org.example.enums.ChargingMode;

import java.time.LocalDateTime;

public class ChargingSession {
    private final String sessionID;
    private final LocalDateTime startTime;
    private int duration; // in minutes
    private final ChargingMode chargingMode;
    private final String chargingPointID;
    private double energyUsed;
    private double price;
    private Account account;
    private ChargingPoint chargingPoint;
    private boolean active = true;
    private Tariff tariffAtStart;

    public ChargingSession(
            String sessionID,
            LocalDateTime startTime,
            ChargingPoint chargingPoint) {

        this.sessionID = sessionID;
        this.startTime = startTime;
        this.chargingPoint = chargingPoint;                 // reference
        this.chargingPointID = chargingPoint.getChargingPointID(); // snapshot
        this.chargingMode = chargingPoint.getMode();// snapshot
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

    public ChargingMode getChargingMode() {
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

    public void setTariffAtStart(Tariff tariff) {
        this.tariffAtStart = tariff;
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
        return active;
    }

    public double getEnergyUsed() {
        return energyUsed;
    }

    public void setEnergyUsed(double energyUsed) {
        this.energyUsed = energyUsed;
    }

    public double calculatePrice() {
        if (tariffAtStart == null) {
            throw new IllegalStateException("No tariff set for session");
        }

        this.price =
                (energyUsed * tariffAtStart.pricePerKwh()) +
                        (duration * tariffAtStart.pricePerMinute());
        return price;
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

    public void endSession(double energyUsed, int durationMinutes) {
        this.energyUsed = energyUsed;
        this.duration = durationMinutes;

        if (tariffAtStart == null) {
            throw new IllegalStateException("No tariff assigned to session");
        }
        active = false;
        this.price = calculatePrice();
    }

}


