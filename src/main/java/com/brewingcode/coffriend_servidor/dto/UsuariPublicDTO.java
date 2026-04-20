package com.brewingcode.coffriend_servidor.dto;

import java.util.List;

/**
 * Restricted view of a user for STAFF access.
 * Exposes id, name, gamification data, earned badges.
 */
public class UsuariPublicDTO {
    private Integer id;
    private String nom;
    private Integer nivell;
    private Integer punts;
    private List<EarnedInsigniaDTO> insignies;

    public UsuariPublicDTO() {}

    public UsuariPublicDTO(Integer id, String nom, Integer nivell, Integer punts) {
        this.id = id;
        this.nom = nom;
        this.nivell = nivell;
        this.punts = punts;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Integer getNivell() { return nivell; }
    public void setNivell(Integer nivell) { this.nivell = nivell; }

    public Integer getPunts() { return punts; }
    public void setPunts(Integer punts) { this.punts = punts; }

    public List<EarnedInsigniaDTO> getInsignies() { return insignies; }
    public void setInsignies(List<EarnedInsigniaDTO> insignies) { this.insignies = insignies; }
}
