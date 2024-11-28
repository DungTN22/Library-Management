package org.example.librarymanagement1.backend;

import org.example.librarymanagement1.User;
import org.example.librarymanagement1.backend.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    // Phương thức lấy tất cả người dùng trong cơ sở dữ liệu
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection conn = Database.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tất cả người dùng: " + e.getMessage());
        }
        return users;
    }

    // Phương thức thêm người dùng mới vào cơ sở dữ liệu
    public boolean addUser(User user) {
        String query = "INSERT INTO users (name, email, phone, account, password, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getAccount());
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, user.getStatus());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm người dùng: " + e.getMessage());
            return false;
        }
    }

    // Phương thức xóa người dùng dựa trên user_id
    public boolean deleteUser(int userId) {
        String query = "DELETE FROM users WHERE user_id = ?";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa người dùng: " + e.getMessage());
            return false;
        }
    }

    // Phương thức cập nhật thông tin người dùng
    public boolean updateUser(User user) {
        String query = "UPDATE users " +
                "SET name = ?, email = ?, phone = ?, account = ?, password = ?, status = ? " +
                "WHERE user_id = ?;";
        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getAccount());
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, user.getStatus());
            pstmt.setInt(7, user.getUserId());

            pstmt.executeUpdate();
            return true; // Trả về true nếu cập nhật thành công
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật người dùng: " + e.getMessage());
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }

    // phương thức ấy ra Id cao nhất phục vụ cho thêm sách
    public int getLastId() {
        String query = "SELECT MAX(user_id) as maxID FROM users;";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("maxID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Phương thức lấy chi tiết người dùng dựa trên user_id
    public User getUserDetails(int userId) {
        User user = null;
        String query = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thông tin chi tiết người dùng: " + e.getMessage());
        }
        return user;
    }

    // Phương thức tìm kiếm người dùng
    public List<User> searchUser(String keyword) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE name LIKE ? OR email LIKE ? OR account LIKE ?";

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs)); // Ánh xạ từ ResultSet sang User
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm người dùng: " + e.getMessage());
        }
        return users;
    }

    // Phương thức ánh xạ ResultSet sang đối tượng User
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("account"),
                rs.getString("password"),
                rs.getString("status")
        );
    }
}