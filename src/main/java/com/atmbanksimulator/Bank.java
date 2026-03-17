
package com.atmbanksimulator;

// ===== 📚🌐Bank (Domain / Service / Business Logic) =====

public class Bank {

    // Maximum number of accounts
    private int maxAccounts = 10;

    // Current number of accounts
    private int numAccounts = 0;

    // Array storing accounts
    private BankAccount[] accounts = new BankAccount[maxAccounts];

    // Currently logged-in account
    private BankAccount loggedInAccount = null;

    // ===============================
    // ACCOUNT VALIDATION METHODS
    // ===============================

    // Validate account number (must be 5 digits)
    private boolean validAccountNumber(String accNumber) {
        if (accNumber == null) return false;
        return accNumber.matches("\\d{5}");
    }

    // Check if account already exists
    private boolean accountExists(String accNumber) {
        for (int i = 0; i < numAccounts; i++) {
            if (accounts[i].getAccNumber().equals(accNumber)) {
                return true;
            }
        }
        return false;
    }

    // ===============================
    // ACCOUNT CREATION METHODS
    // ===============================

    // Factory method
    public BankAccount makeBankAccount(String accNumber, String accPasswd, int balance) {
        return new BankAccount(accNumber, accPasswd, balance);
    }

    // Add an existing account object
    public boolean addBankAccount(BankAccount a) {

        if (numAccounts >= maxAccounts) {
            return false;
        }

        if (!validAccountNumber(a.getAccNumber())) {
            System.out.println("Invalid account number format.");
            return false;
        }

        if (accountExists(a.getAccNumber())) {
            System.out.println("Account number already exists.");
            return false;
        }

        accounts[numAccounts] = a;
        numAccounts++;

        return true;
    }

    // Add account using parameters
    public boolean addBankAccount(String accNumber, String accPasswd, int balance) {

        if (numAccounts >= maxAccounts) return false;

        if (!validAccountNumber(accNumber)) {
            System.out.println("Invalid account number format.");
            return false;
        }

        if (accountExists(accNumber)) {
            System.out.println("Duplicate account number.");
            return false;
        }

        BankAccount newAccount = new BankAccount(accNumber, accPasswd, balance);

        accounts[numAccounts] = newAccount;
        numAccounts++;

        return true;
    }

    // Add account with type
    public boolean addBankAccount(String type, String accNumber, String accPasswd, int balance) {

        if (numAccounts >= maxAccounts) return false;

        if (!validAccountNumber(accNumber)) return false;

        if (accountExists(accNumber)) return false;

        BankAccount newAccount;

        switch (type.toLowerCase()) {

            case "student":
                newAccount = new StudentAccount(accNumber, accPasswd, balance);
                break;

            case "savings":
                newAccount = new SavingsAccount(accNumber, accPasswd, balance, 0.02);
                break;

            case "prime":
                newAccount = new PrimeAccount(accNumber, accPasswd, balance, 5000);
                break;

            default:
                newAccount = new BankAccount(accNumber, accPasswd, balance);
        }

        accounts[numAccounts] = newAccount;
        numAccounts++;

        return true;
    }

    // ===============================
    // LOGIN SYSTEM
    // ===============================

    public boolean login(String accountNumber, String password) {

        logout();

        for (int i = 0; i < numAccounts; i++) {

            BankAccount b = accounts[i];

            if (b.getAccNumber().equals(accountNumber) &&
                    b.getaccPasswd().equals(password)) {

                loggedInAccount = b;
                return true;
            }
        }

        loggedInAccount = null;
        return false;
    }

    public void logout() {
        if (loggedIn()) {
            loggedInAccount = null;
        }
    }

    public boolean loggedIn() {
        return loggedInAccount != null;
    }

    // ===============================
    // TRANSACTIONS
    // ===============================

    public boolean deposit(int amount) {
        if (loggedIn()) {
            return loggedInAccount.deposit(amount);
        }
        return false;
    }

    public boolean withdraw(int amount) {
        if (loggedIn()) {
            return loggedInAccount.withdraw(amount);
        }
        return false;
    }

    public boolean transfer(int amount, String transAccNumber) {

        if (!loggedIn()) return false;

        if (loggedInAccount.getAccNumber().equals(transAccNumber)) {
            return false;
        }

        for (int i = 0; i < numAccounts; i++) {

            BankAccount target = accounts[i];

            if (target.getAccNumber().equals(transAccNumber)) {

                if (!loggedInAccount.withdraw(amount)) {
                    return false;
                }

                target.deposit(amount);
                return true;
            }
        }

        return false;
    }

    // ===============================
    // ACCOUNT INFO
    // ===============================

    public int getBalance() {

        if (loggedIn()) {
            return loggedInAccount.getBalance();
        }

        return -1;
    }

    public BankAccount getLoggedInAccount() {
        return loggedInAccount;
    }

    public int getAccountCount() {
        return numAccounts;
    }

    public String[] getAccountNumbers() {

        String[] accountNumbers = new String[numAccounts];

        for (int i = 0; i < numAccounts; i++) {
            accountNumbers[i] = accounts[i].getAccNumber();
        }

        return accountNumbers;
    }

    // ===============================
    // PASSWORD MANAGEMENT
    // ===============================

    public boolean verifyLoggedInPassword(String oldPassword) {

        if (!loggedIn()) return false;

        return loggedInAccount.getaccPasswd().equals(oldPassword);
    }

    public boolean changeLoggedInPassword(String newPassword) {

        if (!loggedIn()) return false;

        loggedInAccount.setAccPasswd(newPassword);

        System.out.println(
                "DEBUG: password now = " +
                        loggedInAccount.getaccPasswd() +
                        " for acc " +
                        loggedInAccount.getAccNumber()
        );

        return true;
    }
}

