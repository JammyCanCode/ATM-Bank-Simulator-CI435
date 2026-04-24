package com.atmbanksimulator;

/**
 * A PrimeAccount is a type of BankAccount that allows for an overdraft.
 * Users can withdraw funds beyond their current balance up to a specified overdraft limit.
 */
public class PrimeAccount extends BankAccount {
    private int overdraftLimit;

    /**
     * Constructs a PrimeAccount with specified details.
     */
    public PrimeAccount(String accNumber, String accPasswd, int balance, int overdraftLimit) {
        super(accNumber, accPasswd, balance);
        this.overdraftLimit = overdraftLimit; //Constructor for PrimeAccount
    }

    /**
     * Withdraws a specified amount, allowing for overdraft.
     * @return true if successful, false if amount is negative or exceeds balance + overdraftLimit.
     */
    @Override
    public boolean withdraw(int amount) {
        if (amount < 0 || amount > (getBalance() + overdraftLimit)) {
            return false; //Allows withdrawals from PrimeAccount provided the overdraft is not exceeded
        } else {
            return forceWithdraw(amount);
        }
    }
}