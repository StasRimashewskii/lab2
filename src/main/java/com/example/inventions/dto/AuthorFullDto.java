package com.example.inventions.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorFullDto {
    private Long id;
    private String name;
    private String country;
    private Set<InventionDto> inventions;

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Set<InventionDto> getInventions() {
        return inventions;
    }

    public void setInventions(Set<InventionDto> inventions) {
        this.inventions = inventions;
    }
}