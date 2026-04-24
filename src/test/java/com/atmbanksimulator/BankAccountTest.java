package com.atmbanksimulator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    @Test
    public void testBaseAccountDeposit() {
        BankAccount account = new BankAccount("12345", "11111", 100);
        assertTrue(account.deposit(50));
        assertEquals(150, account.getBalance());
        assertFalse(account.deposit(-10));
        assertEquals(150, account.getBalance());
    }

    @Test
    public void testBaseAccountWithdraw() {
        BankAccount account = new BankAccount("12345", "11111", 100);
        assertTrue(account.withdraw(50));
        assertEquals(50, account.getBalance());
        assertFalse(account.withdraw(100));
        assertFalse(account.withdraw(-10));
        assertEquals(50, account.getBalance());
    }

    @Test
    public void testPrimeAccountOverdraft() {
        PrimeAccount account = new PrimeAccount("12345", "11111", 100, 500);
        assertTrue(account.withdraw(200)); // Within overdraft
        assertEquals(-100, account.getBalance());
        assertTrue(account.withdraw(400)); // Exactly at limit (100+500=600 total capacity)
        assertEquals(-500, account.getBalance());
        assertFalse(account.withdraw(1)); // Exceeds overdraft
    }

    @Test
    public void testSavingsAccountInterest() {
        SavingsAccount account = new SavingsAccount("12345", "11111", 1000, 0.05);
        assertFalse(account.hasInterestBeenApplied());
        account.addInterest();
        assertEquals(1050, account.getBalance());
        assertTrue(account.hasInterestBeenApplied());
    }

    @Test
    public void testStudentAccountWithdrawLimit() {
        StudentAccount account = new StudentAccount("12345", "11111", 200);
        assertTrue(account.withdraw(50));
        assertEquals(150, account.getBalance());
        assertFalse(account.withdraw(51)); // Exceeds £50 limit
        assertEquals(150, account.getBalance());
    }
}
