package com.brewingcode.coffriend_servidor.dto;

public class ProducteDTO {
    private Integer id;
    private String nom;
    private Double preu;
    private String categoria;
    private Integer idBotiga;
    
    public ProducteDTO() {}
    public ProducteDTO(Integer id, String nom, Double preu, String categoria, Integer idBotiga) {
        this.id = id;
        this.nom = nom;
        this.preu = preu;
        this.categoria = categoria;
        this.idBotiga = idBotiga;
    }
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public Double getPreu() { return preu; }
    public void setPreu(Double preu) { this.preu = preu; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public Integer getIdBotiga() { return idBotiga; }
    public void setIdBotiga(Integer idBotiga) { this.idBotiga = idBotiga; }
}
