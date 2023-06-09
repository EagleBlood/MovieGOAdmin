package org.example;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.adapters.LoginCredentials;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.GlobFun.showAlert;
import static org.example.GlobFun.showPopup;

public class DeleteUserController {

    private String dbUrl = LoginCredentials.getDbUrl();
    private String username = LoginCredentials.getUsername();
    private String password = LoginCredentials.getPassword();

    @FXML
    private ChoiceBox<String> choiceUser;
    @FXML
    private Button buttonRemoveUser;
    @FXML
    private void initialize() {
        populateUsers();

        // Add event handler for remove button
        buttonRemoveUser.setOnAction(event -> {
            String selectedUser = choiceUser.getValue();

            showAlert(() -> {
                    deleteUserFromDatabase(selectedUser);
                    choiceUser.getItems().clear();
                    populateUsers();

                    showPopup("Usunięto użytkownika");
            });
        });
    }

    private void populateUsers() {
        List<String> userArray = retrieveUserFromDatabase();
        choiceUser.getItems().addAll(userArray);
    }

    private List<String> retrieveUserFromDatabase() {

        List<String> userArray = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT login FROM uzytkownicy");

            while (resultSet.next()) {
                String user = resultSet.getString("login");
                userArray.add(user);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userArray;
    }

    private boolean deleteUserFromDatabase(String user) {
        boolean success = false;

        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);

            String query = "DELETE FROM uzytkownicy WHERE login = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user);

            int rowsDeleted = preparedStatement.executeUpdate();

            if (rowsDeleted > 0) {
                success = true;
                System.out.println("Rekord został usunięty!");
            }

            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return success;
    }


    @FXML
    private void backHome() throws IOException {
        App.setRoot("home");
    }
}
