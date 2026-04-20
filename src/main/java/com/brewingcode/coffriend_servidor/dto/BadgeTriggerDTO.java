package com.brewingcode.coffriend_servidor.dto;

import com.brewingcode.coffriend_servidor.entities.BadgeTrigger;
import java.math.BigDecimal;

/**
 * DTO for Badge Trigger requests and responses.
 */
public class BadgeTriggerDTO {
    
    private Integer id;
    private Integer idInsignia;
    private BadgeTrigger.TriggerType triggerType;
    
    // TIME_OF_DAY fields (hours in 24-hour format)
    private Integer hourStart;
    private Integer hourEnd;
    
    // PRODUCT_CATEGORY field
    private String productCategory;
    
    // PRODUCT_ID field
    private Integer productId;
    
    // SPENDING_AMOUNT field
    private BigDecimal minSpendingAmount;
    
    // ORDER_COUNT field
    private Integer minOrderCount;
    
    private Boolean isActive;

    public BadgeTriggerDTO() {}

    public BadgeTriggerDTO(Integer id, Integer idInsignia, BadgeTrigger.TriggerType triggerType,
                          Integer hourStart, Integer hourEnd, String productCategory, Integer productId,
                          BigDecimal minSpendingAmount, Integer minOrderCount, Boolean isActive) {
        this.id = id;
        this.idInsignia = idInsignia;
        this.triggerType = triggerType;
        this.hourStart = hourStart;
        this.hourEnd = hourEnd;
        this.productCategory = productCategory;
        this.productId = productId;
        this.minSpendingAmount = minSpendingAmount;
        this.minOrderCount = minOrderCount;
        this.isActive = isActive;
    }

    // Getters / Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdInsignia() {
        return idInsignia;
    }

    public void setIdInsignia(Integer idInsignia) {
        this.idInsignia = idInsignia;
    }

    public BadgeTrigger.TriggerType getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(BadgeTrigger.TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public Integer getHourStart() {
        return hourStart;
    }

    public void setHourStart(Integer hourStart) {
        this.hourStart = hourStart;
    }

    public Integer getHourEnd() {
        return hourEnd;
    }

    public void setHourEnd(Integer hourEnd) {
        this.hourEnd = hourEnd;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public BigDecimal getMinSpendingAmount() {
        return minSpendingAmount;
    }

    public void setMinSpendingAmount(BigDecimal minSpendingAmount) {
        this.minSpendingAmount = minSpendingAmount;
    }

    public Integer getMinOrderCount() {
        return minOrderCount;
    }

    public void setMinOrderCount(Integer minOrderCount) {
        this.minOrderCount = minOrderCount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
