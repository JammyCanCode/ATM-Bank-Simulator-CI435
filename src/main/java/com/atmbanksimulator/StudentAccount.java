package com.atmbanksimulator;

/**
 * A StudentAccount is a type of BankAccount with a withdrawal limit.
 * Withdrawals are restricted to a maximum of £50 per transaction.
 */
public class StudentAccount extends BankAccount{

    /**
     * Constructs a StudentAccount with specified details.
     */
    public StudentAccount(String accNumber, String accPasswd, int Balance) {
        super(accNumber, accPasswd, Balance); //Constructor for Student Account
    }

    /**
     * Withdraws a specified amount, enforcing a £50 limit.
     * @return true if successful and within limit, false otherwise.
     */
    @Override
    public boolean withdraw(int amount) {
        if (amount > 50) {
            return false; //Prevents withdrawals from the acc that exceed £50
        } return super.withdraw(amount);
    }
}