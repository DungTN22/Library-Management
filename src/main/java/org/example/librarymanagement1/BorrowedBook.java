package org.example.librarymanagement1;

import java.time.LocalDate;
import java.util.Date;

public class BorrowedBook {
    private Date borrowDate;
    private String account;
    private String bookName;
    private String username;
    private Date returnDate;

    // Constructor
    public BorrowedBook(Date borrowDate, String account, String bookName, String username) {
        this.borrowDate = borrowDate;
        this.account = account;
        this.bookName = bookName;
        this.username = username;

        // Tính toán returnDate tự động: borrowDate + 14 ngày
        this.returnDate = calculateReturnDate(borrowDate);
    }

    // Phương thức tính toán returnDate tự động (borrowDate + 14 ngày)
    private Date calculateReturnDate(Date borrowDate) {
        // Chuyển borrowDate sang LocalDate
        LocalDate borrowLocalDate = new java.sql.Date(borrowDate.getTime()).toLocalDate();

        // Cộng thêm 14 ngày
        LocalDate returnLocalDate = borrowLocalDate.plusDays(14);

        // Chuyển returnLocalDate trở lại thành Date
        return java.sql.Date.valueOf(returnLocalDate);
    }

    // Getters and Setters
    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
        // Cập nhật lại returnDate khi borrowDate thay đổi
        this.returnDate = calculateReturnDate(borrowDate);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}
