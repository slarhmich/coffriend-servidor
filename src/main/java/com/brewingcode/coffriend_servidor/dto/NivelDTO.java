package com.brewingcode.coffriend_servidor.dto;

public class NivelDTO {
    private Integer nivell;
    private Integer punts;
    
    public NivelDTO() {}
    public NivelDTO(Integer nivell, Integer punts) {
        this.nivell = nivell;
        this.punts = punts;
    }
    
    public Integer getNivell() { return nivell; }
    public void setNivell(Integer nivell) { this.nivell = nivell; }
    
    public Integer getPunts() { return punts; }
    public void setPunts(Integer punts) { this.punts = punts; }
}
