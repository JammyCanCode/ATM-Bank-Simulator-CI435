package com.atmbanksimulator;

/**
 * A SavingsAccount is a type of BankAccount that earns interest.
 * Interest can be applied to the balance once during a session or as triggered.
 */
public class SavingsAccount extends BankAccount {
    private double interestRate;
    private boolean interestApplied = false;

    /**
     * Constructs a SavingsAccount with specified details.
     */
    public SavingsAccount(String accNumber, String accPasswd, int Balance, double rate) {
        super(accNumber, accPasswd, Balance);
        this.interestRate = rate; //Constructor for SavingsAccount
    }

    /**
     * Calculates and adds interest to the current balance.
     * Sets the interestApplied flag to true.
     */
    public void addInterest() {
        int interest = (int)(getBalance() * interestRate);
        deposit(interest);
        interestApplied = true;
        //Logic to add Interest to balance stored in acc
    }

    /**
     * Checks if interest has already been applied to this account.
     * @return true if interest has been applied, false otherwise.
     */
    public boolean hasInterestBeenApplied() {
        return interestApplied;
    }
}