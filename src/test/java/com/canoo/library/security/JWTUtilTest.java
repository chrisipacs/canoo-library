package com.canoo.library.security;

import com.canoo.library.model.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class JWTUtilTest {

    @Test
    public void generatedTokenCanBeParsedBack() {
        JWTUtil jwtUtil = new JWTUtil();
        jwtUtil.secret = "secret";

        User userToGenerateTokenFor = new User.Builder()
                .setName("testUser1234")
                .setPassword("pwd1234")
                .setEmail("testUser@canoo.com").build();
        userToGenerateTokenFor.setId(1L);

        String token = jwtUtil.generateToken(userToGenerateTokenFor);

        User parsedUser = jwtUtil.parseToken(token);

        assertEquals(userToGenerateTokenFor.getUsername(),parsedUser.getUsername());
        assertEquals(userToGenerateTokenFor.getEmail(),parsedUser.getEmail());
    }

}