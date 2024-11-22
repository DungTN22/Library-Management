package org.example.librarymanagement1.frontend.Other;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.example.librarymanagement1.Book;
import org.example.librarymanagement1.backend.Images;

import java.net.URL;
import java.util.ResourceBundle;

public class BookTypePageCell implements Initializable {

    @FXML
    private ImageView imageBook;

    @FXML
    private Label nameBook;

    Image image = null;

    private Book currentBook = null;

    public void setImageForBook() {
        this.imageBook.setImage(image);
    }

    public void setCurrentBook(Book book) {
        this.currentBook = book;
        image = Images.setImage(this.currentBook, 1000, 1600);
        nameBook.setText(book.getTitle());
    }

    public void setupImageViewClickEvent(ImageView imageView) {
        // Gán sự kiện nhấn chuột vào ImageView
        imageView.setOnMouseClicked(event -> {
            System.out.println("ImageView clicked!");
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupImageViewClickEvent(imageBook);
    }
}
