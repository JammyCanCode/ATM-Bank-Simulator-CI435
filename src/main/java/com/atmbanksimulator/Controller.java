package com.atmbanksimulator;

// ===== ⚡ Controller (Nerves) =====

// The Controller receives user actions from the View and delegates the appropriate tasks to the UIModel.
// Its main job is to decide what to do based on the user input.
/**
 * The Controller class receives user actions from the View and delegates the appropriate tasks to the UIModel.
 * It acts as the intermediary in the MVC pattern, deciding how to respond to user input.
 */
public class Controller {

    UIModel UIModel; // Reference to the UIModel (part of the MVC setup)

    /**
     * Processes an action string received from the View.
     * Uses a switch statement to delegate the action to the corresponding UIModel method.
     */
    void process( String action ) {
        switch (action) {
            case "LoginButton": //Button for user Login
                UIModel.processLoginChoice();
                break;
            case "CreateAccButton": //Button for user Account creation
                UIModel.processCreateAccChoice();
                break;
            case "ReturnToWelcome":
                UIModel.initialise();
                break;
            case "1" : case "2" : case "3" : case "4" : case "5" :
            case "6" : case "7" : case "8" : case "9" : case "0" :
                UIModel.processNumber(action);
                break;
            case "CLR":
                UIModel.processClear();
                break;
            case "Ent":
                UIModel.processEnter();
                break;
            case "W/D":
                UIModel.processWithdraw();
                break;
            case "Dep":
                UIModel.processDeposit();
                break;
            case "Bal":
                UIModel.processBalance();
                break;
            case "Fin":
                UIModel.processFinish();
                break;
            case " ? ":
                UIModel.processHelp();
                break;
            // New Transfer button
            case "Xfr":
                UIModel.processTransfer();
                break;
            // ✅ New Change Password button
            case "Pwd":
                UIModel.processChangePassword();
                break;
            default:
                UIModel.processUnknownKey(action);
                break;
        }
    }

}


