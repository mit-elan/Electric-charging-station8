package org.example;

public class ScenarioContext {
    public static Exception lastException;

    public static void clear() {
        lastException = null;
    }
}
