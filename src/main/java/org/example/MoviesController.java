package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;



import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MoviesController {

    private String dbUrl = LoginCredentials.getDbUrl();
    private String username = LoginCredentials.getUsername();
    private String password = LoginCredentials.getPassword();


    //For show movies Pane

    @FXML
    private TableView<MovieDetailsAdapter> tableDB;
    @FXML
    private TableColumn<MovieDetailsAdapter, String> tytulCol;
    @FXML
    private TableColumn<MovieDetailsAdapter, Integer> czasTrwaniaCol;
    @FXML
    private TableColumn<MovieDetailsAdapter, Double> ocenaCol;
    @FXML
    private TableColumn<MovieDetailsAdapter, String> opisCol;
    @FXML
    private TableColumn<MovieDetailsAdapter, Integer> gatCol;
    @FXML
    private TableColumn<MovieDetailsAdapter, Double> cenaCol;
    @FXML
    private Button buttonLoadData;


    //For add movie Pane
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
    private TextArea movieDescInput;
    @FXML
    private ImageView coverAddMovie;

    //For edit movie Pane

    @FXML
    private ChoiceBox<String> editMovieList;
    @FXML
    private TextField editMovieLengthInput;
    @FXML
    private TextField editMovieScoreInput;
    @FXML
    private TextArea editMovieDescInput;
    @FXML
    private TextField editMoviePriceInput;
    @FXML
    private ChoiceBox<String> editMovieGenreInput;
    @FXML
    private ImageView coverEditMovie;
    @FXML
    private Button editMovieCoverButton;
    @FXML
    private Button editMovieButton;

    //For remove movie Pane

    @FXML
    private ChoiceBox<MovieAdapter> choiceRemoveMovieTitle;
    @FXML
    private Text textAreaMovieDesc;
    @FXML
    private Button removeMovieButton;


    private ObservableList<String> genreList = FXCollections.observableArrayList();
    private byte[] imageData = null;


    @FXML
    private void initialize() {

        //For show movie

        tableDB.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tytulCol.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        opisCol.setCellValueFactory(new PropertyValueFactory<>("opis"));
        czasTrwaniaCol.setCellValueFactory(new PropertyValueFactory<>("czas_trwania"));
        gatCol.setCellValueFactory(new PropertyValueFactory<>("nazwa_gatunku"));
        ocenaCol.setCellValueFactory(new PropertyValueFactory<>("ocena"));
        cenaCol.setCellValueFactory(new PropertyValueFactory<>("cena"));

        buttonLoadData.setOnAction(event -> {
            List<MovieDetailsAdapter> movieDetailsAdapterList = retrieveMoviesFromDB();
            tableDB.setItems(FXCollections.observableArrayList(movieDetailsAdapterList));
        });

        //For add movie Pane

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

            showPopup("Film został dodany.");

            movieTitleInput.setText(null);
            movieLengthInput.setText(null);
            movieScoreInput.setText(null);
            movieDescInput.setText(null);
            moviePriceInput.setText(null);
            movieGenreInput.setValue(null);
        });


        //For edit


        editMovieButton.setOnAction(event -> {



            showPopup("Film został zaktualizowany");

            editMovieList.setValue(null);
            editMovieLengthInput.setText(null);
            editMovieScoreInput.setText(null);
            editMovieDescInput.setText(null);
            editMoviePriceInput.setText(null);
            editMovieGenreInput.setValue(null);

        });

        //For remove movie Pane


            // Add event handler for choice box selection change
        choiceRemoveMovieTitle.setOnAction(event -> {
            MovieAdapter selectedMovie = choiceRemoveMovieTitle.getValue();
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
        removeMovieButton.setOnAction(event -> {
            MovieAdapter selectedMovie = choiceRemoveMovieTitle.getValue();
            int selectedId = selectedMovie.getId_filmu();

            showAlert(() ->{
                deleteMovieFromDatabase(selectedId);
                choiceRemoveMovieTitle.getItems().clear();
                populateMovieTitles();

                showPopup("Film został usunięty!");
                choiceRemoveMovieTitle.setValue(null);
            });

        });

    }



    //For add movie Pane

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

            String filePath = file.getAbsolutePath();
            Image image = new Image(new File(filePath).toURI().toString());
            coverAddMovie.setImage(image);

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


    //Show movie functions

    private List<MovieDetailsAdapter> retrieveMoviesFromDB() {
        List<MovieDetailsAdapter> movieDetailsAdapterList = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT film.tytul, film.opis, film.czas_trwania, film.ocena, gatunek.nazwa_gatunku AS nazwa_gatunku, film.okladka, film.cena FROM film INNER JOIN gatunek ON film.id_gatunku=gatunek.id_gatunku;");
            System.out.println("Połączono");

            while (resultSet.next()) {
                String tytul = resultSet.getString("tytul");
                String opis = resultSet.getString("opis");
                int czas_trwania = resultSet.getInt("czas_trwania");
                double ocena = resultSet.getDouble("ocena");
                String nazwa_gatunku = resultSet.getString("nazwa_gatunku");
                byte[] okladka = resultSet.getBytes("okladka");
                double cena = resultSet.getDouble("cena");

                MovieDetailsAdapter movieDetailsAdapter = new MovieDetailsAdapter(tytul, opis, czas_trwania, ocena, nazwa_gatunku, okladka, cena);
                movieDetailsAdapterList.add(movieDetailsAdapter);

                System.out.println("Pobrano rekord");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Koniec");
        return movieDetailsAdapterList;
    }

    //Edit movie functions

    @FXML
    private void populateEditMovieList() {
        List<MovieAdapter> movieShowIds = retrieveMovieTitlesFromDatabase();
        editMovieList.getItems().clear(); // Clear the choice box before adding new items

        for (MovieAdapter movieShowId : movieShowIds) {
            editMovieList.getItems().add(String.valueOf(movieShowId));
        }

        editMovieGenreInput.setItems(genreList);

        editMovieList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String selectedMovieShowID = String.valueOf(newValue);

            List<MovieDetailsAdapter> movieList = retrieveMoviesFromDB();
            MovieDetailsAdapter selectedMovie = null;

            for (MovieDetailsAdapter movie : movieList) {
                if (String.valueOf(movie.getTytul()).equals(selectedMovieShowID)) {
                    selectedMovie = movie;
                    break;
                }
            }

            if (selectedMovie != null) {
                for (String movie : editMovieGenreInput.getItems()) {
                    if (movie.equals(selectedMovie.getNazwa_gatunku())) {
                        editMovieGenreInput.setValue(movie);
                        break;
                    }
                }

                editMovieLengthInput.setText(String.valueOf(selectedMovie.getCzas_trwania()));
                editMovieScoreInput.setText(String.valueOf(selectedMovie.getOcena()));
                editMovieDescInput.setText(selectedMovie.getOpis());
                editMoviePriceInput.setText(String.valueOf(selectedMovie.getCena()));
            }
        });


    }


    //Remove movie functions

    @FXML
    private void populateMovieTitles() {
        List<MovieAdapter> movieTitles = retrieveMovieTitlesFromDatabase();
        choiceRemoveMovieTitle.getItems().addAll(movieTitles);
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

    // GLOBAL

    private void showPopup(String message) {

        Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toString());
        dialogPane.getStyleClass().add("my-dialog-pane");

        alert.show();

    }

    private void showAlert(Runnable onConfirmation) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Potwierdzenie");
        alert.setHeaderText("Czy na pewno chcesz usunąć film?");
        alert.setContentText("Ta operacja jest nieodwracalna.");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toString());
        dialogPane.getStyleClass().add("my-dialog-pane");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            onConfirmation.run();
        }
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Błąd");
        alert.setHeaderText("Nie wybrano filmu do edycji.");
        alert.setContentText("Wybierz film z listy.");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toString());
        dialogPane.getStyleClass().add("my-dialog-pane");
    }

    @FXML
    private void backHome() throws IOException {
        App.setRoot("home");
    }
}