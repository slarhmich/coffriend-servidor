package com.brewingcode.coffriend_servidor.repositories;

import com.brewingcode.coffriend_servidor.entities.Producte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProducteRepository extends JpaRepository<Producte, Integer> {
    @Query("SELECT p FROM Producte p WHERE p.botiga.id = ?1")
    List<Producte> findByIdBotiga(Integer idBotiga);
}
