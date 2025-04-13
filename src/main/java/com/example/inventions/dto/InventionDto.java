package com.example.inventions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class InventionDto {
    @NotBlank(message = "Название изобретения не может быть пустым")
    @Size(max = 150, message = "Название не должно превышать 150 символов")
    private String title;

    @NotBlank(message = "Описание изобретения не может быть пустым")
    private String description;

    private String instruction;

    private Set<CategoryDto> categories;

    // Геттеры и сеттеры
    public String getTitle() {
        return title;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDto> categories) {
        this.categories = categories;
    }
}