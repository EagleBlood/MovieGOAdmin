package org.example;

import javafx.fxml.FXML;

import java.io.IOException;

public class MovieShowtimeController {
    @FXML
    private void backHome() throws IOException {
        App.setRoot("home");
    }
}
