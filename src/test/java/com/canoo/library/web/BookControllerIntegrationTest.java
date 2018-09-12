package com.canoo.library.web;

import com.canoo.library.LibraryApplication;
import com.canoo.library.model.Book;
import com.canoo.library.model.Genre;
import com.canoo.library.model.Role;
import com.canoo.library.model.User;
import com.canoo.library.persistence.repository.BookRepository;
import com.canoo.library.security.JWTUtil;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LibraryApplication.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookControllerIntegrationTest {

    //these tests are based on the test data that gets inserted in the DataLoader class
    //if the dataloader is removed, then the entries will have to be inserted inside this class

    //designed to work with the exact page size default and page size limit values from the test property file
    //if the test properties change, these tests have to change as well

    private static final String BOOKS_ENDPOINT = "/api/books";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private BookRepository bookRepository;

    @Value("${pagesizedefault}")
    private static Integer PAGE_SIZE_DEFAULT;

    @Test
    public void notBiggerThanPageSizeMax() throws Exception{
        mvc.perform(get(BOOKS_ENDPOINT+"?pageSize=10000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].title").value("The Lord of the Rings"));
    }

    @Test
    public void matchesPageSizeDefault() throws Exception{
        mvc.perform(get(BOOKS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].title").value("The Lord of the Rings"))
                .andExpect(jsonPath("$[2].title").value("1984"));
    }

    @Test
    public void multipleCriteriaReturnsCorrectResult1() throws Exception{
        //all the dramas ordered by publication date (newest first)
        mvc.perform(get(BOOKS_ENDPOINT+"?orderByDesc=publicationDate&genre=DRAMA&pageNumber=0&pageSize=50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].title").value("The Shining"))
                .andExpect(jsonPath("$[1].title").value("Catch-22"))
                .andExpect(jsonPath("$[2].title").value("1984"));
    }

    @Test
    public void multipleCriteriaReturnsCorrectResult2() throws Exception{
        //all books published in the second half of the 20th century, ordered by the name of the author
        mvc.perform(get(BOOKS_ENDPOINT+"?orderByAsc=author&publicationDateFrom=1950-01-01&publicationDateTo" +
                "=2000-01-01&pageSize=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].author").value("Douglas Adams"));
    }

    @Test
    public void resultsAreOrderedAscending() throws Exception{
        mvc.perform(get(BOOKS_ENDPOINT+"?orderByAsc=title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("1984"))
                .andExpect(jsonPath("$[1].title").value("A Storm of Swords"))
                .andExpect(jsonPath("$[2].title").value("Calvin and Hobbes"));

    }

    @Test
    public void resultsAreOrderedDescending() throws Exception{
        mvc.perform(get(BOOKS_ENDPOINT+"?orderByDesc=title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("The Shining"))
                .andExpect(jsonPath("$[1].title").value("The Lord of the Rings"))
                .andExpect(jsonPath("$[2].title").value("The Hitchiker's guide to the Galaxy"));

    }

    @Test
    public void pagingWorks() throws Exception{
        mvc.perform(get(BOOKS_ENDPOINT+"?pageNumber=0&pageSize=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("The Lord of the Rings"));
    }

    @Test
    public void userCantDelete() throws Exception{
        User user =
                new User.Builder().setName("user").setEmail("a@b.c").setPassword(encoder.encode("password123")).build();
        String token = jwtUtil.generateToken(user);

        Book bookWeWillDelete = new Book("Will be deleted", "No author", LocalDate.of(1954,7,29), "",
                "doesnt matter, will be deleted anyway", List.of(Genre.FANTASY));

        bookWeWillDelete = bookRepository.save(bookWeWillDelete ); //should get the id 10

        mvc.perform(delete(BOOKS_ENDPOINT+"/"+bookWeWillDelete.getId())
                .header("Authorization", "Bearer "+token))
                .andExpect(status().is4xxClientError());

    }

    @Test
    public void adminCanDelete() throws Exception{
        User admin =
                new User.Builder().setName("admin").setEmail("a@b.c").setRole(Role.ROLE_ADMIN.toString()).setPassword(encoder.encode(
                        "admin123")).build();
        String token = jwtUtil.generateToken(admin);

        Book bookWeWillDelete = new Book("Will be deleted", "No author", LocalDate.now(), "",
                "doesnt matter, will be deleted anyway", List.of(Genre.FANTASY));

        bookWeWillDelete = bookRepository.save(bookWeWillDelete ); //should get the id 10

        mvc.perform(delete(BOOKS_ENDPOINT+"/"+bookWeWillDelete.getId())
                .header("Authorization", "Bearer "+token))
                .andExpect(status().isAccepted());

    }

    @Test
    public void userCanUpdateWithJWTToken() throws Exception{
        User user =
                new User.Builder().setName("user").setEmail("a@b.c").setPassword(encoder.encode(
                        "user123")).build();
        String token = jwtUtil.generateToken(user);

        Book bookWeWillModify = new Book("Will be modified", "No author", LocalDate.now(), "",
                "", List.of(Genre.FANTASY));

        bookWeWillModify = bookRepository.save(bookWeWillModify ); //should get the id 10

        String json = "{" + "  \"id\" : "+bookWeWillModify.getId()+"," + "  \"title\" : \"Modified title\"," + "  \"author\" " +
                ": \"Bill Watterson\"," + "  \"publicationDate\" : \"2018-09-02\"," + "  \"description\" : \" \"," + "  \"genres\" : [ \"COMICS\" ],\n" + "  \"isbn\" : \"0740748475\"" + "}";

        mvc.perform(put(BOOKS_ENDPOINT+"/"+bookWeWillModify.getId())
                .header("Authorization", "Bearer "+token)
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        bookRepository.deleteById(bookWeWillModify.getId());

    }

    @Test
    public void userCantUpdateWithoutJWTtoken() throws Exception{
        mvc.perform(put(BOOKS_ENDPOINT+"/4"))
                .andExpect(status().is4xxClientError());

    }
}