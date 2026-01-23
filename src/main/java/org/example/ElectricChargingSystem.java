package org.example;

import org.example.entities.Account;
import org.example.entities.ChargingPoint;
import org.example.entities.ChargingSession;
import org.example.entities.Location;
import org.example.enums.ChargingMode;
import org.example.enums.OperatingStatus;
import org.example.managers.*;

import java.time.LocalDateTime;

import static org.example.enums.ChargingMode.AC;

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
        // LOCATIONS & CHARGING POINTS
        // =======================
        System.out.println("Creating locations...");

        Location loc1 = locationManager.createLocation("LOC-1", "City Parking Center", "Main St 1");
        Location loc2 = locationManager.createLocation("LOC-2", "Shopping Mall East", "Mall Rd 5");
        Location loc3 = locationManager.createLocation("LOC-3", "Airport", "Airport Rd 150");
        Location loc4 = locationManager.createLocation("LOC-4", "Highway", "Exit 12");
        Location loc5 = locationManager.createLocation("LOC-5", "Train Station", "Eastbourne Terrace 10");
        Location loc6 = locationManager.createLocation("LOC-6", "Shopping Mall North", "Westbourne Grove 80");
        Location loc7 = locationManager.createLocation("LOC-7", "University", "New Cross Road 5");
        Location loc8 = locationManager.createLocation("LOC-8", "Opera", "Bow Street 20");
        Location loc9 = locationManager.createLocation("LOC-9", "City Parking West", "Garrick Street 57");
        Location loc10 = locationManager.createLocation("LOC-10", "Highschool", "St. Martins Lane 33");

        // =======================
        // LOCATION PRICING
        // =======================
        System.out.println("Updating location pricing...");


        locationManager.updateLocationPricing("LOC-1", AC, 0.35, 0.05, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-1", ChargingMode.DC, 0.60, 0.10, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-2", AC, 0.35, 0.05, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-2", ChargingMode.DC, 0.65, 0.15, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-3", AC, 0.35, 0.05, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-3", ChargingMode.DC, 0.60, 0.10, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-4", AC, 0.35, 0.05, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-4", ChargingMode.DC, 0.60, 0.10, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-5", AC, 0.35, 0.05, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-5", ChargingMode.DC, 0.60, 0.10, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-6", AC, 0.35, 0.05, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-6", ChargingMode.DC, 0.60, 0.10, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-7", AC, 0.55, 0.05, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-7", ChargingMode.DC, 0.60, 0.10, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-8", AC, 0.35, 0.05, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-8", ChargingMode.DC, 0.60, 0.10, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-9", AC, 0.35, 0.05, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-9", ChargingMode.DC, 0.60, 0.10, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-10", AC, 0.35, 0.05, LocalDateTime.of(2026, 1, 1, 8, 0));
        locationManager.updateLocationPricing("LOC-10", ChargingMode.DC, 0.60, 0.10, LocalDateTime.of(2026, 1, 1, 8, 0));

        System.out.println("Create charging points...\n");
        chargingPointManager.createChargingPointWithName(loc1, "CP-1", "Charging Point 1", AC);
        chargingPointManager.createChargingPointWithName(loc1, "CP-2", "Charging Point 2", ChargingMode.DC);

        chargingPointManager.createChargingPointWithName(loc2, "CP-3", "Charging Point 1", AC);
        chargingPointManager.createChargingPointWithName(loc2, "CP-4", "Charging Point 2", ChargingMode.DC);
        chargingPointManager.createChargingPointWithName(loc2, "CP-5", "Charging Point 3", ChargingMode.DC);

        chargingPointManager.createChargingPointWithName(loc3, "CP-6", "Charging Point 1", AC);
        chargingPointManager.createChargingPointWithName(loc3, "CP-7", "Charging Point 2", AC);

        chargingPointManager.createChargingPointWithName(loc4, "CP-8", "Charging Point 1", ChargingMode.DC);
        chargingPointManager.createChargingPointWithName(loc4, "CP-9", "Charging Point 2", AC);
        chargingPointManager.createChargingPointWithName(loc4, "CP-10", "Charging Point 3", AC);

        chargingPointManager.createChargingPointWithName(loc5, "CP-11", "Charging Point 1", AC);
        chargingPointManager.createChargingPointWithName(loc5, "CP-12", "Charging Point 2", AC);

        chargingPointManager.createChargingPointWithName(loc6, "CP-13", "Charging Point 1", ChargingMode.DC);
        chargingPointManager.createChargingPointWithName(loc6, "CP-14", "Charging Point 2", AC);
        chargingPointManager.createChargingPointWithName(loc6, "CP-15", "Charging Point 3", ChargingMode.DC);

        chargingPointManager.createChargingPointWithName(loc7, "CP-16", "Charging Point 1", ChargingMode.DC);
        chargingPointManager.createChargingPointWithName(loc7, "CP-17", "Charging Point 2", ChargingMode.DC);
        chargingPointManager.createChargingPointWithName(loc7, "CP-18", "Charging Point 3", AC);

        chargingPointManager.createChargingPointWithName(loc8, "CP-19", "Charging Point 1", ChargingMode.DC);
        chargingPointManager.createChargingPointWithName(loc8, "CP-20", "Charging Point 2", ChargingMode.DC);
        chargingPointManager.createChargingPointWithName(loc8, "CP-21", "Charging Point 3", AC);

        chargingPointManager.createChargingPointWithName(loc9, "CP-22", "Charging Point 1", ChargingMode.DC);
        chargingPointManager.createChargingPointWithName(loc9, "CP-23", "Charging Point 2", AC);
        chargingPointManager.createChargingPointWithName(loc9, "CP-24", "Charging Point 3", AC);

        chargingPointManager.createChargingPointWithName(loc10, "CP-25", "Charging Point 1", ChargingMode.DC);
        chargingPointManager.createChargingPointWithName(loc10, "CP-26", "Charging Point 2", AC);
        chargingPointManager.createChargingPointWithName(loc10, "CP-27", "Charging Point 3", AC);

        // =======================
        // CUSTOMERS
        // =======================
        System.out.println("Creating customers...");

        Account cust1 = accountManager.createAccount("Bob", "bob@mail.com", "pw1");
        Account cust2 = accountManager.createAccount("Alice", "alice@mail.com", "pw2");
        Account cust3 = accountManager.createAccount("Charlie", "charlie@mail.com", "pw3");
        Account cust4 = accountManager.createAccount("Diana", "diana@mail.com", "pw4");
        Account cust5 = accountManager.createAccount("Eve", "eve@mail.com", "pw5");

        System.out.println(AccountManager.getInstance());

        // =======================
        // MONITOR CHARGING NETWORK
        // =======================
        System.out.println("Monitor Charging Network:");
        System.out.println(LocationManager.getInstance());

        // ---------------------------------------------------------
        // ACTIVITIES (Jan 1st - Jan 26th, 2026)
        // ---------------------------------------------------------
        System.out.println("\n=== Top-Ups and Sessions between Jan 1st - Jan 26th 2026 ===");

        // Everyone EXCEPT Customer 1 tops up
        creditManager.addManualTopUp(cust2, 100.0, "10-01-2026 10:00:00");
        creditManager.addManualTopUp(cust3, 120.0, "01-01-2026 09:00:00");
        creditManager.addManualTopUp(cust4, 150.0, "15-01-2026 14:00:00");
        creditManager.addManualTopUp(cust5, 20.0, "20-01-2026 18:30:00");

        // Alice (Cust 2): Credit -> Charge -> Result
        System.out.println("\n--- Case 1: Alice (Normal Flow) ---");
        System.out.println("Alice' credit before: €" + cust2.getCredit().getAmount());
        System.out.println("Alice starts charging session");
        chargingSessionManager.createChargingSessionWithId("PAST-1", cust2, chargingPointManager.getChargingPointById("CP-4"), LocalDateTime.of(2026, 1, 11, 10, 0));
        System.out.println("Alice Ends the charging session");
        chargingSessionManager.endChargingSession("PAST-1", 20.0, 30);
        System.out.println("\nAlice' financial history:");
        invoiceManager.readFinancialHistoryForAccount(cust2);

        // Charlie (Cust 3): Charge -> Charge again
        System.out.println("\n--- Case 2: Charlie (Double Charge) ---");
        System.out.println("Charlie's credit before: €" + cust3.getCredit().getAmount());
        System.out.println("Charlie starts Charging session");
        chargingSessionManager.createChargingSessionWithId("PAST-2", cust3, chargingPointManager.getChargingPointById("CP-5"), LocalDateTime.of(2026, 1, 13, 13, 0));
        System.out.println("Charlie Ends the charging session");
        chargingSessionManager.endChargingSession("PAST-2", 10.0, 15);
        System.out.println("Credit after: €" + cust3.getCredit().getAmount());
        System.out.println("Charlie Starts another session");
        chargingSessionManager.createChargingSessionWithId("PAST-3", cust3, chargingPointManager.getChargingPointById("CP-6"), LocalDateTime.of(2026, 1, 14, 15, 0));
        System.out.println("Charlie Ends the charging session");
        chargingSessionManager.endChargingSession("PAST-3", 5.0, 10);
        invoiceManager.readFinancialHistoryForAccount(cust3);

        // Diana (Cust 4): Charge -> Top Up -> Charge again
        System.out.println("\n--- Case 3: Diana (Charge, top-up, charge again) ---");
        System.out.println("Diana's credit before: €" + cust4.getCredit().getAmount());
        System.out.println("Diana's starts Charging session");
        chargingSessionManager.createChargingSessionWithId("PAST-4", cust4, chargingPointManager.getChargingPointById("CP-7"), LocalDateTime.of(2026, 1, 16, 8, 0));
        System.out.println("Diana Ends charging session");
        chargingSessionManager.endChargingSession("PAST-4", 80.0, 120);
        System.out.println("Credit after session 1: " + cust4.getCredit().getAmount());
        System.out.println("Diana Tops up her credit...");
        creditManager.addManualTopUp(cust4, 50.0, "17-01-2026 12:00:00");
        System.out.println("Credit after Top-Up: " + cust4.getCredit().getAmount());
        System.out.println("Diana Starts another session: ...");
        chargingSessionManager.createChargingSessionWithId("PAST-5", cust4, chargingPointManager.getChargingPointById("CP-8"), LocalDateTime.of(2026, 1, 18, 19, 0));
        System.out.println("Diana Ends the charging session\n");
        chargingSessionManager.endChargingSession("PAST-5", 30.0, 45);
        invoiceManager.readFinancialHistoryForAccount(cust4);

        // =======================
        // START CHARGING SESSION TODAY
        // =======================
        System.out.println("\n=== Scenario: Bob tries to charge without credit ===");
        ChargingPoint cp1 = chargingPointManager.getChargingPointById("CP-25");
        try {
            System.out.println("Attempting to start session 'CS-1' for Bob...");
            // This will fail because Bob has 0.0 credit
            chargingSessionManager.createChargingSessionWithId(
                    "CS-1",
                    cust1,
                    cp1,
                    LocalDateTime.now()
            );
        } catch (Exception e) {
            // 1. Print the Error Message
            System.out.println("SYSTEM REJECTED SESSION: " + e.getMessage());

            // 2. Bob tops up his account
            System.out.println("\n[RECOVERY] Bob is topping up his account with 50€...");
            creditManager.addManualTopUp(cust1, 50.0, "23-01-2026 15:00:00");
        }

        // 4. Now try again - this time it will succeed
        System.out.println("\nRetrying session start for Bob...");
        System.out.println("Bob connects Vehicle to \"Charging Point 1\" at Location \"Highschool\" ...");
        ChargingSession session = chargingSessionManager.createChargingSessionWithId(
                "CS-1",
                cust1,
                cp1,
                LocalDateTime.now()
        );

        // Demonstrate session existence
        System.out.println("Vehicle connected: " + cp1.isPhysicallyConnected());
        System.out.println("SUCCESS: Session CS-1 started.");

        // =======================
        // UPDATE CHARGING POINTS
        // =======================

        System.out.println("\n=== During the Charging Session the Operator updates the OperatingStatus of the Charging Points with ID: LOC-1 and LOC-2 ===");
        System.out.println("=== And the Operator updates the Pricing of Charging Points with ID: LOC-3 and LOC-4 ===");

        ChargingPoint cp2 = chargingPointManager.getChargingPointById("CP-2");
        cp2.updateOperatingStatus(OperatingStatus.OUT_OF_ORDER);

        ChargingPoint cp3 = chargingPointManager.getChargingPointById("CP-3");
        cp3.updateOperatingStatus(OperatingStatus.OUT_OF_ORDER);

        locationManager.updateLocationPricing("LOC-3", AC, 0.40, 0.15, LocalDateTime.of(2026, 1, 27, 18, 0));
        locationManager.updateLocationPricing("LOC-3", AC, 0.30, 0.05, LocalDateTime.of(2026, 1, 27, 18, 0));
        locationManager.updateLocationPricing("LOC-3", AC, 0.30, 0.10, LocalDateTime.of(2026, 1, 27, 18, 0));

        // =======================
        // MONITOR CHARGING NETWORK AGAIN TO CHECK CHARGING POINT STATUS OUT_OF_ORDER AND OCCUPIED
        // =======================
        System.out.println("Monitor Charging Network Again:");
        System.out.println(LocationManager.getInstance());


        // =======================
        // END CHARGING SESSION
        // =======================
        System.out.println("Bob ends his charging session");
        chargingSessionManager.endChargingSession(
                "CS-1",
                50.0,   // energy used
                20      // duration in minutes
        );

        invoiceManager.readFinancialHistoryForAccount(cust1);

        // =======================
        // MORE CUSTOMER ACCOUNT TOP UPS AND MORE CHARGING SESSIONS
        // =======================

        System.out.println("More account top ups and charging sessions are happening...");

        // Alice tops up again and charges
        creditManager.addManualTopUp(cust1, 60, "02-01-2026 12:00:00");
        chargingSessionManager.createChargingSessionWithId("CS-3", cust1, chargingPointManager.getChargingPointById("CP-9"), LocalDateTime.of(2026, 1, 2, 10, 0));
        chargingSessionManager.endChargingSession("CS-3", 35.0, 30);

        // Bob tops up and charges at a DC point
        creditManager.addManualTopUp(cust2, 80, "03-01-2026 08:30:00");
        chargingSessionManager.createChargingSessionWithId("CS-4", cust2, chargingPointManager.getChargingPointById("CP-4"), LocalDateTime.of(2026, 1, 14, 10, 0));
        chargingSessionManager.endChargingSession("CS-4", 60.0, 40);

        // Charlie charges twice without top-up
        creditManager.addManualTopUp(cust4, 100, "04-01-2026 14:45:00");
        chargingSessionManager.createChargingSessionWithId("CS-5", cust3, chargingPointManager.getChargingPointById("CP-5"), LocalDateTime.of(2026, 1, 28, 10, 0));
        chargingSessionManager.endChargingSession("CS-5", 45.0, 25);

        chargingSessionManager.createChargingSessionWithId("CS-6", cust3, chargingPointManager.getChargingPointById("CP-6"), LocalDateTime.of(2026, 1, 18, 10, 0));
        chargingSessionManager.endChargingSession("CS-6", 30.0, 20);

        // Diana adds credit and uses the highway charger
        creditManager.addManualTopUp(cust4, 100, "04-01-2026 14:45:00");
        chargingSessionManager.createChargingSessionWithId("CS-7", cust4, chargingPointManager.getChargingPointById("CP-7"), LocalDateTime.of(2026, 1, 21, 10, 0));
        chargingSessionManager.endChargingSession("CS-7", 90.0, 60);

        // Eve adds a small top-up and charges at university
        creditManager.addManualTopUp(cust5, 20, "05-01-2026 18:49:00");
        chargingSessionManager.createChargingSessionWithId("CS-8", cust5, chargingPointManager.getChargingPointById("CP-8"), LocalDateTime.of(2026, 1, 14, 10, 0));
        chargingSessionManager.endChargingSession("CS-8", 25.0, 15);


        // =======================
        // READ INVOICE
        // =======================
        System.out.println("After several top-ups and charging sessions, Bob wants to see his invoice history again");
        System.out.println();
        invoiceManager.readFinancialHistoryForAccount(cust1);

        // =======================
        // ADMIN READ INVOICES
        // =======================
        System.out.println("\nFinally the Operator Views all Invoices and Top-ups ever created:\n");
        InvoiceManager.getInstance().readGlobalFinancialHistory();

        System.out.println("\n=== System demonstration finished ===");


    }
}

