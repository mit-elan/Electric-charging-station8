package org.example.AccountSteps;

import org.example.entities.Account;
import org.example.managers.AccountManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.*;

public class CreateAccountSteps {
    private Account account;
    private final AccountManager accountManager = AccountManager.getInstance();
    private Exception caughtException;

    @When("customer creates an account with name {string}, email {string}, and password {string}")
    public void customerCreatesAccount(String name, String email, String password) {
        try {
            String actualName = name.isEmpty() ? null : name;
            String actualEmail = email.isEmpty() ? null : email;
            String actualPassword = password.isEmpty() ? null : password;

            account = accountManager.createAccount(actualName, actualEmail, actualPassword);
        } catch (IllegalArgumentException e) {
            caughtException = e;
        }
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

    @Then("an exception is thrown indicating account fields cannot be null")
    public void anExceptionIsThrownIndicatingAccountFieldsCannotBeNull() {
        assertNotNull(caughtException, "Expected an exception to be thrown");
        assertInstanceOf(IllegalArgumentException.class, caughtException);
        assertTrue(caughtException.getMessage().contains("cannot be null"));
    }
}
