package AccountSteps;

import io.cucumber.java.en.Given;
import org.example.managers.AccountManager;

public class AccountSystemSteps {


    @Given("a new Account Manager")
    public void aNewAccountManager() {
        AccountManager.getInstance().clearAccounts();
    }
}
