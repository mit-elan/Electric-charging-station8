package org.example.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountManager {
    private static final AccountManager INSTANCE = new AccountManager();
    private final List<Account> accounts = new ArrayList<>();

    private AccountManager() {}

    public static AccountManager getInstance() { return INSTANCE; }

    public Account createAccount(String name, String email, String password) {
        // Simple validation
        if (name == null || email == null || password == null) {
            throw new IllegalArgumentException("Account fields cannot be null");
        }
        String id = UUID.randomUUID().toString();
        Account account = new Account(id, name, email, password);
        accounts.add(account);
        return account;
    }

    public Account readAccount(String customerID) {
        return accounts.stream()
                .filter(a -> a.getCustomerID().equals(customerID))
                .findFirst()
                .orElse(null);
    }

    public List<Account> getAccounts() { return accounts; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Accounts:\n");
        for (Account a : accounts) sb.append(a).append("\n");
        return sb.toString();
    }
}
