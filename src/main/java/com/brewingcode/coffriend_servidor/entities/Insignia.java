package com.brewingcode.coffriend_servidor.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "insignia")
public class Insignia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(columnDefinition = "TEXT")
    private String descripcio;
    
    @Column
    private String imatgeUrl;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isDemo = false;
    
    @OneToMany(mappedBy = "insignia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BadgeTrigger> triggers;

    @OneToMany(mappedBy = "insignia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UsuariInsignia> usuariInsignies;
    
    // getters i setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getDescripcio() { return descripcio; }
    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }
    
    public String getImatgeUrl() { return imatgeUrl; }
    public void setImatgeUrl(String imatgeUrl) { this.imatgeUrl = imatgeUrl; }
    
    public Boolean getIsDemo() { return isDemo; }
    public void setIsDemo(Boolean isDemo) { this.isDemo = isDemo; }
    
    public List<BadgeTrigger> getTriggers() { return triggers; }
    public void setTriggers(List<BadgeTrigger> triggers) { this.triggers = triggers; }

    public List<UsuariInsignia> getUsuariInsignies() { return usuariInsignies; }
    public void setUsuariInsignies(List<UsuariInsignia> usuariInsignies) { this.usuariInsignies = usuariInsignies; }
}
