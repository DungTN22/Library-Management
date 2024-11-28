package org.example.librarymanagement1.frontend.UserMngPage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.librarymanagement1.backend.SetUp;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserDetails implements Initializable {

    // Các trường TextField sẽ hiển thị dữ liệu người dùng
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField accountField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField statusField;

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

    @FXML
    private Label title;  // Tiêu đề nếu cần

    // Phương thức này nhận dữ liệu người dùng từ controller trước đó và điền vào các TextField
    public void setUserDetails(String name, String email, String account, String phone, String password, String status) {
        nameField.setText(name);
        emailField.setText(email);
        accountField.setText(account);
        phoneField.setText(phone);
        passwordField.setText(password);
        statusField.setText(status);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
