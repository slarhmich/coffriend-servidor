package com.brewingcode.coffriend_servidor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.brewingcode.coffriend_servidor.repositories.*;

@RestController
@RequestMapping("/api/system")
@CrossOrigin(origins = "*")
public class SystemController {

    @Autowired private UsuariRepository usuariRepository;
    @Autowired private ComandaRepository comandaRepository;
    @Autowired private BotigaRepository botigaRepository;
    @Autowired private ProducteRepository producteRepository;

    // Allows resetting the database easily during testing/grading.
    @DeleteMapping("/reset")
    public ResponseEntity<String> resetDatabase() {
        comandaRepository.deleteAll();
        botigaRepository.deleteAll();
        producteRepository.deleteAll();
        usuariRepository.deleteAll();
        return ResponseEntity.ok("Database completely wiped! You can now start fresh.");
    }
}
