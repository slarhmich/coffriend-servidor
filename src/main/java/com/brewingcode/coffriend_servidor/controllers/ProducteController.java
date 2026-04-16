package com.brewingcode.coffriend_servidor.controllers;

import com.brewingcode.coffriend_servidor.dto.ProducteDTO;
import com.brewingcode.coffriend_servidor.entities.Producte;
import com.brewingcode.coffriend_servidor.repositories.ProducteRepository;
import com.brewingcode.coffriend_servidor.repositories.BotigaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles CRUD of products.
 * @param botigaRepository
 * @param producteRepository
 */
@RestController
@RequestMapping("/api/productes")
public class ProducteController {

    @Autowired
    private ProducteRepository producteRepository;

    @Autowired
    private BotigaRepository botigaRepository;

    // GET ALL
    @GetMapping
    public ResponseEntity<List<ProducteDTO>> getAll() {
        List<ProducteDTO> productes = producteRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productes);
    }

    // GET BY BOTIGA
    @GetMapping("/botiga/{idBotiga}")
    public ResponseEntity<List<ProducteDTO>> getByBotiga(@PathVariable Integer idBotiga) {
        List<ProducteDTO> productes = producteRepository.findByIdBotiga(idBotiga)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productes);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ProducteDTO> getById(@PathVariable Integer id) {
        return producteRepository.findById(id)
                .map(p -> ResponseEntity.ok(toDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE (ADMIN ONLY)
    @PreAuthorize("hasRole('admin')")
    @PostMapping
    public ResponseEntity<ProducteDTO> create(@RequestBody ProducteDTO dto) {
        if (dto.getIdBotiga() == null) {
            return ResponseEntity.badRequest().build();
        }
        var botiga = botigaRepository.findById(dto.getIdBotiga());
        if (botiga.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Producte producte = new Producte();
        producte.setNom(dto.getNom());
        producte.setPreu(BigDecimal.valueOf(dto.getPreu()));
        producte.setCategoria(dto.getCategoria());
        producte.setBotiga(botiga.get());
        
        Producte saved = producteRepository.save(producte);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    // UPDATE (ADMIN ONLY)
    @PreAuthorize("hasRole('admin')")
    @PutMapping("/{id}")
    public ResponseEntity<ProducteDTO> update(@PathVariable Integer id, @RequestBody ProducteDTO dto) {
        return producteRepository.findById(id).map(producte -> {
            producte.setNom(dto.getNom());
            producte.setPreu(BigDecimal.valueOf(dto.getPreu()));
            producte.setCategoria(dto.getCategoria());
            
            if (dto.getIdBotiga() != null) {
                botigaRepository.findById(dto.getIdBotiga()).ifPresent(producte::setBotiga);
            }
            
            Producte updated = producteRepository.save(producte);
            return ResponseEntity.ok(toDTO(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE (ADMIN ONLY)
    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (producteRepository.existsById(id)) {
            producteRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private ProducteDTO toDTO(Producte p) {
        return new ProducteDTO(p.getId(), p.getNom(), p.getPreu().doubleValue(), 
                p.getCategoria(), p.getBotiga() != null ? p.getBotiga().getId() : null);
    }
}
