package com.generation.personalblog.controller;

import com.generation.personalblog.model.Theme;
import com.generation.personalblog.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/themes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ThemeController {

    @Autowired
    private ThemeRepository repository;

    @GetMapping
    public ResponseEntity<List<Theme>> getAll(){
        return ResponseEntity.ok().body(repository.findAll());
    }
}
