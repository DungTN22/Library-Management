package org.example.librarymanagement1.frontend.BorrowMngPage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.example.librarymanagement1.Book;
import org.example.librarymanagement1.BorrowedBook;
import org.example.librarymanagement1.backend.BookService;
import org.example.librarymanagement1.backend.Images;
import org.example.librarymanagement1.backend.SetUp;
import org.example.librarymanagement1.frontend.BookManagementPage.BookManagement;
import org.example.librarymanagement1.frontend.BookManagementPage.BookManagementTableCell;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

    private Book currentBook;

    private String formattedDate;

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

    // Lấy ngày hiện tại từ LocalDate
    LocalDate currentDate = LocalDate.now();

    // Chuyển đổi từ LocalDate sang Date
    Date date = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

    public void setBookDetails(Book book) {
        bookNameField.setText(book.getTitle()); // Điền tên sách vào trường bookNameField
        bookNameField.setEditable(false); // Ngăn không cho người dùng sửa
        Images.setImage(book.getImageLink(), imageArea, 200, 322);
        currentBook = book;

        // Lấy ngày hiện tại và định dạng nó
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Định dạng ngày
        formattedDate = currentDate.format(formatter);
        System.out.println(formattedDate);

        // Điền ngày hiện tại vào borrowDateField
        borrowDateField.setText(formattedDate);
        borrowDateField.setEditable(false);
    }

    private final BookService bookService = new BookService();

    /**
     * cài đặt cho thông báo khi hiển thị.
     *
     * @param status trạng thái tốt/xấu
     * @param notification loại thông báo
     * @param alarm tên cảnh báo
     */
    private void showNotification(boolean status, Label notification, String alarm) {
        notification.setText(alarm);
        if (status) {
            notification.setTextFill(Color.GREEN);
        } else {
            notification.setTextFill(Color.RED);
        }

        notification.setVisible(true);

        // Tạo Timeline để ẩn nhãn sau 2 giây
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> notification.setVisible(false)));
        timeline.setCycleCount(1);
        timeline.play();
    }

    @FXML
    private void onAddButtonClick() {
        String account = accountField.getText();
        String bookName = bookNameField.getText();
        String username = usernameField.getText();

        if (account.isEmpty() && username.isEmpty()) {
            showNotification(false, usernameNotification, "Username is required!");
            showNotification(false, accountNotification, "Account is required!");
            return;
        } else
        if (account.isEmpty()) {
            showNotification(false, accountNotification, "Account is required!");
            return;
        } else
        if (username.isEmpty()) {
            showNotification(false, usernameNotification, "Username is required!");
            return;
        }

        // Gọi phương thức borrowBook từ BookService
        boolean isSuccess = bookService.borrowBook(account, bookName);

        if (isSuccess) {
            showNotification(true, buttonNotification, "Borrowed book successfully!");
            BorrowBookManagement borrowBookManagement = SetUp.borrowBookLoader.getController();
            BookManagement bookManagement = SetUp.bookManageLoader.getController();
            BorrowedBook borrowedBook = new BorrowedBook(date, account, currentBook.getTitle(), username);
            borrowBookManagement.setUpBookManagePage();
            bookManagement.setUpBookManagePage();

            // Reset các trường thông tin (nếu cần)
            usernameField.clear();
            accountField.clear();
            bookNameField.clear();
        } else {
            showNotification(false, buttonNotification, "Failed to borrow book!");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
