package org.example.librarymanagement1.backend;

import org.example.librarymanagement1.Book;
import org.example.librarymanagement1.BorrowedBook;

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

    // Phương thức lấy sách hay dựa trên xếp hạng (rating). (topN là so luong ma ban muon no in ra nhe )
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
                book = mapResultSetToBook(rs);
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
    public boolean borrowBook(String account, String bookName) {
        // Kiểm tra số lượng sách còn lại
        String checkAvailabilityQuery = "SELECT available FROM books WHERE book_name = ?";
        int availableBooks = 0;

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(checkAvailabilityQuery)) {

            pstmt.setString(1, bookName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                availableBooks = rs.getInt("available");
            } else {
                System.out.println("Sách không tồn tại.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra số lượng sách: " + e.getMessage());
            return false;
        }

        if (availableBooks <= 0) {
            System.out.println("Sách không còn khả dụng để mượn.");
            return false;
        }

        // Lấy username từ bảng users dựa trên account
        String getUsernameQuery = "SELECT username FROM users WHERE account = ?";
        String username = null;

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(getUsernameQuery)) {

            pstmt.setString(1, account);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                username = rs.getString("username");
            } else {
                System.out.println("Tài khoản không tồn tại.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thông tin người dùng: " + e.getMessage());
            return false;
        }

        // Tiến hành mượn sách và cập nhật số lượng sách
        String insertBorrowQuery = "INSERT INTO borrowed_books (borrow_date, account, book_name, username) VALUES (CURRENT_DATE, ?, ?, ?)";
        String updateBookQuery = "UPDATE books SET available = available - 1 WHERE book_name = ?";

        try (Connection conn = Database.connect();
             PreparedStatement insertStmt = conn.prepareStatement(insertBorrowQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateBookQuery)) {

            // Thêm bản ghi vào bảng borrowed_books
            insertStmt.setString(1, account);
            insertStmt.setString(2, bookName);
            insertStmt.setString(3, username);
            insertStmt.executeUpdate();

            // Cập nhật số lượng sách
            updateStmt.setString(1, bookName);
            updateStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi khi mượn sách hoặc cập nhật số lượng: " + e.getMessage());
            return false;
        }
    }



    // Phương thức trả sách
    public boolean returnBook(String account, String bookName) {
        // Truy vấn xóa bản ghi trong bảng borrowed_books
        String deleteBorrowQuery = "DELETE FROM borrowed_books WHERE account = ? AND book_name = ?";

        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(deleteBorrowQuery)) {

            pstmt.setString(1, account);
            pstmt.setString(2, bookName);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Tăng lại số lượng sách trong bảng books
                String updateBookQuery = "UPDATE books SET available = available + 1 WHERE book_name = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateBookQuery)) {
                    updateStmt.setString(1, bookName);
                    updateStmt.executeUpdate();
                }
                return true;
            } else {
                System.out.println("Không tìm thấy bản ghi để trả sách.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi trả sách: " + e.getMessage());
            return false;
        }
    }

    public List<BorrowedBook> getAllBorrowedBooks() {
        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        String query = "SELECT borrow_date, account, book_name, username FROM borrowed_books";

        try (Connection conn = Database.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Lấy dữ liệu từ ResultSet và tạo đối tượng BorrowedBook
                Date borrowDate = rs.getDate("borrow_date");
                String account = rs.getString("account");
                String bookName = rs.getString("book_name");
                String username = rs.getString("username");

                // Thêm đối tượng BorrowedBook vào danh sách
                borrowedBooks.add(new BorrowedBook(borrowDate, account, bookName, username));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tất cả sách đã mượn: " + e.getMessage());
        }
        return borrowedBooks;
    }



}
