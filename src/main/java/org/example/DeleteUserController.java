package org.example;

import javafx.fxml.FXML;

import java.io.IOException;

public class DeleteUserController {

    @FXML
    private void backHome() throws IOException {
        App.setRoot("home");
    }
}
