package org.example.entities;

import org.example.enums.Mode;

import java.time.LocalDateTime;

public class Invoice {
    private final String invoiceItemNumber;
    private final ChargingSession session;   // <-- SOURCE OF TRUTH
    private final Account account;

    public Invoice(String invoiceItemNumber,
                   ChargingSession session,
                   Account account) {

        this.invoiceItemNumber = invoiceItemNumber;
        this.session = session;
        this.account = account;
    }

    // Delegation getters (READ-ONLY)
    public LocalDateTime getStartTime() {
        return session.getStartTime();
    }

    public String getChargingPointID() {
        return session.getChargingPointID();
    }

    public String getLocationName() {
        return session.getLocationName();
    }

    public Mode getChargingMode() {
        return session.getChargingMode();
    }

    public int getDurationMinutes() {
        return session.getDuration();
    }

    public double getEnergyUsedKwh() {
        return session.getEnergyUsed();
    }

    public double getPrice() {
        return session.getPrice();
    }

    public Account getAccount() {
        return account;
    }

    public String getInvoiceItemNumber() {
        return invoiceItemNumber;
    }
}
