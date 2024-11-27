package org.example.librarymanagement1;

import org.example.librarymanagement1.backend.BookService;
import org.example.librarymanagement1.backend.UserService;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JunitTest {
    BookService bookService = new BookService();
    UserService userService = new UserService();

    @Test
    public void testSearchBook() {
        List<Book> books = new ArrayList<>();
        Book book1 = new Book(185, "100 Must-read Fantasy Novels",
                "Nick Rennison", "Language Arts & Disciplines",
                2009, 209, 1,
                "http://books.google.com/books/content?" +
                        "id=dGs8CwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
                "Fantasy is one of the most visible genres in popular culture" +
                        " - we see the creation of magical and " +
                        "imagined worlds and characters " +
                        "in every type of media, with very strong fan bases in tow. " +
                        "This latest guide in the successful " +
                        "Bloomsbury Must-Read series " +
                        "covers work from a wide range of authors: Tolkien, Philip Pullman, " +
                        "Terry Pratchett, Michael Moorcock, " +
                        "Rudyard Kipling and C.S Lewis to very " +
                        "contemporary writers such as Garth " +
                        "Nix and Steven Erikson. If you want to " +
                        "expand your range of reading or deepen " +
                        "your understanding of this genre, " +
                        "this is the best place to start."
        );
        Book book2 = new Book(55, "200 Words to Help You Talk about Philosophy",
                "Anja Steinbauer", "Philosophy",
                2022, 128, 1,
                "http://books.google.com/books" +
                        "/content?id=CpRtEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
                "Have you have ever felt at a disadvantage when joining in " +
                        "a conversation on a subject that you aren\'t confident about? " +
                        "If yes, this new book series is for you. " +
                        "Each book features definitions " +
                        "of two hundred words frequently used to " +
                        "describe and discuss a smart subject. " +
                        "200 Words to Help You Talk About Philosophy is designed to demystify " +
                        "jargon-based philosophic language and make " +
                        "you at ease holding a conversation on the " +
                        "topic. Philosophy can be baffling, as well as " +
                        "fascinating, to the best of us. " +
                        "Let Anja Steinbauer guide you through doubt, " +
                        "dialectic, Dao, and much more. " +
                        "The book is written with digestible text enabling a quick and " +
                        "easy understanding of various topics while broadening your " +
                        "philosophical vocabulary. 200 Words to Help You Talk About Philosophy " +
                        "is one of two new titles beginning a series of smart subjects, " +
                        "also including art, psychology, and music."
        );
        books.add(book1);
        books.add(book2);
        assertEquals(books,bookService.searchBooks("a", 2));
    }

    @Test
    public void testGetBookDetail1() {
        Book book = new Book(185, "100 Must-read Fantasy Novels",
                "Nick Rennison", "Language Arts & Disciplines",
                2009, 209, 1,
                "http://books.google.com/books/content?" +
                        "id=dGs8CwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
                "Fantasy is one of the most visible genres in popular culture" +
                        " - we see the creation of magical and " +
                        "imagined worlds and characters " +
                        "in every type of media, with very strong fan bases in tow. " +
                        "This latest guide in the successful " +
                        "Bloomsbury Must-Read series " +
                        "covers work from a wide range of authors: Tolkien, Philip Pullman, " +
                        "Terry Pratchett, Michael Moorcock, " +
                        "Rudyard Kipling and C.S Lewis to very " +
                        "contemporary writers such as Garth " +
                        "Nix and Steven Erikson. If you want to " +
                        "expand your range of reading or deepen " +
                        "your understanding of this genre, " +
                        "this is the best place to start."
        );
        assertEquals(book, bookService.getBookDetails(185));
    }

    @Test
    public void testGetBookDetail2() {
        Book book = new Book(55, "200 Words to Help You Talk about Philosophy",
                "Anja Steinbauer", "Philosophy",
                2022, 128, 1,
                "http://books.google.com/books" +
                        "/content?id=CpRtEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api",
                "Have you have ever felt at a disadvantage when joining in " +
                        "a conversation on a subject that you aren\'t confident about? " +
                        "If yes, this new book series is for you. " +
                        "Each book features definitions " +
                        "of two hundred words frequently used to " +
                        "describe and discuss a smart subject. " +
                        "200 Words to Help You Talk About Philosophy is designed to demystify " +
                        "jargon-based philosophic language and make " +
                        "you at ease holding a conversation on the " +
                        "topic. Philosophy can be baffling, as well as " +
                        "fascinating, to the best of us. " +
                        "Let Anja Steinbauer guide you through doubt, " +
                        "dialectic, Dao, and much more. " +
                        "The book is written with digestible text enabling a quick and " +
                        "easy understanding of various topics while broadening your " +
                        "philosophical vocabulary. 200 Words to Help You Talk About Philosophy " +
                        "is one of two new titles beginning a series of smart subjects, " +
                        "also including art, psychology, and music."
        );
        assertEquals(book, bookService.getBookDetails(55));
    }

    @Test
    public void testGetUserDetail1() {
        User thisUser = new User(1, "Alice Johnson", "alice@example.com",
        "1234567890", "alice_j", "pass123", "active"
        );
        assertEquals(thisUser, userService.getUserDetails(1));
    }

    @Test
    public void testGetUserDetail2() {
        User thisUser = new User(3, "Carol Lee", "carol@example.com",
                "3456789012", "carol_l", "pass123", "inactive"
        );
        assertEquals(thisUser, userService.getUserDetails(3));
    }
}
