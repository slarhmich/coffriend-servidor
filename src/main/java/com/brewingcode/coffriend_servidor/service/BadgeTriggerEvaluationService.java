package com.brewingcode.coffriend_servidor.service;

import com.brewingcode.coffriend_servidor.entities.*;
import com.brewingcode.coffriend_servidor.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.math.BigDecimal;

/**
 * Evaluates badge triggers against completed orders.
 */
@Service
public class BadgeTriggerEvaluationService {
    
    @Autowired
    private InsigniaRepository insigniaRepository;

    /**
     * Evaluate all badges for an order.
     * Returns list of badge IDs that should be awarded.
     * 
     * @param comanda the completed order
     * @return list of badge IDs to award (empty if none qualify)
     */
    public List<Integer> evaluateBadgesForOrder(Comanda comanda) {
        if (comanda == null || comanda.getUsuari() == null || comanda.getLinies() == null) {
            return Collections.emptyList();
        }
        
        List<Integer> badgesToAward = new ArrayList<>();
        
        List<Insignia> allBadges = insigniaRepository.findAll().stream()
                .filter(b -> b.getTriggers() != null && !b.getTriggers().isEmpty())
                .toList();
        
        for (Insignia badge : allBadges) {
            if (evaluateBadge(badge, comanda, comanda.getUsuari())) {
                badgesToAward.add(badge.getId());
            }
        }
        
        return badgesToAward;
    }

    /**
     * Check if a badge should be awarded based on ALL its triggers (AND logic)
     * Badge is earned if ALL triggers pass.
     */
    private boolean evaluateBadge(Insignia badge, Comanda comanda, Usuari usuari) {
        List<BadgeTrigger> triggers = badge.getTriggers().stream()
                .filter(BadgeTrigger::getIsActive)
                .toList();
        
        if (triggers.isEmpty()) {
            return false;
        }
        
        for (BadgeTrigger trigger : triggers) {
            if (!evaluateTrigger(trigger, comanda, usuari)) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Evaluate a single trigger against an order.
     */
    private boolean evaluateTrigger(BadgeTrigger trigger, Comanda comanda, Usuari usuari) {
        switch (trigger.getTriggerType()) {
            case TIME_OF_DAY:
                return evaluateTimeOfDay(trigger, comanda);
            case PRODUCT_CATEGORY:
                return evaluateProductCategory(trigger, comanda);
            case PRODUCT_ID:
                return evaluateProductId(trigger, comanda);
            case SPENDING_AMOUNT:
                return evaluateSpendingAmount(trigger, comanda);
            case ORDER_COUNT:
                return evaluateOrderCount(trigger, usuari);
            default:
                return false;
        }
    }


    private boolean evaluateTimeOfDay(BadgeTrigger trigger, Comanda comanda) {
        if (comanda.getDataHora() == null || trigger.getHourStart() == null || trigger.getHourEnd() == null) {
            return false;
        }
        
        int orderHour = comanda.getDataHora().getHour();
        int startHour = trigger.getHourStart();
        int endHour = trigger.getHourEnd();
        
        return orderHour >= startHour && orderHour < endHour;
    }

    private boolean evaluateProductCategory(BadgeTrigger trigger, Comanda comanda) {
        if (trigger.getProductCategory() == null || comanda.getLinies() == null) {
            return false;
        }
        
        String requiredCategory = trigger.getProductCategory();
        
        return comanda.getLinies().stream()
                .anyMatch(linea -> {
                    if (linea.getProducte() == null) return false;
                    String productCategory = linea.getProducte().getCategoria();
                    return productCategory != null && productCategory.equals(requiredCategory);
                });
    }

    private boolean evaluateProductId(BadgeTrigger trigger, Comanda comanda) {
        if (trigger.getProductId() == null || comanda.getLinies() == null) {
            return false;
        }
        
        Integer requiredProductId = trigger.getProductId();
        
        return comanda.getLinies().stream()
                .anyMatch(linea -> linea.getProducte() != null && 
                        linea.getProducte().getId().equals(requiredProductId));
    }

    private boolean evaluateSpendingAmount(BadgeTrigger trigger, Comanda comanda) {
        if (trigger.getMinSpendingAmount() == null || comanda.getLinies() == null) {
            return false;
        }
        
        BigDecimal threshold = trigger.getMinSpendingAmount();
        BigDecimal orderTotal = calculateOrderTotal(comanda);
        
        return orderTotal.compareTo(threshold) >= 0;
    }

    private boolean evaluateOrderCount(BadgeTrigger trigger, Usuari usuari) {
        if (trigger.getMinOrderCount() == null || usuari.getComandes() == null) {
            return false;
        }
        
        Integer requiredCount = trigger.getMinOrderCount();
        
        long completedOrderCount = usuari.getComandes().stream()
                .filter(c -> "completat".equals(c.getEstat()))
                .count();
        
        return completedOrderCount >= requiredCount;
    }

    private BigDecimal calculateOrderTotal(Comanda comanda) {
        if (comanda.getLinies() == null || comanda.getLinies().isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        return comanda.getLinies().stream()
                .map(linea -> linea.getPreuUnitari().multiply(new BigDecimal(linea.getQuantitat())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

