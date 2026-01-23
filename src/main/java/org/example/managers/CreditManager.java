package org.example.managers;

import org.example.entities.Account;
import org.example.entities.Credit;

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

}