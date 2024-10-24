module org.example.librarymanagement1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.librarymanagement1 to javafx.fxml;
    exports org.example.librarymanagement1;
}