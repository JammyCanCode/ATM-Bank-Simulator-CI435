
package com.atmbanksimulator;

// ===== 📚🌐Bank (Domain / Service / Business Logic) =====

/**
 * The Bank class manages a collection of BankAccounts.
 * It handles account creation, validation, authentication (login/logout),
 * and transactions like deposits, withdrawals, and transfers.
 */
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

    /**
     * Validates if the account number follows the required format (5 digits).
     * @return true if valid, false otherwise.
     */
    private boolean validAccountNumber(String accNumber) {
        if (accNumber == null) return false;
        return accNumber.matches("\\d{5}");
    }

    /**
     * Checks if an account with the given account number already exists in the bank.
     * @return true if it exists, false otherwise.
     */
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

    /**
     * Factory method to create a basic BankAccount.
     */
    public BankAccount makeBankAccount(String accNumber, String accPasswd, int balance) {
        return new BankAccount(accNumber, accPasswd, balance);
    }

    /**
     * Adds an existing BankAccount object to the bank's records.
     * @return true if added successfully, false if bank is full, format is invalid, or account already exists.
     */
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

    /**
     * Creates and adds a basic BankAccount using provided parameters.
     * @return true if created and added successfully, false otherwise.
     */
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

    /**
     * Creates and adds a specific type of BankAccount.
     * @return true if created and added successfully, false otherwise.
     */
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

    /**
     * Attempts to log in to an account with the provided number and password.
     * @return true if login is successful, false otherwise.
     */
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

    /**
     * Logs out the currently logged-in account.
     */
    public void logout() {
        if (loggedIn()) {
            loggedInAccount = null;
        }
    }

    /**
     * Checks if there is a currently logged-in account.
     * @return true if logged in, false otherwise.
     */
    public boolean loggedIn() {
        return loggedInAccount != null;
    }

    // ===============================
    // TRANSACTIONS
    // ===============================

    /**
     * Deposits an amount into the currently logged-in account.
     * @return true if successful, false otherwise.
     */
    public boolean deposit(int amount) {
        if (loggedIn()) {
            return loggedInAccount.deposit(amount);
        }
        return false;
    }

    /**
     * Withdraws an amount from the currently logged-in account.
     * @return true if successful, false otherwise.
     */
    public boolean withdraw(int amount) {
        if (loggedIn()) {
            return loggedInAccount.withdraw(amount);
        }
        return false;
    }

    /**
     * Transfers an amount from the logged-in account to another account.
     * @return true if transfer is successful, false otherwise.
     */
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

    /**
     * Returns the balance of the currently logged-in account.
     * @return The balance, or -1 if not logged in.
     */
    public int getBalance() {

        if (loggedIn()) {
            return loggedInAccount.getBalance();
        }

        return -1;
    }

    /**
     * Returns the currently logged-in BankAccount object.
     * @return The logged-in account, or null if none.
     */
    public BankAccount getLoggedInAccount() {
        return loggedInAccount;
    }

    /**
     * Returns the total number of accounts in the bank.
     * @return The account count.
     */
    public int getAccountCount() {
        return numAccounts;
    }

    /**
     * Returns an array of all account numbers currently in the bank.
     * @return An array of account number strings.
     */
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

    /**
     * Verifies if the provided password matches the password of the logged-in account.
     * @return true if it matches, false otherwise.
     */
    public boolean verifyLoggedInPassword(String oldPassword) {

        if (!loggedIn()) return false;

        return loggedInAccount.getaccPasswd().equals(oldPassword);
    }

    /**
     * Changes the password of the currently logged-in account.
     * @return true if successful, false otherwise.
     */
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

