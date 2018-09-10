package com.canoo.library.persistence.repository;

import com.canoo.library.model.Book;
import com.canoo.library.model.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookRepositoryCustom {
    Page<Book> searchBooks(Optional<Long> id, Optional<String> title, Optional<String> author,
                           Optional<LocalDate> publicationDateFrom, Optional<LocalDate> publicationDateTo,
                           Optional<String> description, Optional<List<Genre>> genres, Optional<String> sortBy, Pageable pageable);

}
