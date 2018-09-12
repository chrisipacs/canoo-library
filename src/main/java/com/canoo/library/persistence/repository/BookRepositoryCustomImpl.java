package com.canoo.library.persistence.repository;

import com.canoo.library.model.Book;
import com.canoo.library.model.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookRepositoryCustomImpl implements BookRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Page<Book> searchBooks(Optional<Long> id, Optional<String> title, Optional<String> author, Optional<String> isbn,
                                  Optional<LocalDate> publicationDateFrom, Optional<LocalDate> publicationDateTo,
                                  Optional<String> description, Optional<List<Genre>> genres,
                                  Optional<List<String>> orderByAsc, Optional<List<String>> orderByDesc,
                                  Pageable pageable) {

        TypedQuery<Book> query = createQuery(id,title,author,isbn,publicationDateFrom,publicationDateTo,description,
                genres,
                orderByAsc, orderByDesc);

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        List<Book> list = query.getResultList();
        int totalRows = query.getResultList().size();

        return new PageImpl<>(list, pageable, totalRows);
    }

    private TypedQuery<Book> createQuery(Optional<Long> id, Optional<String> title, Optional<String> author, Optional<String> isbn,
                              Optional<LocalDate> publicationDateFrom, Optional<LocalDate> publicationDateTo,
                              Optional<String> description, Optional<List<Genre>> genres,
                                         Optional<List<String>> orderByAsc, Optional<List<String>> orderByDesc){

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
        Root<Book> root = criteria.from(Book.class);
        List<Predicate> predicates = new ArrayList<>();

        id.ifPresent(i->predicates.add(builder.equal(root.<Long>get("id"),i)));

        title.ifPresent(t->predicates.add(builder.like(root.<String>get("title"),"%"+t+"%")));

        author.ifPresent(a->predicates.add(builder.like(root.<String>get("author"),"%"+a+"%")));

        isbn.ifPresent(i->predicates.add(builder.like(root.<String>get("ISBN"),"%"+i+"%")));

        description.ifPresent(d->predicates.add(builder.like(root.<String>get("description"),"%"+d+"%")));

        genres.ifPresent(g->g.forEach(genre->{
            predicates.add(builder.isMember(genre,root.get("genres")));})
        );

        predicates.add(builder.greaterThanOrEqualTo(root.get("publicationDate"),
                builder.literal(publicationDateFrom.orElse(LocalDate.of(1000,1,1))))
        );

        predicates.add(builder.lessThan(root.get("publicationDate"),
                builder.literal(publicationDateTo.orElse(LocalDate.of(LocalDate.now().getYear()+1000,1,1))))
        );

        criteria.select(root);
        criteria.where(builder.and(predicates.toArray(new Predicate[0])));

        orderByAsc.ifPresent(orderByAscList->
                criteria.orderBy(orderByAscList.stream()
                                    .map(orderByField -> builder.asc(root.get(orderByField)))
                                    .collect(Collectors.toList())));


        orderByDesc.ifPresent(orderByDescList->
                criteria.orderBy(orderByDescList.stream()
                        .map(orderByField -> builder.desc(root.get(orderByField)))
                        .collect(Collectors.toList())));

        return entityManager.createQuery(criteria);
    }
}
