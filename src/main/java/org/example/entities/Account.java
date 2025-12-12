package org.example.entities;

public class Account {
    private final String customerID;
    private String name;
    private String email;
    private String password;

    public Account(String customerID, String name, String email, String password) {
        this.customerID = customerID;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getCustomerID() { return customerID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "Account " + customerID + ": " + name + ", " + email;
    }
}
