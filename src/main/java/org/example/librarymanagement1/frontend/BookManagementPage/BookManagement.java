package org.example.librarymanagement1.frontend.BookManagementPage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.librarymanagement1.Book;
import org.example.librarymanagement1.HelloApplication;
import org.example.librarymanagement1.backend.BookService;
import org.example.librarymanagement1.backend.SetUp;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BookManagement implements Initializable {
    @FXML
    private VBox table;

    @FXML
    private TextField searchBookBar;

    @FXML
    private ScrollPane bookCellScrollPane;

    private List<Book> bookList = null;

    private int totalData = 0;

    BookService bookService = new BookService();

    private void setBookList() {
        this.bookList = bookService.getAllBooks();
    }

    private void setBookList(String key) {
        this.bookList = bookService.searchBooks(key,200);
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
     * Sử dụng cho nút thêm sách, dùng để chuyển sang add book page (trang thêm sách).
     */
    @FXML
    public void addBookPage() {
        BookRepairAndAdd add = SetUp.repairAddLoader.getController();
        add.setType(0);
        add.clearDataInField();
        SetUp.newStage.setScene(SetUp.repairAddBookScene);
    }

    /**
     * lấy ra kích thước của tất cả sách có trong database.
     *
     * @return kích thước số lượng sách
     */
    public int getBookListSize() {
        try {
            return bookList.size();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /**
     * thêm các cell data sách vào table (tạo table).
     */
    private void createCells(int number) {
        for (int i = 0; i < number; i++) {
            if (totalData + i >= getBookListSize()) {
                totalData = getBookListSize();
                return;
            }
            try {
                addCellsToTable(bookList.get(totalData + i));
            } catch (NullPointerException e) {
                System.out.println("BookManagement : " + e.getMessage());
            }
        }
        totalData += number;
    }

    /**
     * tao cell dữ liệu của 1 sách.
     *
     * @param book dữ liệu quyển sách dùng tạo cell
     */
    public void addCellsToTable(Book book) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/librarymanagement1/BookManagementTableCell.fxml"));
            HBox hBox = fxmlLoader.load();

            BookManagementTableCell controller = fxmlLoader.getController();
            Platform.runLater(() -> {
                controller.createNewHbox(book);
                table.getChildren().add(hBox);
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * kiểm tra thanh cuộn của GridBook, khi chạm đến đáy sẽ load thêm dữ liệu (nếu còn)
     */
    private void setupScrollListener() {
        bookCellScrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() == 1.0 && !SetUp.loadBookManageTableThread.isAlive()) { // Nếu đã cuộn tới cuối
                loadDataForTable();
            }
            for (int i = 0; i < table.getChildren().size(); i++) {
                Node cell = table.getChildren().get(i);
                cell.setVisible(isNodeVisible(bookCellScrollPane, cell));
            }
        });
    }

    public boolean isNodeVisible(ScrollPane scrollPane, Node node) {
        Bounds viewportBounds = scrollPane.getViewportBounds();
        Bounds nodeBounds = node.localToParent(node.getBoundsInLocal());

        double contentOffsetX = scrollPane.getHvalue() * (scrollPane.getContent().getBoundsInLocal().getWidth()
                - viewportBounds.getWidth());
        double contentOffsetY = scrollPane.getVvalue() * (scrollPane.getContent().getBoundsInLocal().getHeight()
                - viewportBounds.getHeight());

        Bounds visibleBounds = new BoundingBox(
                contentOffsetX,
                contentOffsetY,
                viewportBounds.getWidth(),
                viewportBounds.getHeight()
        );

        return visibleBounds.intersects(nodeBounds);
    }

    private void setUpBookManagePage() {
        setBookList();
        resetBookTable();
        setupScrollListener();
    }

    private void loadDataForTable() {
        SetUp.loadBookManageTableThread = new Thread(() -> {
            createCells(30);
        });

        SetUp.loadBookManageTableThread.start();
    }

    public void resetBookTable() {
        bookCellScrollPane.setVvalue(0);
        table.getChildren().clear();
        loadDataForTable();
        totalData = 0;
    }

    public void listenSearchChange() {
        searchBookBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (searchBookBar.getText().isEmpty()) {
                setBookList();
            } else {
                setBookList(searchBookBar.getText());
            }
            resetBookTable();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpBookManagePage();
        listenSearchChange();
    }
}