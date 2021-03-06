package com.canoo.library.configuration;

import com.canoo.library.model.Book;
import com.canoo.library.model.Genre;
import com.canoo.library.model.Role;
import com.canoo.library.model.User;
import com.canoo.library.persistence.repository.BookRepository;
import com.canoo.library.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PasswordEncoder encoder;

    public void run(ApplicationArguments args) {
        //users

        User testUser = new User.Builder()
                .setName("testUser")
                .setPassword(encoder.encode("password123"))
                .setEmail("testuser@canoo.com")
                .build();

        User admin = new User.Builder()
                .setName("admin")
                .setPassword(encoder.encode("admin123"))
                .setEmail("admin@canoo.com")
                .setRole(Role.ROLE_ADMIN.toString())
                .build();

        userRepository.save(testUser);
        userRepository.save(admin);

        //books

        String lordOfTheRingsDescription = "In ancient times the Rings of Power were crafted by the Elven-smiths, and Sauron, the Dark Lord, forged the One Ring, filling it with his own power so that he could rule all others. But the One Ring was taken from him, and though he sought it throughout Middle-earth, it remained lost to him. After many ages it fell by chance into the hands of the hobbit Bilbo Baggins.";
        String shiningDescription = "Jack Torrance's new job at the Overlook Hotel is the perfect chance for a fresh start. As the off-season caretaker at the atmospheric old hotel, he'll have plenty of time to spend reconnecting with his family and working on his writing. But as the harsh winter weather sets in, the idyllic location feels ever more remote...and more sinister. And the only one to notice the strange and terrible forces gathering around the Overlook is Danny Torrance, a uniquely gifted five-year-old.";
        String calvinAndHobbesDescription = "Calvin and Hobbes is unquestionably one of the most popular comic strips of all time. The imaginative world of a boy and his real-only-to-him tiger was first syndicated in 1985 and appeared in more than 2,400 newspapers when Bill Watterson retired on January 1, 1996. The entire body of Calvin and Hobbes cartoons published in a truly noteworthy tribute to this singular cartoon in The Complete Calvin and Hobbes. Composed of three hardcover, four-color volumes in a sturdy slipcase, this New York Times best-selling edition includes all Calvin and Hobbes cartoons that ever appeared in syndication. This is the treasure that all Calvin and Hobbes fans seek.";

        bookRepository.save(new Book("The Lord of the Rings", "J. R. R. Tolkien", LocalDate.of(1954,7,29), "",
                lordOfTheRingsDescription, List.of(Genre.FANTASY)));
        bookRepository.save(new Book("The Hitchiker's guide to the Galaxy", "Douglas Adams", LocalDate.of(1979,10,12),
                "0-330-25864-8","",List.of(Genre.SF)));
        bookRepository.save(new Book("1984", "George Orwell", LocalDate.of(1949,6,8), "","",List.of(Genre.DYSTOPIA,
                Genre.DRAMA)));
        bookRepository.save(new Book("Catch-22", "Joseph Heller", LocalDate.of(1961,11,10), "","",List.of(Genre.SATIRE,
                Genre.FICTION,Genre.DRAMA)));
        bookRepository.save(new Book("The Shining", "Stephen King", LocalDate.of(1980,5,23), "",shiningDescription,List.of(Genre.HORROR,Genre.DRAMA)));
        bookRepository.save(new Book("A Storm of Swords", "George R. R. Martin", LocalDate.of(2000,8,8), "","",
                List.of(Genre.FANTASY)));
        bookRepository.save(new Book("Calvin and Hobbes", "Bill Watterson", LocalDate.of(2005,10,4), "0740748475",
                calvinAndHobbesDescription,List.of(Genre.COMICS)));
    }
}
