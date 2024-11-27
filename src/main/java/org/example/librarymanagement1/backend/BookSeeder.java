package org.example.librarymanagement1.backend;

import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class BookSeeder {

    public static void main(String[] args) {
        // Kết nối cơ sở dữ liệu
        try (Connection connection = Database.getInstance().getConnection()) {
            GgBookAPI api = new GgBookAPI();

            // Danh sách các từ khóa để tìm kiếm sách
            List<String> keywords = Arrays.asList(
                    "Harry Potter", "Java Programming", "Machine Learning", "Artificial Intelligence", "Data Science",
                    "Software Engineering", "Cybersecurity", "Cloud Computing", "Game Development", "Blockchain",
                    "Astronomy", "Biology", "History", "Philosophy", "Art", "Music", "Physics", "Mathematics",
                    "Economics", "Psychology", "Cooking", "Travel", "Health", "Fitness", "Adventure", "Mystery",
                    "Fantasy", "Romance", "Thriller", "Self-help", "Business", "Leadership", "Programming", "Science Fiction"
            );

            int booksAdded = 0; // Biến đếm số sách đã thêm
            int maxBooks = 200; // Số sách tối đa cần thêm

            for (String keyword : keywords) {
                if (booksAdded >= maxBooks) break; // Dừng nếu đã đạt số lượng yêu cầu

                System.out.println("Searching and adding books for keyword: " + keyword);

                // Gọi API và thêm sách vào cơ sở dữ liệu
                api.searchAndInsertBook(keyword, connection);

                // Cập nhật số sách đã thêm
                booksAdded += 1;
            }

            System.out.println("Total books added: " + booksAdded);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
