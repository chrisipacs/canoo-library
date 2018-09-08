package com.canoo.library.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    //TODO
    private static final long serialVersionUID = -5539025340793479937L;
    String token;
    private Object credentials;
    private Object principal;

    public JWTAuthenticationToken(String token){
        super(null);
        this.token = token;
    }

    public JWTAuthenticationToken(String token, Collection<? extends GrantedAuthority> authorities){
        super(authorities);
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getToken() {
        return token;
    }

    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }

    public void setPrincipal(Object principal) {
        this.principal = principal;
    }



}
