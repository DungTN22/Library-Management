package org.example.librarymanagement1.frontend.SearchPage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.example.librarymanagement1.Book;
import org.example.librarymanagement1.backend.SetUp;
import org.example.librarymanagement1.frontend.BookManagementPage.BookDetails;

import java.net.URL;
import java.util.ResourceBundle;

public class ResultSearchCell implements Initializable {
    @FXML
    private Label cell;

    private int type; // 0 -> odd, 1-> even

    Book currentBook = null;

    public void setBook(Book book) {
        this.currentBook = book;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCell(int mode, Book book) {
        setBook(book);
        setType(mode);
        cell.setText(book.getTitle() + '-' + book.getAuthor());
        System.out.println(type);
        if (type == 0) {
            cell.getStyleClass().remove("search_cell_even");
            cell.getStyleClass().add("search_cell_odd");
        } else {
            cell.getStyleClass().remove("search_cell_odd");
            cell.getStyleClass().add("search_cell_even");
        }
    }

    private void setActionClick() {
        cell.setOnMouseClicked(event -> {
            System.out.println("Label được click!");
            System.out.println(currentBook.getTitle());
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
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setActionClick();
    }
}
