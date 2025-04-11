package com.example.inventions.repository;

import com.example.inventions.entity.Invention;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InventionRepository extends JpaRepository<Invention, Long> {

    // JPQL запрос
    @Query("SELECT i FROM Invention i WHERE LOWER(i.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Invention> findByTitleContainingIgnoreCase(@Param("title") String title);

    // Native SQL запрос
    @Query(value = "SELECT * FROM inventions WHERE LOWER(title) LIKE LOWER(CONCAT('%', :title, '%'))",
            nativeQuery = true)
    List<Invention> findByTitleContainingIgnoreCaseNative(@Param("title") String title);

    @Query("SELECT DISTINCT i FROM Invention i JOIN i.categories c WHERE LOWER(c.name) IN :categoryNames")
    List<Invention> findByCategoriesNameIn(@Param("categoryNames") List<String> categoryNames);

    @Query("SELECT DISTINCT i FROM Invention i JOIN i.authors auth WHERE auth.country = :country")
    List<Invention> findByAuthorsCountry(@Param("country") String country);

    @Query("""
    SELECT i FROM Invention i WHERE SIZE(i.categories) = :categoryCount AND NOT EXISTS (
        SELECT c FROM Category c WHERE c MEMBER OF i.categories AND LOWER(c.name) NOT IN :categoryNames
    )
""")
    List<Invention> findByExactCategories(@Param("categoryNames") List<String> categoryNames,
                                          @Param("categoryCount") long categoryCount);
}
