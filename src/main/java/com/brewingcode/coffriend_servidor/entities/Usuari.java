package com.brewingcode.coffriend_servidor.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuari")
public class Usuari {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String nom;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String rol;
    
    @Column
    private Integer nivell;
    
    @Column
    private Integer punts;
    
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isDemo = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_botiga")
    private Botiga botiga;
    
    @OneToMany(mappedBy = "usuari", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comanda> comandes;
    
    @OneToMany(mappedBy = "usuari", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UsuariInsignia> insignies;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    public Integer getNivell() { return nivell; }
    public void setNivell(Integer nivell) { this.nivell = nivell; }
    
    public Integer getPunts() { return punts; }
    public void setPunts(Integer punts) { this.punts = punts; }
    
    public Botiga getBotiga() { return botiga; }
    public void setBotiga(Botiga botiga) { this.botiga = botiga; }
    
    public Boolean getIsDemo() { return isDemo; }
    public void setIsDemo(Boolean isDemo) { this.isDemo = isDemo; }
    
    public List<Comanda> getComandes() { return comandes; }
    public void setComandes(List<Comanda> comandes) { this.comandes = comandes; }
    
    public List<UsuariInsignia> getInsignies() { return insignies; }
    public void setInsignies(List<UsuariInsignia> insignies) { this.insignies = insignies; }
}
