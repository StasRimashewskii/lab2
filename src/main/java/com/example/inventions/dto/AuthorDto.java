package com.example.inventions.dto;

import java.util.Set;

public class AuthorDto {
    private Long id;
    private String name;
    private String country;
    private Set<InventionDto> inventions;

    public AuthorDto() {}

    public AuthorDto(Long id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

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