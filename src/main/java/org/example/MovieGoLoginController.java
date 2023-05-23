package org.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class MovieGoLoginController {

    @FXML
    private TextField inputUser;
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
//        String dbUrl = "jdbc:mysql://w10.domenomania.eu/wiktor10_kino";
//        String username = inputUser.getText();
//        String password = inputPass.getText();
//
//        try {
//            Connection connection = DriverManager.getConnection(dbUrl, username, password);
//            // Connection successful, perform database operations here
//            System.out.println("Success");
//            connection.close(); // Close the connection when you're done
//        } catch (SQLException e) {
//            System.out.println("GUNWOOOOOOOOOOOOOOOOOO!!11!!");
//            e.printStackTrace();
//        }


        App.setRoot("home");
    }
}