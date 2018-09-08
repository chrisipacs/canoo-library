package com.canoo.library.security;

import com.canoo.library.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JWTAuthenticationProvider implements AuthenticationProvider {

    private JWTUtil jwtUtil;

    @Autowired
    public JWTAuthenticationProvider(JWTUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JWTAuthenticationToken jwtAuthenticationToken = (JWTAuthenticationToken) authentication;
        String token = jwtAuthenticationToken.getToken();

        User parsedUser = jwtUtil.parseToken(token);

        if (parsedUser == null) {
            throw new JWTTokenMalformedException("JWT token is not valid");
        }

        jwtAuthenticationToken.setPrincipal(parsedUser);

        return jwtAuthenticationToken;
    }

}