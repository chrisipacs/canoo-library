package com.canoo.library.security;

import com.canoo.library.model.User;
import org.junit.Test;
import org.springframework.security.authentication.AuthenticationProvider;

import javax.naming.AuthenticationException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class JWTAuthenticationProviderTest {

    @Test
    public void authenticateProvidesCorrectPrincipal() {
        JWTAuthenticationToken token = new JWTAuthenticationToken("fakeToken", new ArrayList<>());
        JWTUtil jwtUtil = mock(JWTUtil.class);

        User userThatWillBeReturned = new User.Builder()
                .setName("testUser")
                .setEmail("e@ma.il")
                .setPassword("pwd")
                .build();

        when(jwtUtil.parseToken(token.getToken())).thenReturn(userThatWillBeReturned);
        AuthenticationProvider provider = new JWTAuthenticationProvider(jwtUtil);

        User authenticatedUser = (User)provider.authenticate(token).getPrincipal();

        assertSame(authenticatedUser,userThatWillBeReturned);
    }

    @Test(expected = JWTTokenMalformedException.class)
    public void unsuccessfulParseResultsInException(){
        JWTAuthenticationToken token = new JWTAuthenticationToken("fakeToken", new ArrayList<>());
        JWTUtil jwtUtil = mock(JWTUtil.class);
        when(jwtUtil.parseToken(token.getToken())).thenReturn(null);
        AuthenticationProvider provider = new JWTAuthenticationProvider(jwtUtil);

        provider.authenticate(token);
    }
}