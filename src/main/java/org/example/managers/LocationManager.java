package org.example.managers;

import org.example.entities.Location;
import org.example.entities.Tariff;
import org.example.enums.ChargingMode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class LocationManager {
    private static final LocationManager INSTANCE = new LocationManager();
    private final List<Location> locations = new ArrayList<>();

    private LocationManager() {
    }

    public static LocationManager getInstance() {
        return INSTANCE;
    }

    public void clearLocations() {
        locations.clear();
    }

    public Location createLocation(String locationID, String name, String address) {
        if (getLocation(locationID) != null) {
            throw new IllegalArgumentException("Location with ID " + locationID + " already exists.");
        }
        Location location = new Location(locationID, name, address);
        locations.add(location);
        return location;
    }

    public Location getLocation(String locationID) {
        for (Location location : locations) {
            if (location.getLocationID().equals(locationID)) {
                return location;
            }
        }
        return null;
    }

    public List<Location> getAllLocations() {
        return locations;
    }

    public void deleteLocation(String locationId) {
        Location locationToRemove = null;
        for (Location location : locations) {
            if (location.getLocationID().equals(locationId)) {
                locationToRemove = location;
                break;
            }
        }
        if (locationToRemove != null) {
            locations.remove(locationToRemove);
        } else {
            throw new IllegalArgumentException("No location found with Location ID" + locationId);
        }
    }

    public Location getLocationByID(String locationID) {
        for (Location location : locations) {
            if (location.getLocationID().equals(locationID)) {
                return location;
            }
        }
        return null;
    }

    public void updateLocationAddress(String locationID, String newAddress) {
        Location location = getLocation(locationID);
        if (location == null) {
            throw new IllegalArgumentException("Location not found: " + locationID);
        }
        if (newAddress == null || newAddress.isBlank()) {
            throw new IllegalArgumentException("Address cannot be null or empty.");
        }
        location.setAddress(newAddress);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Location Overview:\n");
        for (Location location : locations) {
            sb.append(location.toString()).append("\n");
        }
        return sb.toString();
    }


    public void updateLocationPricing(
            String locationID,
            ChargingMode mode,
            double pricePerKwh,
            double pricePerMinute,
            LocalDateTime validFrom
    ) {
        Location location = getLocation(locationID);
        if (location == null) {
            throw new IllegalArgumentException("Location not found: " + locationID);
        }

        Tariff tariff = new Tariff(
                mode,
                pricePerKwh,
                pricePerMinute,
                validFrom
        );

        location.addTariff(tariff);
    }


    public Location getLocationByChargingPoint(String cpId) {
        for (Location loc : locations) {
            // Checking the list of points inside each location
            boolean holdsPoint = loc.getChargingPoints().stream()
                    .anyMatch(cp -> cp.getChargingPointID().equals(cpId));

            if (holdsPoint) {
                return loc;
            }
        }
        throw new IllegalArgumentException("No Location found containing Charging Point: " + cpId);
    }
}
