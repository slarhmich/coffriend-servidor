package com.brewingcode.coffriend_servidor.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "usuari_insignia")
@IdClass(UsuariInsigniaId.class)
public class UsuariInsignia {
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuari", nullable = false)
    private Usuari usuari;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_insignia", nullable = false)
    private Insignia insignia;
    
    @Column(nullable = false)
    private LocalDate dataObtencio;
    
    // Getters and Setters
    public Usuari getUsuari() { return usuari; }
    public void setUsuari(Usuari usuari) { this.usuari = usuari; }
    
    public Insignia getInsignia() { return insignia; }
    public void setInsignia(Insignia insignia) { this.insignia = insignia; }
    
    public LocalDate getDataObtencio() { return dataObtencio; }
    public void setDataObtencio(LocalDate dataObtencio) { this.dataObtencio = dataObtencio; }
}
