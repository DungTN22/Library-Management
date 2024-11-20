package org.example.librarymanagement1;

import java.util.Date;

public class BorrowedBook {
    private Date borrowDate;
    private String account;
    private String bookName;
    private String username;

    // Constructor
    public BorrowedBook(Date borrowDate, String account, String bookName, String username) {
        this.borrowDate = borrowDate;
        this.account = account;
        this.bookName = bookName;
        this.username = username;
    }

    // Getters and Setters
    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
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
}
