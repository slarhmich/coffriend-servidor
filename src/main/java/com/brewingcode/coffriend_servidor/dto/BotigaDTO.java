package com.brewingcode.coffriend_servidor.dto;

public class BotigaDTO {
    private Integer id;
    private String nom;
    private String adreca;
    private String horari;
    
    public BotigaDTO() {}
    public BotigaDTO(Integer id, String nom, String adreca, String horari) {
        this.id = id;
        this.nom = nom;
        this.adreca = adreca;
        this.horari = horari;
    }
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getAdreca() { return adreca; }
    public void setAdreca(String adreca) { this.adreca = adreca; }
    
    public String getHorari() { return horari; }
    public void setHorari(String horari) { this.horari = horari; }
}
