package org.example.librarymanagement1.frontend.BookManagementPage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.example.librarymanagement1.Book;
import org.example.librarymanagement1.backend.BookService;
import org.example.librarymanagement1.backend.Images;
import org.example.librarymanagement1.backend.SetUp;
import org.example.librarymanagement1.frontend.Home.HomeController;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class BookRepairAndAdd implements Initializable {
    @FXML
    private Label authorNotification;

    @FXML
    private Label buttonNotification;

    @FXML
    private Label genresNotification;

    @FXML
    private Label nameNotification;

    @FXML
    private Label numPageNotification;

    @FXML
    private Label pDateNotification;

    @FXML
    private Button addButton;

    @FXML
    private TextField authorField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField genresField;

    @FXML
    private ImageView imageArea;

    @FXML
    private TextField linkImageField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField numberPagesField;

    @FXML
    private TextField publicationDateField;

    @FXML
    private Label title;
    private int type = 0; // 0 là add, 1 là repair

    Book repairBook = null;
    BookService bookService = new BookService();
    BookManagementTableCell parent = null;

    public TextField getLinkImageField() {
        return linkImageField;
    }

    public void setLinkImageField(String linkImage) {
        this.linkImageField.setText(linkImage);
    }

    public TextField getNameField() {
        return nameField;
    }

    public void setNameField(String name) {
        this.nameField.setText(name);
    }

    public TextField getNumberPagesField() {
        return numberPagesField;
    }

    public void setNumberPagesField(String numberPages) {
        this.numberPagesField.setText(numberPages);
    }

    public TextField getAuthorField() {
        return authorField;
    }

    public void setAuthorField(String author) {
        this.authorField.setText(author);
    }

    public TextField getGenresField() {
        return genresField;
    }

    public void setGenresField(String genres) {
        this.genresField.setText(genres);
    }

    public TextArea getDescriptionField() {
        return descriptionField;
    }

    public void setDescriptionField(String description) {
        this.descriptionField.setText(description);
    }

    public TextField getPublicationDateField() {
        return publicationDateField;
    }

    public void setPublicationDateField(String publicationDate) {
        this.publicationDateField.setText(publicationDate);
    }

    public void setBook(Book repairBook) {
        this.repairBook = repairBook;
    }

    public void setParent(BookManagementTableCell parent) {
        this.parent = parent;
    }

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

    }

    @FXML
    public void goToBorrowManagePage() throws IOException {
        SetUp.newStage.setScene(SetUp.borrowBookManageScene);
    }

    /**
     * Cài đặt loại dùng : type0 => add, type1 => repair.
     *
     * @param type loại
     */
    public void setType(int type) {
        this.type = type;
        setNameButtonAndTitle();
    }

    /**
     * cập nhật tiêu đề và tên cho button theo mỗi type.
     */
    public void setNameButtonAndTitle() {
        if (type == 0) {
            addButton.setText("ADD");
            title.setText("ADD BOOK");
        } else if (type == 1) {
            addButton.setText("REPAIR");
            title.setText("REPAIR BOOK");
        }
    }

    /**
     * thực thi khi bấm nút add/repair để thực hiện add hoặc cập nhật tùy theo type.
     */
    @FXML
    public void addOrRepairBook() {
        if (type == 0) {
            addBook();
        } else if (type == 1) {
            repairBook();
        }
    }

    /**
     * cài đặt các giá trị cho các text field.
     *
     * @param book dữ liệu sách
     */
    public void setRepairBook(Book book) {
        repairBook = book;
        setNameField(book.getTitle());
        setAuthorField(book.getAuthor());
        setGenresField(book.getGenre());
        setPublicationDateField(String.valueOf(book.getYear()));
        setNumberPagesField(String.valueOf(book.getPages()));
        setLinkImageField(book.getImageLink());
        setDescriptionField(book.getDescription());
        Images.setImage(book,imageArea,1000, 1600);
    }

    /**
     * dọn dẹp dữ liệu.
     */
    public void clearDataInField() {
        setNameField("");
        setAuthorField("");
        setGenresField("");
        setPublicationDateField("");
        setNumberPagesField("");
        setLinkImageField("");
        setDescriptionField("");
    }

    /**
     * thực hiện thêm sách vào dữ liệu
     */
    private void addBook() {
        if (checkInfoBook()) {
            Book newBook = new Book(bookService.getLastId() + 1, nameField.getText(),authorField.getText(),genresField.getText(),
                    Integer.parseInt(publicationDateField.getText()), Integer.parseInt(numberPagesField.getText()),
                    10, linkImageField.getText(), descriptionField.getText());
            if (bookService.addBook(newBook)) {
                BookManagement bookManagement = SetUp.bookManageLoader.getController();
                bookManagement.addCellsToTable(newBook);

                HomeController home = SetUp.homeLoader.getController();
                home.reLoadHomePage();

                showNotification(true, buttonNotification, "Add Successfully!");

                return;
            }
        }
        showNotification(false, buttonNotification, "Add Failed!");
    }

    /**
     * cập nhật lại các giá trị cho sách
     */
    private void repairBook() {
        if (checkInfoBook()) {
            Book newBook = new Book(repairBook.getBookId(),nameField.getText(),authorField.getText(),genresField.getText(),
                    Integer.parseInt(publicationDateField.getText()), Integer.parseInt(numberPagesField.getText()),
                    10, linkImageField.getText(), descriptionField.getText());
            if (bookService.updateBook(newBook)) {
                parent.updateValueBookCell(newBook);
                showNotification(true, buttonNotification, "Repair Successfully!");
                return;
            }
        }
        showNotification(false, buttonNotification, "Repair Failed");
    }

    /**
     * cập nhật lại ảnh mỗi khi link thay đổi.
     */
    public void setImageArea() {
        String link = linkImageField.getText();
        if (!link.isEmpty()) {
            Images.setImage(link, imageArea, 1000, 1600);
        } else {
            imageArea.setImage(null);
        }
    }

    /**
     * kiểm tra thông tin khi cho phép cập nhật hoặc thêm sách
     */
    private boolean checkInfoBook() {
        boolean status = true;
        if (nameField.getText().isEmpty()) {
            showNotification(false, nameNotification, "The name field cannot be null!");
            status = false;
        }
        if (authorField.getText().isEmpty()) {
            showNotification(false, authorNotification, "The author field cannot be null!");
            status = false;
        }
        if (genresField.getText().isEmpty()) {
            showNotification(false, genresNotification, "The genres field cannot be null!");
            status = false;
        }
        if (publicationDateField.getText().isEmpty()) {
            showNotification(false, pDateNotification, "The public date field cannot be null!");
            status = false;
        }
        if (numberPagesField.getText().isEmpty()) {
            showNotification(false, numPageNotification, "The number pages field cannot be null!");
            status = false;
        }
        return status;
    }

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

    /**
     * lắng nghe sự thay đổi của linkimage.
     */
    public void listenChangeOfImageLink() {
        linkImageField.textProperty().addListener((observable, oldValue, newValue) -> setImageArea());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listenChangeOfImageLink();
    }
}
