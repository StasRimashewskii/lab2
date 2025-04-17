package com.example.inventions.service;

import com.example.inventions.dto.AuthorDto;
import com.example.inventions.dto.AuthorFullDto;
import com.example.inventions.dto.InventionDto;
import com.example.inventions.entity.Author;
import com.example.inventions.entity.Invention;
import com.example.inventions.exceptions.EntityNotFoundException;
import com.example.inventions.mapper.AuthorMapper;
import com.example.inventions.repository.AuthorRepository;
import com.example.inventions.repository.InventionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private InventionRepository inventionRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void getAuthorsByCountry_ShouldReturnAuthors() {
        // Arrange
        String country = "USA";
        Author author = new Author();
        author.setCountry(country);
        AuthorDto authorDto = new AuthorDto();
        authorDto.setCountry(country);

        when(authorRepository.findByCountry(country)).thenReturn(Collections.singletonList(author));
        when(authorMapper.convertToDto(author)).thenReturn(authorDto);

        // Act
        List<AuthorDto> result = authorService.getAuthorsByCountry(country);

        // Assert
        assertEquals(1, result.size());
        assertEquals(country, result.get(0).getCountry());
        verify(authorRepository).findByCountry(country);
    }

    @Test
    void getAuthorWithInventions_ShouldReturnFullDto() {
        // Arrange
        Long authorId = 1L;
        Author author = new Author();
        author.setId(authorId);
        AuthorFullDto fullDto = new AuthorFullDto();
        fullDto.setId(authorId);

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(authorMapper.convertToFullDto(author)).thenReturn(fullDto);

        // Act
        AuthorFullDto result = authorService.getAuthorWithInventions(authorId);

        // Assert
        assertNotNull(result);
        assertEquals(authorId, result.getId());
        verify(authorRepository).findById(authorId);
    }

    @Test
    void getAuthorWithInventions_ShouldThrowWhenNotFound() {
        // Arrange
        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            authorService.getAuthorWithInventions(authorId);
        });
    }

    @Test
    void createAuthor_ShouldSaveAndReturnDto() {
        // Arrange
        AuthorDto inputDto = new AuthorDto();
        inputDto.setName("Test Author");
        inputDto.setCountry("Test Country");

        Author savedAuthor = new Author();
        savedAuthor.setName(inputDto.getName());
        savedAuthor.setCountry(inputDto.getCountry());

        AuthorDto outputDto = new AuthorDto();
        outputDto.setName(inputDto.getName());
        outputDto.setCountry(inputDto.getCountry());

        // Исправленная строка:
        when(authorMapper.convertToDto(any(Author.class))).thenReturn(outputDto);
        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        // Act
        AuthorDto result = authorService.createAuthor(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(inputDto.getName(), result.getName());
        assertEquals(inputDto.getCountry(), result.getCountry());
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void addInventionToAuthor_ShouldUpdateInvention() {
        // Arrange
        Long authorId = 1L;
        Long inventionId = 1L;

        Author author = new Author();
        author.setId(authorId);

        Invention invention = new Invention();
        invention.setId(inventionId);

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(inventionRepository.findById(inventionId)).thenReturn(Optional.of(invention));
        when(authorMapper.convertToDto(author)).thenReturn(authorDto);

        // Act
        AuthorDto result = authorService.addInventionToAuthor(authorId, inventionId);

        // Assert
        assertNotNull(result);
        assertEquals(authorId, result.getId());
        verify(inventionRepository).save(invention);
        assertEquals(author, invention.getAuthor());
    }

    @Test
    void removeAuthor_ShouldUnlinkInventionsAndDelete() {
        // Arrange
        Long authorId = 1L;
        Author author = new Author();
        author.setId(authorId);

        Invention invention = new Invention();
        invention.setId(1L);
        invention.setAuthor(author);

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(inventionRepository.findByAuthorId(authorId)).thenReturn(Collections.singletonList(invention));

        // Act
        authorService.removeAuthor(authorId);

        // Assert
        verify(inventionRepository).save(invention);
        assertNull(invention.getAuthor());
        verify(authorRepository).delete(author);
    }

    @Test
    void getInventionsByAuthor_ShouldReturnInventions() {
        // Arrange
        Long authorId = 1L;
        Invention invention = new Invention();
        invention.setTitle("Test Invention");

        when(inventionRepository.findByAuthorId(authorId)).thenReturn(Collections.singletonList(invention));

        // Act
        List<InventionDto> result = authorService.getInventionsByAuthor(authorId);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Test Invention", result.get(0).getTitle());
    }

    @Test
    void getAuthorByName_ShouldReturnAuthorWithInventions() {
        // Arrange
        String name = "Test Author";
        Author author = new Author();
        author.setName(name);
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName(name);

        when(authorRepository.findByNameWithInventions(name)).thenReturn(Optional.of(author));
        when(authorMapper.convertToDto(author)).thenReturn(authorDto);

        // Act
        AuthorDto result = authorService.getAuthorByName(name);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        verify(authorRepository).findByNameWithInventions(name);
    }

    @Test
    void getAuthorByName_ShouldThrowWhenNotFound() {
        // Arrange
        String name = "Non-existent Author";
        when(authorRepository.findByNameWithInventions(name)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            authorService.getAuthorByName(name);
        });
    }
}