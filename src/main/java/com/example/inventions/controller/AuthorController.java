package com.example.inventions.controller;

import com.example.inventions.dto.AuthorDto;
import com.example.inventions.dto.AuthorFullDto;
import com.example.inventions.dto.InventionDto;
import com.example.inventions.service.AuthorService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<AuthorDto>> getAuthorsByCountry(@PathVariable String country) {
        return ResponseEntity.ok(authorService.getAuthorsByCountry(country));
    }

    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorFullDto> getAuthorWithInventions(@PathVariable Long authorId) {
        return ResponseEntity.ok(authorService.getAuthorWithInventions(authorId));
    }

    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(@RequestBody AuthorDto authorDto) {
        return ResponseEntity.ok(authorService.createAuthor(authorDto));
    }

    @PostMapping("/{authorId}/inventions/{inventionId}")
    public ResponseEntity<AuthorDto> addInventionToAuthor(
            @PathVariable Long authorId,
            @PathVariable Long inventionId) {
        return ResponseEntity.ok(authorService.addInventionToAuthor(authorId, inventionId));
    }

    @DeleteMapping("/{authorId}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long authorId) {
        authorService.removeAuthor(authorId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{authorId}/inventions")
    public ResponseEntity<List<InventionDto>> getInventionsByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(authorService.getInventionsByAuthor(authorId));
    }
}