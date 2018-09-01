package com.canoo.library.persistence.repository;

import com.canoo.library.model.Book;
import com.canoo.library.model.Genre;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;


public class InMemoryBookRepositoryTest {

    private BookRepository repository;

    @Before
    public void setup(){

        //changing the contents of this list can break the test cases
        List<Book> booksInRepository = new ArrayList<>();

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

    @Test(expected = UnsupportedOperationException.class)
    public void findByIdThrowsException() {
        repository.findById(1234L);
    }

    @Test
    public void findByTitleFindsBookBasedOnFullTitle() {
        Iterable<Book> result = repository.findByTitle("The Lord of the Rings");

        assertThat(result, contains(
                hasProperty("title", is("The Lord of the Rings"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void findByTitleFindsBookBasedOnWord() {
        Iterable<Book> result = repository.findByTitle("Galaxy");

        assertThat(result, contains(
                hasProperty("title", is("The Hitchiker's guide to the Galaxy"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void findByTitleIgnoresCase() {
        Iterable<Book> result = repository.findByTitle("galaxy");

        assertThat(result, contains(
                hasProperty("title", is("The Hitchiker's guide to the Galaxy"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void findByAuthorFindsAuthorBasedOnLastName() {
        Iterable<Book> result = repository.findByAuthor("Tolkien");

        assertThat(result, contains(
                hasProperty("author", is("J. R. R. Tolkien"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void findByPublicationDate() {

        LocalDate from=LocalDate.of(1980,1,1);
        LocalDate to=LocalDate.of(1990,1,1);

        Iterable<Book> result = repository.findByPublicationDate(from,to);

        assertThat(result, contains(
                hasProperty("title", is("The Shining"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void searchBasedOnDescriptionFindsExactText() {
        Iterable<Book> result = repository.findByAuthor("Tolkien");

        assertThat(result, contains(
                hasProperty("author", is("J. R. R. Tolkien"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void searchBasedOnDescriptionFindsUppercaseText() {
        Iterable<Book> result = repository.searchBasedOnDescription("CALVIN AND HOBBES IS");

        assertThat(result, contains(
                hasProperty("title", is("Calvin and Hobbes"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void searchBasedOnDescriptionFinds90PercentMatchingText() {
        Iterable<Book> result = repository.searchBasedOnDescription("Calvin and Hobbes is unquestionably one of the most popular WORDTHATDOESNTAPPEAR");

        assertThat(result, contains(
                hasProperty("title", is("Calvin and Hobbes"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void findByISBN() {
        Iterable<Book> result = repository.findByISBN("0740748475");

        assertThat(result, contains(
                hasProperty("ISBN", is("0740748475"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void findByGenre() {

        List<Genre> genresWereLookingFor = new ArrayList<>();
        genresWereLookingFor.add(Genre.DYSTOPIA);
        genresWereLookingFor.add(Genre.DRAMA);

        Iterable<Book> result = repository.findByGenre(genresWereLookingFor);

        assertThat(result, contains(
                hasProperty("title", is("1984"))
        ));

        assertThat(result, iterableWithSize(1));
    }

    @Test
    public void findAll() {
        Iterable<Book> result = repository.findAll();

        assertThat(result, iterableWithSize(7));
    }
}