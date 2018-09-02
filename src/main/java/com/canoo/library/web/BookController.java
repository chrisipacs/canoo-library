package com.canoo.library.web;


import com.canoo.library.model.Book;
import com.canoo.library.model.Genre;
import com.canoo.library.model.SortableBookField;
import com.canoo.library.persistence.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

@RestController("/api")
public class BookController {
    @Autowired
    private BookRepository repository;

    //TODO move these to a property file
    private final Integer PAGE_SIZE_DEFAULT = 10;
    private final Double DESCRIPTION_SEARCH_THRESHOLD = 0.75;

    @ResponseBody
    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Book>> getBooks(
            @RequestParam("id") Optional<Long> id,
            @RequestParam("title") Optional<String> title,
            @RequestParam("author") Optional<String> author,
            @RequestParam("publicationDateFrom") Optional<LocalDate> publicationDateFrom,
            @RequestParam("publicationDateTo") Optional<LocalDate> publicationDateTo,
            @RequestParam("description") Optional<String> description,
            @RequestParam("genre") Optional<List<Genre>> genres,
            @RequestParam("pageNumber") Optional<Integer> pageNumber,
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("sortBy") Optional<String> sortBy) {

        List<Predicate<Book>> predicatesForFiltering = new ArrayList<>();

        id.ifPresent(i -> predicatesForFiltering.add(Book.idPredicate(i)));
        title.ifPresent(t->predicatesForFiltering.add(Book.titlePredicate(t)));
        author.ifPresent(a ->predicatesForFiltering.add(Book.authorPredicate(a)));
        description.ifPresent(d ->predicatesForFiltering.add(Book.descriptionPredicate(description.get(),
                DESCRIPTION_SEARCH_THRESHOLD)));
        genres.ifPresent(g -> predicatesForFiltering.add(Book.genresPredicate(g)));

        if(publicationDateFrom.isPresent() && publicationDateTo.isPresent()) {
            predicatesForFiltering.add(Book.publicationDatePredicate(publicationDateFrom.get(), publicationDateTo.get()));
        }

        Iterable<Book> booksToShow;

        if(sortBy.isPresent()){
            booksToShow = repository.findBasedOnPredicates(predicatesForFiltering,
                    pageNumber.orElse(0), pageSize.orElse(PAGE_SIZE_DEFAULT),
                    SortableBookField.valueOf(sortBy.get().toUpperCase()).getComparator());
        } else {
            booksToShow = repository.findBasedOnPredicates(predicatesForFiltering,
                    pageNumber.orElse(0), pageSize.orElse(PAGE_SIZE_DEFAULT));
        }

        ResponseEntity<Iterable<Book>> response = new ResponseEntity<>(booksToShow, HttpStatus.OK);

        return response;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    @RequestMapping(value = "/books/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") Long id){
        repository.delete(id);

        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/books/{id}", method = RequestMethod.GET)
    public ResponseEntity<Book> getBook(@PathVariable("id") Long id){
        List<Predicate<Book>> predicates = List.of(Book.idPredicate(id));

        Iterable<Book> result = repository.findBasedOnPredicates(predicates,0,1);
        for(Book b: result){
            return new ResponseEntity<Book>(b, HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}