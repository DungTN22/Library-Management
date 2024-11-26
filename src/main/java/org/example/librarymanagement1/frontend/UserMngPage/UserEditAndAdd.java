package org.example.librarymanagement1.frontend.UserMngPage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.example.librarymanagement1.User;
import org.example.librarymanagement1.backend.SetUp;
import org.example.librarymanagement1.backend.UserService;
import org.example.librarymanagement1.frontend.Home.HomeController;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class UserEditAndAdd implements Initializable {
    @FXML
    private Label nameNotification;

    @FXML
    private Label buttonNotification;

    @FXML
    private Label phoneNotification;

    @FXML
    private Label emailNotification;

    @FXML
    private Label accountNotification;

    @FXML
    private Label passwordNotification;

    @FXML
    private Button addButton;

    @FXML
    private ImageView imageArea;

    @FXML
    private TextField nameField;

    @FXML
    private TextField accountField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField statusField;

    @FXML
    private Label title;
    private int type = 0; // 0 là add, 1 là repair

    User editUser = null;
    UserService userService = new UserService();
    UserManagementCell parent = null;

    public TextField getNameField() {
        return nameField;
    }

    public TextField getAccountField() {
        return accountField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public TextField getPhoneField() {
        return phoneField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public TextField getStatusField() {
        return statusField;
    }

    public void setNameField(String name) {
        this.nameField.setText(name);
    }

    public void setAccountField(String account) {
        this.accountNotification.setText(account);
    }

    public void setEmailField(String email) {
        this.emailNotification.setText(email);
    }

    public void setPhoneField(String phone) {
        this.phoneNotification.setText(phone);
    }

    public void setPasswordField(String password) {
        this.passwordNotification.setText(password);
    }

    public void setStatusField(String status) {
        this.statusField.setText(status);
    }

    public void setUser(User editUser) {
        this.editUser = editUser;
    }

    public void setParent(UserManagementCell parent) {
        this.parent = parent;
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
        SetUp.newStage.setScene(SetUp.userScene);
    }

    @FXML
    public void goToBorrowManagePage() throws IOException {
        SetUp.newStage.setScene(SetUp.borrowBookManageScene);
    }

    /**
     * Cài đặt loại dùng : type0 => add, type1 => repair.
     *
     * @param type loại
     */
    public void setType(int type) {
        this.type = type;
        setNameButtonAndTitle();
    }

    /**
     * cập nhật tiêu đề và tên cho button theo mỗi type.
     */
    public void setNameButtonAndTitle() {
        if (type == 0) {
            addButton.setText("ADD");
            title.setText("ADD USER");
        } else if (type == 1) {
            addButton.setText("EDIT");
            title.setText("EDIT USER");
        }
    }

    /**
     * thực thi khi bấm nút add/repair để thực hiện add hoặc cập nhật tùy theo type.
     */
    @FXML
    public void addOrEditUser() {
        if (type == 0) {
            addUser();
        } else if (type == 1) {
            editUser();
        }
    }

    /**
     * cài đặt các giá trị cho các text field.
     *
     * @param user dữ liệu ng dung
     */
    public void setEditUser(User user) {
        editUser = user;
        System.out.println(user.getEmail());
        setNameField(user.getName());
        setAccountField(user.getAccount());
        setEmailField(user.getEmail());
        setPhoneField(user.getPhone());
        setPasswordField(user.getPassword());
        setStatusField(user.getStatus());
    }

    /**
     * dọn dẹp dữ liệu.
     */
    public void clearDataInField() {
        setNameField("");
        setAccountField("");
        setEmailField("");
        setPhoneField("");
        setPasswordField("");
        setStatusField("");
    }

    /**
     * thực hiện thêm sách vào dữ liệu
     */
    private void addUser() {
        if (checkInfoUser()) {
            User newUser = new User(userService.getLastId() + 1, nameField.getText(), emailField.getText(),phoneField.getText(),
                    accountField.getText(), passwordField.getText(), "active");
            if (userService.addUser(newUser)) {
                UserManagement userManagement = SetUp.userLoader.getController();
                userManagement.addCellsToTable(newUser);

                HomeController home = SetUp.homeLoader.getController();
                home.reLoadHomePage();

                showNotification(true, buttonNotification, "Add Successfully!");

                return;
            }
        }
        showNotification(false, buttonNotification, "Add Failed!");
    }

    /**
     * cập nhật lại các giá trị cho sách
     */
    private void editUser() {
        if (checkInfoUser()) {
            User newUser = new User(editUser.getUserId(),nameField.getText(),emailField.getText(),phoneField.getText(),
                    accountField.getText(), passwordField.getText(), "active");
            if (userService.updateUser(newUser)) {
                parent.updateValueUserCell(newUser);
                showNotification(true, buttonNotification, "Edit Successfully!");
                return;
            }
        }
        showNotification(false, buttonNotification, "Edit Failed");
    }

    /**
     * kiểm tra thông tin khi cho phép cập nhật hoặc thêm sách
     */
    private boolean checkInfoUser() {
        boolean status = true;
        if (nameField.getText().isEmpty()) {
            showNotification(false, nameNotification, "The name field cannot be null!");
            status = false;
        }
        if (accountField.getText().isEmpty()) {
            showNotification(false, accountNotification, "The account field cannot be null!");
            status = false;
        }
        if (emailField.getText().isEmpty()) {
            showNotification(false, emailNotification, "The email field cannot be null!");
            status = false;
        }
        if (phoneField.getText().isEmpty()) {
            showNotification(false, phoneNotification, "The phone field cannot be null!");
            status = false;
        }
        if (passwordField.getText().isEmpty()) {
            showNotification(false, passwordNotification, "The password field cannot be null!");
            status = false;
        }
        return status;
    }

    /**
     * cài đặt cho thông báo khi hiển thị.
     *
     * @param status trạng thái tốt/xấu
     * @param notification loại thông báo
     * @param alarm tên cảnh báo
     */
    private void showNotification(boolean status, Label notification, String alarm) {
        notification.setText(alarm);
        if (status) {
            notification.setTextFill(Color.GREEN);
        } else {
            notification.setTextFill(Color.RED);
        }

        notification.setVisible(true);

        // Tạo Timeline để ẩn nhãn sau 2 giây
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> notification.setVisible(false)));
        timeline.setCycleCount(1);
        timeline.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
