package com.atmbanksimulator;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.awt.Toolkit;
/**
 * The View class creates and manages the graphical user interface for the ATM.
 * It is responsible for displaying the current state of the UIModel and capturing user input.
 * It follows the MVC pattern and does not contain business logic.
 */
class View {
    int H = 500;         // Height of window pixels
    int W = 500;         // Width  of window pixels

    Controller controller; // Reference to the Controller (part of the MVC setup)

    // Components (controls and layout) of the user interface
    private Label laMsg;        // Message label, e.g. shows "Welcome to ATM" at startup (not the window title)
    private TextField tfInput;  // Input field where numbers typed on the keypad appear
    private TextArea taResult;  // Output area where instructions and results are displayed
    private ScrollPane scrollPane; // Provides scrollbars around the TextArea
    private GridPane grid;      // Main layout container (grid-based)
    private TilePane buttonPane;// Container for ATM keypad buttons (tiled layout)
    private VBox welcomeLayout; //Container for initial welcome screen buttons
    private VBox goodbyeLayout; //Container for goodbye screen
    private StackPane root; // The container that stacks the welcome screen and ATM keypad

    /**
     * Plays a system beep sound to provide interactive feedback.
     */
    private void playClickSound() {
        Toolkit.getDefaultToolkit().beep();
    }

    /**
     * Sets up and displays the JavaFX stage with all components.
     */
    public void start(Stage window) {
        // Create the user interface component objects.
        // The ATM UI is organized as a vertical grid with four main parts:
        // 1. A message label
        // 2. A text field showing numbers
        // 3. A text area showing transaction results, summaries, and user instructions
        // 4. A tiled panel of buttons
        grid = new GridPane(); // top layout
        grid.setId("Layout");  // assign an id to be used in css file

        //Setup welcome layout: so the users sees this first.
        //Uses VBox to stack the title and buttons vertically in the center.
        welcomeLayout = new VBox(20);
        welcomeLayout.setAlignment(Pos.CENTER);

        buttonPane = new TilePane(); //
        buttonPane.setId("Buttons"); // assign an id to be used in css file

        Label welcomeLbl = new Label("Welcome to Bank Simulator");
        Button loginButton = new Button("Login to Account");
        Button createAccButton = new Button("Create Account");

        loginButton.setOnAction((e) -> {
            playClickSound();
            controller.process("LoginButton");
        });
        createAccButton.setOnAction((e) -> {
            playClickSound();
            controller.process("CreateAccButton");
        });

        welcomeLayout.getChildren().addAll(welcomeLbl, loginButton, createAccButton);

        goodbyeLayout = new VBox(20);
        goodbyeLayout.setAlignment(Pos.CENTER);
        Label goodbyeLbl = new Label("Logged Out Successfully");
        Button returnBtn = new Button("Return to home");

        returnBtn.setOnAction((e) -> {
            playClickSound();
            controller.process("ReturnToWelcome");
        });

        goodbyeLayout.getChildren().addAll(goodbyeLbl, returnBtn);


        // controls
        laMsg = new Label("Welcome to Bank-ATM");  // Message bar at the top
        grid.add(laMsg, 0, 0);         // Add to GUI at the top

        tfInput = new TextField();     // text field for numbers
        tfInput.setEditable(false);     // Read only (user can't type in)
        grid.add(tfInput, 0, 1);    // Add to GUI on second row

        taResult = new TextArea();         // text area for instructions, transaction results
        taResult.setEditable(false);       // Read only
        scrollPane  = new ScrollPane();    // create a scrolling window
        scrollPane.setContent(taResult);   // put the text area 'inside' the scrolling window
        grid.add( scrollPane, 0, 2);    // add the scrolling window to GUI on third row

        // Define the button layout as a 2D array of text labels.
        // Empty strings ("") represent blank spaces in the grid.
        String buttonTexts[][] = {
                {"7",    "8",  "9",  "",  "Dep",  "Pwd"},
                {"4",    "5",  "6",  "",  "W/D",  " ? "},
                {"1",    "2",  "3",  "",  "Bal",  "Fin"},
                {"CLR",  "0",  "",   "",  "Xfr",  "Ent"} };

        // Build the button panel, loop through the array,
        // - For non-empty strings, create a Button
        // - For empty strings, add an empty Text element as a spacer
        // Add all elements to the buttonPane (a tiled pane),
        // then place the buttonPane into the main grid as the fourth row.
        for ( String[] row: buttonTexts ) {
            for (String text: row) {
                if ( text.length() >= 1 ) {
                    // non-empty string - make a button
                    Button btn = new Button( text );

                    // set the tooltip for select buttons
                    if ((getTooltip(text) != null)) {
                        btn.setTooltip(new Tooltip(getTooltip(text)));
                    }

                    btn.setOnAction( this::buttonClicked );
                    // Register event handler: call buttonClicked() whenever this button is pressed
                    buttonPane.getChildren().add( btn );    // add this button to tiled pane
                } else {
                    // empty string - make an empty Text element as a spacer
                    buttonPane.getChildren().add( new Text() );
                }
            }
        }
        grid.add(buttonPane,0,3); // add the tiled pane of buttons to the main grid
        //Allows stacking of the grid so we can toggle their visibility without opening new windows (already preloaded)
        root = new StackPane(grid, welcomeLayout, goodbyeLayout);

        Scene scene = new Scene(root, W, H);
        scene.getStylesheets().add("atm.css"); // tell to use our css file

        // add the complete GUI to the window and display it
        window.setScene(scene);
        window.setTitle("ATM-Bank Simulator"); //set window title
        window.show();
    }

    private String getTooltip(String text) {
        switch (text) {
            // left side of buttons
            case "Clr" : return "Clear input box";

            // right side of buttons
            // first row of right side
            case "Dep" : return "Deposit entered amount";
            case "W/D" : return "Withdraw entered amount";
            case "Bal" : return "Display current balance";
            case "Xfr" : return "Enter transfer state";

            //second row of right side
            case "Pwd" : return "Change password";
            case " ? " : return "Help";
            case "Fin" : return "Finish and sign out";
            case "Ent" : return "Enter";

            default: return null;
        }
    }


    // This is how the View talks to the Controller
    // This method is called when a button is pressed
    // It fetches the label on the button and passes it to the controller's process method
    private void buttonClicked(ActionEvent event) {
        // Play click sound
        playClickSound();

        // this line asks the event to provide the actual Button object that was clicked
        Button b = ((Button) event.getSource());
        String text = b.getText();   // get the button label
        System.out.println( "View::buttonClicked: label = "+ text );
        controller.process( text );  // Pass it to the controller's process method
    }

    /**
     * Updates the GUI with new information from the UIModel.
     * Toggles visibility of different layouts based on the message content.
     */
    public void update(String msg,String tfInputMsg,String taResultMsg) {

        welcomeLayout.setVisible(false);
        goodbyeLayout.setVisible(false);
        grid.setVisible(false);

        //State-based UI toggling
        if (msg.toLowerCase().contains("welcome")) {
            welcomeLayout.setVisible(true);
        } else if (msg.toLowerCase().contains("goodbye")) {
            goodbyeLayout.setVisible(true);
        } else {
            grid.setVisible(true);
        }
        laMsg.setText(msg);
        tfInput.setText(tfInputMsg);
        taResult.setText(taResultMsg);
    }
}