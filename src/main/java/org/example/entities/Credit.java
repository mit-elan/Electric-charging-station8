package org.example.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Credit {
    private double amount;          // Current Balance
    private LocalDateTime lastUpdated;     // Timestamp of last change
    private final List<String> history = new ArrayList<>();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public Credit(double initialAmount) {
        this.amount = initialAmount;
        this.lastUpdated = LocalDateTime.now();
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    //Getter for the history of all top ups for all accounts
    public List<String> getHistory() {
        return new ArrayList<>(history);
    }

    public void addCredit(double amountToAdd) {
        if (amountToAdd < 0) throw new IllegalArgumentException("Cannot add negative credit.");

        this.amount += amountToAdd;
        this.lastUpdated = LocalDateTime.now();

        // Record this action
        recordTransaction(amountToAdd, this.lastUpdated);
    }

    public void addCreditManually(double amountToAdd, String dateTimeString) {
        if (amountToAdd < 0) throw new IllegalArgumentException("Cannot add negative credit.");

        // 1. Create a formatter that matches your Feature File format
        // (dd-MM-yyyy HH:mm:ss)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        // 2. Parse the input string into a real LocalDateTime object
        LocalDateTime manualDate = LocalDateTime.parse(dateTimeString, formatter);

        // 3. Update the amount
        this.amount += amountToAdd;

        // 4. Use the manual date
        this.lastUpdated = manualDate;

        // 5. Record the transaction with the MANUAL date
        recordTransaction(amountToAdd, manualDate);
    }

    public void subtractCredit(double amountToSubtract) {
        if (amountToSubtract < 0) throw new IllegalArgumentException("Cannot subtract negative credit.");
        if (amountToSubtract > this.amount) throw new IllegalArgumentException("Insufficient credit.");

        this.amount -= amountToSubtract;
        this.lastUpdated = LocalDateTime.now();
    }

    //Private helper to format the string
    private void recordTransaction(double amount, LocalDateTime dateTime) {
        String logEntry = String.format(
                "Top-up | Amount: %.2f | Date: %s",
                amount,
                dateTime.format(DATE_FORMATTER)
        );
        history.add(logEntry);
    }

    @Override
    public String toString() {
        return "Current Credit: " + amount;
    }

}