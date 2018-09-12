package com.canoo.library.configuration;

import com.canoo.library.security.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    JWTUtil jwtUtil(){
        return new JWTUtil();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authenticationProvider(authenticationProvider())
                .antMatcher("/api/**").authorizeRequests() //
                .anyRequest().permitAll() //
                .and()
                .addFilterBefore(authenticationFilter(), BasicAuthenticationFilter.class);
        ;
    }

    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/register*")
                .antMatchers("/login*");
    }

    @Bean
    protected PasswordEncoder encoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected AuthenticationProvider authenticationProvider(){
        return new JWTAuthenticationProvider(jwtUtil());
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint(){
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    JWTAuthenticationFilter authenticationFilter() throws Exception {
            JWTAuthenticationFilter filter = new JWTAuthenticationFilter(null, successHandler());
            filter.setAuthenticationManager(super.authenticationManagerBean());

            return filter;
    }

    @Bean
    AuthenticationSuccessHandler successHandler(){
        return new RestAuthenticationSuccessHandler();
    }
}
