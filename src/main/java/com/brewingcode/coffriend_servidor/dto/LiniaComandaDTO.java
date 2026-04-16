package com.brewingcode.coffriend_servidor.dto;

public class LiniaComandaDTO {
    private Integer idComanda;
    private Integer idProducte;
    private Integer quantitat;
    private Double preuUnitari;
    
    public LiniaComandaDTO() {}
    public LiniaComandaDTO(Integer idComanda, Integer idProducte, Integer quantitat, Double preuUnitari) {
        this.idComanda = idComanda;
        this.idProducte = idProducte;
        this.quantitat = quantitat;
        this.preuUnitari = preuUnitari;
    }
    
    public Integer getIdComanda() { return idComanda; }
    public void setIdComanda(Integer idComanda) { this.idComanda = idComanda; }
    
    public Integer getIdProducte() { return idProducte; }
    public void setIdProducte(Integer idProducte) { this.idProducte = idProducte; }
    
    public Integer getQuantitat() { return quantitat; }
    public void setQuantitat(Integer quantitat) { this.quantitat = quantitat; }
    
    public Double getPreuUnitari() { return preuUnitari; }
    public void setPreuUnitari(Double preuUnitari) { this.preuUnitari = preuUnitari; }
}
