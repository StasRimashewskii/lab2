package com.example.inventions.repository;

import com.example.inventions.entity.Category;
import com.example.inventions.entity.Invention;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventionRepository extends JpaRepository<Invention, Long> {

    List<Invention> findByTitleContainingIgnoreCase(String title);

    List<Invention> findByCategories(Category category);
}