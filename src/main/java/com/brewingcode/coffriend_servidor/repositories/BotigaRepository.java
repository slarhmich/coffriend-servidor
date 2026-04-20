package com.brewingcode.coffriend_servidor.repositories;

import com.brewingcode.coffriend_servidor.entities.Botiga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BotigaRepository extends JpaRepository<Botiga, Integer> {
    List<Botiga> findByIsDemo(Boolean isDemo);
}
