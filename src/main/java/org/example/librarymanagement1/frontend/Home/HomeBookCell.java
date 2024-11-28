package org.example.librarymanagement1.frontend.Home;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.example.librarymanagement1.Book;
import org.example.librarymanagement1.backend.Images;
import org.example.librarymanagement1.backend.SetUp;
import org.example.librarymanagement1.frontend.BookManagementPage.BookDetails;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeBookCell implements Initializable {
    @FXML
    private ImageView imageBook;

    Image image = null;

    private Book currebtBook = null;

    public void setImageForBook() {
        imageBook.setImage(image);
    }

    public void setCurrebtBook(Book currebtBook) {
        this.currebtBook = currebtBook;
        image = Images.setImage(currebtBook, 1000, 1500);
    }

    public void setupImageViewClickEvent(ImageView imageView) {
        // Gán sự kiện nhấn chuột vào ImageView
        imageView.setOnMouseClicked(event -> {
            System.out.println("ImageView clicked!");

            try {
                // Lấy controller của UserDetails
                BookDetails bookDetailsController = SetUp.bookDetailsLoader.getController();

                if (bookDetailsController == null) {
                    System.out.println("Error: BookDetails controller is null.");
                    return;
                }

                // Truyền dữ liệu người dùng vào trang chi tiết
                bookDetailsController.setBookDetails(
                        currebtBook.getBookId(),
                        currebtBook.getTitle(),
                        currebtBook.getAuthor(),
                        currebtBook.getGenre(),
                        currebtBook.getYear(),
                        currebtBook.getPages(),
                        currebtBook.isAvailable(),
                        currebtBook.getImageLink(),
                        currebtBook.getDescription()
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
        setupImageViewClickEvent(imageBook);
    }
}