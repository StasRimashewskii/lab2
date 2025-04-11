package com.example.inventions.service;

import com.example.inventions.dto.AuthorDto;
import com.example.inventions.entity.Invention;
import com.example.inventions.entity.Author;
import com.example.inventions.repository.InventionRepository;
import com.example.inventions.repository.AuthorRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final InventionRepository inventionRepository;

    public AuthorService(AuthorRepository authorRepository,
                         InventionRepository inventionRepository) {
        this.authorRepository = authorRepository;
        this.inventionRepository = inventionRepository;
    }

    @Transactional
    public AuthorDto addAuthorToInvention(Long inventionId, AuthorDto authorDto) {
        Invention invention = inventionRepository.findById(inventionId)
                .orElseThrow(() -> new EntityNotFoundException("Invention not found with id " + inventionId));

        Author author = new Author();
        author.setName(authorDto.getName());
        author.setCountry(authorDto.getCountry());
        author.setInvention(invention);

        Author savedAuthor = authorRepository.save(author);
        return convertToDto(savedAuthor);
    }

    @Transactional
    public void deleteAuthorFromInvention(Long inventionId, Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + authorId));

        if (!author.getInvention().getId().equals(inventionId)) {
            throw new IllegalArgumentException("Author does not belong to the specified invention");
        }

        authorRepository.delete(author);
    }

    @Transactional
    public AuthorDto updateAuthorForInvention(Long inventionId, Long authorId, AuthorDto authorDto) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + authorId));

        if (!author.getInvention().getId().equals(inventionId)) {
            throw new IllegalArgumentException("Author does not belong to the specified invention");
        }

        author.setName(authorDto.getName());
        author.setCountry(authorDto.getCountry());
        Author updatedAuthor = authorRepository.save(author);

        return convertToDto(updatedAuthor);
    }

    @Transactional(readOnly = true)
    public Set<AuthorDto> getAllAuthorsForInvention(Long inventionId) {
        Invention invention = inventionRepository.findById(inventionId)
                .orElseThrow(() -> new EntityNotFoundException("Invention not found with id " + inventionId));

        return authorRepository.findByInvention(invention).stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
    }

    private AuthorDto convertToDto(Author author) {
        return new AuthorDto(
                author.getId(),
                author.getName(),
                author.getCountry()
        );
    }
}
