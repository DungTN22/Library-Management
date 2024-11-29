package org.example.librarymanagement1;
import java.util.Objects;

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

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return bookId == book.bookId && year == book.year
                && pages == book.pages
                && Objects.equals(title, book.title)
                && Objects.equals(author, book.author)
                && Objects.equals(genre, book.genre)
                && Objects.equals(imageLink, book.imageLink)
                && Objects.equals(description, book.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, title, author, genre, year, pages, imageLink, description);
    }
}
