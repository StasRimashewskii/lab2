package com.example.inventions.controller;

import com.example.inventions.dto.AuthorDto;
import com.example.inventions.service.AuthorService;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventions")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/{inventionId}/authors")
    public ResponseEntity<AuthorDto> addAuthorToInvention(
            @PathVariable Long inventionId,
            @RequestBody AuthorDto authorDto) {
        AuthorDto addedAuthor = authorService.addAuthorToInvention(inventionId, authorDto);
        return ResponseEntity.ok(addedAuthor);
    }

    @DeleteMapping("/{inventionId}/authors/{authorId}")
    public ResponseEntity<Void> deleteAuthorFromInvention(
            @PathVariable Long inventionId,
            @PathVariable Long authorId) {
        authorService.deleteAuthorFromInvention(inventionId, authorId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{inventionId}/authors/{authorId}")
    public ResponseEntity<AuthorDto> updateAuthorForInvention(
            @PathVariable Long inventionId,
            @PathVariable Long authorId,
            @RequestBody AuthorDto authorDto) {
        AuthorDto updatedAuthor = authorService
                .updateAuthorForInvention(inventionId, authorId, authorDto);
        return ResponseEntity.ok(updatedAuthor);
    }

    @GetMapping("/{inventionId}/authors")
    public ResponseEntity<Set<AuthorDto>> getAllAuthorsForInvention(@PathVariable Long inventionId) {
        Set<AuthorDto> authors = authorService.getAllAuthorsForInvention(inventionId);
        return ResponseEntity.ok(authors);
    }
}
