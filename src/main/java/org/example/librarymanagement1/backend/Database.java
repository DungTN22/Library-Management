package org.example.librarymanagement1.backend;

import java.sql.*;

public class Database {
    // Thông tin kết nối cơ sở dữ liệu MySQL
    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root123";

    // Singleton instance
    private static Database instance;
    private Connection connection;

    // Constructor private để ngăn việc tạo đối tượng từ bên ngoài
    private Database() {
        connect(); // Gọi hàm để tạo kết nối khi khởi tạo
    }

    // Phương thức trả về thể hiện duy nhất của Database
    public static Database getInstance() {
        if (instance == null) {
            synchronized (Database.class) { // Đồng bộ hóa để đảm bảo thread-safe
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    // Tạo hoặc tái tạo kết nối
    private void connect() {
        try {
            if (connection == null || connection.isClosed()) {
                // Đăng ký MySQL JDBC Driver
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Tạo kết nối
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Kết nối MySQL thành công!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy Driver MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Kết nối MySQL thất bại: " + e.getMessage());
            throw new RuntimeException("Failed to connect to database.");
        }
    }

    // Getter để lấy đối tượng Connection
    public Connection getConnection() {
        try {
            // Kiểm tra trạng thái kết nối
            if (connection == null || connection.isClosed()) {
                connect(); // Tái tạo kết nối nếu cần
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra kết nối: " + e.getMessage());
        }
        return connection;
    }

    // Tạo bảng nếu chưa có
    public void createTables() {
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

        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(createBooksTable);
            stmt.execute(createUsersTable);
            stmt.execute(createBorrowedBooksTable);
            System.out.println("Các bảng đã được tạo hoặc đã tồn tại.");
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo bảng: " + e.getMessage());
        }
    }

    // Đóng tài nguyên
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

    // Kiểm tra kết nối và tạo bảng
    public static void main(String[] args) {
        Database db = Database.getInstance();
        db.createTables();
    }
}
