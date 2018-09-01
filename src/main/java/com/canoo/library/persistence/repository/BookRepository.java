package com.canoo.library.persistence.repository;

import com.canoo.library.model.Book;

import java.time.LocalDate;

public interface BookRepository {
    Iterable<Book> findAll();

    Iterable<Book> findById(Long id);

    Iterable<Book> findByTitle(String title);

    Iterable<Book> findByAuthor(String author);

    Iterable<Book> findByPublicationDate(LocalDate from, LocalDate to);

    Iterable<Book> searchBasedOnText(String text); //TODO find more appropriate name

    Iterable<Book> findByISBN(String ISBN);
}
