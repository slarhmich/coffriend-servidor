package com.brewingcode.coffriend_servidor.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "botiga")
public class Botiga {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private String adreca;
    
    @Column
    private String horari;
    
    @OneToMany(mappedBy = "botiga", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Producte> productes;
    
    @OneToMany(mappedBy = "botiga", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comanda> comandes;
    
    @OneToMany(mappedBy = "botiga", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Usuari> treballadors;
    
    // getters i setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getAdreca() { return adreca; }
    public void setAdreca(String adreca) { this.adreca = adreca; }
    
    public String getHorari() { return horari; }
    public void setHorari(String horari) { this.horari = horari; }
    
    public List<Producte> getProductes() { return productes; }
    public void setProductes(List<Producte> productes) { this.productes = productes; }
    
    public List<Comanda> getComandes() { return comandes; }
    public void setComandes(List<Comanda> comandes) { this.comandes = comandes; }
    
    public List<Usuari> getTreballadors() { return treballadors; }
    public void setTreballadors(List<Usuari> treballadors) { this.treballadors = treballadors; }
}
