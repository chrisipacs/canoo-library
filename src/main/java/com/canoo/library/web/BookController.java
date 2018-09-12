package com.canoo.library.web;


import com.canoo.library.model.Book;
import com.canoo.library.model.Genre;
import com.canoo.library.model.SortableBookField;
import com.canoo.library.persistence.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

@RestController
@RequestMapping("/api")
public class BookController {

    @Autowired
    private BookRepository repository;

    //values will be overwritten by spring
    @Value("${pagesize}")
    private Integer PAGE_SIZE_DEFAULT;

    @Value("${pagesizelimit}")
    private Integer PAGE_SIZE_MAX;

    @ResponseBody
    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Book>> getBooks(
            @RequestParam("id") Optional<Long> id,
            @RequestParam("title") Optional<String> title,
            @RequestParam("author") Optional<String> author,
            @RequestParam("ISBN") Optional<String> isbn,
            @RequestParam("publicationDateFrom") Optional<LocalDate> publicationDateFrom,
            @RequestParam("publicationDateTo") Optional<LocalDate> publicationDateTo,
            @RequestParam("description") Optional<String> description,
            @RequestParam("genre") Optional<List<Genre>> genres,
            @RequestParam("pageNumber") Optional<Integer> pageNumber,
            @RequestParam("pageSize") Optional<Integer> pageSize,
            @RequestParam("orderByAsc") Optional<List<String>> orderByAsc,
            @RequestParam("orderByDesc") Optional<List<String>> orderByDesc) {

        Page<Book> booksToShow = repository.searchBooks(id, title, author, isbn, publicationDateFrom, publicationDateTo
                , description, genres, orderByAsc, orderByDesc, PageRequest.of(pageNumber.orElse(0),
                        Math.min(PAGE_SIZE_MAX,pageSize.orElse(PAGE_SIZE_DEFAULT))));


        return new ResponseEntity<>(booksToShow.getContent(), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @ResponseBody
    @RequestMapping(value = "/books/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> deleteBook(@PathVariable("id") Long id){
        repository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/books/{id}", method = RequestMethod.GET)
    public ResponseEntity<Book> getBook(@PathVariable("id") Long id){
        Iterable<Book> result = repository.findAllById(List.of(id));
        for(Book b: result){
            return new ResponseEntity<>(b, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/books/{id}", method = RequestMethod.PUT, consumes =
            MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Book> updateBook(@PathVariable("id") Long id,
                                            @RequestBody Book book){

        if(!id.equals(book.getId()) && book.getId()!=null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        repository.save(book);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/books", method = RequestMethod.PUT, consumes =
            MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Book> createBook(@RequestBody Book book){

        if(book.getId()!=null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        repository.save(book);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}