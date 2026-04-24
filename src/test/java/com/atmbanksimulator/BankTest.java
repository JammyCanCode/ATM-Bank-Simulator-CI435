package com.atmbanksimulator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankTest {
    private Bank bank;

    @BeforeEach
    public void setUp() {
        bank = new Bank();
    }

    @Test
    public void testAddBankAccount() {
        assertTrue(bank.addBankAccount("10001", "11111", 100));
        assertEquals(1, bank.getAccountCount());
        
        // Duplicate
        assertFalse(bank.addBankAccount("10001", "22222", 200));
        assertEquals(1, bank.getAccountCount());

        // Invalid format
        assertFalse(bank.addBankAccount("123", "11111", 100));
        assertEquals(1, bank.getAccountCount());
    }

    @Test
    public void testLogin() {
        bank.addBankAccount("10001", "11111", 100);
        assertTrue(bank.login("10001", "11111"));
        assertTrue(bank.loggedIn());
        assertNotNull(bank.getLoggedInAccount());

        bank.logout();
        assertFalse(bank.loggedIn());
        assertNull(bank.getLoggedInAccount());
    }

    @Test
    public void testTransfer() {
        bank.addBankAccount("10001", "11111", 100);
        bank.addBankAccount("10002", "22222", 50);

        bank.login("10001", "11111");
        assertTrue(bank.transfer(30, "10002"));
        assertEquals(70, bank.getBalance());
        
        bank.logout();
        bank.login("10002", "22222");
        assertEquals(80, bank.getBalance());
    }

    @Test
    public void testChangePassword() {
        bank.addBankAccount("10001", "11111", 100);
        bank.login("10001", "11111");
        
        assertTrue(bank.verifyLoggedInPassword("11111"));
        assertFalse(bank.verifyLoggedInPassword("wrong"));
        
        bank.changeLoggedInPassword("55555");
        bank.logout();
        
        assertFalse(bank.login("10001", "11111"));
        assertTrue(bank.login("10001", "55555"));
    }
}
