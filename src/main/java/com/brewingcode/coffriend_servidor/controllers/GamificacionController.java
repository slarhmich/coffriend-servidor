package com.brewingcode.coffriend_servidor.controllers;

import com.brewingcode.coffriend_servidor.dto.NivelDTO;
import com.brewingcode.coffriend_servidor.entities.UsuariInsignia;
import com.brewingcode.coffriend_servidor.entities.Usuari;
import com.brewingcode.coffriend_servidor.repositories.UsuariInsigniaRepository;
import com.brewingcode.coffriend_servidor.repositories.UsuariRepository;
import com.brewingcode.coffriend_servidor.repositories.InsigniaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Handles CRUD of gamification.
 * @param botigaRepository
 * @param insigniaRepository
 * @param usuariInsigniaRepository
 */
@RestController
@RequestMapping("/api/gamificacion")
public class GamificacionController {

    @Autowired
    private UsuariRepository usuariRepository;

    @Autowired
    private InsigniaRepository insigniaRepository;

    @Autowired
    private UsuariInsigniaRepository usuariInsigniaRepository;

    // GET LEVELS
    @GetMapping("/levels")
    public ResponseEntity<List<NivelDTO>> getLevels() {
        List<NivelDTO> levels = List.of(
            new NivelDTO(1, 0),
            new NivelDTO(2, 100),
            new NivelDTO(3, 250),
            new NivelDTO(4, 500),
            new NivelDTO(5, 1000)
        );
        return ResponseEntity.ok(levels);
    }

    // ASSIGN BADGE TO USER (ADMIN) - using query parameters
    @PostMapping("/assignBadge")
    public ResponseEntity<UsuariInsignia> assignBadge(@RequestParam Integer usuariId, @RequestParam Integer insigniaId) {
        return usuariRepository.findById(usuariId).map(usuari ->
            insigniaRepository.findById(insigniaId).map(insignia -> {
                UsuariInsignia ui = new UsuariInsignia();
                ui.setUsuari(usuari);
                ui.setInsignia(insignia);
                ui.setDataObtencio(LocalDate.now());
                
                UsuariInsignia saved = usuariInsigniaRepository.save(ui);
                return ResponseEntity.status(HttpStatus.CREATED).body(saved);
            }).orElse(ResponseEntity.notFound().build())
        ).orElse(ResponseEntity.notFound().build());
    }

    // UPDATE LEVEL AND POINTS (ADMIN)
    @PutMapping("/updateLevelAndPoints/{idUsuari}")
    public ResponseEntity<Usuari> updateLevelAndPoints(@PathVariable Integer idUsuari, @RequestBody NivelDTO nivelDTO) {
        return usuariRepository.findById(idUsuari).map(usuari -> {
            if ("client".equals(usuari.getRol())) {
                usuari.setNivell(nivelDTO.getNivell());
                usuari.setPunts(nivelDTO.getPunts());
            }
            
            Usuari updated = usuariRepository.save(usuari);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }
}

