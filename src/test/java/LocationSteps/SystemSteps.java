package LocationSteps;

import io.cucumber.java.en.Given;
import org.example.managers.LocationManager;

public class SystemSteps {

    @Given("a new Location Manager")
    public void aNewLocationManager() {
        LocationManager.getInstance().clearLocations();
    }
}
