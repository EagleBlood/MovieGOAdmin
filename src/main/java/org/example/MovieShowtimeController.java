package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MovieShowtimeController {

    private final String dbUrl = LoginCredentials.getDbUrl();
    private final String username = LoginCredentials.getUsername();
    private final String password = LoginCredentials.getPassword();
    private final List<String> timeList = new ArrayList<>();

    //For Show pane
    @FXML
    private TableView<MovieShowtimeAdapter> tableDB;
    @FXML
    private TableColumn<MovieShowtimeAdapter, Integer> id_seansuCol;
    @FXML
    private TableColumn<MovieShowtimeAdapter, Integer> id_filmuCol;
    @FXML
    private TableColumn<MovieShowtimeAdapter, Integer> id_salaCol;
    @FXML
    private TableColumn<MovieShowtimeAdapter, String> dataCol;
    @FXML
    private TableColumn<MovieShowtimeAdapter, String> pora_emisjiCol;
    @FXML
    private Button buttonLoadDataMovieShowtime;


    //For Add pane
    @FXML
    private ChoiceBox<MovieAdapter> choiceAddMovieTitle;
    @FXML
    private DatePicker addDateInput;
    @FXML
    private ChoiceBox<String> choiceAddTimeList;
    @FXML
    private Button addMovieShowButton;


    //For Edit pane
    @FXML
    private ChoiceBox<MovieAdapter> choiceEditMovieTitle;
    @FXML
    private DatePicker editDateInput;
    @FXML
    private ChoiceBox<Integer> choiceEditMovieShowList;
    @FXML
    private ChoiceBox<String> choiceEditTimeList;
    @FXML
    private Button editMovieShowButton;


    //For Delete pane
    @FXML
    private ChoiceBox<String> choiceRemoveMovieShowList;
    @FXML
    private Button removeMovieShowButton;


    @FXML
    private void initialize() {
        timeList.add("13:00");
        timeList.add("16:00");
        timeList.add("19:00");


        //Show tab pane functions
        tableDB.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        id_seansuCol.setCellValueFactory(new PropertyValueFactory<>("id_seansu"));
        id_filmuCol.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        id_salaCol.setCellValueFactory(new PropertyValueFactory<>("id_sala"));
        dataCol.setCellValueFactory(new PropertyValueFactory<>("data"));
        pora_emisjiCol.setCellValueFactory(new PropertyValueFactory<>("pora_emisji"));

        buttonLoadDataMovieShowtime.setOnAction(event -> {
            List<MovieShowtimeAdapter> movieShowtimeList = retrieveMovieShowtimeFromDB();
            tableDB.getItems().setAll(movieShowtimeList);
        });

        //Add event handler for add button
        addMovieShowButton.setOnAction(event -> {
            SendShowtimeToDB();
            showPopup("Seans został dodany.");
            choiceAddMovieTitle.setValue(null);
            choiceAddTimeList.setValue(null);
            addDateInput.setValue(null);
        });


        //Add event handler for edit button
        editMovieShowButton.setOnAction(event -> {
            String selectedMovieShowID = String.valueOf(choiceEditMovieShowList.getValue());
            if (selectedMovieShowID != null) {
                int movieShowId = Integer.parseInt(selectedMovieShowID);
                MovieAdapter selectedMovie = choiceEditMovieTitle.getValue();
                int movieId = selectedMovie.getId_filmu();
                String date = editDateInput.getValue().toString();
                String time = choiceEditTimeList.getValue();

                updateMovieShowInDatabase(movieShowId, movieId, date, time);

                showPopup("Zmiany zostały zapisane.");

                choiceEditMovieShowList.setValue(null);
                choiceEditMovieTitle.setValue(null);
                choiceEditTimeList.setValue(null);
                editDateInput.setValue(null);
            } else {

                showAlert();
            }
        });

        //Add event handler for remove button
        removeMovieShowButton.setOnAction(event -> {
            String selectedMovieShowID = choiceRemoveMovieShowList.getValue();
            if (selectedMovieShowID != null) {

                showAlert(() -> {
                    deleteMovieShowFromDatabase(Integer.parseInt(selectedMovieShowID));
                    choiceRemoveMovieShowList.getItems().clear();
                    populateMovieShowList();

                    showPopup("Seans został usunięty!");
                    choiceRemoveMovieShowList.setValue(null);
                });
            }

        });
    }











    //Show tab pane functions
    private List<MovieShowtimeAdapter> retrieveMovieShowtimeFromDB() {
        List<MovieShowtimeAdapter> movieShowtimeList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbUrl, username, password)) {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT seanse.id_seansu, film.tytul AS tytul, seanse.id_sala, seanse.data, seanse.pora_emisji FROM seanse INNER JOIN film ON seanse.id_filmu=film.id_filmu");
            System.out.println("Połączono");

            while (result.next()) {
                int id_seansu = result.getInt("id_seansu");
                String tytul = result.getString("tytul");
                int id_sala = result.getInt("id_sala");
                String data = result.getString("data");
                String pora_emisji = result.getString("pora_emisji");

                MovieShowtimeAdapter movieShowtime = new MovieShowtimeAdapter(id_seansu, tytul, id_sala, data, pora_emisji);
                movieShowtimeList.add(movieShowtime);

                System.out.println("Pobrano rekord");
            }

            result.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Koniec");
        return movieShowtimeList;
    }




    //Add Movie pane functions
    private void SendShowtimeToDB() {
        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO seanse (id_filmu, data, pora_emisji) VALUES (?, ?, ?)");
            statement.setInt(1, choiceAddMovieTitle.getValue().getId_filmu());
            statement.setString(2, addDateInput.getValue().toString());
            statement.setString(3, choiceAddTimeList.getValue());

            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void populateChoiceBoxes() {

        //Movie title choice box
        RemoveMovieController removeMovieController = new RemoveMovieController();
        List<MovieAdapter> movieTitles = removeMovieController.retrieveMovieTitlesFromDatabase();
        choiceAddMovieTitle.getItems().addAll(movieTitles);

        //Time choice box
        choiceAddTimeList.getItems().addAll(timeList);
    }


    //Edit Movie pane functions
    @FXML
    private void populateEditMovieShowList() {
        List<Integer> movieShowIds = retrieveMovieShowIdsFromDatabase();
        choiceEditMovieShowList.getItems().clear(); // Clear the choice box before adding new items

        for (Integer movieShowId : movieShowIds) {
            choiceEditMovieShowList.getItems().add(movieShowId);
        }

        RemoveMovieController removeMovieController = new RemoveMovieController();
        List<MovieAdapter> movieTitles = removeMovieController.retrieveMovieTitlesFromDatabase();
        choiceEditMovieTitle.getItems().addAll(movieTitles);

        //Time choice box
        choiceEditTimeList.getItems().addAll(timeList);

        choiceEditMovieShowList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            String selectedMovieShowID = String.valueOf(newValue);

            List<MovieShowtimeAdapter> movieShowtimeList = retrieveMovieShowtimeFromDB();
            MovieShowtimeAdapter selectedMovieShowtime = null;

            for (MovieShowtimeAdapter movieShowtime : movieShowtimeList) {
                if (String.valueOf(movieShowtime.getId_seansu()).equals(selectedMovieShowID)) {
                    selectedMovieShowtime = movieShowtime;
                    break;
                }
            }

            if (selectedMovieShowtime != null) {
                for (MovieAdapter movie : choiceEditMovieTitle.getItems()) {
                    if (movie.getTytul().equals(selectedMovieShowtime.getTytul())) {
                        choiceEditMovieTitle.setValue(movie);
                        break;
                    }
                }
                choiceEditTimeList.setValue(selectedMovieShowtime.getPora_emisji());

                String dateString = selectedMovieShowtime.getData();
                LocalDate date = LocalDate.parse(dateString);
                editDateInput.setValue(date);
            }
        });


    }

    private void updateMovieShowInDatabase(int movieShowId, int movieId, String date, String time) {
        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            PreparedStatement statement = connection.prepareStatement("UPDATE seanse SET id_filmu = ?, data = ?, pora_emisji = ? WHERE id_seansu = ?");
            statement.setInt(1, movieId);
            statement.setString(2, date);
            statement.setString(3, time);
            statement.setInt(4, movieShowId);

            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }




    //Delete Movie pane functions
    private List<Integer> retrieveMovieShowIdsFromDatabase() {
        List<Integer> movieShowIds = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT id_seansu FROM seanse");

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int movieShowId = resultSet.getInt("id_seansu");
                movieShowIds.add(movieShowId);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return movieShowIds;
    }


    @FXML
    private void populateMovieShowList() {
        List<Integer> movieShowIds = retrieveMovieShowIdsFromDatabase();
        choiceRemoveMovieShowList.getItems().clear(); // Clear the choice box before adding new items

        for (Integer movieShowId : movieShowIds) {
            choiceRemoveMovieShowList.getItems().add(String.valueOf(movieShowId));
        }
    }

    private void deleteMovieShowFromDatabase(int movieShowId) {
        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            PreparedStatement statement = connection.prepareStatement("DELETE FROM seanse WHERE id_seansu = ?");
            statement.setInt(1, movieShowId);

            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    //Global functions
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
        alert.setHeaderText("Czy na pewno chcesz usunąć rekord?");
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
        alert.setHeaderText("Nie wybrano seansu do edycji.");
        alert.setContentText("Wybierz seans z listy.");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toString());
        dialogPane.getStyleClass().add("my-dialog-pane");
    }

    @FXML
    private void backHome() throws IOException {
        App.setRoot("home");
    }
}
