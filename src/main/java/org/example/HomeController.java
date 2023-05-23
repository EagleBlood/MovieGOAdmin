package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class HomeController {

    @FXML
    private Button buttonLogout;

    @FXML
    private Pane buttonAddMovie;

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
    private void addMovie() throws IOException {
        App.setRoot("addMovie");
    }

    @FXML
    private void editMovie() throws IOException {
        App.setRoot("editMovie");
    }

    @FXML
    private void removeMovie() throws IOException {
        App.setRoot("removeMovie");
    }

    @FXML
    private void deleteUser() throws IOException {
        App.setRoot("deleteUser");
    }

    @FXML
    private void showTickets() throws IOException {
        App.setRoot("tickets");
    }

    @FXML
    private void showTransactions() throws IOException {
        App.setRoot("transactions");
    }

}


