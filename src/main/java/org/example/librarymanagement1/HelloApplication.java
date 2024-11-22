package org.example.librarymanagement1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.librarymanagement1.backend.BookService;
import org.example.librarymanagement1.backend.Images;
import org.example.librarymanagement1.backend.SetUp;
import org.example.librarymanagement1.frontend.BookManagementPage.BookManagement;
import org.example.librarymanagement1.frontend.BookManagementPage.BookRepairAndAdd;
import org.example.librarymanagement1.frontend.Home.HomeController;
import org.example.librarymanagement1.frontend.Other.BookTypePage;
import org.example.librarymanagement1.frontend.SearchPage.SearchController;

import java.io.IOException;

import static org.example.librarymanagement1.backend.SetUp.searchLoader;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        SetUp.homeScene = new Scene(SetUp.homeLoader.load(),1080,720);
        SetUp.searchScene = new Scene(SetUp.searchLoader.load(),1080,720);
        SetUp.bookManageScene = new Scene(SetUp.bookManageLoader.load(),1080,720);
        SetUp.repairAddBookScene = new Scene(SetUp.repairAddLoader.load(),1080,720);
        SetUp.allBookSameTypeScene = new Scene(SetUp.allBookSameTypeLoader.load(),1080,720);
        SetUp.borrowBookManageScene = new Scene(SetUp.borrowBookLoader.load(), 1080, 720);

        SetUp.newStage = stage;
        SetUp.newStage.setResizable(false);

        SetUp.newStage.setTitle("Library Management");
        SetUp.newStage.setScene(SetUp.homeScene);
        SetUp.newStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}