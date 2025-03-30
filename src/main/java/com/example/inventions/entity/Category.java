package com.example.inventions.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<Invention> inventions = new HashSet<>();

    public Category() {}

    public Category(String name) {
        this.name = name;
    }

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

    public Set<Invention> getInventions() {
        return inventions;
    }

    public void setInventions(Set<Invention> inventions) {
        this.inventions = inventions;
    }
}