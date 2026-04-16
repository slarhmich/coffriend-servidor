package com.brewingcode.coffriend_servidor.entities;

import java.io.Serializable;
import java.util.Objects;

public class UsuariInsigniaId implements Serializable {
    private Integer usuari;
    private Integer insignia;
    
    public UsuariInsigniaId() {}
    
    public UsuariInsigniaId(Integer usuari, Integer insignia) {
        this.usuari = usuari;
        this.insignia = insignia;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuariInsigniaId that = (UsuariInsigniaId) o;
        return Objects.equals(usuari, that.usuari) &&
               Objects.equals(insignia, that.insignia);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(usuari, insignia);
    }
}
