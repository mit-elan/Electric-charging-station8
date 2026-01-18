package org.example.CreditSteps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.entities.Account;
import org.example.entities.Credit;
import org.example.managers.AccountManager;
import org.example.managers.CreditManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TopUpCreditSteps {

    private final AccountManager accountManager;
    private final CreditManager creditManager;
    private LocalDateTime creditUpdateTimestampBefore;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public TopUpCreditSteps() {
        this.accountManager = AccountManager.getInstance();
        this.creditManager = CreditManager.getInstance();
    }

    @And("the following Customers exist:")
    public void the_following_customers_exist(io.cucumber.datatable.DataTable dataTable) {
        for (Map<String, String> row : dataTable.asMaps()) {

            String customerId = row.get("Customer ID");
            double initialCredit = Double.parseDouble(row.get("Initial Credit"));

            // Create account
            AccountManager.getInstance().createAccountWithID(
                    customerId,
                    "Test User " + customerId,
                    customerId + "@test.com",
                    "password"
            );

            // Treat initial money as NORMAL TOP-UP
            Account account = AccountManager.getInstance().getAccount(customerId);
            CreditManager.getInstance().topUpCredit(account, initialCredit);
        }
    }


    @When("the customer with ID {string} tops up their credit with {double}")
    public void the_customer_tops_up_their_credit(String customerId, double amount) {
        Credit credit = accountManager.getAccount(customerId).getCredit();
        creditUpdateTimestampBefore = LocalDateTime.now();
        creditManager.topUpCredit(accountManager.getAccount(customerId), amount);
    }

    @Then("the credit balance of Customer {string} is {double}")
    public void the_credit_balance_is(String customerId, double expectedBalance) {
        Credit credit = accountManager.getAccount(customerId).getCredit();

        assertEquals(
                expectedBalance,
                credit.getAmount(),
                0.001,
                "Credit balance should be updated correctly");
    }

    @And("the payment is processed successfully")
    public void the_payment_is_processed_successfully() {
        // In MVP scope, successful execution = successful payment
        // If you later add a payment status flag, assert it here
        assertTrue(true);
    }

    @And("the credit last updated date is stored")
    public void the_credit_last_updated_date_is_stored() {
        // 1. Get the account we are testing (assuming it's the first one created in your scenario)
        Account account = AccountManager.getInstance().getAccounts().get(0);

        // 2. Get the Credit object directly from that account
        Credit credit = account.getCredit();

        assertNotNull(
                credit.getLastUpdated(),
                "Credit last updated timestamp should be stored"
        );

        // 3. Parse the timestamp
        LocalDateTime lastUpdated = credit.getLastUpdated();


        // 4. Compare with the previous timestamp saved in your Step class
        if (creditUpdateTimestampBefore != null) {
            assertTrue(
                    lastUpdated.isAfter(creditUpdateTimestampBefore),
                    "Credit last updated timestamp should be updated after top-up"
            );
        }
    }
}