package org.example.librarymanagement1.frontend.Other;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import org.example.librarymanagement1.Book;
import org.example.librarymanagement1.backend.BookService;
import org.example.librarymanagement1.backend.SetUp;
import org.example.librarymanagement1.frontend.BookManagementPage.BookManagement;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

public class BookTypePage implements Initializable {

    @FXML
    private GridPane gridBooks;

    @FXML
    private Label topicListBooks;

    @FXML
    private ScrollPane scrollGrid;

    private int currentColumn = 0; // vị trí cột hiện tại
    private int currentRow = 0; // vị trí dòng hiện tại
    private int totalBooks = 0; // tổng số sách đã được load trên toàn bộ sách lấy được

    List<Book> bookList = new ArrayList<>();

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    public void setTopicListBooks(String name) {
        this.topicListBooks.setText(name);
    }

    @FXML
    public void goToHomePage() throws IOException {
        if (!SetUp.loadPageBookThread.isAlive()) {
            SetUp.loadPageBookThread.interrupt();
        }
        SetUp.newStage.setScene(SetUp.homeScene);
    }

    @FXML
    public void goToSearchPage() throws IOException {
        if (!SetUp.loadPageBookThread.isAlive()) {
            SetUp.loadPageBookThread.interrupt();
        }
        SetUp.newStage.setScene(SetUp.searchScene);
    }

    @FXML
    public void goToBookManagePage() throws IOException {
        if (!SetUp.loadPageBookThread.isAlive()) {
            SetUp.loadPageBookThread.interrupt();
        }
        SetUp.newStage.setScene(SetUp.bookManageScene);
    }

    @FXML
    public void goToUserManagePage() throws IOException {
        if (!SetUp.loadPageBookThread.isAlive()) {
            SetUp.loadPageBookThread.interrupt();
        }
        SetUp.newStage.setScene(SetUp.userScene);
    }

    @FXML
    public void goToBorrowManagePage() throws IOException {
        if (!SetUp.loadPageBookThread.isAlive()) {
            SetUp.loadPageBookThread.interrupt();
        }
        SetUp.newStage.setScene(SetUp.borrowBookManageScene);
    }

    /**
     * Cập nhật lại các giá trị cần (vị trí hàng, cột, số sách đã có trên GridPane).
     */
    private void changeStatus() {
        currentColumn++;
        if (currentColumn >= 5 ) {
            currentRow++;
            currentColumn = 0;
        }
    }

    /**
     * load dữ liệu cho GridBook dựa trên số lượng truyền vào.
     *
     * @param numberData số lượng dữ liệu muốn load
     */
    public void loadDataBook(int numberData) {
        if (bookList.size() - totalBooks < numberData) { numberData = bookList.size() - totalBooks; }
        Map<AnchorPane, BookTypePageCell> anchorPaneList = new LinkedHashMap<>();
        for (int i = 0; i < numberData; i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/librarymanagement1/BookTypePageCell.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                BookTypePageCell bookTypePageCell = fxmlLoader.getController();
                bookTypePageCell.setCurrentBook(bookList.get(totalBooks));

                anchorPaneList.put(anchorPane, bookTypePageCell);
                totalBooks++;

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (AnchorPane anchorPane : anchorPaneList.keySet()) {
            Platform.runLater(() -> {
                anchorPaneList.get(anchorPane).setImageForBook();
                gridBooks.add(anchorPane, currentColumn, currentRow);
                makeAnimationBook(anchorPane);
                changeStatus();
            });
        }
    }

    /**
     * kiểm tra thanh cuộn của GridBook, khi chạm đến đáy sẽ load thêm dữ liệu (nếu còn)
     */
    private void setupScrollListener() {
        scrollGrid.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() == 1.0 && !SetUp.loadPageBookThread.isAlive()) { // Nếu đã cuộn tới cuối
                SetUp.loadPageBookThread = new Thread(() -> {
                    loadDataBook(15);
                });

                SetUp.loadPageBookThread.start();
            }
        });
    }

    /**
     * khởi tạo ràng buộc cho GridBook
     */
    public void setUpConstraintGridBook() {
        gridBooks.getChildren().clear();
        gridBooks.getRowConstraints().clear();
        gridBooks.getColumnConstraints().clear();
    }

    /**
     * làm mới (reload) page khi mở lại.
     *
     * @param numberData số lượng dữ liệu (sách) được load
     */
    public void resetValue(int numberData) {
        gridBooks.getChildren().clear();
        scrollGrid.setVvalue(0);
        totalBooks = 0;
        currentColumn = 0;
        currentRow = 0;

        // thực hiện sử dụng đa luồng để cập nhật sách, tránh dừng tạm thời do phải chờ load xong hết sách
        SetUp.loadPageBookThread = new Thread(() -> {
            loadDataBook(numberData);
        });

        SetUp.loadPageBookThread.start();
    }

    /**
     * tạo hiệu ứng xuất hiện cho mỗi ô sách.
     *
     * @param anchorPane ô sách
     */
    public void makeAnimationBook(AnchorPane anchorPane) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), anchorPane);
        fadeTransition.setFromValue(0.0);  // Bắt đầu từ độ mờ 0 (mờ dần)
        fadeTransition.setToValue(1.0);    // Kết thúc ở độ mờ 1 (hiện rõ)
        fadeTransition.setCycleCount(1);   // Số lần chạy hiệu ứng (1 lần)
        fadeTransition.setAutoReverse(false);  // Không đảo ngược hiệu ứng

        // Bắt đầu hiệu ứng khi kích hoạt
        fadeTransition.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupScrollListener();
        setUpConstraintGridBook();
    }
}
