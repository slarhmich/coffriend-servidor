package com.brewingcode.coffriend_servidor.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comanda")
public class Comanda {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private LocalDateTime dataHora;
    
    @Column(nullable = false)
    private String estat;
    
    @Column(nullable = false)
    private String tipus;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuari", nullable = false)
    private Usuari usuari;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_botiga", nullable = false)
    private Botiga botiga;
    
    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LiniaComanda> linies;
    
    // getters i setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    
    public String getEstat() { return estat; }
    public void setEstat(String estat) { this.estat = estat; }
    
    public String getTipus() { return tipus; }
    public void setTipus(String tipus) { this.tipus = tipus; }
    
    public Usuari getUsuari() { return usuari; }
    public void setUsuari(Usuari usuari) { this.usuari = usuari; }
    
    public Botiga getBotiga() { return botiga; }
    public void setBotiga(Botiga botiga) { this.botiga = botiga; }
    
    public List<LiniaComanda> getLinies() { return linies; }
    public void setLinies(List<LiniaComanda> linies) { this.linies = linies; }
}
