package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.adapters.LoginCredentials;
import org.example.adapters.MovieDetailsAdapter;
import org.example.adapters.TransactionsAdapter;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class TransactionsController {

    //GLOBAL
    private final String dbUrl = LoginCredentials.getDbUrl();
    private final String username = LoginCredentials.getUsername();
    private final String password = LoginCredentials.getPassword();

    @FXML
    private TableView<TransactionsAdapter> tableDB;
    @FXML
    private TableColumn<TransactionsAdapter, String> nr_rezerwacjiCol;
    @FXML
    private TableColumn<TransactionsAdapter, String> tytulCol;
    @FXML
    private TableColumn<TransactionsAdapter, String> loginCol;
    @FXML
    private TableColumn<TransactionsAdapter, Integer> id_biletuCol;
    @FXML
    private TableColumn<TransactionsAdapter, List<String>> miejscaCol;
    @FXML
    private TableColumn<TransactionsAdapter, Double> cenaCol;
    @FXML
    private Button buttonLoadDataFromDB;

    public void initialize() {
        // Initialize table columns
        nr_rezerwacjiCol.setCellValueFactory(new PropertyValueFactory<>("nr_rezerwacji"));
        tytulCol.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        loginCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        id_biletuCol.setCellValueFactory(new PropertyValueFactory<>("id_biletu"));
        miejscaCol.setCellValueFactory(new PropertyValueFactory<>("miejsca"));
        cenaCol.setCellValueFactory(new PropertyValueFactory<>("cena"));

        buttonLoadDataFromDB.setOnAction(actionEvent -> {
            // Load data from the database
            List<TransactionsAdapter> reservations = loadDataFromDatabase();

            // Populate the table with data
            tableDB.getItems().addAll(reservations);
        });
    }

    private List<TransactionsAdapter> loadDataFromDatabase() {
        List<TransactionsAdapter> transactions = new ArrayList<>();

        try {
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT rezerwacje.nr_rezerwacji, film.tytul, uzytkownicy.login, bilet.id_rezer, GROUP_CONCAT(CONCAT(miejsca.rzad, '-', miejsca.fotel)) AS miejsca, bilet.cena FROM bilet " +
                    "INNER JOIN rezerwacje ON bilet.id_rezer = rezerwacje.id_rezer " +
                    "INNER JOIN uzytkownicy ON rezerwacje.id_uzyt = uzytkownicy.id_uzyt " +
                    "INNER JOIN miejsca ON bilet.id_miejsca = miejsca.id_miejsca " +
                    "INNER JOIN seanse ON bilet.id_seansu = seanse.id_seansu " +
                    "INNER JOIN film ON seanse.id_filmu = film.id_filmu " +
                    "GROUP BY bilet.id_rezer;");
            System.out.println("Connected");

            while (resultSet.next()) {
                String reservationNumber = resultSet.getString("nr_rezerwacji");
                String movieTitle = resultSet.getString("tytul");
                String userLogin = resultSet.getString("login");
                int reservationId = resultSet.getInt("id_rezer");
                String seatDescription = resultSet.getString("miejsca");
                double orderValue = resultSet.getDouble("cena");

                List<String> seatDescriptions = Arrays.asList(seatDescription.split(","));

                TransactionsAdapter transaction = new TransactionsAdapter(reservationNumber, movieTitle, userLogin, reservationId, seatDescriptions, orderValue);
                transactions.add(transaction);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("Finished");
        return transactions;
    }











    @FXML
    private void backHome() throws IOException {
        App.setRoot("home");
    }
}
