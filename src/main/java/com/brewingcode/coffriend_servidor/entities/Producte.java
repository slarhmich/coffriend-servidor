package com.brewingcode.coffriend_servidor.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "producte")
public class Producte {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false)
    private BigDecimal preu;
    
    @Column(nullable = false)
    private String categoria;
    
    @Column
    private String imatgeURL;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_botiga", nullable = false)
    private Botiga botiga;
    
    @OneToMany(mappedBy = "producte", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LiniaComanda> linies;
    
    // getters i setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public BigDecimal getPreu() { return preu; }
    public void setPreu(BigDecimal preu) { this.preu = preu; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    
    public String getImatgeURL() { return imatgeURL; }
    public void setImatgeURL(String imatge) { this.imatgeURL = imatge; }
    
    public Botiga getBotiga() { return botiga; }
    public void setBotiga(Botiga botiga) { this.botiga = botiga; }
    
    public List<LiniaComanda> getLinies() { return linies; }
    public void setLinies(List<LiniaComanda> linies) { this.linies = linies; }
}
