package com.example.inventions.dto;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InventionFullDtoTest {

    @Test
    void testIdSetterAndGetter() {
        InventionFullDto inventionFullDto = new InventionFullDto();
        Long expectedId = 1L;
        inventionFullDto.setId(expectedId);

        assertEquals(expectedId, inventionFullDto.getId());
    }

    @Test
    void testTitleSetterAndGetter() {
        InventionFullDto inventionFullDto = new InventionFullDto();
        String expectedTitle = "Invention Title";
        inventionFullDto.setTitle(expectedTitle);

        assertEquals(expectedTitle, inventionFullDto.getTitle());
    }

    @Test
    void testDescriptionSetterAndGetter() {
        InventionFullDto inventionFullDto = new InventionFullDto();
        String expectedDescription = "Invention Description";
        inventionFullDto.setDescription(expectedDescription);

        assertEquals(expectedDescription, inventionFullDto.getDescription());
    }

    @Test
    void testInstructionSetterAndGetter() {
        InventionFullDto inventionFullDto = new InventionFullDto();
        String expectedInstruction = "Invention Instruction";
        inventionFullDto.setInstruction(expectedInstruction);

        assertEquals(expectedInstruction, inventionFullDto.getInstruction());
    }

    @Test
    void testCategoriesSetterAndGetter() {
        InventionFullDto inventionFullDto = new InventionFullDto();
        Set<CategoryDto> expectedCategories = new HashSet<>();
        expectedCategories.add(new CategoryDto()); // Add a CategoryDto object for test purposes
        inventionFullDto.setCategories(expectedCategories);

        assertEquals(expectedCategories, inventionFullDto.getCategories());
    }

    @Test
    void testCategoriesNullSetterAndGetter() {
        InventionFullDto inventionFullDto = new InventionFullDto();
        inventionFullDto.setCategories(null);

        assertNull(inventionFullDto.getCategories());
    }

    @Test
    void testConstructorWithAllFields() {
        Set<CategoryDto> categories = new HashSet<>();
        categories.add(new CategoryDto()); // Add a CategoryDto object for test purposes

        InventionFullDto inventionFullDto = new InventionFullDto();
        inventionFullDto.setId(1L);
        inventionFullDto.setTitle("Invention Title");
        inventionFullDto.setDescription("Invention Description");
        inventionFullDto.setInstruction("Invention Instruction");
        inventionFullDto.setCategories(categories);

        assertEquals(1L, inventionFullDto.getId());
        assertEquals("Invention Title", inventionFullDto.getTitle());
        assertEquals("Invention Description", inventionFullDto.getDescription());
        assertEquals("Invention Instruction", inventionFullDto.getInstruction());
        assertEquals(categories, inventionFullDto.getCategories());
    }
}
