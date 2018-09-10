package com.canoo.library.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

public class JWTAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    AuthenticationSuccessHandler successHandler;

    public JWTAuthenticationFilter(AuthenticationManager manager, AuthenticationSuccessHandler successHandler) {
        super("/api/**");
        RequestMatcher matcher = new AntPathRequestMatcher("/api/**");
        setRequiresAuthenticationRequestMatcher(matcher);
        this.setAuthenticationManager(manager);
        this.setAuthenticationSuccessHandler(successHandler);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {

        if(request.getMethod().equals("OPTIONS")){
            return false;
        }

        //TODO describe this logic more elegantly

        Boolean requestMethodIsNotGet = !request.getMethod().equals(RequestMethod.GET.toString());
        //Boolean tryingToAccessUsers = request.getPathInfo().contains("/user");

        Boolean requiresAuth = (requestMethodIsNotGet)  && super.requiresAuthentication(request,response);

        return (requestMethodIsNotGet)  && super.requiresAuthentication(request,response);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new JWTTokenMissingException("No JWT token found in request headers");
        }

        String authToken = header.substring(7);
        JWTAuthenticationToken authRequest = new JWTAuthenticationToken(authToken);

        return getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        chain.doFilter(request, response);
    }
}