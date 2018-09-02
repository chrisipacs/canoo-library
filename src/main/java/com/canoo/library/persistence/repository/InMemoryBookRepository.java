package com.canoo.library.persistence.repository;

import com.canoo.library.model.Book;

import java.util.ArrayList;

import java.util.Comparator;
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
        Stream<Book> bookStream = createStreamForPredicates(predicates);

        return paginate(bookStream, pageNumber, pageSize).collect(Collectors.toSet());
    }

    @Override
    public Iterable<Book> findBasedOnPredicates(List<Predicate<Book>> predicates, Integer pageNumber, Integer pageSize, Comparator<Book> comparator) {
        Stream<Book> bookStream = createStreamForPredicates(predicates);

        return paginate(bookStream.sorted(comparator), pageNumber, pageSize).collect(Collectors.toList());
    }

    private Stream<Book> createStreamForPredicates(List<Predicate<Book>> predicates){
        Stream<Book> bookStream = books.stream();

        for(Predicate<Book> predicate: predicates){
            bookStream = bookStream.filter(predicate);
        }

        return bookStream;
    }

    private Stream<Book> paginate(Stream<Book> bookStream, Integer pageNumber, Integer pageSize){
        return bookStream.skip(pageSize*pageNumber).limit(pageSize);
    }

    @Override
    public void delete(Long id) {
        synchronized (books){
            books.stream()
                    .filter(book -> book.getId().equals(id))
                    .findFirst()
                    .ifPresentOrElse(books::remove,RuntimeException::new);
        }
    }
}
