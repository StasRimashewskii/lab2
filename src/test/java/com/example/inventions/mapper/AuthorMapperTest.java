package com.example.inventions.mapper;

import com.example.inventions.dto.AuthorDto;
import com.example.inventions.dto.AuthorFullDto;
import com.example.inventions.dto.InventionDto;
import com.example.inventions.entity.Author;
import com.example.inventions.entity.Invention;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorMapperTest {

    private InventionMapper inventionMapper;
    private AuthorMapper authorMapper;

    @BeforeEach
    void setUp() {
        inventionMapper = mock(InventionMapper.class);
        authorMapper = new AuthorMapper(inventionMapper);
    }

    @Test
    void testConvertToDto() {
        Author author = new Author();
        author.setId(1L);
        author.setName("Никола Тесла");
        author.setCountry("Сербия");

        AuthorDto dto = authorMapper.convertToDto(author);

        assertEquals(1L, dto.getId());
        assertEquals("Никола Тесла", dto.getName());
        assertEquals("Сербия", dto.getCountry());
    }

    @Test
    void testConvertToFullDtoWithoutInventions() {
        Author author = new Author();
        author.setId(2L);
        author.setName("Томас Эдисон");
        author.setCountry("США");

        AuthorFullDto dto = authorMapper.convertToFullDto(author);

        assertEquals(2L, dto.getId());
        assertEquals("Томас Эдисон", dto.getName());
        assertEquals("США", dto.getCountry());
        assertNotNull(dto.getInventions(), "Список изобретений не должен быть null");
        assertTrue(dto.getInventions().isEmpty(), "Список изобретений должен быть пустым");
    }

    @Test
    void testConvertToFullDtoWithInventions() {
        Invention invention = new Invention();
        invention.setId(1L);
        invention.setTitle("Лампа");

        Set<Invention> inventions = new HashSet<>();
        inventions.add(invention);

        Author author = new Author();
        author.setId(3L);
        author.setName("Тестовый Автор");
        author.setCountry("Неизвестно");
        author.setInventions(inventions);

        InventionDto inventionDto = mock(InventionDto.class);
        when(inventionDto.getTitle()).thenReturn("Лампа");

        when(inventionMapper.convertToDto(invention)).thenReturn(inventionDto);

        AuthorFullDto dto = authorMapper.convertToFullDto(author);

        assertEquals(3L, dto.getId());
        assertEquals("Тестовый Автор", dto.getName());
        assertEquals("Неизвестно", dto.getCountry());
        assertNotNull(dto.getInventions());
        assertEquals(1, dto.getInventions().size());

        InventionDto resultDto = dto.getInventions().iterator().next();
        assertEquals("Лампа", resultDto.getTitle());
    }

    @Test
    void testConvertInventionsToDto() {
        Invention invention = new Invention();
        invention.setId(5L);
        invention.setTitle("Радио");

        InventionDto inventionDto = mock(InventionDto.class);
        when(inventionDto.getTitle()).thenReturn("Радио");

        when(inventionMapper.convertToDto(invention)).thenReturn(inventionDto);

        Set<Invention> inventions = new HashSet<>();
        inventions.add(invention);

        Set<InventionDto> result = authorMapper.convertInventionsToDto(inventions);

        assertEquals(1, result.size());
        assertEquals("Радио", result.iterator().next().getTitle());
    }
}
