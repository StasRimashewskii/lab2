package com.example.inventions.dto;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AuthorFullDtoTest {

    @Test
    void testIdSetterAndGetter() {
        AuthorFullDto authorFullDto = new AuthorFullDto();
        Long expectedId = 1L;
        authorFullDto.setId(expectedId);

        assertEquals(expectedId, authorFullDto.getId());
    }

    @Test
    void testNameSetterAndGetter() {
        AuthorFullDto authorFullDto = new AuthorFullDto();
        String expectedName = "John Doe";
        authorFullDto.setName(expectedName);

        assertEquals(expectedName, authorFullDto.getName());
    }

    @Test
    void testCountrySetterAndGetter() {
        AuthorFullDto authorFullDto = new AuthorFullDto();
        String expectedCountry = "USA";
        authorFullDto.setCountry(expectedCountry);

        assertEquals(expectedCountry, authorFullDto.getCountry());
    }

    @Test
    void testInventionsSetterAndGetter() {
        AuthorFullDto authorFullDto = new AuthorFullDto();
        Set<InventionDto> expectedInventions = new HashSet<>();
        expectedInventions.add(new InventionDto());
        authorFullDto.setInventions(expectedInventions);

        assertEquals(expectedInventions, authorFullDto.getInventions());
    }

    @Test
    void testInventionsNullSetterAndGetter() {
        AuthorFullDto authorFullDto = new AuthorFullDto();
        authorFullDto.setInventions(null);

        assertNull(authorFullDto.getInventions());
    }

    @Test
    void testConstructorWithAllFields() {
        Set<InventionDto> inventions = new HashSet<>();
        inventions.add(new InventionDto());

        AuthorFullDto authorFullDto = new AuthorFullDto();
        authorFullDto.setId(1L);
        authorFullDto.setName("John Doe");
        authorFullDto.setCountry("USA");
        authorFullDto.setInventions(inventions);

        assertEquals(1L, authorFullDto.getId());
        assertEquals("John Doe", authorFullDto.getName());
        assertEquals("USA", authorFullDto.getCountry());
        assertEquals(inventions, authorFullDto.getInventions());
    }
}
