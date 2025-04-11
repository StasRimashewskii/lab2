package com.example.inventions.entity;

import jakarta.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invention_id", nullable = false)
    private Invention invention;

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

    public Invention getInvention() {
        return invention;
    }

    public void setInvention(Invention invention) {
        this.invention = invention;
    }
}