package com.canoo.library.persistence.repository;

import com.canoo.library.model.Book;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InMemoryBookRepository implements BookRepository {

    private static final List<Book> books = new ArrayList<>();

    public InMemoryBookRepository(){
        books.add(new Book("The Lord if the Rings", "", LocalDate.now(), "",""));
        books.add(new Book("The Hitchiker's guide to the Galaxy", "", LocalDate.now(), "",""));
        books.add(new Book("1984", "", LocalDate.now(), "",""));
        books.add(new Book("Catch-22", "", LocalDate.now(), "",""));
        books.add(new Book("The Shining", "", LocalDate.now(), "",""));
        books.add(new Book("A Storm of Swords", "", LocalDate.now(), "",""));
        books.add(new Book("Calvin and Hobbes", "", LocalDate.now(), "",""));

    }

    @Override
    public Iterable<Book> findById(Long id) {
        return null;
    }

    @Override
    public Iterable<Book> findByTitle(String title) {
        return null;
    }

    @Override
    public Iterable<Book> findByAuthor(String author) {
        return null;
    }

    @Override
    public Iterable<Book> findByPublicationDate(LocalDate from, LocalDate to) {
        return null;
    }

    @Override
    public Iterable<Book> searchBasedOnText(String text) {
        return null;
    }

    @Override
    public Iterable<Book> findByISBN(String ISBN) {
        return null;
    }

    @Override
    public Iterable<Book>
    findAll() {
        return null;
    }
}
