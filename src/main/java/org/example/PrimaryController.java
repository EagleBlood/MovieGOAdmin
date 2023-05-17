package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PrimaryController {

    @FXML
    private TextField inputUser;
    @FXML
    private PasswordField inputPass;
    @FXML
    private Button buttonConnect;

    @FXML
    private void initialize() {
        buttonConnect.setOnAction(event -> connectJDBC());
    }

    private void connectJDBC() {
        String dbUrl = "jdbc:postgresql://195.150.230.208:5432/2023_nosal_dawid";
        String username = inputUser.getText();
        String password = inputPass.getText();

        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            // Connection successful, perform database operations here
            System.out.println("Success");
            connection.close(); // Close the connection when you're done
        } catch (SQLException e) {
            System.out.println("GUNWOOOOOOOOOOOOOOOOOO!!11!!");
            e.printStackTrace();
        }
    }
}