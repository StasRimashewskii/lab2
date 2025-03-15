package com.example.inventions.service;

import com.example.inventions.model.Invention;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventionService {

    private final List<Invention> inventions = new ArrayList<>();

    public InventionService() {
        // Добавляем тестовые данные
        inventions.add(new Invention(1L, "Лампочка", "Электрическая лампочка, изобретённая Томасом Эдисоном."));
        inventions.add(new Invention(2L, "Телефон", "Устройство для передачи голоса, изобретённое Александром Беллом."));
        inventions.add(new Invention(3L, "Интернет", "Система глобальной связи, изобретённая в 20 веке."));
    }

    // Получение всех изобретений
    public List<Invention> getAllInventions() {
        return inventions;
    }

    // Поиск изобретения по ID
    public Invention getInventionById(Long id) {
        return inventions.stream()
                .filter(invention -> invention.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Добавление нового изобретения
    public void addInvention(Invention invention) {
        inventions.add(invention);
    }
}