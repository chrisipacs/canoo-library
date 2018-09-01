package com.canoo.library.model;

import java.time.LocalDate;
import java.util.List;

public class Book {

    public Book(String title, String author, LocalDate publicationDate, String ISBN, String description, List<Genre> genres){
        this.title = title;
        this.author = author;
        this.description = description;
        this.ISBN = ISBN;
        this.publicationDate = publicationDate;
        this.genres = genres;
    }

    private Long id;
    private String title;
    private String author;
    private LocalDate publicationDate;
    private String ISBN;
    private String description;
    private List<Genre> genres;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private List<Genre> genre;

    public Long getId(){
        throw new UnsupportedOperationException("In memory books don't have a queryable id, only persisted ones do");
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public String getISBN() {
        return ISBN;
    }

    public List<Genre> getGenres(){
        return genres;
    }
}
