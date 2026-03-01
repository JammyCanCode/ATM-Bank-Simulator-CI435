package com.atmbanksimulator;

public class StudentAccount extends BankAccount{


    public StudentAccount(String accNumber, String accPasswd, int Balance) {
        super(accNumber, accPasswd, Balance); //Constructor for Student Account
    }

    @Override
    public boolean withdraw(int amount) {
        if (amount > 50) {
            return false; //Prevents withdrawals from the acc that exceed £50
        } return super.withdraw(amount);
    }
}