package com.brewingcode.coffriend_servidor.dto;

public class InsigniaDTO {
    private Integer id;
    private String nom;
    private String criteri;
    
    public InsigniaDTO() {}
    public InsigniaDTO(Integer id, String nom, String criteri) {
        this.id = id;
        this.nom = nom;
        this.criteri = criteri;
    }
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getCriteri() { return criteri; }
    public void setCriteri(String criteri) { this.criteri = criteri; }
}
