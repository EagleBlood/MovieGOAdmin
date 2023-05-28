package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class HomeController {

    @FXML
    private Button buttonLogout;


    @FXML
    private void initialize() {
        buttonLogout.setOnAction(event -> {
            try {
                logout();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    private void logout() throws IOException {
        App.setRoot("login");
    }

    @FXML
    private void movies() throws IOException {
        App.setRoot("movies");
    }

    @FXML
    private void deleteUser() throws IOException {
        App.setRoot("deleteUser");
    }

    @FXML
    private void movieShowtime() throws IOException {
        App.setRoot("movieShowtime");
    }

    @FXML
    private void showTransactions() throws IOException {
        App.setRoot("transactions");
    }

}


