module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.adapters;
    opens org.example.adapters to javafx.fxml;
}