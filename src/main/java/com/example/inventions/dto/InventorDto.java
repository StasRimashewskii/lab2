package com.example.inventions.dto;

public class InventorDto {
    private Long id;
    private String name;      // Изменено с message на name
    private String expertise; // Аналог rating, но для изобретателя

    public InventorDto() {}

    public InventorDto(Long id, String name, String expertise) {
        this.id = id;
        this.name = name;
        this.expertise = expertise;
    }

    // Геттеры и сеттеры
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}