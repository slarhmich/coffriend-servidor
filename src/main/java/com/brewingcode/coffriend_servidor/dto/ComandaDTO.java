package com.brewingcode.coffriend_servidor.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ComandaDTO {
    private Integer id;
    private LocalDateTime dataHora;
    private String estat;
    private String tipus;
    private Integer idUsuari;
    private Integer idBotiga;
    private List<LiniaComandaDTO> linies;
    
    public ComandaDTO() {}
    public ComandaDTO(Integer id, LocalDateTime dataHora, String estat, String tipus, Integer idUsuari, Integer idBotiga, List<LiniaComandaDTO> linies) {
        this.id = id;
        this.dataHora = dataHora;
        this.estat = estat;
        this.tipus = tipus;
        this.idUsuari = idUsuari;
        this.idBotiga = idBotiga;
        this.linies = linies;
    }
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    
    public String getEstat() { return estat; }
    public void setEstat(String estat) { this.estat = estat; }
    
    public String getTipus() { return tipus; }
    public void setTipus(String tipus) { this.tipus = tipus; }
    
    public Integer getIdUsuari() { return idUsuari; }
    public void setIdUsuari(Integer idUsuari) { this.idUsuari = idUsuari; }
    
    public Integer getIdBotiga() { return idBotiga; }
    public void setIdBotiga(Integer idBotiga) { this.idBotiga = idBotiga; }
    
    public List<LiniaComandaDTO> getLinies() { return linies; }
    public void setLinies(List<LiniaComandaDTO> linies) { this.linies = linies; }
}
