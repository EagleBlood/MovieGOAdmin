package org.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.adapters.LoginCredentials;

public class LoginController {

    @FXML
    private TextField inputUser;
    @FXML
    private Label errorMsg;
    @FXML
    private PasswordField inputPass;
    @FXML
    private Button buttonConnect;

    @FXML
    private void initialize() {
        buttonConnect.setOnAction(event -> {
            try {
                connectJDBC();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void connectJDBC() throws IOException {
        String dbUrl = LoginCredentials.getDbUrl();
        String username = inputUser.getText();
        String password = inputPass.getText();
        LoginCredentials.setCredentials(username, password);

        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            // Connection successful, perform database operations here
            System.out.println("Success");
            App.setRoot("home");
            connection.close(); // Close the connection when you're done
        } catch (SQLException e) {
            errorMsg.setText("Błąd podczas logowania!");
            e.printStackTrace();
        }

    }
}


