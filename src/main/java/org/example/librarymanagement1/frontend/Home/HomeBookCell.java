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
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupImageViewClickEvent(imageBook);
    }
}