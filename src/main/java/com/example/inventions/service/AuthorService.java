package com.example.inventions.service;

import com.example.inventions.dto.AuthorDto;
import com.example.inventions.dto.AuthorFullDto;
import com.example.inventions.dto.CategoryDto;
import com.example.inventions.dto.InventionDto;
import com.example.inventions.entity.Author;
import com.example.inventions.entity.Invention;
import com.example.inventions.mapper.AuthorMapper;
import com.example.inventions.repository.AuthorRepository;
import com.example.inventions.repository.InventionRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final InventionRepository inventionRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository,
                         InventionRepository inventionRepository,
                         AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.inventionRepository = inventionRepository;
        this.authorMapper = authorMapper;
    }

    @Transactional(readOnly = true)
    public List<AuthorDto> getAuthorsByCountry(String country) {
        List<Author> authors = authorRepository.findByCountry(country);
        return authors.stream()
                .map(author -> {
                    AuthorDto dto = authorMapper.convertToDto(author);
                    if (author.getInventions() != null && !author.getInventions().isEmpty()) {
                        dto.setInventions(authorMapper.convertInventionsToDto(author.getInventions()));
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AuthorFullDto getAuthorWithInventions(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + authorId));
        return authorMapper.convertToFullDto(author);
    }

    @Transactional
    public AuthorDto createAuthor(AuthorDto authorDto) {
        Author author = new Author();
        author.setName(authorDto.getName());
        author.setCountry(authorDto.getCountry());

        Author savedAuthor = authorRepository.save(author);
        return authorMapper.convertToDto(savedAuthor);
    }

    @Transactional
    public AuthorDto addInventionToAuthor(Long authorId, Long inventionId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + authorId));

        Invention invention = inventionRepository.findById(inventionId)
                .orElseThrow(() -> new EntityNotFoundException("Invention not found with id " + inventionId));

        invention.setAuthor(author);
        inventionRepository.save(invention);

        return authorMapper.convertToDto(author);
    }

    @Transactional
    public void removeAuthor(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with id " + authorId));

        // Отвязываем изобретения от автора, но не удаляем их
        List<Invention> inventions = inventionRepository.findByAuthorId(authorId);
        for (Invention invention : inventions) {
            invention.setAuthor(null);
            inventionRepository.save(invention);
        }

        authorRepository.delete(author);
    }

    @Transactional(readOnly = true)
    public List<InventionDto> getInventionsByAuthor(Long authorId) {
        return inventionRepository.findByAuthorId(authorId).stream()
                .map(invention -> {
                    InventionDto dto = new InventionDto();
                    dto.setTitle(invention.getTitle());
                    dto.setDescription(invention.getDescription());
                    dto.setInstruction(invention.getInstruction());
                    dto.setCategories(invention.getCategories().stream()
                            .map(category -> {
                                CategoryDto categoryDto = new CategoryDto();
                                categoryDto.setName(category.getName());
                                return categoryDto;
                            })
                            .collect(Collectors.toSet()));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AuthorDto getAuthorByName(String name) {
        Author author = authorRepository.findByNameWithInventions(name)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with name " + name));

        AuthorDto dto = authorMapper.convertToDto(author);
        if (author.getInventions() != null && !author.getInventions().isEmpty()) {
            dto.setInventions(authorMapper.convertInventionsToDto(author.getInventions()));
        }

        return dto;
    }
}