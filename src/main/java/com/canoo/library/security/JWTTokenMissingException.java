package com.canoo.library.security;

import org.springframework.security.core.AuthenticationException;

public class JWTTokenMissingException extends AuthenticationException {
    public JWTTokenMissingException(String str){
        super(str);
    }
}
