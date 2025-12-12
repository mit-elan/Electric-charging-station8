package org.example.managers;

import org.example.entities.Account;
import org.example.entities.Location;

import java.util.ArrayList;
import java.util.List;

import org.example.enums.Mode;


public class LocationManager {
    private static final LocationManager INSTANCE = new LocationManager();
    private final List<Location> locations = new ArrayList<>();

    private LocationManager() {
    }

    public static LocationManager getInstance() {
        return INSTANCE;
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

    // Needed for BDD cleanup in the "new Location Manager" step
    public void clearLocations() {
        locations.clear();
    }

    public void updatePricing(String locationID, double acPrice, double dcPrice) {
        Location location = getLocation(locationID);

        if (location == null) {
            throw new IllegalArgumentException("Location not found: " + locationID);
        }

        location.setAcPrice(acPrice);
        location.setDcPrice(dcPrice);
    }

    public void addLocation(Location location) {
        locations.add(location);
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
        return null; // return null if not found
    }

}
