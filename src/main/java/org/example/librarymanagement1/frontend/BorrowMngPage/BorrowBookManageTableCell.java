package org.example.librarymanagement1.frontend.BorrowMngPage;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.librarymanagement1.Book;
import org.example.librarymanagement1.BorrowedBook;
import org.example.librarymanagement1.User;
import org.example.librarymanagement1.backend.BookService;
import org.example.librarymanagement1.backend.SetUp;
import org.example.librarymanagement1.backend.UserService;
import org.example.librarymanagement1.frontend.BookManagementPage.BookManagement;
import org.example.librarymanagement1.frontend.Home.HomeController;

public class BorrowBookManageTableCell {
    @FXML
    private HBox cell; // đại diện cho chính fileFXML này

    @FXML
    private Label accountColumn;

    @FXML
    private Label returnColumn;

    @FXML
    private Label bookNameColumn;

    @FXML
    private Label borrowDateColumn;

    @FXML
    private Button returnBookButton;

    @FXML
    private Label userNameColumn;


    BorrowedBook currentBorrowBook = null;

    BookService bookService = new BookService();

    public void setAccountColumn(String accountColumn) {
        this.accountColumn.setText(accountColumn);
    }

    public void setBookNameColumn(String bookNameColumn) {
        this.bookNameColumn.setText(bookNameColumn);
    }

    public void setBorrowDateColumn(String borrowDateColumn) {
        this.borrowDateColumn.setText(borrowDateColumn);
    }

    public void setUserNameColumn(String userNameColumn) {
        this.userNameColumn.setText(userNameColumn);
    }

    @FXML
    public void returnBorrowBook() {
        if (bookService.returnBook(currentBorrowBook.getAccount(), currentBorrowBook.getBookName())) {
            Node parent = cell.getParent();
            if (parent instanceof VBox) {
                BookManagement bookManagement = SetUp.bookManageLoader.getController();
                bookManagement.setBookList();
                bookManagement.resetBookTable();
                VBox table = (VBox) parent;
                table.getChildren().remove(cell);
                System.out.println("Return successfully!");
            }
        } else {
            System.out.println("Return failed!");
        }
    }

    public void setDetails(BorrowedBook borrowedBook) {
        setUpData(borrowedBook);
        setAccountColumn(borrowedBook.getAccount());
        setBookNameColumn(borrowedBook.getBookName());
        setBorrowDateColumn(String.valueOf(borrowedBook.getBorrowDate()));
        setUserNameColumn(borrowedBook.getUsername());
    }

    public void setUpData(BorrowedBook borrowedBook) {
        this.currentBorrowBook = borrowedBook;
    }

    public void setValueBorrowBookCell() {
        userNameColumn.setText(currentBorrowBook.getUsername());
        bookNameColumn.setText(currentBorrowBook.getBookName());
        accountColumn.setText(currentBorrowBook.getAccount());
        borrowDateColumn.setText(String.valueOf(currentBorrowBook.getBorrowDate()));
        returnColumn.setText(String.valueOf(currentBorrowBook.getReturnDate()));
    }

}
