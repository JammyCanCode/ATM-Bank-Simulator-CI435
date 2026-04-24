package com.atmbanksimulator;

/**
 * Represents a basic bank account with an account number, password, and balance.
 * Provides core functionalities such as deposit, withdrawal, and balance inquiries.
 */

public class BankAccount {
    private String accNumber = "";
    private String accPasswd ="";
    protected int balance = 0;

    /**
     * Default constructor for BankAccount.
     */
    public BankAccount() {}

    /**
     * Constructs a BankAccount with specified details.
     */
    public BankAccount(String a, String p, int b) {
        accNumber = a;
        accPasswd = p;
        balance = b;
    }

    /**
     * Withdraws a specified amount from the account.
     */
    public boolean withdraw( int amount ) {
        if (amount < 0 || balance < amount) {
            return false;
        } else {
            balance = balance - amount;  // subtract amount from balance
            return true;
        }
    }

    /**
     * Deposits a specified amount into the account.
     */
    public boolean deposit( int amount ) {
        if (amount < 0) {
            return false;
        } else {
            balance = balance + amount;  // add amount to balance
            return true;
        }
    }

    /**
     * Forces a withdrawal without balance checks. Used by subclasses for special logic.
     */
    protected boolean forceWithdraw(int amount) {
        balance = balance - amount;
        return true;
    }

    /**
     * Returns the current balance of the account.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Returns the account number.
     */
    public String getAccNumber() {
        return accNumber;
    }

    /**
     * Returns the account password.
     */
    public String getaccPasswd() {
        return accPasswd;
    }

    /**
     * Sets a new password for the account.
     */
    public void setAccPasswd(String newPasswd) {
        this.accPasswd = newPasswd;
    }
}

