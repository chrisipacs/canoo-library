package com.canoo.library.persistence.repository;

import com.canoo.library.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class InMemoryUserRepositoryTest {

    private InMemoryUserRepository userRepository;
    private List<User> usersInRepository;
    private User testUser;
    private User secondUser;

    @Before
    public void setup(){
        usersInRepository = new ArrayList<>();

        testUser =new User.Builder()
                .setName("testUser")
                .setPassword("password123sosecure")
                .setEmail("testUser@canoo.com")
                .build();

        testUser.setId(1L);

        secondUser =new User.Builder()
                .setName("secondUser")
                .setPassword("XokHP6GepGonycgrPoywTotN")
                .setEmail("secondUser@canoo.com")
                .build();

        secondUser.setId(2L);

        usersInRepository.add(testUser);
        usersInRepository.add(secondUser);

        userRepository = new InMemoryUserRepository(usersInRepository);
    }

    @Test
    public void getByIdGetsUser() {
        Long id = 1L;

        User result = userRepository.getById(id);

        assertSame(result,testUser);
    }

    @Test(expected = RecordNotFoundException.class)
    public void getByIdReturnsExceptionForNonExistingId() {
        Long id = 1000L;

        userRepository.getById(id);
    }

    @Test
    public void getByNameGetsUser() {
        String name = "testUser";

        User result = userRepository.getByName(name);

        assertSame(result,testUser);
    }

    @Test
    public void getByNameGetsSecondUser() {
        String name = "secondUser";

        User result = userRepository.getByName(name);

        assertSame(result,secondUser);
    }

    @Test
    public void getByEmailAddress() {
        String emailAddress = "secondUser@canoo.com";

        User result = userRepository.getByEmailAddress(emailAddress);

        assertSame(result, this.secondUser);
    }

    @Test
    public void save() {
        User newUserForId1 =
                new User.Builder()
                        .setName("newUser")
                        .setPassword("newPassword")
                        .setEmail("new@e.mail")
                        .build();

        newUserForId1.setId(1L); //TODO assign id automatically

        userRepository.save(newUserForId1);

        assertSame(newUserForId1, userRepository.getById(1L));
    }
}