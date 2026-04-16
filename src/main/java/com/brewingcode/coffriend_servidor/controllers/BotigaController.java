package com.brewingcode.coffriend_servidor.controllers;

import com.brewingcode.coffriend_servidor.dto.BotigaDTO;
import com.brewingcode.coffriend_servidor.entities.Botiga;
import com.brewingcode.coffriend_servidor.repositories.BotigaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles CRUD of the store.
 * @param botigaRepository
 */
@RestController
@RequestMapping("/api/botigues")
public class BotigaController {

    @Autowired
    private BotigaRepository botigaRepository;

    // GET ALL
    @GetMapping
    public ResponseEntity<List<BotigaDTO>> getAll() {
        List<BotigaDTO> botigues = botigaRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(botigues);
    }

    // CREATE
    @PreAuthorize("hasRole('admin')")
    @PostMapping
    public ResponseEntity<BotigaDTO> create(@RequestBody BotigaDTO dto) {
        Botiga botiga = new Botiga();
        botiga.setNom(dto.getNom());
        botiga.setAdreca(dto.getAdreca());
        botiga.setHorari(dto.getHorari());
        Botiga saved = botigaRepository.save(botiga);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<BotigaDTO> getById(@PathVariable Integer id) {
        return botigaRepository.findById(id)
                .map(b -> ResponseEntity.ok(toDTO(b)))
                .orElse(ResponseEntity.notFound().build());
    }

    // UPDATE (ADMIN ONLY)
    @PreAuthorize("hasRole('admin')")
    @PutMapping("/{id}")
    public ResponseEntity<BotigaDTO> update(@PathVariable Integer id, @RequestBody BotigaDTO dto) {
        return botigaRepository.findById(id).map(botiga -> {
            botiga.setNom(dto.getNom());
            botiga.setAdreca(dto.getAdreca());
            botiga.setHorari(dto.getHorari());
            
            Botiga updated = botigaRepository.save(botiga);
            return ResponseEntity.ok(toDTO(updated));
        }).orElse(ResponseEntity.notFound().build());
    }

    private BotigaDTO toDTO(Botiga b) {
        return new BotigaDTO(b.getId(), b.getNom(), b.getAdreca(), b.getHorari());
    }
}
