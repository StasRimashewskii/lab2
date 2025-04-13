package com.example.inventions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public class AuthorDto {
    private Long id;

    @NotBlank(message = "Имя автора не может быть пустым")
    @Size(max = 100, message = "Имя автора не должно превышать 100 символов")
    private String name;

    @NotBlank(message = "Страна не может быть пустой")
    @Size(max = 100, message = "Название страны не должно превышать 100 символов")
    private String country;

    private Set<InventionDto> inventions;

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Set<InventionDto> getInventions() {
        return inventions;
    }

    public void setInventions(Set<InventionDto> inventions) {
        this.inventions = inventions;
    }
}