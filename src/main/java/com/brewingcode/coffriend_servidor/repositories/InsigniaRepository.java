package com.brewingcode.coffriend_servidor.repositories;

import com.brewingcode.coffriend_servidor.entities.Insignia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InsigniaRepository extends JpaRepository<Insignia, Integer> {
    List<Insignia> findByIsDemo(Boolean isDemo);
}
