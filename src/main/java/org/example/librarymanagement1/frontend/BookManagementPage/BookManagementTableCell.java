package org.example.librarymanagement1.frontend.BookManagementPage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.librarymanagement1.Book;
import org.example.librarymanagement1.HelloApplication;
import org.example.librarymanagement1.backend.BookService;
import org.example.librarymanagement1.backend.SetUp;
import org.example.librarymanagement1.frontend.Home.HomeController;
import org.example.librarymanagement1.frontend.UserMngPage.UserDetails;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class BookManagementTableCell implements Initializable {
    @FXML
    private HBox cell; // đại diện cho chính fileFXML này

    @FXML
    private Label authorColumn;

    @FXML
    private Label descriptionColumn;

    @FXML
    private Label idColumn;

    @FXML
    private Label nameColumn;

    @FXML
    private Label statusColumn;


    private Book currentBook = null; // dữ liệu quyển sách đang lưu trữ

    BookService bookService = new BookService();

    /**
     * xóa dữ liệu của sách khỏi database, đồng thời cập nhật lại chương trình thông qua nút xóa.
     */
    @FXML
    public void delThisData() {
        if (bookService.deleteBook(currentBook.getBookId())) {
            Node parent = cell.getParent();
            if (parent instanceof VBox) {
                HomeController home = SetUp.homeLoader.getController();
                home.reLoadHomePage();
                VBox table = (VBox) parent;
                table.getChildren().remove(cell);
                System.out.println("Clear successfully!");
            }
        }
    }

    /**
     * chuyển sang trang sửa sách thông qua nút sửa.
     */
    @FXML
    public void repairBookData() {
        BookManagement bookManagement = SetUp.bookManageLoader.getController();
        bookManagement.resetBookTable();

        BookRepairAndAdd repair = SetUp.repairAddLoader.getController();
        repair.setType(1);
        repair.clearDataInField();
        repair.setParent(this);
        repair.setRepairBook(currentBook);
        SetUp.newStage.setScene(SetUp.repairAddBookScene);
    }

    /**
     * xem chi tiết dữ liệu sách.
     */
    @FXML
    public void seeDetailBookData() {
        if (currentBook == null) {
            System.out.println("No user selected!");
            return;
        }

        try {
            // Lấy controller của UserDetails
            BookDetails bookDetailsController = SetUp.bookDetailsLoader.getController();

            if (bookDetailsController == null) {
                System.out.println("Error: BookDetails controller is null.");
                return;
            }

            // Truyền dữ liệu người dùng vào trang chi tiết
            bookDetailsController.setBookDetails(
                    currentBook.getBookId(),
                    currentBook.getTitle(),
                    currentBook.getAuthor(),
                    currentBook.getGenre(),
                    currentBook.getYear(),
                    currentBook.getPages(),
                    currentBook.isAvailable(),
                    currentBook.getImageLink(),
                    currentBook.getDescription()
            );

            // Chuyển sang trang UserDetails
            SetUp.newStage.setScene(SetUp.bookDetailsScene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * cập nhật lại giá trị cho cell.
     *
     * @param book Dữ liệu sách
     */
    public void updateValueBookCell(Book book) {
        idColumn.setText(String.valueOf(book.getBookId()));
        nameColumn.setText(String.valueOf(book.getTitle()));
        authorColumn.setText(String.valueOf(book.getAuthor()));
        descriptionColumn.setText(String.valueOf(book.getDescription()));
        statusColumn.setText(String.valueOf(book.isAvailable()));
    }

    /**
     * tạo ra dữ liệu cho cell mới khi thêm sách
     *
     * @param book dữ liệu sách
     */
    public void createNewHbox(Book book) {
        updateValueBookCell(book);
        currentBook = book;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
