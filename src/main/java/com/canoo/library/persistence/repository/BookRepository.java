package com.canoo.library.persistence.repository;

import com.canoo.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long>, BookRepositoryCustom {
}