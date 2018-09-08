package com.canoo.library.persistence.repository;

import com.canoo.library.model.User;

public interface UserRepository {

    User getById(Long id);
    User getByName(String name);
    User getByEmailAddress(String emailAddress);
    void save(User toSave);

}
