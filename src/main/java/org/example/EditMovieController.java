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
    private TableColumn<MovieDetailsAdapter, Integer> id_filmuCol;
    @FXML
    private TableColumn<MovieDetailsAdapter, String> tytulCol;
    @FXML
    private TableColumn<MovieDetailsAdapter, Integer> czas_trwaniaCol;
    @FXML
    private TableColumn<MovieDetailsAdapter, Double> ocenaCol;
    @FXML
    private TableColumn<MovieDetailsAdapter, String> opisCol;
    @FXML
    private TableColumn<MovieDetailsAdapter, Integer> id_gatunkuCol;
    @FXML
    private TableColumn<MovieDetailsAdapter, Byte[]> okladkaCol;
    @FXML
    private TableColumn<MovieDetailsAdapter, Double> cenaCol;
    @FXML
    private Button buttonLoadData;


    //TODO
    /* Edycja rekordów w bazie (funkcjonalność) */

    public void initialize() {
        id_filmuCol.setCellValueFactory(new PropertyValueFactory<>("id_filmu"));
        tytulCol.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        czas_trwaniaCol.setCellValueFactory(new PropertyValueFactory<>("czas_trwania"));
        ocenaCol.setCellValueFactory(new PropertyValueFactory<>("ocena"));
        opisCol.setCellValueFactory(new PropertyValueFactory<>("opis"));
        id_gatunkuCol.setCellValueFactory(new PropertyValueFactory<>("id_gatunku"));
        okladkaCol.setCellValueFactory(new PropertyValueFactory<>("okladka"));
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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM film");
            System.out.println("Połączono");

            while (resultSet.next()) {
                int id_filmu = resultSet.getInt("id_filmu");
                String tytul = resultSet.getString("tytul");
                int czas_trwania = resultSet.getInt("czas_trwania");
                double ocena = resultSet.getDouble("ocena");
                String opis = resultSet.getString("opis");
                int id_gatunku = resultSet.getInt("id_gatunku");
                byte[] okladka = resultSet.getString("okladka").getBytes();
                double cena = resultSet.getDouble("cena");

                MovieDetailsAdapter movieDetailsAdapter = new MovieDetailsAdapter(id_filmu, tytul, czas_trwania, ocena, opis, id_gatunku, okladka, cena);
                movieDetailsAdapterList.add(movieDetailsAdapter);

                System.out.println("Dodano rekord");
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
