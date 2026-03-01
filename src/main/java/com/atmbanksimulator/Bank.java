package com.atmbanksimulator;

// ===== 📚🌐Bank (Domain / Service / Business Logic) =====

// Bank class: a simple implementation of a bank, containing a list of bank accounts
// and has a currently logged-in account (loggedInAccount).
public class Bank {

    // ToDO: Optional extension:
    // Improve account management in the Bank class:
    // Replace Array with ArrayList for managing BankAccount objects.
    // Refactor addBankAccount and login methods to leverage ArrayList.

    // Instance variables storing bank information
    private int maxAccounts = 10;                       // Maximum number of accounts the bank can hold
    private int numAccounts = 0;                        // Current number of accounts in the bank
    private BankAccount[] accounts = new BankAccount[maxAccounts];  // Array to hold BankAccount objects
    private BankAccount loggedInAccount = null;         // Currently logged-in account ('null' if no one is logged in)

    // a method to create new BankAccount - this is known as a 'factory method' and is a more
    // flexible way to do it than just using the 'new' keyword directly.
    public BankAccount makeBankAccount(String accNumber, String accPasswd, int balance) {
        return new BankAccount(accNumber, accPasswd, balance);
    }

    // a method to add a new bank account to the bank - it returns true if it succeeds
    // or false if it fails (because the bank is 'full')
    public boolean addBankAccount(BankAccount a) {
        if (numAccounts < maxAccounts) {
            accounts[numAccounts] = a;
            numAccounts++;
            return true;
        } else {
            return false;
        }
    }

    // Variant of addBankAccount: creates a BankAccount and adds it in one step.
    // This is an example of method overloading: two methods can share the same name
    // if they have different parameter lists.
    public boolean addBankAccount(String accNumber, String accPasswd, int balance) {
        return addBankAccount(makeBankAccount(accNumber, accPasswd, balance));
    }

    // Check whether the given accountNumber and password match an existing BankAccount.
    // If successful, set 'loggedInAccount' to that account and return true.
    // Otherwise, set 'loggedInAccount' to null and return false.
    public boolean login(String accountNumber, String password) {
        logout(); // logout of any previous loggedInAccount

        // Search the accounts array to find a BankAccount with a matching accountNumber and password.
        // - If found, set 'loggedInAccount' to that account and return true.
        // - If not found, reset 'loggedInAccount' to null and return false.
        for (int i = 0; i < numAccounts; i++) {
            BankAccount b = accounts[i];
            if (b.getAccNumber().equals(accountNumber) && b.getaccPasswd().equals(password)) {
                // found the right account
                loggedInAccount = b;
                return true;
            }
        }
        // not found - return false
        loggedInAccount = null;
        return false;
    }

    //Log out of the currently logged-in account, if any
    public void logout() {
        if (loggedIn()) {
            loggedInAccount = null;
        }
    }

    // Check whether the bank currently has a logged-in account
    public boolean loggedIn() {
        if (loggedInAccount == null) {
            return false;
        } else {
            return true;
        }
    }

    // Attempt to deposit money into the currently logged-in account
    // by calling the deposit method of the BankAccount object
    public boolean deposit(int amount) {
        if (loggedIn()) {
            return loggedInAccount.deposit(amount);
        } else {
            return false;
        }
    }


    // Attempt to withdraw money from the currently logged-in account
    // by calling the withdraw method of the BankAccount object
    public boolean withdraw(int amount) {
        if (loggedIn()) {
            return loggedInAccount.withdraw(amount);
        } else {
            return false;
        }
    }

    // Attempt to transfer money to a different account
    // by calling the transfer method of the BankAccount object
    public boolean transfer(int amount, String transAccNumber) {

        if (!loggedIn()) {
            return false;
        }

        //TODO get a suitable error message for transferring to yourself
        // Prevent transferring to yourself
        if (loggedInAccount.getAccNumber().equals(transAccNumber)) {
            return false;
        }

        // Find target account
        for (int i = 0; i < numAccounts; i++) {

            BankAccount target = accounts[i];

            if (target.getAccNumber().equals(transAccNumber)) {

                // First withdraw from sender
                if (!loggedInAccount.withdraw(amount)) {
                    return false; // insufficient funds
                }

                // Then deposit to receiver
                target.deposit(amount);

                return true;
            }
        }

        return false; // account not found
    }

    // get the currently logged-in account balance
    // by calling the getBalance method of the BankAccount object
    public int getBalance() {
        if (loggedIn()) {
            return loggedInAccount.getBalance();
        } else {
            return -1; // use -1 as an indicator of an error
        }
    }

    //Checks which account type is logged in (Savings, Prime, Current or Student)
    public BankAccount getLoggedInAccount() {
        return loggedInAccount;
    }

    //Return the number of accounts
    public int getAccountCount() {
        return numAccounts;
    }

    // Returns array of account numbers
    // - Only account numbers are returned to prevent security vulnerability from returning private account info to UIModel
    // - And to maintain ethical and legal footing of only using data as needed
    public String[] getAccountNumbers() {
        String[] accountNumbers = new String[numAccounts];
        for (int i = 0; i < numAccounts; i++) {
            accountNumbers[i]=accounts[i].getAccNumber();
        }
        return accountNumbers;
    }
    // ===== 🔐 Change Password Support =====

    // Validate the old password against the currently logged-in account
    public boolean verifyLoggedInPassword(String oldPassword) {
        if (!loggedIn()) return false;
        return loggedInAccount.getaccPasswd().equals(oldPassword);
    }

    // Change the password of the currently logged-in account
// NOTE: BankAccount must implement setAccPasswd(String)
    public boolean changeLoggedInPassword(String newPassword) {
        if (!loggedIn()) return false;
        loggedInAccount.setAccPasswd(newPassword);
        System.out.println("DEBUG: password now = " + loggedInAccount.getaccPasswd()
                + " for acc " + loggedInAccount.getAccNumber());
        return true;
    }
}

