package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class RemoveMovieController {

    @FXML
    private void backHome() throws IOException {
        App.setRoot("home");
    }
}
