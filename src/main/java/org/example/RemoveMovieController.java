package org.example;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.App;
import org.example.LoginCredentials;
import org.example.MovieAdapter;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RemoveMovieController {

    private String dbUrl = LoginCredentials.getDbUrl();
    private String username = LoginCredentials.getUsername();
    private String password = LoginCredentials.getPassword();

    @FXML
    private ChoiceBox<MovieAdapter> choiceMovieTitle;
    @FXML
    private Text textAreaMovieDesc;
    @FXML
    private Button buttonRemoveData;

    @FXML
    private void initialize() {
        populateMovieTitles();

        // Add event handler for choice box selection change
        choiceMovieTitle.setOnAction(event -> {
            MovieAdapter selectedMovie = choiceMovieTitle.getValue();
            int selectedId = selectedMovie.getId_filmu();
            String selectedTitle = selectedMovie.getTytul();
            String movieDescription = retrieveMovieDescriptionFromDatabase(selectedId, selectedTitle);
            if (movieDescription != null) {
                textAreaMovieDesc.setText(movieDescription);
            } else {
                textAreaMovieDesc.setText("Movie description not available.");
            }
        });

        // Add event handler for remove button
        buttonRemoveData.setOnAction(event -> {
            MovieAdapter selectedMovie = choiceMovieTitle.getValue();
            int selectedId = selectedMovie.getId_filmu();

            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Potwierdzenie");
            confirmationDialog.setHeaderText("Czy na pewno chcesz usunąć rekord?");
            confirmationDialog.setContentText("Ta operacja jest nieodwracalna.");

            Optional<ButtonType> result = confirmationDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                deleteMovieFromDatabase(selectedId);
                choiceMovieTitle.getItems().clear();
                populateMovieTitles();

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);

                Label messageLabel = new Label("Film został usunięty!");
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

    private void populateMovieTitles() {
        List<MovieAdapter> movieTitles = retrieveMovieTitlesFromDatabase();
        choiceMovieTitle.getItems().addAll(movieTitles);
    }

    private String retrieveMovieDescriptionFromDatabase(int movieId, String movieTitle) {
        String movieDescription = "";

        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            String query = "SELECT opis FROM film WHERE id_filmu = ? AND tytul = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, movieId);
            preparedStatement.setString(2, movieTitle);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                movieDescription = resultSet.getString("opis");
            }

            // Close the result set, prepared statement, and connection
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the database error gracefully
        }

        return movieDescription;
    }

    public List<MovieAdapter> retrieveMovieTitlesFromDatabase() {
        List<MovieAdapter> movieTitles = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id_filmu, tytul FROM film");

            while (resultSet.next()) {
                int id = resultSet.getInt("id_filmu");
                String title = resultSet.getString("tytul");
                movieTitles.add(new MovieAdapter(id, title));
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movieTitles;
    }

    private boolean deleteMovieFromDatabase(int movieId) {
        boolean success = false;

        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);

            String query = "DELETE FROM film WHERE id_filmu = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, movieId);

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

    // Other methods for database operations and error handling

    @FXML
    private void backHome() throws IOException {
        App.setRoot("home");
    }
}
