package org.example.librarymanagement1;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private String genre;
    private int year;
    private int pages;
    private int available;
    private String imageLink;
    private String description;

    // Constructor
    public Book(int bookId, String title, String author, String genre, int year, int pages, int available, String imageLink, String description) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.pages = pages;
        this.available = available;
        this.imageLink = imageLink;
        this.description = description;
    }

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public int getYear() { return year; }

    public int getPages() {
        return pages;
    }

    public int isAvailable() {
        return available;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getDescription() {
        return description;
    }

    public void setAvailable(int available) {
        this.available = available;
    }
}
