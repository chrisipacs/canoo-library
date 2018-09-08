package com.canoo.library.persistence.repository;

import com.canoo.library.model.User;

import java.util.ArrayList;
import java.util.List;

public class InMemoryUserRepository implements UserRepository {

    private final List<User> users = new ArrayList<>();

    public InMemoryUserRepository(Iterable<User> users){
        users.forEach(this.users::add);
    }

    @Override
    public User getById(Long id) {
        return users.stream().filter(user->user.getId().equals(id)).findFirst().orElseThrow(RecordNotFoundException::new);
    }

    @Override
    public User getByName(String name) {
        return users.stream().filter(user->user.getUsername().equals(name)).findFirst().orElseThrow(RecordNotFoundException::new);
    }

    @Override
    public User getByEmailAddress(String emailAddress) {
        return users.stream().filter(user->user.getEmail().equals(emailAddress)).findFirst().orElseThrow(RecordNotFoundException::new);
    }

    @Override
    public void save(User toSave) {
        synchronized (users) {
            users.stream().filter(book -> book.equals(toSave)).findFirst().ifPresent(users::remove);

            users.add(toSave);
        }
    }
}
