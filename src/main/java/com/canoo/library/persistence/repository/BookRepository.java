package com.canoo.library.persistence.repository;

import com.canoo.library.model.Book;
import com.canoo.library.model.Genre;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository {
    Iterable<Book> findAll();

    Iterable<Book> findById(Long id);

    Iterable<Book> findByTitle(String title);

    Iterable<Book> findByAuthor(String author);

    Iterable<Book> findByPublicationDate(LocalDate from, LocalDate to);

    Iterable<Book> searchBasedOnDescription(String text);

    Iterable<Book> findByISBN(String ISBN);

    Iterable<Book> findByGenre(List<Genre> genres);
}
