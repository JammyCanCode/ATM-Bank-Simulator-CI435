package com.atmbanksimulator;

// ===== 🧠 UIModel (Brain) =====

import java.util.Objects;

// The UIModel represents all the actual content and functionality of the app
// For the ATM, it keeps track of the information shown in the display
// (the laMsg and two tfInput boxes), and the interaction with the bank, executes
// commands provided by the controller and tells the view to update when
// something changes
public class UIModel {
    View view; // Reference to the View (part of the MVC setup)
    private Bank bank; // The ATM communicates with this Bank

    // The ATM UIModel can be in one of three states:
    // 1. Waiting for an account number
    // 2. Waiting for a password
    // 3. Logged in (ready to process requests for the logged-in account)
    // We represent each state with a String constant.
    // The 'final' keyword ensures these values cannot be changed.
    // 4. Ensures interest is not applied to any account until the check has been performed.
    // 5. Waiting for an amount of money to transfer to a selected account
    // 6. New initial entry point for the application
    private final String STATE_ACCOUNT_NO = "account_no";
    private final String STATE_PASSWORD = "password";
    private final String STATE_LOGGED_IN = "logged_in";
    private final String STATE_TRANS_MONEY = "trans_money";
    private final String STATE_WELCOME = "welcome";
    private final String STATE_GOODBYE = "goodbye";

    // Change-password states
    private final String STATE_PW_OLD     = "pw_old";
    private final String STATE_PW_NEW     = "pw_new";
    private final String STATE_PW_CONFIRM = "pw_confirm";

    // Interest flag (savings)
    private boolean interestApplied = false;

    // Variables representing the state and data of the ATM UIModel
    private String state = STATE_ACCOUNT_NO;    // Current state of the ATM
    private String accNumber = "";              // Account number being typed
    private String accPasswd = "";              // Password being typed
    private int attempts = 0;                   // Amount of wrong login attempts
    private String transAccNumber = "";         // Account number of account being transferred to

    // Change-password working vars
    private String pendingNewPassword = "";
    private int pwAttempts = 0;

    // Variables shown on the View display
    private String message;                // Message label text
    private String numberPadInput;         // Current number displayed in the TextField (as a string)
    private String result;                 // Contents of the TextArea (may be multiple lines)

    // UIModel constructor: pass a Bank object that the ATM interacts with
    public UIModel(Bank bank) {
        this.bank = bank;
    }

    // Initialize the ATM UIModel: this method is called by Main when starting the app
    // - Set state to STATE_ACCOUNT_NO - Changed 01/03/2026 to use new welcome page (STATE_WELCOME)
    // - Clear the numberPadInput - numbers displayed in the TextField
    // - Display the welcome message and user instructions
    public void initialise() {
        setState(STATE_WELCOME);
        message = "Welcome to the ATM";
        result = "Please select an option to begin.";
        update();
    }
    //Handle the transition between the welcome screen and the login screen.
    public void processLoginChoice() {
        setState(STATE_ACCOUNT_NO);
        numberPadInput = "";
        message = "Login";
        result = "Enter your account number\nFollowed by \"Password\"";
        update();
    }

    public void processCreateAccChoice() {
        //setState(STATE_CREATE_ACC); //For future account creation
        numberPadInput = "";
        message = "Register new Account";
        result = "Feature coming soon!\n Returning to Welcome..."; //Will add once Clay finishes the account
                                                                    // creation feature
        update();
    }

    // Reset the ATM UIModel after an invalid action or logout:
    // - Set state to STATE_ACCOUNT_NO
    // - Clear the numberPadInput
    // - Display the provided message and user instructions
    private void reset(String msg) {
        setState(STATE_ACCOUNT_NO);
        numberPadInput = "";
        message = msg;
        result = "Enter your account number\nFollowed by \"Ent\"";
    }

    // Change the ATM state and print a debug message whenever the state changes
    private void setState(String newState)
    {
        if ( !state.equals(newState) )
        {
            String oldState = state;
            state = newState;
            System.out.println("UIModel::setState: changed state from "+ oldState + " to " + newState);
        }
    }

    // These process**** methods are called by the Controller
    // in response to specific button presses on the GUI.

    // Handle a number button press: append the digit to numberPadInput
    public void processNumber(String numberOnButton) {
        // Optional extension:
        // Improve feedback by showing what the number is being entered for based on the current state.
        // e.g.  if state is STATE_ACCOUNT_NO, display "Receiving Account Number, Beep 5 received"
        numberPadInput += numberOnButton;
        message = "Beep! " + numberOnButton + " received";
        update();
    }

    // Handle the Clear button: reset the current number stored in numberPadInput
    public void processClear() {
        // Optional extension:
        // Improve feedback by showing what was cleared depending on the current state.
        // e.g. if state is STATE_ACCOUNT_NO, display "Account Number cleared: 123"
        if (!numberPadInput.isEmpty()) {
            numberPadInput = "";
            message = "Input Cleared";
            update();
        }
    }

    // Handle the Enter button.
    // This is a more complex method: pressing Enter causes the ATM to change state,
    // progressing from STATE_ACCOUNT_NO → STATE_PASSWORD → STATE_LOGGED_IN,
    // and back to STATE_ACCOUNT_NO when logging out.
    public void processEnter()
    {
        // The action depends on the current ATM state
        switch ( state )
        {
            case STATE_ACCOUNT_NO:
                // Waiting for a complete account number
                // If nothing was entered, reset with "Invalid Account Number"
                if (numberPadInput.equals("")) {
                    message = "Invalid Account Number";
                    reset(message);
                }
                else{
                    // Save the entered number as accNumber, clear numberPadInput,
                    // update the state to expect password, and provide instructions
                    accNumber = numberPadInput;
                    numberPadInput = "";
                    setState(STATE_PASSWORD);
                    message = "Account Number Accepted";
                    result = "Now enter your password\nFollowed by \"Ent\"";
                }
                break;

            case STATE_PASSWORD:
                // Waiting for a password
                // Save the typed number as accPasswd, clear numberPadInput,
                // then contact the bank to attempt login
                accPasswd = numberPadInput;
                numberPadInput = "";

                //TODO I KNOW THIS CODE LOOKS AND IS HORRIBLE, WILL FIX LATER - adrian
                //checks if more than 3 attempts have been made
                if (attempts < 3) {
                    if (bank.login(accNumber, accPasswd)) {
                        // Successful login: change state to STATE_LOGGED_IN and provide instructions
                        setState(STATE_LOGGED_IN);
                        message = "Logged In";
                        result = "Now enter the amount\nThen press transaction\n(Dep = Deposit, W/D = Withdraw)" +
                                "\nOr enter the account number\nTo transfer to\n(Xfr = Transfer)";
                        attempts = 0; //resets attempts counter
                    } else {
                        // Login failed: reset ATM and display error
                        // Check if 3 attempts made, if so, reset
                        if (attempts < 2) {
                            attempts++;
                            message = "Login failed: Remaining attempts = " + (3-attempts);
                            result = "Incorrect Account Number or password\nNow enter your password\n" +
                                    "Followed by \"Ent\"\naccount number=" + accNumber;
                        } else {
                            message = "Login Failed: Reset";
                            bank.logout();
                            attempts = 0;
                            reset(message);
                        }
                    }
                }
                break;

            // ===== Change Password flow =====

            case STATE_PW_OLD: {
                String oldPw = numberPadInput;
                numberPadInput = "";

                if (bank.verifyLoggedInPassword(oldPw)) {
                    setState(STATE_PW_NEW);
                    message = "Old password verified";
                    result = "Enter your NEW password (5 digits)\nFollowed by \"Ent\"";
                } else {
                    pwAttempts++;
                    if (pwAttempts >= 3) {
                        bank.logout();
                        interestApplied = false;
                        reset("Password check failed: Logged out");
                    } else {
                        message = "Incorrect password";
                        result = "Try again (" + (3 - pwAttempts) + " attempts left)\nEnter CURRENT password\nFollowed by \"Ent\"";
                        setState(STATE_PW_OLD);
                    }
                }
                break;
            }

            case STATE_PW_NEW: {
                String newPw = numberPadInput;
                numberPadInput = "";

                String oldPw = bank.getLoggedInAccount().getaccPasswd();
                String err = validateNewPassword(oldPw, newPw);

                if (err == null) {
                    pendingNewPassword = newPw;
                    setState(STATE_PW_CONFIRM);
                    message = "Confirm new password";
                    result = "Re-enter your NEW password\nFollowed by \"Ent\"";
                } else {
                    message = "New password rejected";
                    result = err + "\nEnter a NEW password\nFollowed by \"Ent\"";
                    setState(STATE_PW_NEW);
                }
                break;
            }

            case STATE_PW_CONFIRM: {
                String confirm = numberPadInput;
                numberPadInput = "";

                if (confirm.equals(pendingNewPassword)) {
                    bank.changeLoggedInPassword(pendingNewPassword);
                    pendingNewPassword = "";
                    setState(STATE_LOGGED_IN);
                    message = "Password changed";
                    result = "Now enter the amount\nThen press transaction\n(Dep = Deposit, W/D = Withdraw)";
                } else {
                    message = "Passwords do not match";
                    result = "Re-enter your NEW password\nFollowed by \"Ent\"";
                    setState(STATE_PW_CONFIRM);
                }
                break;
            }

            case STATE_TRANS_MONEY:
                //modified code from processWithdraw
                int amount = parseValidAmount(numberPadInput);
                if (amount > 0) {
                    if (bank.transfer(amount, transAccNumber)) {

                        message = "Transfer Successful";
                        result = "Transferred: £" + amount + " to " + transAccNumber;
                        setState(STATE_LOGGED_IN);

                    }
                    else {
                        //Checks which account is logged in to choose which error message displays.
                        message = "Transfer Failed";
                        BankAccount current = bank.getLoggedInAccount();

                        if (current instanceof StudentAccount && amount > 50) {
                            result = "Error: Student limit is £50 per transaction.";
                        } else if (current instanceof PrimeAccount) {
                            result = "Error: Amount exceeds your overdraft limit.";
                        } else if (Objects.equals(transAccNumber, accNumber)){
                            result = "Error: Cannot transfer to own account.";
                        } else {
                            result = "Error: Insufficient Funds.";
                        }
                        setState(STATE_LOGGED_IN);
                    }
                }


                break;

            case STATE_LOGGED_IN:
            default:
                // Do nothing for other states (user is already logged in)
        }

        update(); // Refresh the GUI to show messages and input
    }

    /**
     * Parses a string into a valid transaction amount.
     * - If the string is empty, invalid, or consists only of zeros, returns 0.
     * - Otherwise, returns the integer value.
     *
     * Purpose:
     * Helper method for validating user-entered amounts in transactions (Deposit, Withdraw, etc.).
     *
     * Note: If you later add features like Transfer, this method can be reused.
     */
    private int parseValidAmount(String number) {
        if (number.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return 0; // Invalid input -> treated as 0
        }
    }

    // Handle the Balance button:
    // - If the user is logged in, retrieve the current balance and update messages/results accordingly
    // - Otherwise, reset the ATM and display an error message
    public void processBalance() {
        if (state.equals(STATE_LOGGED_IN) ) {
            BankAccount current = bank.getLoggedInAccount();
            numberPadInput = "";

            //Checks if the account is a SavingsAccount
            //Added protection to prevent user gaining additional interest by simply logging out and in.
            if (current instanceof SavingsAccount) {
                SavingsAccount sa = (SavingsAccount) current;
                if (!sa.hasInterestBeenApplied()) {
                    sa.addInterest();
                    message = "Interest applied";
                } else {
                    message = "Balance available";
                }
            }
            result = "Your balance is: £" + current.getBalance();
        } else {
            reset ("You are not logged in");
        }
        update();
    }



    // Handle the Withdraw button:
    // If the user is logged in, attempt to withdraw the amount entered;
    // otherwise, reset the ATM and display an error message.
    // Reads the amount from numberPadInput, validates it, and updates messages/results accordingly.
    public void processWithdraw() {
        if (state.equals(STATE_LOGGED_IN) || state.equals(STATE_TRANS_MONEY)) {
            int amount = parseValidAmount(numberPadInput);
            if (amount > 0) {
                if(bank.withdraw( amount )){
                    message = "Withdraw Successful";
                    result = "Withdrawn: " + numberPadInput;
                }
                else{
                    //Checks which account is logged in to choose which error message displays.
                    message = "Withdraw Failed";
                    BankAccount current = bank.getLoggedInAccount();

                    if (current instanceof StudentAccount && amount > 50) {
                        result = "Error: Student limit is £50 per transaction.";
                    } else if (current instanceof PrimeAccount) {
                        result = "Error: Amount exceeds your overdraft limit.";
                    } else {
                        result = "Error: Insufficient Funds.";
                    }
                }
            }
            else{
                message = "Invalid Amount";
                result = "Now enter the amount\nThen press transaction\n(Dep = Deposit, W/D = Withdraw)" +
                        "\nOr enter the account number\nTo transfer to\n(Xfr = Transfer)";
            }
            numberPadInput = "";
        }
        else {
            reset("You are not logged in");
        }
        update();
    }

    // Handle the Deposit button:
    // - If the user is logged in, deposit the amount entered into the bank
    // - Reads the amount from numberPadInput, validates it, and updates messages/results accordingly
    // - Otherwise, reset the ATM and display an error message
    public void processDeposit() {
        if (state.equals(STATE_LOGGED_IN)) {
            int amount = parseValidAmount(numberPadInput);
            if (amount > 0) {
                bank.deposit( amount );
                message = "Deposit Successful";
                result = "Deposited: " + numberPadInput;
            }
            else {
                message = "Invalid Amount";
                result = "Now enter the amount\nThen press transaction\n(Dep = Deposit, W/D = Withdraw)" +
                        "\nOr enter the account number\nTo transfer to\n(Xfr = Transfer)";
            }
            numberPadInput = "";
        }
        else {
            reset("You are not logged in");
        }
        update();
    }

    // ✅ Change Password button handler
    public void processChangePassword() {
        if (!state.equals(STATE_LOGGED_IN)) {
            reset("You are not logged in");
            update();
            return;
        }

        numberPadInput = "";
        pendingNewPassword = "";
        pwAttempts = 0;
        setState(STATE_PW_OLD);
        message = "Change Password";
        result = "Enter your CURRENT password\nFollowed by \"Ent\"";
        update();
    }

    private String validateNewPassword(String oldPw, String newPw) {
        if (newPw == null || newPw.isEmpty()) return "Password cannot be empty.";
        if (!newPw.matches("\\d+")) return "Password must be numbers only.";
        if (newPw.length() != 5) return "Password must be exactly 5 digits.";
        if (newPw.equals(oldPw)) return "New password cannot match old password.";
        if (newPw.matches("0+")) return "Password cannot be all zeros.";
        return null;
    }

    //Handle the Xfer button:
    // - if the user is logged in, check if the value entered is a valid account number
    // - if so, set state to transfer money in, otherwise reject and set state to logged in
    public void processTransfer() {
        if (state.equals(STATE_LOGGED_IN) || state.equals(STATE_TRANS_MONEY)) {
            int count = bank.getAccountCount();
            String[] accountNumbers = bank.getAccountNumbers();
            for (int i = 0; i < count; i++) {
                //check if the number pad input is one of the account numbers
                if (numberPadInput.equals(accountNumbers[i])) {
                    transAccNumber = numberPadInput;
                    message = "Transfer to: " + transAccNumber;
                    result = "Enter how much you wish to send\nThen press [Ent]";
                    setState(STATE_TRANS_MONEY);
                    numberPadInput = "";
                    break;
                }

                // if the account number is incorrect/not in the accounts list
                message = "Invalid Account Number";
            }
        }
        else {
            reset("You are not logged in");
        }
        update();
    }

    // Handle the Finish button:
    // - If the user is logged in, log out
    // - Otherwise, reset the ATM and display an error message
    public void processFinish() {
        bank.logout();
        setState(STATE_GOODBYE);
        numberPadInput = ""; //Resets buffer so user cannot see previous user's balance or input
        message = "Goodbye";
        result = "Thank you for using our ATM.";
        update();
        //Returns user to home automatically
        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(5));
        delay.setOnFinished(event -> initialise());
        delay.play();
    }

    // Handle unknown or invalid buttons for the current state:
    // - Reset the ATM and display an "Invalid Command" message
    public void processUnknownKey(String action) {
        reset("Invalid Command");
        update();
    }

    // Handle the help/? button
    // - Reads the current state to determine position
    // - Displays helpful message for the user in result
    public void processHelp() {
        message = "Help";
        switch (state) {
            case STATE_ACCOUNT_NO:
                result = "Enter account number, then Ent.";
                break;
            case STATE_PASSWORD:
                result = "Enter password, then Ent.";
                break;
            case STATE_LOGGED_IN:
                result = "Enter amount, then Dep / W/D / Bal.\nPress Pwd to change password.\nPress Fin to logout.";
                break;
            case STATE_PW_OLD:
                result = "Enter CURRENT password, then Ent.";
                break;
            case STATE_PW_NEW:
                result = "Enter NEW 5-digit password, then Ent.";
                break;
            case STATE_PW_CONFIRM:
                result = "Re-enter NEW password to confirm, then Ent.";
                break;
            default:
                result = "Help unavailable.";
        }
        update();
    }

    // Notify the View of changes by calling its update method
    private void update() {
        view.update(message,numberPadInput, result);
    }
}
