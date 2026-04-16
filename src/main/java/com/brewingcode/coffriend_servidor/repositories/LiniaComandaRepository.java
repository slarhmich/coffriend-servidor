package com.brewingcode.coffriend_servidor.repositories;

import com.brewingcode.coffriend_servidor.entities.LiniaComanda;
import com.brewingcode.coffriend_servidor.entities.LiniaComandaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LiniaComandaRepository extends JpaRepository<LiniaComanda, LiniaComandaId> {
    List<LiniaComanda> findByComandaId(Integer comandaId);
}
