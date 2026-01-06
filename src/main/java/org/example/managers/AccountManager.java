package org.example.managers;

import org.example.entities.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountManager {
    //The application uses singleton managers to represent system-wide services, and SystemSteps reset their state to guarantee isolated BDD scenarios.
    private static final AccountManager INSTANCE = new AccountManager();
    private final List<Account> accounts = new ArrayList<>();

    private AccountManager() {
    }

    public static AccountManager getInstance() {
        return INSTANCE;
    }

    public void clearAccounts() {
        accounts.clear();
    }

    public Account createAccount(String name, String email, String password) {
        if (name == null || email == null || password == null) {
            throw new IllegalArgumentException("Account fields cannot be null");
        }
        String id = UUID.randomUUID().toString();
        Account account = new Account(id, name, email, password);
        accounts.add(account);
        return account;
    }

    //function create Account With ID for testing purposes
    public void createAccountWithID(String customerID, String name, String email, String password) {
        if (customerID == null || name == null || email == null || password == null) {
            throw new IllegalArgumentException("Account fields cannot be null");
        }
        Account account = new Account(customerID, name, email, password);
        accounts.add(account);
    }

    public Account getAccount(String customerID) {
        for (Account account : accounts) {
            if (account.getCustomerID().equals(customerID)) {
                return account;
            }
        }
        return null; // return null if not found
    }


    public Account readAccount(String customerID) {
        return accounts.stream()
                .filter(a -> a.getCustomerID().equals(customerID))
                .findFirst()
                .orElse(null);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void updateAccount(String customerID, String name, String email, String password) {
        for (Account account : accounts) {
            if (account.getCustomerID().equals(customerID)) {
                if (name != null) account.setName(name);
                if (email != null) account.setEmail(email);
                if (password != null) account.setPassword(password);
                return; // account found and updated, exit method
            }
        }
        throw new IllegalArgumentException("No account found with Customer ID: " + customerID);
    }

    public void deleteAccount(String customerID) {
        Account accountToRemove = null;
        for (Account account : accounts) {
            if (account.getCustomerID().equals(customerID)) {
                accountToRemove = account;
                break;
            }
        }
        if (accountToRemove != null) {
            accounts.remove(accountToRemove);
        } else {
            throw new IllegalArgumentException("No account found with Customer ID: " + customerID);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Account Overview:\n");
        for (Account a : accounts) {
            sb.append(a.toString()).append("\n");
        }
        return sb.toString();
    }
}

