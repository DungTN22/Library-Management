package org.example.librarymanagement1.backend;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import org.example.librarymanagement1.Book;

public class Images {
    // hàm cài đặt link tải image và scale theo Book
    public static void setImage(Book book, ImageView imageBook, double newWidth, double newHeight) {
        Image originalImage = null;
        try{
            originalImage = new Image(book.getImageLink(), newWidth, newHeight, false, true);
        } catch (Exception e) {
            System.out.println("Home BookCell : " + e.getMessage());
        }
        if (originalImage != null) {
            imageBook.setImage(originalImage);
        } else {
            Image failLoadImage = new Image(Images.class.getResource("/image/plus.jpg").toExternalForm(),
                    newWidth, newHeight, false, true);
            imageBook.setImage(failLoadImage);
        }
    }

    // hàm cài đặt link tải image và scale theo Link
    public static void setImage(String link, ImageView imageBook, double newWidth, double newHeight) {
        Image originalImage = null;
        try{
            originalImage = new Image(link, newWidth, newHeight, false, true); // Tải ảnh gốc từ đường dẫn hoặc URL
        } catch (Exception e) {
            System.out.println("Images : " + e.getMessage());
        }
        if (originalImage != null) {
            imageBook.setImage(originalImage);
        } else {
            Image failLoadImage = new Image(Images.class.getResource("/image/plus.jpg").toExternalForm(),
                    newWidth, newHeight, false, true);
            imageBook.setImage(failLoadImage);
        }
    }

    // hàm cài đặt link tải image và scale theo Book có trả về
    public static Image setImage(Book book, double newWidth, double newHeight) {
        Image originalImage = null;
        try{
            originalImage = new Image(book.getImageLink(), newWidth, newHeight, false, true);
        } catch (Exception e) {
            System.out.println("Images : " + e.getMessage());
        }
        if (originalImage == null) {
            originalImage = new Image(Images.class.getResource("/image/plus.jpg").toExternalForm(),
                    newWidth, newHeight, false, true);
        }
        return originalImage;
    }

    // hàm cài đặt link tải image và scale theo link có trả về
    public static Image setImage(String link, double newWidth, double newHeight) {
        Image originalImage = null;
        try{
            originalImage = new Image(link, newWidth, newHeight, false, true); // Tải ảnh gốc từ đường dẫn hoặc URL
        } catch (Exception e) {
            System.out.println("Images : " + e.getMessage());
        }
        if (originalImage == null) {
            originalImage = new Image(Images.class.getResource("/image/plus.jpg").toExternalForm(),
                    newWidth, newHeight, false, true);
        }
        return originalImage;
    }
}
