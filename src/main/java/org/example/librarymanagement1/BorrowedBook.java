package org.example.librarymanagement1;

import java.util.Date;

public class BorrowedBook {
    private Date borrowDate;
    private Date returnDate;
    private String status;
    private int userId;
    private int bookId;

    // Constructor
    public BorrowedBook(Date borrowDate, Date returnDate, String status, int userId, int bookId) {
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
        this.userId = userId;
        this.bookId = bookId;
    }

    // Getters and Setters
    public Date getBorrowDate() {
        return borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public String getStatus() {
        return status;
    }

    public int getUserId() {
        return userId;
    }

    public int getBookId() {
        return bookId;
    }
}
