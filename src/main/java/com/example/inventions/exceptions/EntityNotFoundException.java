package com.example.inventions.exceptions;

// Кастомная версия стандартного исключения
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}