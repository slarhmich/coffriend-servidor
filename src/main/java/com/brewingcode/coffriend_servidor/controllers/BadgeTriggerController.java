package com.brewingcode.coffriend_servidor.controllers;

import com.brewingcode.coffriend_servidor.dto.BadgeTriggerDTO;
import com.brewingcode.coffriend_servidor.entities.BadgeTrigger;
import com.brewingcode.coffriend_servidor.entities.Insignia;
import com.brewingcode.coffriend_servidor.repositories.BadgeTriggerRepository;
import com.brewingcode.coffriend_servidor.repositories.InsigniaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * REST API for managing badge triggers.
 */
@RestController
@RequestMapping("/api/badges/{badgeId}/triggers")
@PreAuthorize("hasRole('admin')")
public class BadgeTriggerController {
    
    @Autowired
    private BadgeTriggerRepository badgeTriggerRepository;
    
    @Autowired
    private InsigniaRepository insigniaRepository;
    

    @GetMapping
    public ResponseEntity<List<BadgeTriggerDTO>> getTriggers(@PathVariable Integer badgeId) {
        Optional<Insignia> badge = insigniaRepository.findById(badgeId);
        if (badge.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        List<BadgeTriggerDTO> triggers = badge.get().getTriggers().stream()
                .map(this::toDTO)
                .toList();
        
        return ResponseEntity.ok(triggers);
    }
    

    @GetMapping("/{triggerId}")
    public ResponseEntity<BadgeTriggerDTO> getTrigger(@PathVariable Integer badgeId, @PathVariable Integer triggerId) {
        Optional<BadgeTrigger> trigger = badgeTriggerRepository.findById(triggerId);
        if (trigger.isEmpty() || !trigger.get().getInsignia().getId().equals(badgeId)) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(toDTO(trigger.get()));
    }
    
    @PostMapping
    public ResponseEntity<?> createTrigger(@PathVariable Integer badgeId, @RequestBody BadgeTriggerDTO dto) {
        Optional<Insignia> badge = insigniaRepository.findById(badgeId);
        if (badge.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Badge not found");
        }
        
        if (dto.getTriggerType() == null) {
            return ResponseEntity.badRequest().body("triggerType is required");
        }
        
        String validationError = validateTrigger(dto);
        if (validationError != null) {
            return ResponseEntity.badRequest().body(validationError);
        }
        
        BadgeTrigger trigger = new BadgeTrigger();
        trigger.setInsignia(badge.get());
        trigger.setTriggerType(dto.getTriggerType());
        trigger.setIsActive(true);
        trigger.setIsDemo(false);
        
        switch (dto.getTriggerType()) {
            case TIME_OF_DAY:
                trigger.setHourStart(dto.getHourStart());
                trigger.setHourEnd(dto.getHourEnd());
                break;
            case PRODUCT_CATEGORY:
                trigger.setProductCategory(dto.getProductCategory());
                break;
            case PRODUCT_ID:
                trigger.setProductId(dto.getProductId());
                break;
            case SPENDING_AMOUNT:
                trigger.setMinSpendingAmount(dto.getMinSpendingAmount());
                break;
            case ORDER_COUNT:
                trigger.setMinOrderCount(dto.getMinOrderCount());
                break;
        }
        
        BadgeTrigger saved = badgeTriggerRepository.save(trigger);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }
    
    @PutMapping("/{triggerId}")
    public ResponseEntity<?> updateTrigger(@PathVariable Integer badgeId, @PathVariable Integer triggerId, 
                                          @RequestBody BadgeTriggerDTO dto) {
        Optional<BadgeTrigger> existing = badgeTriggerRepository.findById(triggerId);
        if (existing.isEmpty() || !existing.get().getInsignia().getId().equals(badgeId)) {
            return ResponseEntity.notFound().build();
        }
        
        BadgeTrigger trigger = existing.get();
        
        if (dto.getIsActive() != null) {
            trigger.setIsActive(dto.getIsActive());
        }
        
        if (dto.getTriggerType() != null && !trigger.getTriggerType().equals(dto.getTriggerType())) {
            return ResponseEntity.badRequest().body("Cannot change trigger type after creation");
        }
        
        switch (trigger.getTriggerType()) {
            case TIME_OF_DAY:
                if (dto.getHourStart() != null) {
                    trigger.setHourStart(dto.getHourStart());
                }
                if (dto.getHourEnd() != null) {
                    trigger.setHourEnd(dto.getHourEnd());
                }
                break;
            case PRODUCT_CATEGORY:
                if (dto.getProductCategory() != null) {
                    trigger.setProductCategory(dto.getProductCategory());
                }
                break;
            case PRODUCT_ID:
                if (dto.getProductId() != null) {
                    trigger.setProductId(dto.getProductId());
                }
                break;
            case SPENDING_AMOUNT:
                if (dto.getMinSpendingAmount() != null) {
                    trigger.setMinSpendingAmount(dto.getMinSpendingAmount());
                }
                break;
            case ORDER_COUNT:
                if (dto.getMinOrderCount() != null) {
                    trigger.setMinOrderCount(dto.getMinOrderCount());
                }
                break;
        }
        
        String validationError = validateTrigger(toDTO(trigger));
        if (validationError != null) {
            return ResponseEntity.badRequest().body(validationError);
        }
        
        BadgeTrigger updated = badgeTriggerRepository.save(trigger);
        return ResponseEntity.ok(toDTO(updated));
    }
    
    /**
     * Delete a trigger
     * DELETE /api/badges/1/triggers/5
     */
    @DeleteMapping("/{triggerId}")
    public ResponseEntity<?> deleteTrigger(@PathVariable Integer badgeId, @PathVariable Integer triggerId) {
        Optional<BadgeTrigger> trigger = badgeTriggerRepository.findById(triggerId);
        if (trigger.isEmpty() || !trigger.get().getInsignia().getId().equals(badgeId)) {
            return ResponseEntity.notFound().build();
        }
        
        badgeTriggerRepository.deleteById(triggerId);
        return ResponseEntity.noContent().build();
    }
    
    // helper methods
    private String validateTrigger(BadgeTriggerDTO dto) {
        if (dto.getTriggerType() == null) {
            return "triggerType is required";
        }
        
        switch (dto.getTriggerType()) {
            case TIME_OF_DAY:
                if (dto.getHourStart() == null || dto.getHourEnd() == null) {
                    return "TIME_OF_DAY requires hourStart and hourEnd";
                }
                if (dto.getHourStart() < 0 || dto.getHourStart() > 23) {
                    return "hourStart must be 0-23";
                }
                if (dto.getHourEnd() < 0 || dto.getHourEnd() > 24) {
                    return "hourEnd must be 0-24";
                }
                if (dto.getHourStart() >= dto.getHourEnd()) {
                    return "hourStart must be less than hourEnd";
                }
                break;
                
            case PRODUCT_CATEGORY:
                if (dto.getProductCategory() == null || dto.getProductCategory().isBlank()) {
                    return "PRODUCT_CATEGORY requires productCategory";
                }
                break;
                
            case PRODUCT_ID:
                if (dto.getProductId() == null) {
                    return "PRODUCT_ID requires productId";
                }
                if (dto.getProductId() <= 0) {
                    return "productId must be positive";
                }
                break;
                
            case SPENDING_AMOUNT:
                if (dto.getMinSpendingAmount() == null) {
                    return "SPENDING_AMOUNT requires minSpendingAmount";
                }
                if (dto.getMinSpendingAmount().signum() < 0) {
                    return "minSpendingAmount must be positive";
                }
                break;
                
            case ORDER_COUNT:
                if (dto.getMinOrderCount() == null) {
                    return "ORDER_COUNT requires minOrderCount";
                }
                if (dto.getMinOrderCount() <= 0) {
                    return "minOrderCount must be positive";
                }
                break;
        }
        
        return null;
    }
    
    private BadgeTriggerDTO toDTO(BadgeTrigger trigger) {
        return new BadgeTriggerDTO(
            trigger.getId(),
            trigger.getInsignia().getId(),
            trigger.getTriggerType(),
            trigger.getHourStart(),
            trigger.getHourEnd(),
            trigger.getProductCategory(),
            trigger.getProductId(),
            trigger.getMinSpendingAmount(),
            trigger.getMinOrderCount(),
            trigger.getIsActive()
        );
    }
}
