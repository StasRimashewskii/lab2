package com.example.inventions.repository;

import com.example.inventions.entity.Invention;
import com.example.inventions.entity.Inventor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventorRepository extends JpaRepository<Inventor, Long> {

    List<Inventor> findByInvention(Invention invention);
}