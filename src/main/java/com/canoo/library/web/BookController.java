package com.canoo.library.web;


import com.canoo.library.model.Book;
import com.canoo.library.model.Genre;
import com.canoo.library.persistence.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@RestController("/api")
public class BookController {
    @Autowired
    BookRepository repository;

    @ResponseBody
    @RequestMapping(value="/books",method= RequestMethod.GET)
    public ResponseEntity<Iterable<Book>> getBooks(
                                                @RequestParam("id") Optional<Long> id,
                                                @RequestParam("title") Optional<String> title,
                                                @RequestParam("author") Optional<String> author,
                                                @RequestParam("publicationDateFrom") Optional<LocalDate> publicationDateFrom,
                                                @RequestParam("publicationDateTo") Optional<LocalDate> publicationDateTo,
                                                @RequestParam("description") Optional<String> description,
                                                @RequestParam("genre") Optional<List<Genre>> genres){

        //TODO implement pagination

        List<Predicate<Book>> predicatesForFiltering = new ArrayList<>();

        if(id.isPresent()){
            predicatesForFiltering.add(Book.idPredicate(id.get()));
        }
        if(title.isPresent()){
            predicatesForFiltering.add(Book.titlePredicate(title.get()));
        }
        if(author.isPresent()){
            predicatesForFiltering.add(Book.authorPredicate(author.get()));
        }
        if(publicationDateFrom.isPresent() && publicationDateTo.isPresent()){
            predicatesForFiltering.add(Book.publicationDatePredicate(publicationDateFrom.get(),publicationDateTo.get()));
        }
        if(title.isPresent()){
            predicatesForFiltering.add(Book.titlePredicate(title.get()));
        }
        if(description.isPresent()){
            predicatesForFiltering.add(Book.titlePredicate(description.get()));
        }

        ResponseEntity<Iterable<Book>> response = new ResponseEntity<>(repository.findBasedOnPredicates(predicatesForFiltering,0,10),HttpStatus.OK);

        return response;
    }

}
