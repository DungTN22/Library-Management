package org.example.librarymanagement1.frontend.BookManagementPage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.example.librarymanagement1.backend.Images;
import org.example.librarymanagement1.backend.SetUp;
import org.example.librarymanagement1.frontend.BorrowMngPage.BorrowBookPage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookDetails implements Initializable {

    // Các trường TextField sẽ hiển thị dữ liệu book
    @FXML
    private TextField nameField;

    @FXML
    private TextField bookIDField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField genreField;

    @FXML
    private Label descriptionLabel;

    @FXML
    private ImageView bookImage;

    @FXML
    private Button borrowButton;

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

    String imageLink;

    // Phương thức này nhận dữ liệu book từ controller trước đó và điền vào các TextField
    public void setBookDetails(int id, String title, String author, String genre, int year, int pages, int available, String imageLink, String description) {
        nameField.setText(title);
        bookIDField.setText(String.valueOf(id));
        authorField.setText(author);
        genreField.setText(genre);
        descriptionLabel.setText(description);
        this.imageLink = imageLink;
        Images.setImage(imageLink, bookImage, 200, 320);
    }

    @FXML
    public void gotoBorrowPage() throws IOException {
        try {
            String bookName = nameField.getText();

            // Lấy controller của BorrowBookPage
            BorrowBookPage borrowBookPageController = SetUp.borrowBookPageLoader.getController();

            // Truyền tên sách sang BorrowBookPage
            borrowBookPageController.setBookDetails(bookName, imageLink);

            // Hiển thị giao diện mượn sách
            SetUp.newStage.setScene(SetUp.borrowBookPageScene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
