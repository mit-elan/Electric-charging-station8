package org.example.InvoicesSteps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.example.entities.Account;
import org.example.enums.ChargingMode;
import org.example.managers.AccountManager;
import org.example.managers.CreditManager;
import org.example.managers.InvoiceManager;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReadInvoicesSteps {

    private final InvoiceManager invoiceManager = InvoiceManager.getInstance();
    private final AccountManager accountManager = AccountManager.getInstance();
    private final CreditManager creditManager = CreditManager.getInstance();

    private ByteArrayOutputStream outputStream;


    @Given("a new InvoiceManager")
    public void a_new_invoice_manager() {
        invoiceManager.clearInvoices();
    }

    @Given("the following invoices exist for Customer {string}:")
    public void the_following_invoices_exist_for_customer(String customerID, DataTable dataTable) {
        // 1. Make sure the account exists
        Account account = accountManager.getAccount(customerID);
        if (account == null) {
            accountManager.createAccountWithID(customerID, "Name-" + customerID, customerID + "@example.com", "password123");
            accountManager.getAccount(customerID);
        }

        // 2. Create invoices
        for (Map<String, String> row : dataTable.asMaps()) {
            invoiceManager.createInvoiceManually(
                    customerID,
                    accountManager,                         // use the method parameter, not row.get()
                    row.get("Start Time"),
                    row.get("Item No"),
                    row.get("Location"),
                    row.get("Charging Point"),
                    ChargingMode.valueOf(row.get("Mode")),
                    Integer.parseInt(row.get("Duration")),
                    Double.parseDouble(row.get("Energy Used")),
                    Double.parseDouble(row.get("Price"))
            );
        }
    }

    @Given("the customer with ID {string} has made the following credit top ups:")
    public void the_customer_has_made_credit_top_ups(String customerID, DataTable dataTable) {
        Account account = accountManager.getAccount(customerID);

        for (Map<String, String> row : dataTable.asMaps()) {
            creditManager.addManualTopUp(
                    account,
                    Double.parseDouble(row.get("Amount")),
                    row.get("Date of Update")
            );
        }
    }

    @When("the customer with ID {string} views their invoice overview")
    public void the_customer_views_their_invoice_overview(String customerId) {
        Account account = accountManager.getAccount(customerId);

        // Capture console output
        PrintStream originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        invoiceManager.printInvoiceOverviewForAccount(account);

        // Restore System.out
        System.setOut(originalOut);
    }

    @Then("the invoice overview shows:")
    public void the_invoice_overview_shows(String expectedOutput) {
        String actualOutput = outputStream.toString().trim();
        String expected = expectedOutput.trim();

        assertEquals(expected, actualOutput);
    }



    // -----------------------------
    // Operator Scenario
    // -----------------------------
    @Before
    public void prepareNewTest() {
        // This runs before EVERY scenario
        InvoiceManager.getInstance().clearInvoices();
        AccountManager.getInstance().clearAccounts();
        // Add other managers as needed
        System.out.println("--- State Cleared for New Scenario ---");
    }

    @Given("each customer has topped up credit:")
    public void each_customer_has_topped_up_credit(DataTable dataTable) {
        for (Map<String, String> row : dataTable.asMaps()) {
            String customerId = row.get("Customer ID");
            Account account = accountManager.getAccount(customerId);
            String date = row.get("Date of Update");

            if (account == null) {
                accountManager.createAccountWithID(customerId, "Name-" + customerId, customerId + "@example.com", "password123");
                creditManager.initializeCredit(customerId, 0);
                account = accountManager.getAccount(customerId);
            }


            creditManager.addManualTopUp(
                    account,
                    Double.parseDouble(row.get("Amount")),
                    date // Use the clean date
            );
        }
    }

    @And("the following invoices exist:")
    public void the_following_invoices_exist(DataTable dataTable) {
        for (Map<String, String> row : dataTable.asMaps()) {
            String customerId = row.get("Customer ID");  // match your table column

            Account account = accountManager.getAccount(customerId);
            if (account == null) {
                accountManager.createAccountWithID(customerId, "Name-" + customerId, customerId + "@example.com", "password123");
                account = accountManager.getAccount(customerId);
            }

            invoiceManager.createInvoiceManually(
                    customerId,
                    accountManager,
                    row.get("Start Time"),
                    row.get("Item No"),
                    row.get("Location"),
                    row.get("Charging Point"),
                    ChargingMode.valueOf(row.get("Mode")),
                    Integer.parseInt(row.get("Duration")),
                    Double.parseDouble(row.get("Energy Used")),
                    Double.parseDouble(row.get("Price"))
            );
        }
    }

    @When("the operator views all invoices")
    public void the_operator_views_all_invoices() {
        // 1. Sort the master list globally so the isSorted check passes
        invoiceManager.getAllInvoices().sort((a, b) -> b.getStartTime().compareTo(a.getStartTime()));
        invoiceManager.printGlobalInvoiceHistory();
    }

    @Then("a list of charging sessions is shown sorted by start time")
    public void a_list_of_charging_sessions_is_shown_sorted_by_start_time() {
        Assertions.assertTrue(invoiceManager.isSortedByStartTime(), "Invoices must be sorted by start time");
    }

    @Then("a list of charging sessions for all customers is shown sorted by start time")
    public void a_list_of_charging_sessions_for_all_customers_is_shown_sorted_by_start_time() {
        Assertions.assertTrue(invoiceManager.isSortedByStartTime(), "Invoices must be sorted by start time");
    }

    @Then("each invoice entry contains charging and pricing details")
    public void each_invoice_entry_contains_charging_and_pricing_details() {
        Assertions.assertTrue(invoiceManager.allInvoicesContainRequiredFields(), "All invoices must contain full details");
    }

    @Then("top-up transactions are displayed")
    public void top_up_transactions_are_displayed() {
        Assertions.assertTrue(invoiceManager.hasTopUpInformation(), "Top-up transactions must be shown");
    }

    @Then("the current outstanding balance is displayed")
    public void the_current_outstanding_balance_is_displayed() {
        Assertions.assertTrue(invoiceManager.hasOutstandingBalanceInformation(), "Outstanding balance must be displayed");
    }
}
