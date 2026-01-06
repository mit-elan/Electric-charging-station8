package org.example.managers;

import org.example.entities.Account;
import org.example.entities.Credit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CreditManager {
    private static final CreditManager INSTANCE = new CreditManager();

    private CreditManager() {}

    public static CreditManager getInstance() {
        return INSTANCE;
    }

    // We don't need to clear anything here because the data lives in the Accounts
    public void clearCredits() {}

    public void initializeCredit(String customerId, double initialAmount) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null");
        }

        if (initialAmount < 0) {
            throw new IllegalArgumentException("Initial credit cannot be negative");
        }

        Account account = AccountManager.getInstance().readAccount(customerId);

        if (account == null) {
            throw new IllegalArgumentException("Account not found for ID: " + customerId);
        }

        if (account.getCredit() != null) {
            throw new IllegalStateException("Credit already initialized for this account");
        }

        account.setCredit(new Credit(initialAmount));
    }

    public void topUpCredit(Account account, double amount) {
        if (account == null || account.getCredit() == null) {
            throw new IllegalStateException("Account or credit not initialized");
        }
        account.getCredit().addCredit(amount);
    }

    public void addManualTopUp(Account account, double amount, String date) {
        if (account == null || account.getCredit() == null) {
            throw new IllegalStateException("Account or credit not initialized");
        }
        account.getCredit().addCreditManually(amount, date);
    }

    // NEW: Get the simple list of strings from the Account's Credit
    public List<String> getTopUpHistoryByAccount(Account account) {
        if (account == null) return Collections.emptyList();
        return account.getCredit().getHistory();
    }

    public List<String> getAllTopUpHistories() {
        List<String> allHistories = new ArrayList<>();

        List<Account> allAccounts = AccountManager.getInstance().getAccounts();

        //Loop through each account and grab their history
        for (Account account : allAccounts) {
            List<String> accountHistory = account.getCredit().getHistory();

            //header so you know whose history it is
            if (!accountHistory.isEmpty()) {
                allHistories.add("History for Customer: " + account.getCustomerID());
                allHistories.addAll(accountHistory);
                allHistories.add("-----------------------");
            }
        }
        return allHistories;
    }

}