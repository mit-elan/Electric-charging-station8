package org.example;

import org.example.entities.Account;
import org.example.managers.AccountManager;

public class ElectricFillingStationNetwork {

    public static void main(String[] args) {

        System.out.println("=== Electric Filling Station Network Test ===");

        // get the singleton manager
        AccountManager accountManager = AccountManager.getInstance();

        System.out.println("-> Creating a new account...");
        Account alice = accountManager.createAccount(
                "Alice",
                "alice@example.com",
                "pass123"
        );

        System.out.println("Account created:");
        System.out.println(alice);

        System.out.println("\n-> Reading account from manager...");
        Account readBack = accountManager.readAccount(alice.getCustomerID());
        System.out.println(readBack);

        System.out.println("\n-> Listing all accounts in system:");
        System.out.println(accountManager);

        System.out.println("\n=== End of test ===");
    }
}
