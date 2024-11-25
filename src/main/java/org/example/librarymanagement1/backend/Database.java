package org.example.librarymanagement1.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    // Thông tin kết nối cơ sở dữ liệu MySQL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root123";

    // Khởi tạo kết nối
    public static Connection connect() {
        Connection connection = null;
        try {
            // Đăng ký MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver"); // here

            // Tạo kết nối
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Kết nối MySQL thành công!");
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy Driver MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Kết nối MySQL thất bại: " + e.getMessage());
        }
        return connection;
    }

    // Tạo bảng nếu chưa có
    public static void createTables() {
        String createBooksTable = "CREATE TABLE IF NOT EXISTS books ("
                + "book_id INT AUTO_INCREMENT PRIMARY KEY,"
                + "title VARCHAR(255) NOT NULL,"
                + "author VARCHAR(255),"
                + "genre VARCHAR(255) NOT NULL,"
                + "year INT,"
                + "pages INT,"
                + "available BOOLEAN NOT NULL,"
                + "image_link VARCHAR(255),"
                + "description TEXT"
                + ")";

        String createUsersTable = "CREATE TABLE IF NOT EXISTS users ("
                + "user_id INT AUTO_INCREMENT PRIMARY KEY,"
                + "name VARCHAR(255) NOT NULL,"
                + "email VARCHAR(255) NOT NULL,"
                + "phone VARCHAR(15) NOT NULL,"
                + "account VARCHAR(255),"
                + "password VARCHAR(255),"
                + "status VARCHAR(50)"
                + ")";

        String createBorrowedBooksTable = "CREATE TABLE IF NOT EXISTS borrowed_books ("
                + "borrow_date DATE NOT NULL,"
                + "return_date DATE,"
                + "status_br VARCHAR(50) NOT NULL,"
                + "user_id INT,"
                + "book_id INT,"
                + "PRIMARY KEY (borrow_date, user_id, book_id),"
                + "FOREIGN KEY (user_id) REFERENCES users(user_id),"
                + "FOREIGN KEY (book_id) REFERENCES books(book_id)"
                + ")";

        try (Connection conn = connect()) {
            if (conn != null) {
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(createBooksTable);
                    stmt.execute(createUsersTable);
                    stmt.execute(createBorrowedBooksTable);
                    System.out.println("Các bảng đã được tạo hoặc đã tồn tại.");
                }
            } else {
                System.err.println("Kết nối không thành công. Không thể tạo bảng.");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo bảng: " + e.getMessage());
        }
    }

    // Đóng kết nối
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng tài nguyên: " + e.getMessage());
        }
    }

    public static void close(Connection conn, PreparedStatement pstmt) {
        close(conn, pstmt, null);
    }

    public static void main(String[] args) {
        // Kết nối và tạo bảng
        createTables();
    }
}
