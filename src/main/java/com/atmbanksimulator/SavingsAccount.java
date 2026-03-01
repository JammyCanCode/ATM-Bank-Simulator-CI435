package com.atmbanksimulator;

public class SavingsAccount extends BankAccount {
    private double interestRate;

    public SavingsAccount(String accNumber, String accPasswd, int Balance, double rate) {
        super(accNumber, accPasswd, Balance);
        this.interestRate = rate; //Constructor for SavingsAccount
    }
    public void addInterest() {
        int interest = (int)(getBalance() * interestRate);
        deposit(interest);
        //Logic to add Interest to balance stored in acc
    }
}