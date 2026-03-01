package com.atmbanksimulator;

public class PrimeAccount extends BankAccount {
    private int overdraftLimit;

    public PrimeAccount(String accNumber, String accPasswd, int balance, int overdraftLimit) {
        super (accNumber, accPasswd, balance);
        this.overdraftLimit = overdraftLimit; //Constructor for PrimeAccount
    }
    @Override
    public boolean withdraw(int amount) {
        if (amount < 0 || amount > (getBalance() + overdraftLimit)) {
            return false; //Allows withdrawals from PrimeAccount provided the overdraft is not exceeded
        } else {
            return forceWithdraw(amount);
        }
    }
    private boolean forceWithdraw(int amount) {
        return super.withdraw(amount);
    }
}