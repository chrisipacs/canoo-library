package com.canoo.library.configuration;

import com.canoo.library.security.*;
import org.springframework.beans.factory.annotation.Autowired;
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
                .antMatchers("/register")
                //.antMatchers(HttpMethod.GET,"/api/books**")
                //.antMatchers(HttpMethod.DELETE,"/api/books**")
                //.antMatchers(HttpMethod.DELETE,"/api/books/**")
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

    //TODO add all the other necessary stuff based on https://github.com/chrisipacs/Mozarella/blob/master/src/main/java/com/hybridtheory/mozarella/configuration/authentication/AuthenticationConfig.java

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint(){
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    JWTAuthenticationFilter authenticationFilter(){
        try {
            return new JWTAuthenticationFilter(authenticationManagerBean(), successHandler());
        } catch (Exception e) {
            return new JWTAuthenticationFilter(null,successHandler());
        }
    }

    @Bean
    AuthenticationSuccessHandler successHandler(){
        return new RestAuthenticationSuccessHandler();
    }
}
