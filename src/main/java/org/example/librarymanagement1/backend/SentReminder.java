package org.example.librarymanagement1.backend;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class SentReminder {

    // Cấu hình thông tin gửi email
    private static final Dotenv dotenv = Dotenv.load();
    private static final String EMAIL_USERNAME = dotenv.get("LIBRARY_EMAIL");
    private static final String EMAIL_PASSWORD = dotenv.get("LIBRARY_PASSWORD");

    public static void main(String[] args) {
        checkAndSendReminders();
    }

    /**
     * Kiểm tra hạn trả sách và gửi nhắc nhở đến người dùng
     */
    public static void checkAndSendReminders() {
        String query = """
            SELECT bb.borrow_id, u.email, bb.book_name, bb.return_date
            FROM borrowed_books bb
            JOIN users u ON bb.account = u.account
            WHERE bb.return_date = CURDATE() + INTERVAL 2 DAY
            AND bb.reminder_sent = 0
        """;

        try (Connection conn = Database.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int borrowId = rs.getInt("borrow_id");
                String userEmail = rs.getString("email");
                String bookName = rs.getString("book_name");
                LocalDate returnDate = rs.getDate("return_date").toLocalDate();

                if (userEmail != null && !userEmail.isEmpty()) {
                    // Gửi email nhắc nhở nếu địa chỉ email hợp lệ
                    sendEmail(userEmail, bookName, returnDate);

                    // Cập nhật trạng thái đã gửi nhắc nhở
                    markReminderSent(conn, borrowId);
                } else {
                    System.out.println("Email không hợp lệ cho mượn sách ID: " + borrowId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi truy vấn dữ liệu: " + e.getMessage());
        }
    }

    /**
     * Gửi email nhắc nhở trả sách
     */
    private static void sendEmail(String email, String bookName, LocalDate returnDate) {
        // Kiểm tra nếu email là null hoặc chuỗi rỗng
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Email không hợp lệ: " + email);
            return;  // Dừng việc gửi email nếu email không hợp lệ
        }

        // Cấu hình SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Xác thực tài khoản email
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_USERNAME, EMAIL_PASSWORD);
            }
        });

        try {
            // Tạo email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Nhắc nhở trả sách thư viện");
            message.setText(String.format("Xin chào,\n\nBạn đã mượn cuốn sách '%s'. Ngày hết hạn trả sách là %s.\n\nVui lòng trả sách đúng hạn để tránh phí phạt.\n\nCảm ơn!",
                    bookName, returnDate));

            // Gửi email
            Transport.send(message);
            System.out.println("Đã gửi nhắc nhở đến: " + email);
        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi email đến " + email + ": " + e.getMessage());
        }
    }


    /**
     * Cập nhật trạng thái đã gửi nhắc nhở trong cơ sở dữ liệu
     */
    private static void markReminderSent(Connection conn, int borrowId) {
        String updateQuery = "UPDATE borrowed_books SET reminder_sent = 1 WHERE borrow_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(updateQuery)) {
            ps.setInt(1, borrowId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cập nhật trạng thái reminder_sent cho mượn sách ID: " + borrowId);
            } else {
                System.out.println("Không tìm thấy mượn sách ID: " + borrowId);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật trạng thái reminder_sent: " + e.getMessage());
        }
    }

}
