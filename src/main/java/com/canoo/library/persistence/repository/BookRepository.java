package com.canoo.library.persistence.repository;

import com.canoo.library.model.Book;

import java.util.List;
import java.util.function.Predicate;

public interface BookRepository {
    Iterable<Book> findBasedOnPredicates(List<Predicate<Book>> predicates, Integer pageNumber, Integer pageSize);

}
