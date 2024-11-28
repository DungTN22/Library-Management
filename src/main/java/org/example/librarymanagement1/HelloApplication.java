package org.example.librarymanagement1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.example.librarymanagement1.backend.SetUp;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        SetUp.homeScene = new Scene(SetUp.homeLoader.load(),1080,720);
        SetUp.searchScene = new Scene(SetUp.searchLoader.load(),1080,720);
        SetUp.bookManageScene = new Scene(SetUp.bookManageLoader.load(),1080,720);
        SetUp.repairAddBookScene = new Scene(SetUp.repairAddLoader.load(),1080,720);
        SetUp.allBookSameTypeScene = new Scene(SetUp.allBookSameTypeLoader.load(),1080,720);
        SetUp.borrowBookManageScene = new Scene(SetUp.borrowBookLoader.load(), 1080, 720);
        SetUp.userScene = new Scene(SetUp.userLoader.load(), 1080, 720);
        SetUp.editAddUserScene = new Scene(SetUp.editAddLoader.load(), 1080, 720);
        SetUp.userDetailsScene = new Scene(SetUp.userDetailsLoader.load(), 1080, 720);
        SetUp.bookDetailsScene = new Scene(SetUp.bookDetailsLoader.load(), 1080, 720);
        SetUp.borrowBookPageScene = new Scene(SetUp.borrowBookPageLoader.load(), 1080, 720);

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