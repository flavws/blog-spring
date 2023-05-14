package com.generation.personalblog.controller;

import com.generation.personalblog.model.User;
import com.generation.personalblog.model.UserLogin;
import com.generation.personalblog.repository.UserRepository;
import com.generation.personalblog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity <List<User>> getAll(){

        return ResponseEntity.ok(userRepository.findAll());

    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(res -> ResponseEntity.ok(res))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<UserLogin> userAuthentication(@RequestBody Optional<UserLogin> userLogin){

        return userService.userAuthentication(userLogin)
                .map(res -> ResponseEntity.status(HttpStatus.OK).body(res))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }


    @PostMapping("/cadastrar")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {

        return userService.createUser(user)
                .map(res -> ResponseEntity.status(HttpStatus.CREATED).body(res))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

    }

    @PutMapping("/atualizar")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {

        return userService.updateUser(user)
                .map(res -> ResponseEntity.status(HttpStatus.OK).body(res))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }
}
