package com.canoo.library.persistence.repository;

import com.canoo.library.model.User;

import java.util.ArrayList;
import java.util.List;

public class InMemoryUserRepository  {

    private final List<User> users = new ArrayList<>();

    public InMemoryUserRepository(Iterable<User> users){
        users.forEach(this.users::add);
    }

    public User getById(Long id) {
        return users.stream().filter(user->user.getId().equals(id)).findFirst().orElseThrow(RecordNotFoundException::new);
    }

    public User getByName(String name) {
        return users.stream().filter(user->user.getUsername().equals(name)).findFirst().orElseThrow(RecordNotFoundException::new);
    }

    public User getByEmailAddress(String emailAddress) {
        return users.stream().filter(user->user.getEmail().equals(emailAddress)).findFirst().orElseThrow(RecordNotFoundException::new);
    }

    public User save(User toSave) {
        synchronized (users) {
            users.stream().filter(book -> book.equals(toSave)).findFirst().ifPresent(users::remove);

            users.add(toSave);
        }

        return toSave;
    }
}
