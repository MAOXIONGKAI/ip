package gopher.gui;

import gopher.Gopher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Gopher gopher;

    private Image userImage =
            new Image(this
                    .getClass()
                    .getResourceAsStream("/images/DaUser.png")
            );
    private Image gopherImage =
            new Image(this
                    .getClass()
                    .getResourceAsStream("/images/Gopher.png")
            );

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Gopher instance */
    public void setGopher(Gopher gopher) {
        this.gopher = gopher;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Gopher's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = gopher.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getGopherDialog(response, gopherImage)
        );
        userInput.clear();
    }
}
