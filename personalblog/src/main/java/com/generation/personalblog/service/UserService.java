package com.generation.personalblog.service;

import com.generation.personalblog.model.User;
import com.generation.personalblog.model.UserLogin;
import com.generation.personalblog.repository.UserRepository;
import com.generation.personalblog.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public Optional<User> createUser(User user) {

        if (userRepository.findByUser(user.getUser()).isPresent())
            return Optional.empty();

        user.setPassword(passwordCriptare(user.getPassword()));

        return Optional.of(userRepository.save(user));

    }

    public Optional<User> updateUser(User user) {

        if(userRepository.findById(user.getId()).isPresent()) {

            Optional<User> findUser = userRepository.findByUser(user.getUser());

            if ( (findUser.isPresent()) && ( findUser.get().getId() != user.getId()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

            user.setPassword(passwordCriptare(user.getPassword()));

            return Optional.ofNullable(userRepository.save(user));

        }

        return Optional.empty();

    }

    public Optional<UserLogin> userAuthentication(Optional<UserLogin> userLogin) {

        var credentials = new UsernamePasswordAuthenticationToken(userLogin.get().getUser(), userLogin.get().getPassword());

        Authentication authentication = authenticationManager.authenticate(credentials);

        if (authentication.isAuthenticated()) {

            Optional<User> user = userRepository.findByUser(userLogin.get().getUser());

            if (user.isPresent()) {
                userLogin.get().setId(user.get().getId());
                userLogin.get().setName(user.get().getName());
                userLogin.get().setPicture(user.get().getPicture());
                userLogin.get().setToken(generateToken(userLogin.get().getUser()));
                userLogin.get().setPassword("");
                return userLogin;
            }
        }
        return Optional.empty();
    }

    private String passwordCriptare(String password) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.encode(password);

    }

    private String generateToken(String user) {
        return "Bearer " + jwtService.generateToken(user);
    }

}
