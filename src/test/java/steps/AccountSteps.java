package steps;

import io.cucumber.datatable.DataTable;
import org.example.entities.Account;
import org.example.entities.AccountManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AccountSteps {
    private Account account;
    private AccountManager accountManager;
    private Account retrievedAccount;
    private String generatedCustomerID;


// ----------------------------
// Create Account
// ----------------------------


    @Given("a new Account Manager")
    public void newAccountManager() {
        accountManager = AccountManager.getInstance().clearAccounts();
    }

    @When("customer creates an account with name {string}, email {string}, and password {string}")
    public void customerCreatesAccount(String name, String email, String password) {
        account = accountManager.createAccount(name, email, password);
    }

    @Then("the account exists with name {string} and email {string}")
    public void theAccountExists(String name, String email) {
        assertEquals(name, account.getName());
        assertEquals(email, account.getEmail());
    }

    @And("a unique customer ID is generated")
    public void uniqueCustomerIDGenerated() {
        assertNotNull(account.getCustomerID());
    }

// ----------------------------
// Read Account / One Single Account Scenario
// ----------------------------

    @Given("an account exists with:")
    public void anAccountExistsWith(DataTable table) {
        Map<String, String> data = table.asMap(String.class, String.class);

        String name = data.get("Name");
        String email = data.get("Email");
        String password = data.get("Password");

        Account singleTestAccount = accountManager.createAccount(name, email, password);
        generatedCustomerID = singleTestAccount.getCustomerID();
    }

    @When("the customer requests the account with the generated Customer ID")
    public void theCustomerRequestsAccount() {
        retrievedAccount = accountManager.readAccount(generatedCustomerID);
    }

    @Then("the displayed Name is {string}")
    public void displayedNameIs(String expectedName) {
        assertEquals(expectedName, retrievedAccount.getName());
    }

    @Then("the displayed Email is {string}")
    public void displayedEmailIs(String expectedEmail) {
        assertEquals(expectedEmail, retrievedAccount.getEmail());
    }

    @Then("the displayed Password is {string}")
    public void displayedPasswordIs(String expectedPassword) {
        assertEquals(expectedPassword, retrievedAccount.getPassword());
    }

// ----------------------------
// Multiple Accounts Scenario
// ----------------------------

    @When("the following accounts are created:")
    public void theFollowingAccountsAreCreated(DataTable table) {
        List<Map<String, String>> rows = table.asMaps();

        for (Map<String, String> row : rows) {
            accountManager.createAccount(
                    row.get("Name"),
                    row.get("Email"),
                    row.get("Password")
            );
        }
    }

    @Then("reading the account list shows the following output:")
    public void readingAccountListShows(String expectedOutput) {
        String actualOutput = accountManager.toString().trim();
        assertEquals(expectedOutput.trim(), actualOutput);
    }
}

