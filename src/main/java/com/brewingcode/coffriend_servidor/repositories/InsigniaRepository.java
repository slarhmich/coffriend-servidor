package com.brewingcode.coffriend_servidor.repositories;

import com.brewingcode.coffriend_servidor.entities.Insignia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsigniaRepository extends JpaRepository<Insignia, Integer> {
}
