package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class AddMovieController {

    @FXML
    private Button backButton;

    @FXML
    private void initialize() {
        backButton.setOnAction(event -> {
            try {
                logout();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    private void logout() throws IOException {
        App.setRoot("home");
    }

}
