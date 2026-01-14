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
                "Item %s | Start Time: %s | Location: %s | CP: %s | Mode: %s | Duration: %d minutes | Energy used: %s kWh | Price: %s",
                invoice.getInvoiceItemNumber(),
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

        // Header
        System.out.println("Invoice Overview for Customer: " + account.getCustomerID());

        // 1. Get invoices for this account, sorted descending by start time
        List<Invoice> customerInvoices = getInvoicesForAccount(account);
        customerInvoices.sort((a, b) -> b.getStartTime().compareTo(a.getStartTime())); // newest first

        // 2. Print each invoice line
        for (Invoice invoice : customerInvoices) {
            System.out.println(formatInvoiceLine(invoice));
        }

        // 3. Print top-ups
        List<String> topUps = CreditManager.getInstance().getTopUpHistoryByAccount(account);
        if (!topUps.isEmpty()) {
            System.out.println("\nCredit Top-Ups:");
            for (String entry : topUps) {
                // Assume entries already formatted as "Top-up | Amount: XX,YY | Date: dd-MM-yyyy HH:mm:ss"
                System.out.println(entry);
            }
        }

        // 4. Outstanding balance
        // Use comma for decimal separator in Europe style
        double balance = account.getCredit().getAmount();
        String formattedBalance = String.format("%.2f", balance);
        System.out.println("\nOutstanding Balance: " + formattedBalance);
    }

    public void printGlobalInvoiceHistory() {
        System.out.println("Global Invoice and Top-up History (Sorted by Date and Time)");
        System.out.println("------------------------------------------------");

        // 1. Sort the master list: Newest First
        invoices.sort((a, b) -> b.getStartTime().compareTo(a.getStartTime()));

        // 2. Print every invoice in the list
        if (invoices.isEmpty()) {
            System.out.println("No invoices found.");
        } else {
            for (Invoice invoice : invoices) {
                // Reusing your existing line formatter
                System.out.println(formatInvoiceLine(invoice));
            }
        }

        List<String> topUps = CreditManager.getInstance().getAllTopUpHistories();
        if (!topUps.isEmpty()) {
            System.out.println("\nCredit Top-Ups:");
            for (String entry : topUps) {
                // Assume entries already formatted as "Top-up | Amount: XX,YY | Date: dd-MM-yyyy HH:mm:ss"
                System.out.println(entry);
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
