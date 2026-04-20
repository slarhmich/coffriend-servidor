package com.brewingcode.coffriend_servidor.service;

import com.brewingcode.coffriend_servidor.entities.*;
import com.brewingcode.coffriend_servidor.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

/**
 * Handles automatic gamification logic:
 * - Award points when orders are completed
 * - Auto-update levels based on points
 * - Auto-assign badges based on milestones
 */
@Service
public class GamificationService {

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private UsuariInsigniaRepository usuariInsigniaRepository;

    @Autowired
    private InsigniaRepository insigniaRepository;
    
    @Autowired
    private BadgeTriggerEvaluationService badgeTriggerEvaluationService;

    // Points thresholds for levels (5 levels)
    private static final int[] LEVEL_THRESHOLDS = {0, 100, 250, 500, 1000};

    /**
     * Process order completion: Award points and update level/badges
     * 
     * Called when order status changes to "completat"
     * 
     * Only applies to user (customer)
     * 
     * @param comanda the completed order
     */
    public void processOrderCompletion(Comanda comanda) {
        if (comanda == null || comanda.getUsuari() == null) {
            return;
        }

        Usuari usuari = comanda.getUsuari();

        if (!"client".equals(usuari.getRol())) {
            return;
        }

        int pointsEarned = calculatePointsFromOrder(comanda);

        if (pointsEarned > 0) {
            awardPoints(usuari, pointsEarned);
            updateLevel(usuari);
            checkAndAwardBadges(usuari, comanda);
        }
    }

    /**
     * Calculate points based on order total.
     * 
     * @param comanda the order
     * @return points to award
     */
    private int calculatePointsFromOrder(Comanda comanda) {
        if (comanda.getLinies() == null || comanda.getLinies().isEmpty()) {
            return 0;
        }

        BigDecimal total = comanda.getLinies().stream()
                .map(linea -> linea.getPreuUnitari().multiply(new BigDecimal(linea.getQuantitat())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.intValue(); // 1 point per €1
    }

    /**
     * Award points to customers
     * 
     * @param usuari the user
     * @param points points to add
     */
    public void awardPoints(Usuari usuari, int points) {
        if (usuari == null || !"client".equals(usuari.getRol())) {
            return;
        }

        int currentPoints = usuari.getPunts() != null ? usuari.getPunts() : 0;
        usuari.setPunts(currentPoints + points);
        usuariRepository.save(usuari);
    }

    /**
     * Auto-update customer level based on current points.
     * 
     * @param usuari the customer
     */
    public void updateLevel(Usuari usuari) {
        if (usuari == null || !"client".equals(usuari.getRol())) {
            return;
        }

        int currentPoints = usuari.getPunts() != null ? usuari.getPunts() : 0;
        int newLevel = 1;

        // Find appropriate level for current points
        for (int i = LEVEL_THRESHOLDS.length - 1; i >= 0; i--) {
            if (currentPoints >= LEVEL_THRESHOLDS[i]) {
                newLevel = i + 1; // Level is 1-indexed
                break;
            }
        }

        if (newLevel != (usuari.getNivell() != null ? usuari.getNivell() : 1)) {
            usuari.setNivell(newLevel);
            usuariRepository.save(usuari);
        }
    }

    /**
     * Check and auto-award badges based on configured triggers.
     * 
     * @param usuari the customer
     * @param comanda the order that triggered badge evaluation
     */
    private void checkAndAwardBadges(Usuari usuari, Comanda comanda) {
        if (usuari == null || !"client".equals(usuari.getRol()) || comanda == null) {
            return;
        }

        List<Integer> badgesToAward = badgeTriggerEvaluationService.evaluateBadgesForOrder(comanda);
        
        for (Integer badgeId : badgesToAward) {
            Optional<Insignia> insignia = insigniaRepository.findById(badgeId);
            if (insignia.isPresent()) {
                awardBadgeIfNotExists(usuari, insignia.get().getNom());
            }
        }
    }

    private void awardBadgeIfNotExists(Usuari usuari, String badgeName) {
        Optional<Insignia> insignia = insigniaRepository.findAll().stream()
                .filter(i -> badgeName.equals(i.getNom()))
                .findFirst();

        if (insignia.isEmpty()) {
            return;
        }

        boolean alreadyHas = usuariInsigniaRepository.findAll().stream()
                .anyMatch(ui -> ui.getUsuari().getId().equals(usuari.getId()) &&
                        ui.getInsignia().getId().equals(insignia.get().getId()));

        if (!alreadyHas) {
            UsuariInsignia ui = new UsuariInsignia();
            ui.setUsuari(usuari);
            ui.setInsignia(insignia.get());
            ui.setDataObtencio(LocalDate.now());
            usuariInsigniaRepository.save(ui);
        }
    }


    public int getPointsToNextLevel(Usuari usuari) {
        if (usuari == null) {
            return LEVEL_THRESHOLDS[1];
        }

        int currentPoints = usuari.getPunts() != null ? usuari.getPunts() : 0;
        int currentLevel = usuari.getNivell() != null ? usuari.getNivell() : 1;

        if (currentLevel >= LEVEL_THRESHOLDS.length) {
            return -1; // Already at max level
        }

        int nextThreshold = LEVEL_THRESHOLDS[currentLevel];
        return Math.max(0, nextThreshold - currentPoints);
    }
}
