package AccountSteps;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.entities.Account;
import org.example.managers.AccountManager;
import org.junit.jupiter.api.Assertions;

import java.util.Map;

public class UpdateAccountSteps {
    private final AccountManager accountManager = AccountManager.getInstance();


    @Given("an account exists with the following details:")
    public void an_account_exists_with_the_following_details(DataTable dataTable) {
        // Convert the vertical table (Key | Value) into a Map
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        accountManager.createAccountWithID(
                data.get("Customer ID"),
                data.get("Name"),
                data.get("Email"),
                data.get("Password")
        );
    }

    @When("the customer updates the account with Customer ID {string} to:")
    public void the_customer_updates_the_account_with_customer_id_to(String customerID, DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        // We call the update method from your Manager
        accountManager.updateAccount(
                customerID,
                data.get("Name"),
                data.get("Email"),
                data.get("Password")
        );
    }

    /* Optimized "Then" Step:
       Instead of writing 3 separate methods for Name, Email, and Password,
       we use {word} to capture the field name dynamically.
    */
    @Then("the account with Customer ID {string} has {word} {string}")
    public void the_account_with_customer_id_has_field_value(String customerID, String field, String expectedValue) {
        Account account = accountManager.getAccount(customerID);

        // Ensure account actually exists before checking fields
        Assertions.assertNotNull(account, "Account should exist");

        switch (field) {
            case "Name":
                Assertions.assertEquals(expectedValue, account.getName());
                break;
            case "Email":
                Assertions.assertEquals(expectedValue, account.getEmail());
                break;
            case "Password":
                Assertions.assertEquals(expectedValue, account.getPassword());
                break;
            default:
                throw new IllegalArgumentException("Unknown field: " + field);
        }
    }
}