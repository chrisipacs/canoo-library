package com.canoo.library.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String author;
    private LocalDate publicationDate;
    private String ISBN;

    @Lob
    @Column( length = 100000 )
    private String description;

    @ElementCollection(targetClass=Genre.class)
    @Enumerated(EnumType.STRING)
    private List<Genre> genres;

    public Book(){

    }

    public Book(String title, String author, LocalDate publicationDate, String ISBN, String description, List<Genre> genres) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.ISBN = ISBN;
        this.publicationDate = publicationDate;
        this.genres = genres;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId(){
        return id;
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

    public static Predicate<Book> titlePredicate(String title){
        return book -> book.getTitle().toLowerCase().contains(title.toLowerCase());
    }

    public static Predicate<Book> authorPredicate(String author){
        return book -> book.getAuthor().toLowerCase().contains(author.toLowerCase());
    }

    public static Predicate<Book> publicationDatePredicate(LocalDate from, LocalDate to){
        return book -> book.getPublicationDate()
                .isAfter(from) &&  book.getPublicationDate().isBefore(to);
    }

    public static Predicate<Book> ISBNPredicate(String ISBN){
        return book -> book.getISBN().equals(ISBN);
    }

    public static Predicate<Book> genresPredicate(List<Genre> genres){
        return book -> book.getGenres().containsAll(genres);
    }

    public static Predicate<Book> idPredicate(Long id){
        return book -> book.getId().equals(id);
    }

    public static Predicate<Book> descriptionPredicate(String searchText, Double threshold){
        return book -> {
            List<String> searchKeywords = Arrays.asList(searchText.split(" "));
            long numberOfSearchWordsAppearing =
                    searchKeywords.stream().filter(word -> book.getDescription().toLowerCase().contains(word.toLowerCase())).count();

            return (double)numberOfSearchWordsAppearing/(double)(searchKeywords.size()) > threshold;

        };
    }

    public static Comparator<Book> titleComparator(){
        return Comparator.comparing(Book::getTitle);
    }

    public static Comparator<Book> authorComparator(){
        return Comparator.comparing(Book::getAuthor);
    }

    public static Comparator<Book> publicationDateComparator(){
        return Comparator.comparing(Book::getPublicationDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
