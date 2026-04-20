package com.brewingcode.coffriend_servidor.repositories;

import com.brewingcode.coffriend_servidor.entities.BadgeTrigger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BadgeTriggerRepository extends JpaRepository<BadgeTrigger, Integer> {
    
    /**
     * Find all active triggers for a specific badge
     */
    List<BadgeTrigger> findByInsigniaIdAndIsActiveTrue(Integer insigniaId);
    
    /**
     * Find all active triggers by trigger type
     */
    List<BadgeTrigger> findByTriggerTypeAndIsActiveTrue(BadgeTrigger.TriggerType triggerType);
}
