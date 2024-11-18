package org.example.librarymanagement1.backend;

import org.example.librarymanagement1.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    // Phương thức lấy tất cả sách trong cơ sở dữ liệu
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tất cả sách: " + e.getMessage());
        }
        return books;
    }

    // Phương thức lấy danh sách sách mới
    public List<Book> getNewBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books ORDER BY year DESC LIMIT 10";

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách sách mới: " + e.getMessage());
        }
        return books;
    }

    // Phương thức lấy sách hay dựa trên xếp hạng (rating).
    public List<Book> getTopRatedBooks(int topN) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books ORDER BY rating DESC LIMIT ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, topN);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy sách hay: " + e.getMessage());
        }
        return books;
    }

    // Phương thức lấy sách phổ biến dựa trên độ phổ biến (popularity).
    public List<Book> getMostPopularBooks(int topN) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books ORDER BY popularity DESC LIMIT ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, topN);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy sách phổ biến: " + e.getMessage());
        }
        return books;
    }

    // Phương thức thêm sách mới vào cơ sở dữ liệu
    public boolean addBook(Book book) {
        String query = "INSERT INTO books (title, author, genre, year, pages, available, image_link, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getGenre());
            pstmt.setInt(4, book.getYear());
            pstmt.setInt(5, book.getPages());
            pstmt.setBoolean(6, book.isAvailable());
            pstmt.setString(7, book.getImageLink());
            pstmt.setString(8, book.getDescription());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi thêm sách: " + e.getMessage());
            return false;
        }
    }

    // Phương thức xóa sách
    public boolean deleteBook(int bookId) {
        String query = "DELETE FROM books WHERE book_id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa sách: " + e.getMessage());
            return false;
        }
    }

    // Phương thức tìm kiếm sách
    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR genre LIKE ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm sách: " + e.getMessage());
        }
        return books;
    }

    // Phương thức lấy chi tiết sách dựa trên bookId
    public Book getBookDetails(int bookId) {
        Book book = null;
        String query = "SELECT * FROM books WHERE book_id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                book = mapResultSetToBook(rs);  // Phương thức ánh xạ kết quả vào đối tượng Book
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }
    // Phương thức ánh xạ ResultSet sang đối tượng Book
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("book_id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("genre"),
                rs.getInt("year"),
                rs.getInt("pages"),
                rs.getBoolean("available"),
                rs.getString("image_link"),
                rs.getString("description")
        );
    }

    // Phương thức mượn sách
    public boolean borrowBook(int userId, int bookId) {
        // Kiểm tra tính khả dụng của sách
        String checkAvailabilityQuery = "SELECT available FROM books WHERE book_id = ?";
        boolean isAvailable = false;

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(checkAvailabilityQuery)) {

            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                isAvailable = rs.getBoolean("available");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra tình trạng sách: " + e.getMessage());
        }

        if (!isAvailable) {
            System.out.println("Sách không còn khả dụng để mượn.");
            return false;
        }

        // Tiến hành mượn sách
        String query = "INSERT INTO borrowed_books (borrow_date, user_id, book_id, status_br) VALUES (CURRENT_DATE, ?, ?, 'đang mượn')";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi mượn sách: " + e.getMessage());
            return false;
        }
    }


    // Phương thức trả sách
    public boolean returnBook(int userId, int bookId) {
        String query = "UPDATE borrowed_books SET return_date = CURRENT_DATE, status_br = 'đã trả' WHERE user_id = ? AND book_id = ? AND status_br = 'đang mượn'";
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi trả sách: " + e.getMessage());
            return false;
        }
    }
}
