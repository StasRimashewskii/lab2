package com.example.inventions.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventionDto {
    private String title;
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