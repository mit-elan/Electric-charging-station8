package org.example.entities;

import org.example.enums.ChargingMode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Location {
    private final String locationID;
    private String name;
    private String address;
    private final List<ChargingPoint> chargingPoints = new ArrayList<>();
    private final List<Tariff> tariffs = new ArrayList<>();

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

    public void addTariff(Tariff tariff) {
        if (tariff == null) {
            throw new IllegalArgumentException("Tariff cannot be null");
        }
        tariffs.add(tariff);
    }

    public Tariff getTariffAt(LocalDateTime time, ChargingMode mode) {
        return tariffs.stream()
                .filter(t -> t.getMode() == mode)
                .filter(t -> !t.getValidFrom().isAfter(time))
                .max((a, b) -> a.getValidFrom().compareTo(b.getValidFrom()))
                .orElse(null);
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void addChargingPoint(ChargingPoint cp) {
        if (cp == null) {
            throw new IllegalArgumentException("ChargingPoint cannot be null.");
        }
        chargingPoints.add(cp);
    }

    public List<ChargingPoint> getChargingPoints() {
        return chargingPoints;
    }

    public List<Tariff> getTariffs() {
        return tariffs;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Location ID: ")
                .append(locationID)
                .append(" | Location name: ")
                .append(name)
                .append(" | Location address: ")
                .append(address)
                .append("\n");

        // 2. Pricing Table (Current Tariffs)
        sb.append("| Mode | Price per kWh | Price per minute | Valid from       |\n");

        // Find the unique modes currently available in tariffs
        List<ChargingMode> modes = tariffs.stream()
                .map(Tariff::getMode)
                .distinct()
                .collect(Collectors.toList());

        for (ChargingMode mode : modes) {
            // Get the tariff currently valid for this mode
            Tariff activeTariff = getTariffAt(LocalDateTime.now(), mode);

            if (activeTariff != null) {
                sb.append(String.format("| %-4s | %-13.2f | %-16.2f | %-16s |\n",
                        activeTariff.getMode(),
                        activeTariff.getPricePerKwh(),
                        activeTariff.getPricePerMinute(),
                        activeTariff.getValidFrom()));
            }
        }

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
