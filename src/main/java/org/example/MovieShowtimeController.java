package org.example;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieShowtimeController {

    private String dbUrl = LoginCredentials.getDbUrl();
    private String username = LoginCredentials.getUsername();
    private String password = LoginCredentials.getPassword();
    private List<String> timeList = new ArrayList<>();

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
        id_filmuCol.setCellValueFactory(new PropertyValueFactory<>("id_filmu"));
        id_salaCol.setCellValueFactory(new PropertyValueFactory<>("id_sala"));
        dataCol.setCellValueFactory(new PropertyValueFactory<>("data"));
        pora_emisjiCol.setCellValueFactory(new PropertyValueFactory<>("pora_emisji"));

        buttonLoadDataMovieShowtime.setOnAction(event -> {
            List<MovieShowtimeAdapter> movieShowtimeList = retrieveMovieShowtimeFromDB();
            tableDB.getItems().setAll(movieShowtimeList);
        });


        //Add tab pane functions
        populateChoiceBoxes();

        //Add event handler for add button
        addMovieShowButton.setOnAction(event -> {
            SendShowtimeToDB();
        });


        //Edit tab pane functions
        populateEditMovieShowList();

        //Add event handler for edit button
        editMovieShowButton.setOnAction(event -> {
            String selectedMovieShowID = String.valueOf(choiceEditMovieShowList.getValue());
            if (selectedMovieShowID != null) {
                int movieShowId = Integer.parseInt(selectedMovieShowID);
                MovieAdapter selectedMovie = choiceEditMovieTitle.getValue();
                int movieId = selectedMovie.getId_filmu();
                String date = editDateInput.getValue().toString();
                String time = choiceEditTimeList.getValue().toString();

                updateMovieShowInDatabase(movieShowId, movieId, date, time);
            } else {
                Alert errorDialog = new Alert(Alert.AlertType.ERROR);
                errorDialog.setTitle("Błąd");
                errorDialog.setHeaderText("Nie wybrano seansu do edycji.");
                errorDialog.setContentText("Wybierz seans z listy.");
                errorDialog.showAndWait();
            }
        });


        //Delete tab pane functions
        populateMovieShowList();

        //Add event handler for remove button
        removeMovieShowButton.setOnAction(event -> {
            String selectedMovieShowID = choiceRemoveMovieShowList.getValue();
            if (selectedMovieShowID != null) {

                Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationDialog.setTitle("Potwierdzenie");
                confirmationDialog.setHeaderText("Czy na pewno chcesz usunąć rekord?");
                confirmationDialog.setContentText("Ta operacja jest nieodwracalna.");

                Optional<ButtonType> result = confirmationDialog.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {

                    deleteMovieShowFromDatabase(Integer.parseInt(selectedMovieShowID));
                    choiceRemoveMovieShowList.getItems().clear();
                    populateMovieShowList();

                    Stage popupStage = new Stage();
                    popupStage.initModality(Modality.APPLICATION_MODAL);

                    Label messageLabel = new Label("Film został usunięty!");
                    showPopup(popupStage, messageLabel);
                }
            }
        });
    }











    //Show tab pane functions
    private List<MovieShowtimeAdapter> retrieveMovieShowtimeFromDB() {
        List<MovieShowtimeAdapter> movieShowtimeList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbUrl, username, password)) {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM seanse");
            System.out.println("Połączono");

            while (result.next()) {
                int id_seansu = result.getInt("id_seansu");
                int id_filmu = result.getInt("id_filmu");
                int id_sala = result.getInt("id_sala");
                String data = result.getString("data");
                String pora_emisji = result.getString("pora_emisji");

                MovieShowtimeAdapter movieShowtime = new MovieShowtimeAdapter(id_seansu, id_filmu, id_sala, data, pora_emisji);
                movieShowtimeList.add(movieShowtime);

                System.out.println("Pobrano rekord");
            }
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

    private void populateChoiceBoxes() {

        //Movie title choice box
        RemoveMovieController removeMovieController = new RemoveMovieController();
        List<MovieAdapter> movieTitles = removeMovieController.retrieveMovieTitlesFromDatabase();
        choiceAddMovieTitle.getItems().addAll(movieTitles);

        //Time choice box
        choiceAddTimeList.getItems().addAll(timeList);
    }




    //Edit Movie pane functions
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

    @FXML
    private void backHome() throws IOException {
        App.setRoot("home");
    }
}
