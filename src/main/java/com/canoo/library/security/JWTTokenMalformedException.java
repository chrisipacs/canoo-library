package com.canoo.library.security;

import org.springframework.security.core.AuthenticationException;

public class JWTTokenMalformedException extends AuthenticationException {
    JWTTokenMalformedException(String str){

        super(str);
    }
}
