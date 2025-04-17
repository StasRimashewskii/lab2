package com.example.inventions.entity;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InventionTest {

    @Test
    void testConstructorAndBasicFields() {
        Invention invention = new Invention("Название", "Описание", "Инструкция");

        assertEquals("Название", invention.getTitle(), "Название должно совпадать");
        assertEquals("Описание", invention.getDescription(), "Описание должно совпадать");
        assertEquals("Инструкция", invention.getInstruction(), "Инструкция должна совпадать");
        assertNotNull(invention.getCategories(), "Категории должны быть инициализированы");
        assertTrue(invention.getCategories().isEmpty(), "Категории должны быть пустыми по умолчанию");
    }

    @Test
    void testSetAndGetId() {
        Invention invention = new Invention();
        invention.setId(42L);
        assertEquals(42L, invention.getId(), "ID должен быть установлен корректно");
    }

    @Test
    void testSetAndGetTitle() {
        Invention invention = new Invention();
        invention.setTitle("Тестовое название");
        assertEquals("Тестовое название", invention.getTitle());
    }

    @Test
    void testSetAndGetDescription() {
        Invention invention = new Invention();
        invention.setDescription("Тестовое описание");
        assertEquals("Тестовое описание", invention.getDescription());
    }

    @Test
    void testSetAndGetInstruction() {
        Invention invention = new Invention();
        invention.setInstruction("Инструкция для теста");
        assertEquals("Инструкция для теста", invention.getInstruction());
    }

    @Test
    void testSetAndGetCategories() {
        Invention invention = new Invention();
        Set<Category> categories = new HashSet<>();
        Category category = new Category();
        category.setId(1L);
        category.setName("Электроника");
        categories.add(category);

        invention.setCategories(categories);

        assertEquals(1, invention.getCategories().size(), "Должна быть одна категория");
        assertEquals("Электроника", invention.getCategories().iterator().next().getName());
    }

    @Test
    void testSetAndGetAuthor() {
        Invention invention = new Invention();
        Author author = new Author();
        author.setId(100L);
        author.setName("Иван Иванов");

        invention.setAuthor(author);

        assertNotNull(invention.getAuthor(), "Автор должен быть установлен");
        assertEquals("Иван Иванов", invention.getAuthor().getName());
    }
}
