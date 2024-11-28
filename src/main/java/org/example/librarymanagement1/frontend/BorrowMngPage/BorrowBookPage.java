package org.example.librarymanagement1.frontend.BorrowMngPage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.example.librarymanagement1.backend.BookService;
import org.example.librarymanagement1.backend.Images;
import org.example.librarymanagement1.backend.SetUp;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class BorrowBookPage implements Initializable {
    @FXML
    private TextField borrowDateField;

    @FXML
    private TextField bookNameField;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField accountField;

    @FXML
    private ImageView imageArea;

    @FXML
    private Label borrowDateNotification;

    @FXML
    private Label bookNameNotification;

    @FXML
    private Label usernameNotification;

    @FXML
    private Label accountNotification;

    @FXML
    private Label buttonNotification;

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

    public void setBookDetails(String bookName, String imageLink) {
        bookNameField.setText(bookName); // Điền tên sách vào trường bookNameField
        bookNameField.setEditable(false); // Ngăn không cho người dùng sửa
        Images.setImage(imageLink, imageArea, 200, 322);

        // Lấy ngày hiện tại và định dạng nó
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Định dạng ngày
        String formattedDate = currentDate.format(formatter);
        System.out.println(formattedDate);

        // Điền ngày hiện tại vào borrowDateField
        borrowDateField.setText(formattedDate);
        borrowDateField.setEditable(false);
    }

    private final BookService bookService = new BookService();

    @FXML
    private void onAddButtonClick() {
        String account = accountField.getText();
        String bookName = bookNameField.getText();
        String username = usernameField.getText();

        if (account.isEmpty() && username.isEmpty()) {
            usernameNotification.setText("Username is required!");
            usernameNotification.setVisible(true);
            accountNotification.setText("Account is required!");
            accountNotification.setVisible(true);
            return;
        } else
        if (account.isEmpty()) {
            accountNotification.setText("Account is required!");
            accountNotification.setVisible(true);
            return;
        } else
        if (username.isEmpty()) {
            usernameNotification.setText("Username is required!");
            usernameNotification.setVisible(true);
            return;
        }

        // Gọi phương thức borrowBook từ BookService
        boolean isSuccess = bookService.borrowBook(account, bookName);

        if (isSuccess) {
            buttonNotification.setText("Book borrowed successfully!");
            buttonNotification.setVisible(true);

            // Reset các trường thông tin (nếu cần)
            usernameField.clear();
            accountField.clear();
            bookNameField.clear();
        } else {
            buttonNotification.setText("Failed to borrow book");
            buttonNotification.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
