package org.example;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditMovieController {

    private String dbUrl = LoginCredentials.getDbUrl();
    private String username = LoginCredentials.getUsername();
    private String password = LoginCredentials.getPassword();

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


    //TODO
    /* Edycja rekordów w bazie (funkcjonalność) */

    public void initialize() {

        tableDB.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tytulCol.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        opisCol.setCellValueFactory(new PropertyValueFactory<>("opis"));
        czasTrwaniaCol.setCellValueFactory(new PropertyValueFactory<>("czas_trwania"));
        gatCol.setCellValueFactory(new PropertyValueFactory<>("nazwa_gatunku"));
        ocenaCol.setCellValueFactory(new PropertyValueFactory<>("ocena"));
        cenaCol.setCellValueFactory(new PropertyValueFactory<>("cena"));

        buttonLoadData.setOnAction(event -> {
            List<MovieDetailsAdapter> movieDetailsAdapterList = retrieveMoviesFromDatabase();
            tableDB.setItems(FXCollections.observableArrayList(movieDetailsAdapterList));
        });

    }

    @FXML
    private void backHome() throws IOException {
        App.setRoot("home");
    }

    private List<MovieDetailsAdapter> retrieveMoviesFromDatabase() {
        List<MovieDetailsAdapter> movieDetailsAdapterList = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT film.tytul, film.opis, film.czas_trwania, film.ocena, gatunek.nazwa_gatunku AS nazwa_gatunku, film.cena FROM film INNER JOIN gatunek ON film.id_gatunku=gatunek.id_gatunku;");
            System.out.println("Połączono");

            while (resultSet.next()) {
                String tytul = resultSet.getString("tytul");
                String opis = resultSet.getString("opis");
                int czas_trwania = resultSet.getInt("czas_trwania");
                double ocena = resultSet.getDouble("ocena");
                String nazwa_gatunku = resultSet.getString("nazwa_gatunku");
                double cena = resultSet.getDouble("cena");

                MovieDetailsAdapter movieDetailsAdapter = new MovieDetailsAdapter(tytul, opis, czas_trwania, ocena, nazwa_gatunku, cena);
                movieDetailsAdapterList.add(movieDetailsAdapter);

                System.out.println("Pobrano rekord");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the database error gracefully
        }

        System.out.println("Koniec");
        return movieDetailsAdapterList;
    }
}
