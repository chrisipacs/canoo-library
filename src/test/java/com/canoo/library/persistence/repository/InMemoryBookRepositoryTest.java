package com.canoo.library.persistence.repository;

import com.canoo.library.model.Book;
import com.canoo.library.model.Genre;
import com.canoo.library.model.SortableBookField;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.junit.Assert.assertTrue;

public class InMemoryBookRepositoryTest {

    private InMemoryBookRepository repository;
    private List<Book> booksInRepository = new ArrayList<>();

    @Before
    public void setup(){

        //changing the contents of this list can break the test cases

        String lordOfTheRingsDescription = "In ancient times the Rings of Power were crafted by the Elven-smiths, and Sauron, the Dark Lord, forged the One Ring, filling it with his own power so that he could rule all others. But the One Ring was taken from him, and though he sought it throughout Middle-earth, it remained lost to him. After many ages it fell by chance into the hands of the hobbit Bilbo Baggins.";
        String shiningDescription = "Jack Torrance's new job at the Overlook Hotel is the perfect chance for a fresh start. As the off-season caretaker at the atmospheric old hotel, he'll have plenty of time to spend reconnecting with his family and working on his writing. But as the harsh winter weather sets in, the idyllic location feels ever more remote...and more sinister. And the only one to notice the strange and terrible forces gathering around the Overlook is Danny Torrance, a uniquely gifted five-year-old.";
        String calvinAndHobbesDescription = "Calvin and Hobbes is unquestionably one of the most popular comic strips of all time. The imaginative world of a boy and his real-only-to-him tiger was first syndicated in 1985 and appeared in more than 2,400 newspapers when Bill Watterson retired on January 1, 1996. The entire body of Calvin and Hobbes cartoons published in a truly noteworthy tribute to this singular cartoon in The Complete Calvin and Hobbes. Composed of three hardcover, four-color volumes in a sturdy slipcase, this New York Times best-selling edition includes all Calvin and Hobbes cartoons that ever appeared in syndication. This is the treasure that all Calvin and Hobbes fans seek.";


        //changing the properties of these objects can break the test cases
        booksInRepository.add(new Book("The Lord of the Rings", "J. R. R. Tolkien", LocalDate.now(), "",lordOfTheRingsDescription, List.of(Genre.FANTASY)));
        booksInRepository.add(new Book("The Hitchiker's guide to the Galaxy", "Douglas Adams", LocalDate.now(), "","",List.of(Genre.SF)));
        booksInRepository.add(new Book("1984", "George Orwell", LocalDate.now(), "","",List.of(Genre.DYSTOPIA,Genre.DRAMA)));
        booksInRepository.add(new Book("Catch-22", "Joseph Heller", LocalDate.now(), "","",List.of(Genre.SATIRE,Genre.FICTION,Genre.DRAMA)));
        booksInRepository.add(new Book("The Shining", "Stephen King", LocalDate.of(1980,5,23), "",shiningDescription,List.of(Genre.HORROR,Genre.DRAMA)));
        booksInRepository.add(new Book("A Storm of Swords", "George R. R. Martin", LocalDate.now(), "","",List.of(Genre.FANTASY)));
        booksInRepository.add(new Book("Calvin and Hobbes", "Bill Watterson", LocalDate.now(), "0740748475",calvinAndHobbesDescription,List.of(Genre.COMICS)));

        repository = new InMemoryBookRepository(booksInRepository);
    }

    @Test
    public void findByIdFindsBookBasedOnId() {
        Iterable<Book> result = repository.findBasedOnPredicates(List.of(Book.idPredicate(booksInRepository.get(0).getId())),0,10);

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void findByTitleFindsBookBasedOnFullTitle() {
        Iterable<Book> result = repository.findBasedOnPredicates(List.of(Book.titlePredicate("The Lord of the Rings")),0,10);

        assertThat(result, contains(
                hasProperty("title", is("The Lord of the Rings"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void findByTitleFindsBookBasedOnWord() {
        Iterable<Book> result = repository.findBasedOnPredicates(List.of(Book.titlePredicate("Galaxy")),0,10);

        assertThat(result, contains(
                hasProperty("title", is("The Hitchiker's guide to the Galaxy"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void findByTitleIgnoresCase() {
        Iterable<Book> result = repository.findBasedOnPredicates(List.of(Book.titlePredicate("galaxy")),0,10);

        assertThat(result, contains(
                hasProperty("title", is("The Hitchiker's guide to the Galaxy"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void findByAuthorFindsAuthorBasedOnLastName() {
        Iterable<Book> result = repository.findBasedOnPredicates(List.of(Book.authorPredicate("Tolkien")),0,10);

        assertThat(result, contains(
                hasProperty("author", is("J. R. R. Tolkien"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void findByPublicationDate() {

        LocalDate from=LocalDate.of(1980,1,1);
        LocalDate to=LocalDate.of(1990,1,1);

        Iterable<Book> result = repository.findBasedOnPredicates(List.of(Book.publicationDatePredicate(from,to)),0,10);

        assertThat(result, contains(
                hasProperty("title", is("The Shining"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void searchBasedOnDescriptionFindsExactText() {
        Iterable<Book> result = repository.findBasedOnPredicates(List.of(Book.descriptionPredicate("Overlook Hotel",0.75)),0,10);

        assertThat(result, contains(
                hasProperty("title", is("The Shining"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void searchBasedOnDescriptionFindsUppercaseText() {
        Iterable<Book> result = repository.findBasedOnPredicates(List.of(Book.descriptionPredicate("CALVIN AND HOBBES IS", 0.75)),0,10);

        assertThat(result, contains(
                hasProperty("title", is("Calvin and Hobbes"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void searchBasedOnDescriptionFinds90PercentMatchingText() {
        Iterable<Book> result = repository.findBasedOnPredicates(List.of(Book.descriptionPredicate("Calvin and Hobbes is unquestionably one of the most popular WORDTHATDOESNTAPPEAR", 0.75)),0,10);

        assertThat(result, contains(
                hasProperty("title", is("Calvin and Hobbes"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void findByISBN() {
        Iterable<Book> result = repository.findBasedOnPredicates(List.of(Book.ISBNPredicate("0740748475")),0,10);

        assertThat(result, contains(
                hasProperty("ISBN", is("0740748475"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void findByGenre() {
        List<Genre> genresWeAreLookingFor = new ArrayList<>();
        genresWeAreLookingFor.add(Genre.DYSTOPIA);
        genresWeAreLookingFor.add(Genre.DRAMA);

        Iterable<Book> result = repository.findBasedOnPredicates(List.of(Book.genresPredicate(genresWeAreLookingFor)),0,10);

        assertThat(result, contains(
                hasProperty("title", is("1984"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void findBasedOnMultiplePredicates() {
        Iterable<Book> result = repository.findBasedOnPredicates(List.of(Book.titlePredicate("A Storm of Swords"),Book.authorPredicate("George R. R. Martin")),0,10);

        assertThat(result, contains(
                hasProperty("title", is("A Storm of Swords"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void dontFindBasedOnNonMatchingMultiplePredicates() {
        Iterable<Book> result = repository.findBasedOnPredicates(List.of(Book.titlePredicate("A Storm of Swords"),Book.authorPredicate("Stephen King")),0,10);

        assertThat(result, iterableWithSize(0));
    }

    @Test
    public void findOneBookOnPage2() {
        Iterable<Book> result = repository.findBasedOnPredicates(new ArrayList<>(),2,3);

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void sortingByTitleWorks() {
        Iterable<Book> result = repository.findBasedOnPredicates(new ArrayList<>(),0,10, SortableBookField.TITLE.getComparator());

        assertThat(result, iterableWithSize(7));

        List<Book> resultAsList = StreamSupport.stream(result.spliterator(), false)
                .collect(Collectors.toList());

        assertTrue(resultAsList.get(0).getTitle().equals("1984"));
        assertTrue(resultAsList.get(4).getTitle().equals("The Hitchiker's guide to the Galaxy"));
        assertTrue(resultAsList.get(6).getTitle().equals("The Shining"));
    }
}