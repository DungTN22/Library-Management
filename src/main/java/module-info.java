module org.example.librarymanagement1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;
    requires mysql.connector.j;


    opens org.example.librarymanagement1 to javafx.fxml;
    exports org.example.librarymanagement1;
}