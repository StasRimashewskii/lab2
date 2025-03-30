package com.example.inventions.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventionFullDto {
    private Long id;
    private String title;
    private String description;
    private String instruction;
    private Set<CategoryDto> categories;  // Изменено с ingredients на categories
    private Set<InventorDto> inventors;   // Изменено с reviews на inventors

    // Геттеры и сеттеры
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public void setCategories(Set<CategoryDto> categories) {
        this.categories = categories;
    }

    public void setInventors(Set<InventorDto> inventors) {
        this.inventors = inventors;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getInstruction() {
        return instruction;
    }

    public Set<CategoryDto> getCategories() {
        return categories;
    }

    public Set<InventorDto> getInventors() {
        return inventors;
    }
}