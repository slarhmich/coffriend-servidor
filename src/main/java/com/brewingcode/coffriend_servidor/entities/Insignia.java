package com.brewingcode.coffriend_servidor.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "insignia")
public class Insignia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String criteri;
    
    // getters i setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getCriteri() { return criteri; }
    public void setCriteri(String criteri) { this.criteri = criteri; }
}
