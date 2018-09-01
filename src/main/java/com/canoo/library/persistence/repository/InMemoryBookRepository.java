package com.canoo.library.persistence.repository;

import com.canoo.library.model.Book;
import com.canoo.library.model.Genre;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryBookRepository implements BookRepository {

    private final List<Book> books = new ArrayList<>();
    private static final double TEXT_SEARCH_THRESHOLD = 0.75;

    public InMemoryBookRepository(Iterable<Book> books){

        books.forEach(this.books::add);
    }

    @Override
    public Iterable<Book> findById(Long id) {
        return books.stream().filter(book -> book.getId().equals(id)).collect(Collectors.toList());
    }

    @Override
    public Iterable<Book> findByTitle(String title) {
        return books.stream().filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase())).collect(Collectors.toList());
    }

    @Override
    public Iterable<Book> findByAuthor(String author) {
        return books.stream().filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Book> findByPublicationDate(LocalDate from, LocalDate to) {
        return books.stream().filter(book -> book.getPublicationDate()
                    .isAfter(from) &&  book.getPublicationDate().isBefore(to))
                    .collect(Collectors.toList());
    }

    @Override
    public Iterable<Book> searchBasedOnDescription(String searchText) {
        return books.stream().filter(book -> {
            List<String> searchKeywords = Arrays.asList(searchText.split(" "));
            Long numberOfSearchWordsAppearing = searchKeywords.stream().filter(word -> book.getDescription().toLowerCase().contains(word.toLowerCase())).count();

            return (double)numberOfSearchWordsAppearing/(double)(searchKeywords.size()) > TEXT_SEARCH_THRESHOLD;

        }).collect(Collectors.toList());
    }

    @Override
    public Iterable<Book> findByISBN(String ISBN) {
        return books.stream().filter(book -> book.getISBN().equals(ISBN)).collect(Collectors.toList());
    }

    @Override
    public Iterable<Book> findByGenre(List<Genre> genres) {
        return books.stream().filter(book->book.getGenres().containsAll(genres)).collect(Collectors.toList());
    }

    @Override
    public Iterable<Book> findAll() {
        return books;
    }
}
