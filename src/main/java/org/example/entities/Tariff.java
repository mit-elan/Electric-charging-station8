package org.example.entities;

import org.example.enums.ChargingMode;

import java.time.LocalDateTime;

public class Tariff {

    private final ChargingMode mode;
    private final double pricePerKwh;
    private final double pricePerMinute;
    private final LocalDateTime validFrom;

    public Tariff(
            ChargingMode mode,
            double pricePerKwh,
            double pricePerMinute,
            LocalDateTime validFrom
    ) {
        if (mode == null || validFrom == null) {
            throw new IllegalArgumentException("Mode and validFrom must not be null");
        }
        if (pricePerKwh < 0 || pricePerMinute < 0) {
            throw new IllegalArgumentException("Prices must not be negative");
        }

        this.mode = mode;
        this.pricePerKwh = pricePerKwh;
        this.pricePerMinute = pricePerMinute;
        this.validFrom = validFrom;
    }

    public ChargingMode getMode() {
        return mode;
    }

    public double getPricePerKwh() {
        return pricePerKwh;
    }

    public double getPricePerMinute() {
        return pricePerMinute;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }
}
