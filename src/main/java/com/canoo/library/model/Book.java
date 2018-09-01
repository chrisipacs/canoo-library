package com.canoo.library.model;

import java.time.LocalDate;
import java.util.List;

public class Book {

    public Book(String title, String author, LocalDate publicationDate, String ISBN, String description){

    }

    private String title;
    private String author;
    private LocalDate publicationDate;
    private String ISBN;
    private List<Genre> genre;

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
}
