package org.example.managers;

import org.example.entities.*;
import org.example.enums.ChargingMode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InvoiceManager {

    private static final InvoiceManager INSTANCE = new InvoiceManager();
    private final List<Invoice> invoices = new ArrayList<>();
    private int invoiceCounter = 0;

    private InvoiceManager() {
    }

    public static InvoiceManager getInstance() {
        return INSTANCE;
    }

    /* ----------------------------------------------------
       Basic lifecycle helpers
     ---------------------------------------------------- */

    public void clearInvoices() {
        invoices.clear();
        invoiceCounter = 0;
    }

    public List<Invoice> getAllInvoices() {
        return new ArrayList<>(invoices);
    }

    /* ----------------------------------------------------
       Invoice creation
     ---------------------------------------------------- */

    public Invoice createInvoiceFromSession(ChargingSession session) {
        if (session == null) {
            throw new IllegalArgumentException("ChargingSession cannot be null");
        }

        if (session.getAccount() == null) {
            throw new IllegalStateException("ChargingSession has no Account");
        }

        String invoiceId = String.valueOf(++invoiceCounter);

        Invoice invoice = new Invoice(
                invoiceId,                  // invoice item number
                session,                        // real charging session
                session.getAccount()            // account comes FROM session
        );

        invoices.add(invoice);
        return invoice;
    }

    /**
     * Used ONLY for tests / Cucumber steps
     */
    public void createInvoiceManually(
            String customerID,
            AccountManager accountManager,
            String startTime,
            String itemNo,
            String locationName,
            String chargingPointId,
            ChargingMode chargingMode,
            int durationMinutes,
            double energyUsedKwh,
            double price
    ) {
        if (customerID == null || accountManager == null) {
            throw new IllegalArgumentException("Customer ID and AccountManager must not be null");
        }

        Account account = accountManager.getAccount(customerID);
        if (account == null) {
            throw new IllegalArgumentException("No account found for customer ID: " + customerID);
        }

        // 1. Parse Austrian date format
        // FIX: Replace 'T' with space to handle inputs like "03-12-2025T10:00:00"

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime parsedStartTime = LocalDateTime.parse(startTime, formatter);

        // 2. Create minimal Location + ChargingPoint (test-only)
        Location fakeLocation = new Location("LOC-MANUAL", locationName, "N/A");

        ChargingPoint fakeChargingPoint = new ChargingPoint(
                fakeLocation,
                chargingPointId,
                chargingMode
        );

        // 3. Create fake ChargingSession
        ChargingSession fakeSession = new ChargingSession(
                "MANUAL-" + itemNo,
                parsedStartTime,
                fakeChargingPoint
        );

        fakeSession.setAccount(account);
        fakeSession.setDuration(durationMinutes);
        fakeSession.setEnergyUsed(energyUsedKwh);
        fakeSession.setPrice(price);

        // 4. Create invoice from session (same path as real system)
        Invoice invoice = new Invoice(itemNo, fakeSession, account);
        invoices.add(invoice);

        // 5. Reflect credit usage
        account.getCredit().subtractCredit(price);
    }


    /* ----------------------------------------------------
       Queries
     ---------------------------------------------------- */

    public List<Invoice> getInvoicesForAccount(Account account) {
        List<Invoice> result = new ArrayList<>();

        for (Invoice invoice : invoices) {
            if (invoice.getAccount().equals(account)) {
                result.add(invoice);
            }
        }

        result.sort(
                Comparator.comparing(Invoice::getStartTime)
        );

        return result;
    }

    private String formatInvoiceLine(Invoice invoice) {
        // European format for date
        DateTimeFormatter europeanFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedStartTime = invoice.getStartTime().format(europeanFormatter);

        // European decimal format (comma instead of dot)
        String formattedEnergy = String.format("%.2f", invoice.getEnergyUsedKwh());
        String formattedPrice = String.format("%.2f", invoice.getPrice());

        return String.format(
                "Item %s | Customer: %s | Start Time: %s | Location: %s | CP: %s | Mode: %s | Duration: %d minutes | Energy used: %s kWh | Price: %s",
                invoice.getInvoiceItemNumber(),
                invoice.getAccount().getCustomerID(),
                formattedStartTime,
                invoice.getLocationName(),
                invoice.getChargingPointID(),
                invoice.getChargingMode(),
                invoice.getDurationMinutes(),
                formattedEnergy,
                formattedPrice
        );
    }

    public void printInvoiceOverviewForAccount(Account account) {
        if (account == null) return;

        System.out.println("Invoice Overview for Customer: " + account.getCustomerID());
        System.out.println();

        // 1. Get invoices
        List<Invoice> customerInvoices = getInvoicesForAccount(account);

        // 2. Get top-ups
        List<Credit.TopUpEntry> topUps = account.getCredit().getHistoryEntries(); // We'll add this helper

        // 3. Create a unified list
        List<Map.Entry<LocalDateTime, String>> timeline = new ArrayList<>();

        // 3a. Add invoices
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        for (Invoice invoice : customerInvoices) {
            String line = String.format(
                    "Charging Session | Item No %s | Location: %s | CP: %s | Mode: %s | Duration: %d minutes | Energy used: %.2f kWh | Price: %.2f",
                    invoice.getInvoiceItemNumber(),
                    invoice.getLocationName(),
                    invoice.getChargingPointID(),
                    invoice.getChargingMode(),
                    invoice.getDurationMinutes(),
                    invoice.getEnergyUsedKwh(),
                    invoice.getPrice()
            );
            timeline.add(Map.entry(invoice.getStartTime(), line));
        }

        // 3b. Add top-ups
        for (Credit.TopUpEntry topUp : topUps) {
            String line = String.format(
                    "Top-up | Amount: %.2f",
                    topUp.getAmount()
            );
            timeline.add(Map.entry(topUp.getDateTime(), line));
        }

        // 4. Sort by date/time
        timeline.sort(Comparator.comparing(Map.Entry::getKey));

        // 5. Print unified list
        for (Map.Entry<LocalDateTime, String> entry : timeline) {
            System.out.println(formatter.format(entry.getKey()) + " | " + entry.getValue());
        }

        // 6. Print outstanding balance
        System.out.println();
        System.out.println("Outstanding Balance: " + String.format("%.2f", account.getCredit().getAmount()));
    }



    public void printGlobalInvoiceHistory() {
        System.out.println("Global Invoice and Top-up History (Sorted by Date and Time)");
        System.out.println("------------------------------------------------");

        // 1. Prepare a combined timeline: Map<LocalDateTime, String>
        List<Map.Entry<LocalDateTime, String>> timeline = new ArrayList<>();

        DateTimeFormatter europeanFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // 2. Add all invoices to timeline
        for (Invoice invoice : invoices) {
            String line = String.format(
                    "%s | Charging Session %s | Item No %s | Location: %s | CP: %s | Mode: %s | Duration: %d minutes | Energy used: %s kWh | Price: %s",
                    invoice.getStartTime().format(europeanFormatter),
                    invoice.getInvoiceItemNumber(),
                    invoice.getInvoiceItemNumber(),
                    invoice.getLocationName(),
                    invoice.getChargingPointID(),
                    invoice.getChargingMode(),
                    invoice.getDurationMinutes(),
                    String.format("%.2f", invoice.getEnergyUsedKwh()).replace('.', ','),  // European decimal
                    String.format("%.2f", invoice.getPrice()).replace('.', ',')
            );
            timeline.add(Map.entry(invoice.getStartTime(), line));
        }

        // 3. Add all top-ups to timeline
        List<Account> accounts = AccountManager.getInstance().getAccounts();
        for (Account account : accounts) {
            if (account.getCredit() != null) {
                for (String topUpEntry : account.getCredit().getHistory()) {
                    // Example entry: "Top-up | Amount: 50.00 | Date: 03-12-2025 11:30:00"
                    String[] parts = topUpEntry.split("\\|");
                    double amount = Double.parseDouble(parts[1].split(":")[1].trim().replace(',', '.'));
                    LocalDateTime dateTime = LocalDateTime.parse(parts[2].split(": ")[1], europeanFormatter);

                    String line = String.format(
                            "%s | Top-up | Customer: %s | Amount: %s",
                            dateTime.format(europeanFormatter),
                            account.getCustomerID(),
                            String.format("%.2f", amount).replace('.', ',')  // European decimal
                    );

                    timeline.add(Map.entry(dateTime, line));
                }
            }
        }

        // 4. Sort the timeline ascending by date (oldest first)
        timeline.sort(Map.Entry.comparingByKey());

        // 5. Print combined timeline
        for (Map.Entry<LocalDateTime, String> entry : timeline) {
            System.out.println(entry.getValue());
        }

        // 6. Print outstanding balances per account
        System.out.println("\nOutstanding Balances per Account:");
        System.out.println("--------------------------------");
        for (Account account : accounts) {
            if (account.getCredit() != null) {
                String formattedBalance = String.format("%.2f", account.getCredit().getAmount()).replace('.', ',');
                System.out.println(
                        "Customer " + account.getCustomerID() + " | Outstanding Balance: " + formattedBalance
                );
            }
        }
    }


    public boolean isSortedByStartTime() {
        if (invoices.size() < 2) return true;

        for (int i = 0; i < invoices.size() - 1; i++) {
            LocalDateTime current = invoices.get(i).getStartTime();
            LocalDateTime next = invoices.get(i + 1).getStartTime();

            // Checking for DESCENDING (Newest first)
            // If the current invoice is BEFORE the next one, it's not sorted newest-first.
            if (current.isBefore(next)) {
                return false;
            }
        }
        return true;
    }

    public boolean allInvoicesContainRequiredFields() {
        for (Invoice invoice : invoices) {
            if (invoice.getInvoiceItemNumber() == null ||
                    invoice.getLocationName() == null ||
                    invoice.getChargingPointID() == null ||
                    invoice.getChargingMode() == null ||
                    invoice.getDurationMinutes() <= 0 ||
                    invoice.getEnergyUsedKwh() <= 0 ||
                    invoice.getPrice() <= 0) {
                return false;
            }
        }
        return true;
    }

    public boolean hasTopUpInformation() {
        for (Invoice invoice : invoices) {
            if (invoice.getAccount() != null &&
                    invoice.getAccount().getCredit() != null) {
                return true;
            }
        }
        return false;
    }

    public boolean hasOutstandingBalanceInformation() {
        for (Invoice invoice : invoices) {
            if (invoice.getAccount() != null &&
                    invoice.getAccount().getCredit() != null &&
                    invoice.getAccount().getCredit().getAmount() >= 0) {
                return true;
            }
        }
        return false;
    }
}
