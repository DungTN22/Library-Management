package org.example.librarymanagement1.frontend.UserMngPage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.librarymanagement1.User;
import org.example.librarymanagement1.backend.SetUp;
import org.example.librarymanagement1.backend.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserManagement implements Initializable {
    @FXML
    private VBox table;

    @FXML
    private ScrollPane userCellScrollPane;

    @FXML
    private TextField searchUserBar;

    private List<User> userList = null;

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
        SetUp.newStage.setScene(SetUp.userScene);
    }

    @FXML
    public void goToBorrowManagePage() throws IOException {
        SetUp.newStage.setScene(SetUp.borrowBookManageScene);
    }

    @FXML
    public void addUserPage() throws IOException {
        UserEditAndAdd add = SetUp.editAddLoader.getController();
        add.setType(0);
        add.clearDataInField();
        SetUp.newStage.setScene(SetUp.editAddUserScene);
    }

    private int totalData = 0;

    private void setBookList(String key) {
        this.userList = userService.searchUser(key);
    }

    UserService userService = new UserService();

    private void setUserList() {
        this.userList = userService.getAllUsers();
    }

    private void setUserList(String key) {
        this.userList = userService.searchUser(key);
    }

    /**
     * lấy ra kích thước của tất cả sách có trong database.
     *
     * @return kích thước số lượng sách
     */
    public int getUserListSize() {
        try {
            return userList.size();
        } catch (NullPointerException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /**
     * thêm các cell data sách vào table (tạo table).
     */
    private void createCells(int number) {
        for (int i = 0; i < number; i++) {
            if (totalData + i >= getUserListSize()) {
                totalData = getUserListSize();
                return;
            }
            try {
                addCellsToTable(userList.get(totalData + i));
            } catch (NullPointerException e) {
                System.out.println("UserManagement : " + e.getMessage());
            }
        }
        totalData += number;
    }

    /**
     * tao cell dữ liệu của 1 nguoi dung.
     *
     */
    public void addCellsToTable(User user) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/librarymanagement1/UserManagementTableCell.fxml"));
            HBox hBox = fxmlLoader.load();

            UserManagementCell controller = fxmlLoader.getController();
            Platform.runLater(() -> {
                controller.createNewHbox(user);
                table.getChildren().add(hBox);
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * kiểm tra thanh cuộn của GridBook, khi chạm đến đáy sẽ load thêm dữ liệu (nếu còn)
     */
    private void setupScrollListener() {
        userCellScrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() == 1.0 && !SetUp.loadUserManageTableThread.isAlive()) { // Nếu đã cuộn tới cuối
                loadDataForTable();
            }
            for (int i = 0; i < table.getChildren().size(); i++) {
                Node cell = table.getChildren().get(i);
                cell.setVisible(isNodeVisible(userCellScrollPane, cell));
            }
        });
    }

    public boolean isNodeVisible(ScrollPane scrollPane, Node node) {
        Bounds viewportBounds = scrollPane.getViewportBounds();
        Bounds nodeBounds = node.localToParent(node.getBoundsInLocal());

        double contentOffsetX = scrollPane.getHvalue() * (scrollPane.getContent().getBoundsInLocal().getWidth()
                - viewportBounds.getWidth());
        double contentOffsetY = scrollPane.getVvalue() * (scrollPane.getContent().getBoundsInLocal().getHeight()
                - viewportBounds.getHeight());

        Bounds visibleBounds = new BoundingBox(
                contentOffsetX,
                contentOffsetY,
                viewportBounds.getWidth(),
                viewportBounds.getHeight()
        );

        return visibleBounds.intersects(nodeBounds);
    }

    private void setUpUserManagePage() {
        setUserList();
        resetUserTable();
        setupScrollListener();
    }

    private void loadDataForTable() {
        SetUp.loadUserManageTableThread = new Thread(() -> {
            createCells(30);
        });

        SetUp.loadUserManageTableThread.start();
    }

    public void resetUserTable() {
        Platform.runLater(() -> {
            userCellScrollPane.setVvalue(0); // Reset thanh cuộn
            table.getChildren().clear();    // Xóa tất cả các cell
            if (userList != null && !userList.isEmpty()) {
                totalData = 0;
                loadDataForTable(); // Load dữ liệu từ danh sách mới
            } else {
                System.out.println("Danh sách người dùng trống!");
            }
        });
    }


    /**
     * kiểm tra sự thay đổi của thanh searchBar
     */
    public void listenSearchChange() {
        searchUserBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (searchUserBar.getText().isEmpty()) {
                setUserList();
            } else {
                setUserList(newValue);
            }
            resetUserTable();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpUserManagePage();
        listenSearchChange();
    }
}