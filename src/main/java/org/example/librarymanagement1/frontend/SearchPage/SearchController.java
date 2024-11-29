package org.example.librarymanagement1.frontend.SearchPage;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.librarymanagement1.Book;
import org.example.librarymanagement1.HelloApplication;
import org.example.librarymanagement1.backend.BookService;
import org.example.librarymanagement1.backend.Database;
import org.example.librarymanagement1.backend.GgBookAPI;
import org.example.librarymanagement1.backend.SetUp;
import org.example.librarymanagement1.frontend.Other.BookTypePage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class SearchController implements Initializable {
    @FXML
    private VBox cellResultSearch;

    @FXML
    private Label changeModeButton;

    @FXML
    private TextField searchBar;

    private boolean loadBookMode = true;  // true là từ database_Local, false là từ API_BOOK

    BookService bookService = new BookService();

    GgBookAPI ggBookAPI = new GgBookAPI();

    List<Book> bookListApi = new ArrayList<>();

    @FXML
    public void goToHomePage() throws IOException {
        searchBar.setText("");
        SetUp.newStage.setScene(SetUp.homeScene);
    }

    @FXML
    public void goToSearchPage() throws IOException {
        searchBar.setText("");
        SetUp.newStage.setScene(SetUp.searchScene);
    }

    @FXML
    public void goToBookManagePage() throws IOException {
        searchBar.setText("");
        SetUp.newStage.setScene(SetUp.bookManageScene);
    }

    @FXML
    public void goToUserManagePage() throws IOException {
        searchBar.setText("");
        SetUp.newStage.setScene(SetUp.userScene);
    }

    @FXML
    public void goToBorrowManagePage() throws IOException {
        searchBar.setText("");
        SetUp.newStage.setScene(SetUp.borrowBookManageScene);
    }

    /**
     * mở trang xem tất cả các sách là kết quả tìm kiếm
     *
     * @throws IOException trả về ngoại lệ khu load không thành công
     */
    @FXML
    public void seeAllResultBookPage() throws IOException {
        if (!searchBar.getText().isEmpty() || !Objects.equals(searchBar.getText(), "")) {
            BookTypePage resultBookPage = SetUp.allBookSameTypeLoader.getController();

            resultBookPage.setTopicListBooks("All Results");
            if (loadBookMode) {
                resultBookPage.setBookList(bookService.searchBooks(searchBar.getText(), 100));
            } else {
                resultBookPage.setBookList(bookListApi);
            }
            resultBookPage.resetValue(15);

            searchBar.setText("");

            SetUp.newStage.setScene(SetUp.allBookSameTypeScene);
        }
    }

    /**
     * tìm kiếm dữ liệu trong database và hiển thị kết quả.
     */
    @FXML
    public void searchData() {
        cellResultSearch.getChildren().clear();
        String textSearch = searchBar.getText();
        if (!textSearch.isEmpty()) {
            if (loadBookMode) {
                searchWithLocal(textSearch);
            } else {
                searchWithApi(textSearch);
            }
        } else {
            Thread thread = new Thread(() -> {
                Platform.runLater(() -> {
                    bookListApi = new ArrayList<>();
                });
            });
            thread.setDaemon(true);
            thread.start();
        }
    }

    private void searchWithLocal(String textSearch) {
        bookListApi = bookService.searchBooks(textSearch, 10);
        loadCellSearch(bookListApi);
    }

    private void searchWithApi(String textSearch) {
        Thread thread = new Thread(() -> {
            bookListApi = ggBookAPI.searchBookngoai(textSearch);
            Platform.runLater(() -> loadCellSearch(bookListApi));
            }
        );

        thread.setDaemon(true);
        thread.start();
    }

    private void loadCellSearch(List<Book> books) {
        cellResultSearch.getChildren().clear();
        for (int i = 0 ; i < books.size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/librarymanagement1/ResultSearchCell.fxml"));
                Label label = fxmlLoader.load();
                ResultSearchCell resultSearchCell = fxmlLoader.getController();
                resultSearchCell.setCell(i % 2, books.get(i));
                cellResultSearch.getChildren().add(label);
                if (bookListApi.isEmpty()) {
                    cellResultSearch.getChildren().clear();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setUpChangeModeButton() {
        changeModeButton.setOnMouseClicked(event -> {

            if (changeModeButton.getStyleClass().contains("change_mode_local")) {
                changeModeButton.getStyleClass().remove("change_mode_local");
                changeModeButton.getStyleClass().add("change_mode_api");
                changeModeButton.setText("API");
                loadBookMode = false;
            } else {
                changeModeButton.getStyleClass().remove("change_mode_api");
                changeModeButton.getStyleClass().add("change_mode_local");
                changeModeButton.setText("LOCAL");
                loadBookMode = true;
            }
            searchBar.setText("");
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // lệnh lắng nghe sự thay đổi của thanh searchbar để tực hiện lệnh
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> searchData());
        setUpChangeModeButton();
    }
}