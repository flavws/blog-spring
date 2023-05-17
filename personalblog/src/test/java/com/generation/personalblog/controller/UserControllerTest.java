package com.generation.personalblog.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.generation.personalblog.model.User;
import com.generation.personalblog.repository.UserRepository;
import com.generation.personalblog.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    void start(){

        userRepository.deleteAll();

        userService.createUser(new User(0L, "Root", "root@root.com", "rootroot", " "));
    }

    @Test
    @DisplayName("Cadastrar usuário")
    public void shouldCreateAnUser(){

        HttpEntity<User> requestBody = new HttpEntity<User>(new User(0L, "Flavia", "flavia@email.com.br", "12345678", "-"));

        ResponseEntity<User> responseBody = testRestTemplate.exchange(
                "/users/create",
                HttpMethod.POST,
                requestBody,
                User.class
        );

        assertEquals(HttpStatus.CREATED, responseBody.getStatusCode());
    }

    @Test
    @DisplayName("Não deve permitir duplicação do Usuário")
    public void shouldNotDuplicateUser(){
        userService.createUser(new User(0L, "Angelina", "angelina@email.com", "12345678", "-"));

        HttpEntity<User> requestBody = new HttpEntity<User>(new User(0L, "Angelina", "angelina@email.com", "12345678", "-"));

        ResponseEntity<User> responseBody = testRestTemplate.exchange(
                "/users/create",
                HttpMethod.POST,
                requestBody,
                User.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, responseBody.getStatusCode());
    }

    @Test
    @DisplayName("Atualizar Usuário")
    public void shouldUpdateAnUser(){
        Optional<User> registeredUser = userService.createUser(new User(0L, "Armando", "armando_pereira@email.com", "armandinho", "-"));

        User updatedUser = new User(registeredUser.get().getId(), "Armando Pereira", "armando_pereira@email.com", "armandinho", "-");

        HttpEntity<User> requestBody = new HttpEntity<User>(updatedUser);

        ResponseEntity<User> responseBody = testRestTemplate.withBasicAuth("root@root.com", "rootroot")
                .exchange(
                        "/users/update",
                        HttpMethod.PUT,
                        requestBody,
                        User.class
                );

        assertEquals(HttpStatus.OK, responseBody.getStatusCode());
    }

    @Test
    @DisplayName("Listar Usuários")
    public void shouldDisplayAllUsers(){

        userService.createUser(new User(0L,
                "Leonardo", "leonardo@email.com", "leonardo123", "-"));
        userService.createUser(new User(0L,
                "Dalila", "dalila@email.com", "dalila123", "-"));

        ResponseEntity<String> response = testRestTemplate
                .withBasicAuth("root@root.com", "rootroot")
                .exchange(
                        "/users/all",
                        HttpMethod.GET,
                        null,
                        String.class
                );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
