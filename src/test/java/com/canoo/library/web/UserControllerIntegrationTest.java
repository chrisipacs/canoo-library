package com.canoo.library.web;

import com.canoo.library.LibraryApplication;

import com.canoo.library.model.User;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;


import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LibraryApplication.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@TestPropertySource(locations = "classpath:application-test.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerIntegrationTest {

    private static final String REGISTER_ENDPOINT = "/register";

    private static final String LOGIN_ENDPOINT = "/login";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PasswordEncoder encoder;

    @Test
    public void userCanRegisterAndSubsequentlyLogIn() throws Exception{

        String name = "name";
        String password = "password";

        User user = new User.Builder().setName(name).setEmail("a@b.c").setPassword(
                password).build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);

        String tokenRegister = mvc.perform(put(REGISTER_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertTrue(tokenRegister!=null);

        String basicDigestHeaderValue = "Basic " + new String(Base64.encodeBase64((name+":"+password).getBytes()));

        String tokenLogin = mvc.perform(post(LOGIN_ENDPOINT)
                .header("Authorization", basicDigestHeaderValue))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void cantRegisterSameUserTwice() throws Exception{
        String name = "name2";
        String password = "password";

        User user = new User.Builder().setName(name).setEmail("a@b.c").setPassword(encoder.encode(
                password)).build();

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(user);

        mvc.perform(put(REGISTER_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());

        mvc.perform(put(REGISTER_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().is4xxClientError());
    }
}