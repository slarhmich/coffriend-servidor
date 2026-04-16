package com.brewingcode.coffriend_servidor.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "linia_comanda")
@IdClass(LiniaComandaId.class)
public class LiniaComanda {
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comanda", nullable = false)
    private Comanda comanda;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producte", nullable = false)
    private Producte producte;
    
    @Column(nullable = false)
    private Integer quantitat;
    
    @Column(nullable = false)
    private BigDecimal preuUnitari;
    
    // getters i setters
    public Comanda getComanda() { return comanda; }
    public void setComanda(Comanda comanda) { this.comanda = comanda; }
    
    public Producte getProducte() { return producte; }
    public void setProducte(Producte producte) { this.producte = producte; }
    
    public Integer getQuantitat() { return quantitat; }
    public void setQuantitat(Integer quantitat) { this.quantitat = quantitat; }
    
    public BigDecimal getPreuUnitari() { return preuUnitari; }
    public void setPreuUnitari(BigDecimal preuUnitari) { this.preuUnitari = preuUnitari; }
}
