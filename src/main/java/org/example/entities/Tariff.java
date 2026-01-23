package org.example.entities;

import org.example.enums.ChargingMode;

import java.time.LocalDateTime;

public record Tariff(ChargingMode mode, double pricePerKwh, double pricePerMinute, LocalDateTime validFrom) {

    public Tariff {
        if (mode == null || validFrom == null) {
            throw new IllegalArgumentException("Mode and validFrom must not be null");
        }
        if (pricePerKwh < 0 || pricePerMinute < 0) {
            throw new IllegalArgumentException("Prices must not be negative");
        }

    }
}
