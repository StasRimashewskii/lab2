package com.example.inventions.dto;

public class InventorDto {
    private Long id;
    private String name;
    private String country;

    public InventorDto() {}

    public InventorDto(Long id, String name, String country) {
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
}