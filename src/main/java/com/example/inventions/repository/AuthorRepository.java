package com.example.inventions.repository;

import com.example.inventions.entity.Author;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Добавляем JOIN FETCH для загрузки изобретений
    @Query("SELECT DISTINCT a FROM Author a LEFT JOIN FETCH a.inventions WHERE a.country = :country")
    List<Author> findByCountryWithInventions(@Param("country") String country);

    // Старый метод оставляем для совместимости
    List<Author> findByCountry(String country);

    // Основной метод поиска по имени с подгрузкой изобретений
    @Query("SELECT DISTINCT a FROM Author a LEFT JOIN FETCH a.inventions WHERE a.name = :name")
    Optional<Author> findByNameWithInventions(@Param("name") String name);
}
