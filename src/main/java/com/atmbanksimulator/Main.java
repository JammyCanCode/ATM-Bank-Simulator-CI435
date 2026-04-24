
package com.atmbanksimulator;

import javafx.application.Application;
import javafx.stage.Stage;
/**
 * The Main class serves as the entry point for the JavaFX application.
 * It initializes the Bank, UIModel, View, and Controller, and links them together.
 */
public class Main extends Application {
    public static void main( String args[] ) {launch(args);}

    /**
     * Starts the JavaFX application by setting up the MVC components.
     */
    @Override
    public void start(Stage window) {
        // Create a Bank object add bank accounts for test
        Bank bank = new Bank();
        bank.addBankAccount("10001", "11111", 100);
        bank.addBankAccount("10002", "22222", 50);
        bank.addBankAccount(new StudentAccount("10003", "33333", 200));
        bank.addBankAccount(new SavingsAccount("10004", "44444", 500, 0.05));
        bank.addBankAccount(new PrimeAccount("10005", "55555", 4000, 5000));
        //UIModel-View-Controller structure setup
        // Create the UIModel, View and Controller objects and link them together
        UIModel UIModel = new UIModel(bank);   // the UIModel needs the Bank object to 'talk to' the bank
        View  view  = new View();
        Controller controller  = new Controller();

        // Link them together so they can talk to each other
        view.controller = controller;
        controller.UIModel = UIModel;
        UIModel.view = view;

        // start up the GUI (view), and then tell the UIModel to initialise itself
        view.start(window);
        UIModel.initialise();
    }
}
