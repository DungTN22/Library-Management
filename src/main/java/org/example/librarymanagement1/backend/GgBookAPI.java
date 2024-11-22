package org.example.librarymanagement1.backend;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.librarymanagement1.Book;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import io.github.cdimascio.dotenv.Dotenv;
public class GgBookAPI {

    private static final String GOOGLE_BOOKS_API_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final Dotenv dotenv = Dotenv.load(); // Tự động tải tệp .env
    private static final String API_KEY = dotenv.get("API_KEY");
    // Phương thức để tìm kiếm sách bằng ISBN hoặc tiêu đề
    public Optional<Book> searchBook(String query) {
        try {
            // Tạo URL để gọi Google Books API với API key
            URL url = new URL(GOOGLE_BOOKS_API_URL + query + "&key=" + API_KEY);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // Kiểm tra phản hồi từ API
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                // Đọc dữ liệu JSON trả về từ API
                JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(connection.getInputStream()));
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray items = jsonObject.getAsJsonArray("items");

                if (items != null && items.size() > 0) {
                    // Chỉ lấy thông tin từ cuốn sách đầu tiên trong danh sách
                    JsonObject volumeInfo = items.get(0).getAsJsonObject().getAsJsonObject("volumeInfo");

                    // Ánh xạ dữ liệu JSON thành đối tượng Book
                    String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "Unknown";
                    String author = volumeInfo.has("authors") ? volumeInfo.getAsJsonArray("authors").get(0).getAsString() : "Unknown";
                    String genre = volumeInfo.has("categories") ? volumeInfo.getAsJsonArray("categories").get(0).getAsString() : "Unknown";
                    int year = volumeInfo.has("publishedDate") ? parseYear(volumeInfo.get("publishedDate").getAsString()) : 0;
                    int pages = volumeInfo.has("pageCount") ? volumeInfo.get("pageCount").getAsInt() : 0;
                    String imageLink = volumeInfo.has("imageLinks") ? volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString() : "";
                    String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "No description available";

                    Book book = new Book(0, title, author, genre, year, pages, 10, imageLink, description);
                    return Optional.of(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();  // Không tìm thấy sách
    }

    public List<Book> searchBookngoai(String query) {
        List<Book> books = new ArrayList<>();
        try {
            // Gửi yêu cầu đến Google Books API với API key
            URL url = new URL(GOOGLE_BOOKS_API_URL + query + "&key=" + API_KEY);
            HttpURLConnection connectionAPI = (HttpURLConnection) url.openConnection();
            connectionAPI.setRequestMethod("GET");
            connectionAPI.connect();

            // Kiểm tra phản hồi từ API
            int responseCode = connectionAPI.getResponseCode();
            if (responseCode == 200) {
                // Đọc dữ liệu JSON trả về từ API
                JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(connectionAPI.getInputStream()));
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonArray items = jsonObject.getAsJsonArray("items");

                if (items != null && items.size() > 0) {
                    int count = 0; // Biến đếm để kiểm soát số lượng sách được thêm
                    for (JsonElement item : items) {
                        if (count >= 10) break; // Dừng khi đã thêm 10 cuốn sách

                        JsonObject volumeInfo = item.getAsJsonObject().getAsJsonObject("volumeInfo");

                        // Lấy thông tin sách từ JSON
                        String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "Unknown";
                        String author = volumeInfo.has("authors") ? volumeInfo.getAsJsonArray("authors").get(0).getAsString() : "Unknown";
                        String genre = volumeInfo.has("categories") ? volumeInfo.getAsJsonArray("categories").get(0).getAsString() : "Unknown";
                        int year = volumeInfo.has("publishedDate") ? parseYear(volumeInfo.get("publishedDate").getAsString()) : 0;
                        int pages = volumeInfo.has("pageCount") ? volumeInfo.get("pageCount").getAsInt() : 0;
                        String imageLink = volumeInfo.has("imageLinks") ? volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString() : "";
                        String description = volumeInfo.has("description") ? volumeInfo.get("description").getAsString() : "No description available";

                        // Thêm sách vào danh sách
                        books.add(new Book(0,title, author, genre, year, pages,10, imageLink, description));

                        count++; // Tăng biến đếm
                    }
                }
            } else {
                System.out.println("Failed to fetch data from Google Books API. Response code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }



    // Phương thức để phân tích năm từ chuỗi "publishedDate"
    private int parseYear(String publishedDate) {
        try {
            return Integer.parseInt(publishedDate.substring(0, 4));
        } catch (Exception e) {
            return 0;
        }
    }

    // Phương thức thêm sách vào cơ sở dữ liệu book trong
    private void insertBookIntoDatabase(Connection connection, String title, String author, String genre, int year,
                                        int pages, boolean available, String imageLink, String description,
                                        float rating, int popularity) {
        // Kiểm tra xem sách đã tồn tại chưa
        String checkSql = "SELECT COUNT(*) FROM books WHERE title = ? AND author = ?";
        String insertSql = "INSERT INTO books (title, author, genre, year, pages, available, image_link, description, rating, popularity) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
            // Gán giá trị cho câu lệnh kiểm tra
            checkStatement.setString(1, title);
            checkStatement.setString(2, author);

            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    // Nếu sách đã tồn tại, không thực hiện thêm
                    System.out.println("Book already exists: " + title);
                    return;
                }
            }

            // Nếu sách chưa tồn tại, thực hiện chèn
            try (PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {
                insertStatement.setString(1, title);
                insertStatement.setString(2, author);
                insertStatement.setString(3, genre);
                insertStatement.setInt(4, year);
                insertStatement.setInt(5, pages);
                insertStatement.setBoolean(6, available);
                insertStatement.setString(7, imageLink);
                insertStatement.setString(8, description);
                insertStatement.setFloat(9, rating);
                insertStatement.setInt(10, popularity);

                // Thực thi câu lệnh SQL
                int rowsAffected = insertStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Book inserted successfully: " + title);
                } else {
                    System.out.println("Failed to insert book: " + title);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
