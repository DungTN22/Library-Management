package org.example.librarymanagement1.backend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.librarymanagement1.Book;
import org.example.librarymanagement1.HelloApplication;

public class SetUp {
    public static FXMLLoader homeLoader = new FXMLLoader(Book.class.getResource("Home.fxml"));
    public static FXMLLoader searchLoader = new FXMLLoader(Book.class.getResource("Search.fxml"));
    public static FXMLLoader bookManageLoader = new FXMLLoader(Book.class.getResource("BookManagement.fxml"));
    public static FXMLLoader repairAddLoader = new FXMLLoader(Book.class.getResource("BookRepairAndAdd.fxml"));
    public static FXMLLoader allBookSameTypeLoader = new FXMLLoader(Book.class.getResource("BookTypePage.fxml"));
    public static FXMLLoader borrowBookLoader = new FXMLLoader(Book.class.getResource("BorrowBookPage.fxml"));
    public static FXMLLoader userLoader = new FXMLLoader(Book.class.getResource("UserManagement.fxml"));
    public static FXMLLoader editAddLoader = new FXMLLoader(Book.class.getResource("EditAndAddUser.fxml"));
    public static FXMLLoader userDetailsLoader = new FXMLLoader(Book.class.getResource("UserDetails.fxml"));
    public static FXMLLoader bookDetailsLoader = new FXMLLoader(Book.class.getResource("BookDetails.fxml"));
    public static FXMLLoader borrowBookPageLoader = new FXMLLoader(Book.class.getResource("BorrowBookTypePage.fxml"));

    public static Scene homeScene;
    public static Scene searchScene;
    public static Scene bookManageScene;
    public static Scene repairAddBookScene;
    public static Scene allBookSameTypeScene;
    public static Scene borrowBookManageScene;
    public static Scene userScene;
    public static Scene editAddUserScene;
    public static Scene userDetailsScene;
    public static Scene bookDetailsScene;
    public static Scene borrowBookPageScene;

    public static Thread loadPageBookThread = null;
    public static Thread loadBookManageTableThread = null;
    public static Thread loadUserManageTableThread = null;

    public static Stage newStage;
}
