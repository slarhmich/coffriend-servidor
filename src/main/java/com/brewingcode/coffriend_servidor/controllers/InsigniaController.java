package com.brewingcode.coffriend_servidor.controllers;

import com.brewingcode.coffriend_servidor.dto.InsigniaDTO;
import com.brewingcode.coffriend_servidor.entities.Insignia;
import com.brewingcode.coffriend_servidor.repositories.InsigniaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles CRUD of insignies.
 * @param botigaRepository
 */
@RestController
@RequestMapping("/api/insignies")
public class InsigniaController {

    @Autowired
    private InsigniaRepository insigniaRepository;

    // GET ALL
    @GetMapping
    public ResponseEntity<List<InsigniaDTO>> getAll() {
        List<InsigniaDTO> insignies = insigniaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(insignies);
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<InsigniaDTO> getById(@PathVariable Integer id) {
        return insigniaRepository.findById(id)
                .map(i -> ResponseEntity.ok(toDTO(i)))
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE (ADMIN ONLY)
    @PreAuthorize("hasRole('admin')")
    @PostMapping
    public ResponseEntity<InsigniaDTO> create(@RequestBody InsigniaDTO dto) {
        Insignia insignia = new Insignia();
        insignia.setNom(dto.getNom());
        insignia.setDescripcio(dto.getDescripcio());
        insignia.setImatgeUrl(dto.getImatgeUrl());
        
        Insignia saved = insigniaRepository.save(insignia);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    // UPDATE (ADMIN ONLY)
    @PreAuthorize("hasRole('admin')")
    @PutMapping("/{id}")
    public ResponseEntity<InsigniaDTO> update(@PathVariable Integer id, @RequestBody InsigniaDTO dto) {
        return insigniaRepository.findById(id).map(insignia -> {
            insignia.setNom(dto.getNom());
            insignia.setDescripcio(dto.getDescripcio());
            insignia.setImatgeUrl(dto.getImatgeUrl());
            
            Insignia updated = insigniaRepository.save(insignia);
            return ResponseEntity.ok(toDTO(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE (ADMIN ONLY)
    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (insigniaRepository.existsById(id)) {
            insigniaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private InsigniaDTO toDTO(Insignia i) {
        return new InsigniaDTO(i.getId(), i.getNom(), i.getDescripcio(), i.getImatgeUrl());
    }
}
