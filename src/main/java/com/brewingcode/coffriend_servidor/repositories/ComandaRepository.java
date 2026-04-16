package com.brewingcode.coffriend_servidor.repositories;

import com.brewingcode.coffriend_servidor.entities.Comanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComandaRepository extends JpaRepository<Comanda, Integer> {
    @Query("SELECT c FROM Comanda c WHERE c.usuari.id = ?1")
    List<Comanda> findByIdUsuari(Integer idUsuari);
    
    @Query("SELECT c FROM Comanda c WHERE c.botiga.id = ?1")
    List<Comanda> findByIdBotiga(Integer idBotiga);
}
