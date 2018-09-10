package com.canoo.library.persistence.repository;

import com.canoo.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public interface BookRepository extends JpaRepository<Book,Long>, BookRepositoryCustom {
}