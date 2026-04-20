package com.brewingcode.coffriend_servidor.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * Defines conditions/triggers for earning a badge.
 */
@Entity
@Table(name = "badge_trigger")
public class BadgeTrigger {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_insignia", nullable = false)
    private Insignia insignia;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TriggerType triggerType;
    
    public enum TriggerType {
        TIME_OF_DAY,        // order placed between hourStart and hourEnd
        PRODUCT_CATEGORY,   // order contains product from this category
        PRODUCT_ID,         // order contains this specific product
        SPENDING_AMOUNT,    // order total >= minSpendingAmount
        ORDER_COUNT         // customer has >= minOrderCount completed orders
    }
    
    // TIME_OF_DAY: hour in 24-hour format (0-23)
    @Column(name = "hour_start")
    private Integer hourStart;
    
    @Column(name = "hour_end")
    private Integer hourEnd;
    
    // PRODUCT_CATEGORY: category name
    @Column(name = "product_category")
    private String productCategory;
    
    // PRODUCT_ID: specific product
    @Column(name = "product_id")
    private Integer productId;
    
    // SPENDING_AMOUNT: minimum euros
    @Column(name = "min_spending_amount")
    private BigDecimal minSpendingAmount;
    
    // ORDER_COUNT: minimum completed orders
    @Column(name = "min_order_count")
    private Integer minOrderCount;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isActive = true;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isDemo = false;
    
    @Column
    private LocalDateTime createdAt = LocalDateTime.now();
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Insignia getInsignia() {
        return insignia;
    }

    public void setInsignia(Insignia insignia) {
        this.insignia = insignia;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(TriggerType triggerType) {
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

    public Boolean getIsDemo() {
        return isDemo;
    }

    public void setIsDemo(Boolean isDemo) {
        this.isDemo = isDemo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
