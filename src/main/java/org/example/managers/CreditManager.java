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

    public void createCreditIfAbsent(Account account) {
        if (account.getCredit() == null) {
            account.setCredit(new Credit());
        }
    }

    public void topUpCredit(Account account, double amount) {
        if (account == null) {
            throw new IllegalStateException("Account not found");
        }
        createCreditIfAbsent(account);
        account.getCredit().addCredit(amount);
    }

    public void addManualTopUp(Account account, double amount, String date) {
        if (account == null) {
            throw new IllegalStateException("Account not found");
        }
        createCreditIfAbsent(account);
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