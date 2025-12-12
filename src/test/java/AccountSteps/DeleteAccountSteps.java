package AccountSteps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.example.managers.AccountManager;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteAccountSteps {
    private final AccountManager accountManager = AccountManager.getInstance();
    // Variable to capture expected exception in Sad Path scenarios
    private Exception caughtException = null;

    @Given("the following accounts exist:")
    public void the_following_accounts_exist(DataTable dataTable) {

        // Data is passed as a list of maps (one map per row)
        List<Map<String, String>> accountsData = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> data : accountsData) {
            accountManager.createAccountWithID(data.get("Customer ID"), data.get("Name"), data.get("Email"), data.get("Password"));
        }
    }

    // --- WHEN Steps ---

    @When("the admin deletes the account with Customer ID {string}")
    public void the_admin_deletes_the_account_with_customer_id(String customerID) {
        try {
            accountManager.deleteAccount(customerID);
        } catch (IllegalArgumentException e) {
            // Catch the expected exception when deleting a non-existing account
            caughtException = e;
        }
    }


    @Then("the total number of existing accounts is {int}")
    public void the_total_number_of_existing_accounts_is(int expectedCount) {
        int actualCount = accountManager.getAccounts().size();
        Assertions.assertEquals(expectedCount, actualCount, "Expected account count to be " + expectedCount + ", but found " + actualCount);
    }

    @Then("the account with Customer ID {string} does not exist")
    public void the_account_with_customer_id_does_not_exist(String customerID) {
        // Use the getAccount method which returns null if not found
        Assertions.assertNull(accountManager.getAccount(customerID), "Account with ID " + customerID + " should have been deleted, but was found.");
    }

    @Then("an exception is thrown indicating no account was found")
    public void an_exception_is_thrown_indicating_no_account_was_found() {
        // This step is specifically for the 'Sad Path' scenario
        Assertions.assertNotNull(caughtException, "Expected an exception to be thrown, but no exception was caught.");

        assertInstanceOf(IllegalArgumentException.class, caughtException, "Expected an IllegalArgumentException, but found: " + caughtException.getClass().getSimpleName());

        // Optional: Check the message to ensure it's the correct error
        Assertions.assertTrue(caughtException.getMessage().contains("No account found"), "Exception message should indicate account not found.");
    }
}