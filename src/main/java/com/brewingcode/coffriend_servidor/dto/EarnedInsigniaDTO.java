package com.brewingcode.coffriend_servidor.dto;

import java.time.LocalDate;

/**
 * Represents a badge that a user has earned
 */
public class EarnedInsigniaDTO {
    private Integer id;
    private String nom;
    private String descripcio;
    private String imatgeUrl;
    private LocalDate dataObtencio;

    public EarnedInsigniaDTO() {}

    public EarnedInsigniaDTO(Integer id, String nom, String descripcio, String imatgeUrl, LocalDate dataObtencio) {
        this.id = id;
        this.nom = nom;
        this.descripcio = descripcio;
        this.imatgeUrl = imatgeUrl;
        this.dataObtencio = dataObtencio;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescripcio() { return descripcio; }
    public void setDescripcio(String descripcio) { this.descripcio = descripcio; }

    public String getImatgeUrl() { return imatgeUrl; }
    public void setImatgeUrl(String imatgeUrl) { this.imatgeUrl = imatgeUrl; }

    public LocalDate getDataObtencio() { return dataObtencio; }
    public void setDataObtencio(LocalDate dataObtencio) { this.dataObtencio = dataObtencio; }
}
