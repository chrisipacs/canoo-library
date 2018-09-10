package com.canoo.library.persistence.repository;

import com.canoo.library.model.Book;
import com.canoo.library.model.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.data.domain.Pageable;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<Book> searchBooks(Optional<Long> id, Optional<String> title, Optional<String> author,
                                  Optional<LocalDate> publicationDateFrom, Optional<LocalDate> publicationDateTo,
                                  Optional<String> description, Optional<List<Genre>> genres, Optional<String> sortBy, Pageable pageable) {

        Query query = createQuery(id,title,author,publicationDateFrom,publicationDateTo,description,genres,sortBy);

        List<Book> list = query.getResultList();
        int totalRows = query.getResultList().size();

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        Page<Book> page = new PageImpl<Book>(query.getResultList(), pageable, totalRows);

        return page;
    }

    private Query createQuery(Optional<Long> id, Optional<String> title, Optional<String> author,
                              Optional<LocalDate> publicationDateFrom, Optional<LocalDate> publicationDateTo,
                              Optional<String> description, Optional<List<Genre>> genres, Optional<String> sortBy){

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        List<Predicate> predicates = new ArrayList<>();

        id.ifPresent(i->predicates.add(builder.equal(root.<Long>get("id"),i)));

        title.ifPresent(t->predicates.add(builder.like(root.<String>get("title"),"%"+t+"%")));

        author.ifPresent(a->predicates.add(builder.like(root.<String>get("author"),"%"+a+"%")));

        description.ifPresent(d->predicates.add(builder.like(root.<String>get("description"),"%"+d+"%")));

        predicates.add(builder.greaterThanOrEqualTo(root.<LocalDate> get("publicationDate"),
                builder.literal(publicationDateFrom.orElse(LocalDate.of(1000,1,1))))
        );

        predicates.add(builder.lessThan(root.<LocalDate> get("publicationDate"),
                builder.literal(publicationDateTo.orElse(LocalDate.of(LocalDate.now().getYear()+1000,1,1))))
        );

        criteria.select(root);
        criteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));

        return entityManager.createQuery( criteria );
    }
}
