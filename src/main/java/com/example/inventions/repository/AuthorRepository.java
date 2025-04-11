package com.example.inventions.repository;

import com.example.inventions.entity.Invention;
import com.example.inventions.entity.Author;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByInvention(Invention invention);
}