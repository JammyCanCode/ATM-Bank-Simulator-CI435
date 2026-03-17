package com.atmbanksimulator;

public class SavingsAccount extends BankAccount {
    private double interestRate;
    private boolean interestApplied = false;

    public SavingsAccount(String accNumber, String accPasswd, int Balance, double rate) {
        super(accNumber, accPasswd, Balance);
        this.interestRate = rate; //Constructor for SavingsAccount
    }
    public void addInterest() {
        int interest = (int)(getBalance() * interestRate);
        deposit(interest);
        interestApplied = true;
        //Logic to add Interest to balance stored in acc
    }

    //Checks to see if interest has already been applied to prevent other savings accounts not receiving interest.
    //If true, apply interest, if false, return balance
    public boolean hasInterestBeenApplied() {
        return interestApplied;
    }
}