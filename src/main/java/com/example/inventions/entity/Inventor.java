package com.example.inventions.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventors")
public class Inventor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String expertise;

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

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public Invention getInvention() {
        return invention;
    }

    public void setInvention(Invention invention) {
        this.invention = invention;
    }
}