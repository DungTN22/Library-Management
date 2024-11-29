package org.example.librarymanagement1.frontend.Home;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.input.ScrollEvent;
import org.example.librarymanagement1.Book;
import org.example.librarymanagement1.HelloApplication;
import org.example.librarymanagement1.backend.BookService;
import org.example.librarymanagement1.backend.Images;
import org.example.librarymanagement1.backend.SetUp;
import org.example.librarymanagement1.frontend.Other.BookTypePage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    private Button allGBookButton;

    @FXML
    private Button allNBookButton;

    @FXML
    private Button allPBookButton;

    @FXML
    private ScrollPane greatBookScroll;

    @FXML
    private Button leftGBookButton;

    @FXML
    private Button leftNBookButton;

    @FXML
    private Button leftPBookButton;

    @FXML
    private HBox listGreatBooks;

    @FXML
    private HBox listNewBooks;

    @FXML
    private HBox listPopuplarBooks;

    @FXML
    private ScrollPane newBookScroll;

    @FXML
    private ScrollPane popularBookScroll;

    @FXML
    private Button rightGBookButton;

    @FXML
    private Button rightNBookButton;

    @FXML
    private Button rightPBookButton;

    BookService bookService = new BookService();

    boolean isNMoving = false, isPMoving = false, isGMoving;

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

    /**
     * mở trang xem tất cả các sách cùng thể loại (new, popular,great).
     *
     * @param event sự kiện bấm chuột
     */
    @FXML
    void seeAllBookPage(ActionEvent event) {
        BookTypePage allBookPage = SetUp.allBookSameTypeLoader.getController();

        if (event.getSource() == allNBookButton) {
            allBookPage.setTopicListBooks("New Books");
            allBookPage.setBookList(bookService.getNewBooks(100));
        } else if (event.getSource() == allGBookButton) {
            allBookPage.setTopicListBooks("Great Books");
            allBookPage.setBookList(bookService.getTopRatedBooks(100));
        } else if (event.getSource() == allPBookButton) {
            allBookPage.setTopicListBooks("Popular Books");
            allBookPage.setBookList(bookService.getMostPopularBooks(100));
        }

        allBookPage.resetValue(15);
        SetUp.newStage.setScene(SetUp.allBookSameTypeScene);
    }

    /**
     * Phương thức để chặn sự kiện cuộn ngang trên ScrollPane.
     *
     * @param scrollPane scrollPane
     */
    private void blockHorizontalScrolling(ScrollPane scrollPane) {
        scrollPane.addEventFilter(ScrollEvent.SCROLL, event -> {
            if (event.getDeltaX() != 0) {  // Kiểm tra nếu có cuộn ngang
                event.consume();  // Chặn sự kiện cuộn ngang
            }
        });
    }

    /**
     * chặn sử kiện cuộn ngang tất cả các scroll có trong home.
     */
    private void blockTouchBar() {
        blockHorizontalScrolling(newBookScroll);
        blockHorizontalScrolling(popularBookScroll);
        blockHorizontalScrolling(greatBookScroll);
    }

    /**
     * tạo hiệu ứng trượt ngang cho các scrollPane.
     *
     * @param scrollPane scrollPane
     * @param targetValue giá trị trượt
     */
    private void animateScrollBar(ScrollPane scrollPane, double targetValue) {
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(scrollPane.hvalueProperty(), targetValue);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.5), keyValue);  // Thời gian cuộn là 0.5 giây
        timeline.getKeyFrames().add(keyFrame);

        if (scrollPane == newBookScroll) {
            isNMoving = true;
            timeline.setOnFinished(event -> isNMoving = false);
        } else if (scrollPane == popularBookScroll) {
            isPMoving = true;
            timeline.setOnFinished(event -> isPMoving = false);
        } else if (scrollPane == greatBookScroll) {
            isGMoving = true;
            timeline.setOnFinished(event -> isGMoving = false);
        }

        timeline.play();
    }

    /**
     * di chuyển movelist sang phải.
     *
     * @param scrollPane scrollPane
     */
    private void setUpMovementRight(ScrollPane scrollPane) {
        double currentScroll = scrollPane.getHvalue();
        double targetValue = Math.min(1, currentScroll + 0.5);
        animateScrollBar(scrollPane, targetValue);
    }

    /**
     * di chuyển movelist sang trái.
     *
     * @param scrollPane scrollPane
     */
    private void setUpMovementLeft(ScrollPane scrollPane) {
        double currentScroll = scrollPane.getHvalue();
        double targetValue = Math.max(0, currentScroll - 0.5);
        animateScrollBar(scrollPane, targetValue);
    }

    /**
     * kiểm tra điều kiện xem các thanh scroll nào được phép trượt.
     *
     * @param event nút bấm
     */
    @FXML
    public void moveList(ActionEvent event) {
        if (event.getSource() == rightNBookButton && !isNMoving) {
            setUpMovementRight(newBookScroll);
        } else if (event.getSource() == leftNBookButton && !isNMoving) {
            setUpMovementLeft(newBookScroll);
        } else if (event.getSource() == rightPBookButton && !isPMoving) {
            setUpMovementRight(popularBookScroll);
        } else if (event.getSource() == leftPBookButton && !isPMoving) {
            setUpMovementLeft(popularBookScroll);
        } else if (event.getSource() == rightGBookButton && !isGMoving) {
            setUpMovementRight(greatBookScroll);
        } else if (event.getSource() == leftGBookButton && !isGMoving) {
            setUpMovementLeft(greatBookScroll);
        }
    }

    /**
     * lấy kích thước số lượng sách.
     *
     * @param books danh sách các sách
     * @return kích thước
     */
    public int getBookListSize(List<Book> books) {
        try {
            return books.size();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /**
     * thực hiện cài đặt, load các sách trong list sách lên màn hình.
     */
    private void settingListBooks(List<Book> typeBooks, HBox listBooks) {
        int sizeListBooks = getBookListSize(typeBooks);
        for (int i = 0 ; i < sizeListBooks; i++) {
            if (i >= 16) { return; }
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/librarymanagement1/BookCellHome.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                HomeBookCell homeBookCell = fxmlLoader.getController();
                homeBookCell.setCurrebtBook(typeBooks.get(i));
                homeBookCell.setImageForBook();

                Platform.runLater(() -> {
                    listBooks.getChildren().add(anchorPane);
                });
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * khởi tạo danh sách các sách hay
     */
    public void initialGreatBooks() {
        settingListBooks(bookService.getTopRatedBooks(12), listGreatBooks);
    }

    /**
     * khởi tạo danh sách các sách mới
     */
    public void initialNewBooks() {
        settingListBooks(bookService.getNewBooks(12), listNewBooks);
    }

    /**
     * Khởi tạo danh sách cac sách nổi.
     */
    public void initialPopularBooks() {
        settingListBooks(bookService.getMostPopularBooks(12), listPopuplarBooks);
    }

    /**
     * load lại trang Home.
     */
    public void reLoadHomePage() {
        listGreatBooks.getChildren().clear();
        listNewBooks.getChildren().clear();
        listPopuplarBooks.getChildren().clear();

        // thực hiện đa luồng tăng trải nghiệm người dùng
        Thread newBookThread = new Thread(this::initialNewBooks);
        Thread popularBookThread = new Thread(this::initialPopularBooks);
        Thread greatBookThread = new Thread(this::initialGreatBooks);
        newBookThread.start();
        popularBookThread.start();
        greatBookThread.start();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        blockTouchBar();
        initialNewBooks();
        initialPopularBooks();
        initialGreatBooks();
    }
}