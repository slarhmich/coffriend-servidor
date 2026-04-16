package com.brewingcode.coffriend_servidor.repositories;

import com.brewingcode.coffriend_servidor.entities.UsuariInsignia;
import com.brewingcode.coffriend_servidor.entities.UsuariInsigniaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuariInsigniaRepository extends JpaRepository<UsuariInsignia, UsuariInsigniaId> {
    @Query("SELECT ui FROM UsuariInsignia ui WHERE ui.usuari.id = ?1")
    List<UsuariInsignia> findByUsuariId(Integer usuariId);
}
