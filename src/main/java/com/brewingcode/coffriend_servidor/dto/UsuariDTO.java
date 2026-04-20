package com.brewingcode.coffriend_servidor.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

public class UsuariDTO {
    private Integer id;
    private String nom;
    private String email;
    private String rol;
    private Integer nivell;
    private Integer punts;
    private Integer idBotiga;
    private String password;
    private List<EarnedInsigniaDTO> insignies;

    public UsuariDTO() {}
    
    public UsuariDTO(Integer id, String nom, String email, String rol, Integer nivell, Integer punts, Integer idBotiga) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.rol = rol;
        this.nivell = nivell;
        this.punts = punts;
        this.idBotiga = idBotiga;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    
    public Integer getNivell() { return nivell; }
    public void setNivell(Integer nivell) { this.nivell = nivell; }
    
    public Integer getPunts() { return punts; }
    public void setPunts(Integer punts) { this.punts = punts; }
    
    public Integer getIdBotiga() { return idBotiga; }
    public void setIdBotiga(Integer idBotiga) { this.idBotiga = idBotiga; }
    
    @JsonIgnore
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<EarnedInsigniaDTO> getInsignies() { return insignies; }
    public void setInsignies(List<EarnedInsigniaDTO> insignies) { this.insignies = insignies; }
}
