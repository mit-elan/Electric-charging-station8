package AccountSteps;

import org.example.entities.Account;
import org.example.managers.AccountManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.*;

public class CreateAccountSteps {
    private Account account;
    private final AccountManager accountManager = AccountManager.getInstance();

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
}
