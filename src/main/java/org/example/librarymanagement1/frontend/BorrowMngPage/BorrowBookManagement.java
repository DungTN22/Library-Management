package org.example.librarymanagement1.frontend.BorrowMngPage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.librarymanagement1.BorrowedBook;
import org.example.librarymanagement1.backend.BookService;
import org.example.librarymanagement1.backend.SetUp;
import org.example.librarymanagement1.frontend.BookManagementPage.BookManagement;
import org.example.librarymanagement1.frontend.BookManagementPage.BookManagementTableCell;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BorrowBookManagement implements Initializable {
    @FXML
    private VBox table;

    @FXML
    private TextField searchBorrowBookBar;

    @FXML
    private ScrollPane borrowCellScrollPane;

    private int totalData = 0;

    List<BorrowedBook> borrowedBookList = new ArrayList<>();

    BookService bookService = new BookService();

    public void setBorrowedBookList() {
        this.borrowedBookList = bookService.getAllBorrowedBooks();
    }

    public void setBorrowedBookList(String key) {
        this.borrowedBookList = bookService.getBorrowedBooks(key);
    }

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

    }

    @FXML
    public void goToBorrowManagePage() throws IOException {
        SetUp.newStage.setScene(SetUp.borrowBookManageScene);
    }

    private void createCells(int number) {
        for (int i = 0; i < number; i++) {
            if (totalData + i >= borrowedBookList.size()) {
                totalData = borrowedBookList.size();
                return;
            }
            try {
                loadDataToCell(borrowedBookList.get(totalData + i));
            } catch (NullPointerException e) {
                System.out.println("BookManagement : " + e.getMessage());
            }
        }
        totalData += number;
    }

    public void loadDataToCell(BorrowedBook borrowedBook) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/librarymanagement1/BorrowBookCell.fxml"));
            HBox hBox = fxmlLoader.load();

            BorrowBookManageTableCell controller = fxmlLoader.getController();
            controller.setUpData(borrowedBook);

            Platform.runLater(() -> {
                controller.setValueBorrowBookCell();
                table.getChildren().add(hBox);
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void setupScrollListener() {
        borrowCellScrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() == 1.0 && !SetUp.loadBookManageTableThread.isAlive()) { // Nếu đã cuộn tới cuối
                loadDataForTable();
            }
            for (int i = 0; i < table.getChildren().size(); i++) {
                Node cell = table.getChildren().get(i);
                BookManagement bookManagement = new BookManagement();
                cell.setVisible(bookManagement.isNodeVisible(borrowCellScrollPane, cell));
            }
        });
    }

    private void loadDataForTable() {
        SetUp.loadBookManageTableThread = new Thread(() -> {
            createCells(30);
        });

        SetUp.loadBookManageTableThread.start();
    }

    public void resetBookTable() {
        table.getChildren().clear();
        borrowCellScrollPane.setVvalue(0);
        loadDataForTable();
        totalData = 0;
    }

    private void setUpBookManagePage() {
        setBorrowedBookList();
        resetBookTable();
        setupScrollListener();
    }

    public void listenSearchChange() {
        searchBorrowBookBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (searchBorrowBookBar.getText().isEmpty()) {
                setBorrowedBookList();
            } else {
                setBorrowedBookList(searchBorrowBookBar.getText());
            }
            resetBookTable();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpBookManagePage();
        listenSearchChange();
    }
}
