package org.example;

import org.example.entities.Account;
import org.example.entities.ChargingPoint;
import org.example.entities.ChargingSession;
import org.example.entities.Location;
import org.example.enums.Mode;
import org.example.managers.*;

import static org.example.enums.Mode.AC;

public class ElectricChargingSystem {

    public static void main(String[] args) {

        System.out.println("=== Electric Charging Network System ===\n");

        // -------------------------
        // Managers
        // -------------------------
        AccountManager accountManager = AccountManager.getInstance();
        CreditManager creditManager = CreditManager.getInstance();
        LocationManager locationManager = LocationManager.getInstance();
        ChargingPointManager chargingPointManager = ChargingPointManager.getInstance();
        ChargingSessionManager chargingSessionManager = ChargingSessionManager.getInstance();
        InvoiceManager invoiceManager = InvoiceManager.getInstance();

        // Clean system
        accountManager.clearAccounts();
        creditManager.clearCredits();
        locationManager.clearLocations();
        chargingPointManager.clearChargingPoints();
        chargingSessionManager.clearChargingSessions();
        invoiceManager.clearInvoices();

        // =======================
        // CUSTOMERS
        // =======================
        System.out.println("Creating customers...");

        Account cust1 = accountManager.createAccount("Alice", "alice@mail.com", "pw1");
        Account cust2 = accountManager.createAccount("Bob", "bob@mail.com", "pw2");
        Account cust3 = accountManager.createAccount("Charlie", "charlie@mail.com", "pw3");
        Account cust4 = accountManager.createAccount("Diana", "diana@mail.com", "pw4");
        Account cust5 = accountManager.createAccount("Eve", "eve@mail.com", "pw5");

        System.out.println(AccountManager.getInstance());

        creditManager.initializeCredit(cust1.getCustomerID(), 80.00);
        creditManager.initializeCredit(cust2.getCustomerID(), 90.00);
        creditManager.initializeCredit(cust3.getCustomerID(), 150.00);
        creditManager.initializeCredit(cust4.getCustomerID(), 100.00);
        creditManager.initializeCredit(cust5.getCustomerID(), 120.00);

        // =======================
        // LOCATIONS & CHARGING POINTS
        // =======================
        System.out.println("Creating locations and charging points...");

        Location loc1 = locationManager.createLocation("LOC-1", "City Center", "Main St 1");
        Location loc2 = locationManager.createLocation("LOC-2", "Mall", "Mall Rd 5");
        Location loc3 = locationManager.createLocation("LOC-3", "Airport", "Airport Rd");
        Location loc4 = locationManager.createLocation("LOC-4", "Highway", "Exit 12");
        Location loc5 = locationManager.createLocation("LOC-5", "University", "Campus Ave");

        chargingPointManager.createChargingPoint(loc1, "CP-1", AC);
        chargingPointManager.createChargingPoint(loc1, "CP-2", Mode.DC);

        chargingPointManager.createChargingPoint(loc2, "CP-3", AC);
        chargingPointManager.createChargingPoint(loc2, "CP-4", Mode.DC);
        chargingPointManager.createChargingPoint(loc2, "CP-5", Mode.DC);

        chargingPointManager.createChargingPoint(loc3, "CP-6", AC);

        chargingPointManager.createChargingPoint(loc4, "CP-7", Mode.DC);

        chargingPointManager.createChargingPoint(loc5, "CP-8", AC);

        System.out.println(LocationManager.getInstance());

        // =======================
        // LOCATION PRICING
        // =======================
        System.out.println("Updating location pricing...\n");

        locationManager.updateLocationPricing("LOC-1", 0.35, 0.60);
        locationManager.updateLocationPricing("LOC-2", 0.40, 0.65);
        locationManager.updateLocationPricing("LOC-3", 0.50, 0.65);
        locationManager.updateLocationPricing("LOC-4", 0.50, 0.60);
        locationManager.updateLocationPricing("LOC-5", 0.40, 0.70);

        System.out.println(LocationManager.getInstance());

        // =======================
        // START & END CHARGING SESSION
        // =======================
        System.out.println("Starting charging session...");

        System.out.println("Connecting Vehicle to Charging Point \"CP-1\"...");
        boolean connectionResult = chargingPointManager.getChargingPointById("CP-1").isPhysicallyConnected();
        System.out.println("Vehicle is connected:" + connectionResult + "\n");

        chargingSessionManager.createChargingSessionWithId(
                "CS-1",
                cust1,
                chargingPointManager.getChargingPointById("CP-1")
        );

        System.out.println("Ending charging session...");
        System.out.println("Disconnecting Vehicle from Charging Point \"CP-1\"...");

        chargingSessionManager.endChargingSession(
                "CS-1",
                50.0,   // energy used
                20      // duration in minutes
        );

        connectionResult = chargingPointManager.getChargingPointById("CP-1").isPhysicallyConnected();
        System.out.println("Vehicle is connected:" + connectionResult);

        // =======================
        // CUSTOMER ACCOUNT TOP UPS AND MORE CHARGING SESSIONS
        // =======================

        // Alice tops up again and charges
        creditManager.addManualTopUp(cust1, 60, "02-01-2026 12:00:00");
        chargingSessionManager.createChargingSessionWithId("CS-3", cust1, chargingPointManager.getChargingPointById("CP-2"));
        chargingSessionManager.endChargingSession("CS-3", 35.0, 30);

        // Bob tops up and charges at a DC point
        creditManager.addManualTopUp(cust2, 50, "03-01-2026 08:30:00");
        chargingSessionManager.createChargingSessionWithId("CS-4", cust2, chargingPointManager.getChargingPointById("CP-4"));
        chargingSessionManager.endChargingSession("CS-4", 60.0, 40);

        // Charlie charges twice without top-up
        chargingSessionManager.createChargingSessionWithId("CS-5", cust3, chargingPointManager.getChargingPointById("CP-5"));
        chargingSessionManager.endChargingSession("CS-5", 45.0, 25);

        chargingSessionManager.createChargingSessionWithId("CS-6", cust3, chargingPointManager.getChargingPointById("CP-6"));
        chargingSessionManager.endChargingSession("CS-6", 30.0, 20);

        // Diana adds credit and uses the highway charger
        creditManager.addManualTopUp(cust4, 100, "04-01-2026 14:45:00");
        chargingSessionManager.createChargingSessionWithId("CS-7", cust4, chargingPointManager.getChargingPointById("CP-7"));
        chargingSessionManager.endChargingSession("CS-7", 90.0, 60);

        // Eve adds a small top-up and charges at university
        creditManager.addManualTopUp(cust5, 20, "05-01-2026 18:49:00");
        chargingSessionManager.createChargingSessionWithId("CS-8", cust5, chargingPointManager.getChargingPointById("CP-8"));
        chargingSessionManager.endChargingSession("CS-8", 25.0, 15);


        // =======================
        // READ INVOICES
        // =======================
        System.out.println("\nCustomer invoices:\n");
        invoiceManager.printInvoiceOverviewForAccount(cust1);

        System.out.println("\nAll invoices (Operator view):\n");
        InvoiceManager.getInstance().printGlobalInvoiceHistory();

        System.out.println("\n=== System demonstration finished ===");
    }
}

