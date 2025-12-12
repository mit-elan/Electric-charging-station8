package steps;

import io.cucumber.java.en.Given;
import org.example.entities.AccountManager;

public class SystemSteps {


    @Given("a new Account Manager")
    public void aNewAccountManager() {
        AccountManager.getInstance().clearAccounts();
    }
}
