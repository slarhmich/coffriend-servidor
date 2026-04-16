package com.brewingcode.coffriend_servidor.repositories;

import com.brewingcode.coffriend_servidor.entities.Usuari;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuariRepository extends JpaRepository<Usuari, Integer> {
    Optional<Usuari> findByEmail(String email);
    boolean existsByEmail(String email);
}