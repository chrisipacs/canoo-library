package com.canoo.library.persistence.repository;

import com.canoo.library.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryBookRepository implements BookRepository {

    private final List<Book> books = new ArrayList<>();

    public InMemoryBookRepository(Iterable<Book> books){

        books.forEach(this.books::add);
    }

    @Override
    public Iterable<Book> findBasedOnPredicates(List<Predicate<Book>> predicates, Integer pageNumber, Integer pageSize) {
        Stream<Book> bookStream = books.stream();

        for(Predicate<Book> predicate: predicates){
            bookStream = bookStream.filter(predicate);
        }

        return bookStream.skip(pageSize*pageNumber).limit(pageSize).collect(Collectors.toSet());
    }
}
