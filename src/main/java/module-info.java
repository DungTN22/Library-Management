module org.example.librarymanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires com.google.gson;
    requires java.sql;
    requires java.dotenv;
    requires java.mail;
    requires javafx.graphics;
    requires junit;

    opens org.example.librarymanagement1.frontend.Home to javafx.fxml;
    opens  org.example.librarymanagement1.frontend.Other to javafx.fxml;
    opens  org.example.librarymanagement1.frontend.SearchPage to javafx.fxml;
    opens org.example.librarymanagement1.frontend.BookManagementPage to javafx.fxml;
    opens org.example.librarymanagement1.frontend.BorrowMngPage to javafx.fxml;
    opens org.example.librarymanagement1.frontend.UserMngPage to javafx.fxml;
    opens org.example.librarymanagement1 to javafx.fxml;
    exports org.example.librarymanagement1;
}