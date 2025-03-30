package com.example.inventions.repository;

import com.example.inventions.entity.Invention;
import com.example.inventions.entity.Inventor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InventorRepository extends JpaRepository<Inventor, Long> {

    List<Inventor> findByInvention(Invention invention);
}