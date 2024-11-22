package org.example.librarymanagement1.frontend.SearchPage;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.example.librarymanagement1.Book;
import org.example.librarymanagement1.HelloApplication;
import org.example.librarymanagement1.backend.BookService;
import org.example.librarymanagement1.backend.Database;
import org.example.librarymanagement1.backend.GgBookAPI;
import org.example.librarymanagement1.backend.SetUp;
import org.example.librarymanagement1.frontend.Other.BookTypePage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    @FXML
    private ListView<String> resultBar;

    @FXML
    private TextField searchBar;

    BookService bookService = new BookService();

    GgBookAPI ggBookAPI = new GgBookAPI();

    String [] resultsSearch = {};

    List<Book> resultBook = new ArrayList<>();

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
            resultBookPage.setBookList(bookService.searchBooks(searchBar.getText(), 100));
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
        String textSearch = searchBar.getText();

        if (!textSearch.isEmpty()) {
            List<Book> books = bookService.searchBooks(textSearch, 10);
            if (books.isEmpty()) {
                ggBookAPI.searchAndInsertBook(textSearch, Database.connect());
                books = bookService.searchBooks(textSearch, 10);
            }
            resultBook = books;
            int sizeResultBar = books.size();
            resultsSearch = new String[sizeResultBar];

            for (int i = 0; i < sizeResultBar; i++) {
                resultsSearch[i] = books.get(i).getTitle() + "  -  " + books.get(i).getAuthor();
            }

            resultBar.setVisible(true);
            resultBar.getItems().clear();
            resultBar.getItems().addAll(resultsSearch);

        } else {
            resultBar.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // lệnh lắng nghe sự thay đổi của thanh searchbar để tực hiện lệnh
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> searchData());
    }
}