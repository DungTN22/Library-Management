package org.example.librarymanagement1.frontend.UserMngPage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.librarymanagement1.User;
import org.example.librarymanagement1.backend.UserService;
import org.example.librarymanagement1.backend.SetUp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserManagementCell implements Initializable {

    @FXML
    private HBox cell;

    @FXML
    private Label idColumn;

    @FXML
    private Label nameColumn;

    @FXML
    private Label emailColumn;

    @FXML
    private Label accountColumn;

    @FXML
    private AnchorPane actionsColumn;

    private User currentUser = null; // dữ liệu ng dung đang lưu trữ

    UserService userService = new UserService();

    /**
     * xóa dữ liệu của ng dung khỏi database, đồng thời cập nhật lại chương trình thông qua nút xóa.
     */
    @FXML
    public void delThisData() {
        if (userService.deleteUser(currentUser.getUserId())) {
            Node parent = cell.getParent();
            if (parent instanceof VBox) {
                //UserManagementCell user = SetUp.userLoader.getController();
                VBox table = (VBox) parent;
                table.getChildren().remove(cell);
                System.out.println("Clear successfully!");
            }
        }
    }

    @FXML
    public void editUserData() {
        UserManagement userManagement = SetUp.userLoader.getController();
        userManagement.resetUserTable();

        UserEditAndAdd edit = SetUp.editAddLoader.getController();
        edit.setType(1);
        edit.clearDataInField();
        edit.setParent(this);
        edit.setEditUser(currentUser);
        SetUp.newStage.setScene(SetUp.editAddUserScene);
    }
    /*
    @FXML
    public void userDetailsPage() {
        System.out.println("Current user: " + currentUser);

        UserManagement userManagement = SetUp.userLoader.getController();
        userManagement.resetUserTable();

        UserEditAndAdd see = SetUp.userDetailsLoader.getController();
        if (see == null) {
            System.out.println("Error: UserDetails controller is null.");
            return;
        }
        see.clearDataInField();
        see.setParent(this);
        SetUp.newStage.setScene(SetUp.userDetailsScene);
    }
     */

    @FXML
    public void userDetailsPage() {
        if (currentUser == null) {
            System.out.println("No user selected!");
            return;
        }

        try {
            // Lấy controller của UserDetails
            UserDetails userDetailsController = SetUp.userDetailsLoader.getController();

            if (userDetailsController == null) {
                System.out.println("Error: UserDetails controller is null.");
                return;
            }

            // Truyền dữ liệu người dùng vào trang chi tiết
            userDetailsController.setUserDetails(
                    currentUser.getName(),
                    currentUser.getEmail(),
                    currentUser.getAccount(),
                    currentUser.getPhone(),
                    currentUser.getPassword(),
                    currentUser.getStatus()
            );

            // Chuyển sang trang UserDetails
            SetUp.newStage.setScene(SetUp.userDetailsScene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * cập nhật lại giá trị cho cell.
     *
     * @param user Dữ liệu nguoi dung
     */
    public void updateValueUserCell(User user) {
        idColumn.setText(String.valueOf(user.getUserId()));
        nameColumn.setText(String.valueOf(user.getName()));
        accountColumn.setText(String.valueOf(user.getAccount()));
        emailColumn.setText(String.valueOf(user.getEmail()));
    }

    /**
     * tạo ra dữ liệu cho cell mới khi thêm nguoi dung
     *
     * @param user dữ liệu nguoi dung
     */
    public void createNewHbox(User user) {
        updateValueUserCell(user);
        currentUser = user;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}