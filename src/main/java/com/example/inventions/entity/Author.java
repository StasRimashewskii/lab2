package com.example.inventions.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String country;

    @OneToMany(mappedBy = "author",
            cascade = {},  // Убираем все каскадные операции
            orphanRemoval = false,  // Отключаем orphanRemoval
            fetch = FetchType.LAZY)
    private Set<Invention> inventions = new HashSet<>();

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

    public Set<Invention> getInventions() {
        return inventions;
    }

    public void setInventions(Set<Invention> inventions) {
        this.inventions = inventions;
    }
}