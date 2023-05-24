package org.example;

import javafx.fxml.FXML;

import java.io.IOException;

public class TransactionsController {
    @FXML
    private void backHome() throws IOException {
        App.setRoot("home");
    }
}
