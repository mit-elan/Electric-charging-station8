package org.example;

import org.example.entities.Account;
import org.example.entities.Location;
import org.example.enums.Mode;
import org.example.managers.AccountManager;
import org.example.managers.ChargingPointManager;
import org.example.managers.LocationManager;

public class ElectricChargingSystem {

    private ElectricChargingSystem() {
        // prevent instantiation
    }

    // -------------------------
    // Electric Charging System main
    // -------------------------
    public static void main(String[] args) {

        System.out.println("Welcome to the Electric Charging Network System");
        System.out.println("------------------------------------------------");

        // -------------------------
        // Managers (Singletons)
        // -------------------------
        AccountManager accountManager = AccountManager.getInstance();
        LocationManager locationManager = LocationManager.getInstance();
        ChargingPointManager chargingPointManager = ChargingPointManager.getInstance();

        // Clean system (fresh start, like in BDD Background)
        accountManager.clearAccounts();
        locationManager.clearLocations();
        chargingPointManager.clearChargingPoints();

        // =======================
        // ACCOUNTS
        // =======================
        System.out.println("=== Account Management ===");

        Account acc1 = accountManager.createAccount(
                "Alice",
                "alice@mail.com",
                "password123"
        );

        Account acc2 = accountManager.createAccount(
                "Bob",
                "bob@mail.com",
                "securePwd"
        );

        // Read accounts
        System.out.println(accountManager);

        // Update account
        System.out.println("Updating Alice's email...\n");
        accountManager.updateAccount(
                acc1.getCustomerID(),
                null,
                "alice.new@mail.com",
                null
        );

        // Read updated account
        System.out.println(accountManager.readAccount(acc1.getCustomerID()));

        // Delete account
        System.out.println("\nDeleting Bob's account...\n");
        accountManager.deleteAccount(acc2.getCustomerID());

        System.out.println(accountManager);

        // -------------------------
        // Locations
        // -------------------------
        System.out.println("\nCreating Locations...");
        Location loc1 = locationManager.createLocation(
                "LOC-1",
                "City Center",
                "Main Street 1"
        );

        Location loc2 = locationManager.createLocation(
                "LOC-2",
                "Shopping Mall",
                "Mall Road 5"
        );

        System.out.println("\nLocations:");
        System.out.println(locationManager);

        // -------------------------
        // Charging Points
        // -------------------------
        System.out.println("\nCreating Charging Points...");
        chargingPointManager.createChargingPoint(loc1, "CP-AC-1", Mode.AC);
        chargingPointManager.createChargingPoint(loc1, "CP-DC-1", Mode.DC);

        chargingPointManager.createChargingPoint(loc2, "CP-AC-2", Mode.AC);

        // -------------------------
        // Read Charging Network
        // -------------------------
        System.out.println("\nCharging Network Overview:");
        locationManager.getAllLocations()
                .forEach(System.out::println);

        System.out.println("\nSystem initialized successfully.");

        // =======================
        // DELETE LOCATION
        // =======================
        System.out.println("Deleting location LOC-2...\n");
        locationManager.deleteLocation("LOC-2");

        // Read network after deletion
        System.out.println(locationManager);

        System.out.println("System demonstration finished.");
    }
}
