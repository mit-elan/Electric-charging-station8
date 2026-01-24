package org.example;

import io.cucumber.java.Before;

public class Hooks {
    @Before
    public void resetState() {
        ScenarioContext.clear();
    }
}
