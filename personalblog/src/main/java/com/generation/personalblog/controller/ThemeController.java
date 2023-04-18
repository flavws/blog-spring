package com.generation.personalblog.controller;

import com.generation.personalblog.model.Theme;
import com.generation.personalblog.repository.ThemeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/themes")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ThemeController {

    @Autowired
    private ThemeRepository repository;

    @GetMapping
    public ResponseEntity<List<Theme>> getAll(){
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theme> getById(@PathVariable Long id){
        return repository.findById(id).map(res -> ResponseEntity.ok(res)).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/description/{description}")
    public ResponseEntity<List<Theme>> getByTitle(@PathVariable String description){
        return ResponseEntity.ok(repository.findAllByDescriptionContainingIgnoreCase(description));
    }

    @PostMapping
    public ResponseEntity<Theme> createTheme(@Valid @RequestBody Theme theme){
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(theme));
    }

    @PutMapping
    public ResponseEntity<Theme> updateTheme(@Valid @RequestBody Theme theme){
        return repository.findById(theme.getId())
                .map(res -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(repository.save(theme)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTheme(@PathVariable Long id){
        Optional<Theme> theme = repository.findById(id);

        if (theme.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        repository.deleteById(id);
    }
}
