package org.example.librarymanagement1.frontend.BorrowMngPage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.example.librarymanagement1.backend.SetUp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BorrowBookPage implements Initializable {
    @FXML
    public void goToHomePage() throws IOException {
        SetUp.newStage.setScene(SetUp.homeScene);
    }

    @FXML
    public void goToSearchPage() throws IOException {
        SetUp.newStage.setScene(SetUp.searchScene);
    }

    @FXML
    public void goToBookManagePage() throws IOException {
        SetUp.newStage.setScene(SetUp.bookManageScene);
    }

    @FXML
    public void goToUserManagePage() throws IOException {
        SetUp.newStage.setScene(SetUp.userScene);
    }

    @FXML
    public void goToBorrowManagePage() throws IOException {
        SetUp.newStage.setScene(SetUp.borrowBookManageScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
