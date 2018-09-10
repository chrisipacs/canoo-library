package com.canoo.library.persistence.repository;

import com.canoo.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User getById(Long id);
    User getByUsername(String name);
    User getByEmail(String emailAddress);
    User save(User toSave);

}
