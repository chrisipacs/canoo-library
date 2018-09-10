package com.canoo.library.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"username" , "email"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String username;

    @Email
    private String email;

    @NotNull
    private String password;

    private String role;

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private static Long idCounter = 0L;

    private User(){
        synchronized(User.class){
            this.id = idCounter;
            idCounter++;
        }
    }

    private User(String username, String password){

        this.username = username;
        this.password = password;

        synchronized(User.class){
            this.id = idCounter;
            idCounter++;
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Long id){
        this.id=id;
    }

    public static class Builder {

        private Long id;
        private String username;
        private String password;
        private String email;
        private String role;

        public Builder setName(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setRole(String role) {
            this.role = role;
            return this;
        }

        public Builder setId(Long id){
            this.id = id;
            return this;
        }

        public User build() {
            User toBuild = new User(username,password);

            toBuild.email= Objects.requireNonNull(email);
            toBuild.role = Objects.requireNonNullElse(role,DEFAULT_ROLE);

            if(id!=null){
                toBuild.id = id;
            }

            return toBuild;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


