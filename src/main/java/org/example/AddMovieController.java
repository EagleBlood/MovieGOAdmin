package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;

public class AddMovieController {

    private String dbUrl = LoginCredentials.getDbUrl();
    private String username = LoginCredentials.getUsername();
    private String password = LoginCredentials.getPassword();


    @FXML
    private Button movieCoverButton;
    @FXML
    private Button buttonSendToDB;
    private FileChooser fileChooser;
    @FXML
    private ChoiceBox<String> movieGenreInput;
    @FXML
    private TextField movieTitleInput;
    @FXML
    private TextField movieLengthInput;
    @FXML
    private TextField movieScoreInput;
    @FXML
    private TextField moviePriceInput;
    @FXML
    private TextField movieDescInput;



    private ObservableList<String> genreList = FXCollections.observableArrayList();
    private byte[] imageData = null;


    @FXML
    private void initialize() {

        //Load img init
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        //Load img
        movieCoverButton.setOnAction(event -> {
            loadMovieCoverImg();
        });

        //List
        retrieveGenreNamesFromDatabase();
        movieGenreInput.setItems(genreList);

        //DB action
        buttonSendToDB.setOnAction(event -> {
            SendMovieToDB();
        });

    }

    @FXML
    private void backHome() throws IOException {
        App.setRoot("home");
    }

    @FXML
    private void loadMovieCoverImg() {
        File file = fileChooser.showOpenDialog(movieCoverButton.getScene().getWindow());
        if (file != null) {
            try (InputStream inputStream = new FileInputStream(file)) {
                BufferedImage bufferedImage = ImageIO.read(file);
                ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "png", byteOutputStream);
                imageData = byteOutputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the error gracefully
            }

            movieCoverButton.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void SendMovieToDB() {
        String selectedGenre = movieGenreInput.getValue();
        int genreId = retrieveGenreIdFromDatabase(selectedGenre);

        String title = movieTitleInput.getText();
        String lengthText = movieLengthInput.getText();
        int length = Integer.parseInt(lengthText);
        String scoreText = movieScoreInput.getText();
        double score = Double.parseDouble(scoreText);
        String description = movieDescInput.getText();
        String priceText = moviePriceInput.getText();
        double price = Double.parseDouble(priceText);

        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO film (tytul, czas_trwania, ocena, opis, id_gatunku , okladka, cena) VALUES (?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, title);
            statement.setInt(2, length);
            statement.setDouble(3, score);
            statement.setString(4, description);
            statement.setInt(5, genreId);
            statement.setBytes(6, imageData);
            statement.setDouble(7, price);

            int rowsAffected = statement.executeUpdate();

            System.out.println(rowsAffected + " row(s) inserted.");

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void retrieveGenreNamesFromDatabase() {
        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT nazwa_gatunku FROM gatunek");

            while (resultSet.next()) {
                String genreName = resultSet.getString("nazwa_gatunku");
                genreList.add(genreName);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int retrieveGenreIdFromDatabase(String genreName) {
        int genreId = -1; // Default value if genre ID is not found

        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT id_gatunku FROM gatunek WHERE nazwa_gatunku = ?");
            statement.setString(1, genreName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                genreId = resultSet.getInt("id_gatunku");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return genreId;
    }


}
