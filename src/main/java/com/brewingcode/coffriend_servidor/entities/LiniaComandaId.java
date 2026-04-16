package com.brewingcode.coffriend_servidor.entities;

import java.io.Serializable;
import java.util.Objects;

public class LiniaComandaId implements Serializable {
    private Integer comanda;
    private Integer producte;
    
    public LiniaComandaId() {}
    
    public LiniaComandaId(Integer comanda, Integer producte) {
        this.comanda = comanda;
        this.producte = producte;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiniaComandaId that = (LiniaComandaId) o;
        return Objects.equals(comanda, that.comanda) &&
               Objects.equals(producte, that.producte);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(comanda, producte);
    }
}
