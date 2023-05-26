package org.example;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Potwierdzenie");
            confirmationDialog.setHeaderText("Czy na pewno chcesz usunąć rekord?");
            confirmationDialog.setContentText("Ta operacja jest nieodwracalna.");

            Optional<ButtonType> result = confirmationDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                deleteUserFromDatabase(selectedUser);
                choiceUser.getItems().clear();
                populateUsers();

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);

                Label messageLabel = new Label("Użytkownik został usunięty!");
                showPopup(popupStage, messageLabel);
            }
        });
    }

    private void showPopup(Stage popupStage, Label messageLabel) {
        messageLabel.setAlignment(Pos.CENTER);

        Button closeButton = new Button("Zamknij");
        closeButton.setOnAction(e -> popupStage.close());

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(messageLabel, closeButton);

        Scene scene = new Scene(vbox, 200, 100);
        popupStage.setScene(scene);

        popupStage.showAndWait();
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
