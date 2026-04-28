package com.atmbanksimulator;

import javafx.application.Application;

/**
 * A helper class to launch the JavaFX application.
 * This is often used to avoid issues with modules in some environments.
 */
public class Launcher {
    /**
     * Main method to launch the Main application class.
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}
