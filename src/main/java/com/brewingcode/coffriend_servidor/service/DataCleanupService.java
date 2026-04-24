package com.brewingcode.coffriend_servidor.service;

import com.brewingcode.coffriend_servidor.entities.Usuari;
import com.brewingcode.coffriend_servidor.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to delete all data from the db (demo data and user generated data (shop, customers, products, etc.))
 */
@Service
public class DataCleanupService {

    @Autowired private UsuariRepository usuariRepository;
    @Autowired private ComandaRepository comandaRepository;
    @Autowired private BotigaRepository botigaRepository;
    @Autowired private ProducteRepository producteRepository;
    @Autowired private InsigniaRepository insigniaRepository;
    @Autowired private UsuariInsigniaRepository usuariInsigniaRepository;
    @Autowired private LiniaComandaRepository liniaComandaRepository;

    /**
     * Deletes only demo data 
     */
    public void deleteDemoDataOnly() {
        List<Usuari> demoUsersToDelete = usuariRepository.findByIsDemo(true).stream()
                .filter(usuari -> !isAdmin(usuari))
                .toList();

        demoUsersToDelete.forEach(usuario -> {
            usuario.getComandes().forEach(comanda -> {
                liniaComandaRepository.deleteAll(comanda.getLinies());
            });
        });

        demoUsersToDelete.forEach(usuario -> {
            comandaRepository.deleteAll(usuario.getComandes());
        });

        demoUsersToDelete.forEach(usuario -> {
            usuariInsigniaRepository.deleteAll(usuario.getInsignies());
        });

        usuariRepository.deleteAll(demoUsersToDelete);

        botigaRepository.findByIsDemo(true).forEach(botiga -> {
            producteRepository.deleteAll(botiga.getProductes());
        });

        botigaRepository.deleteAll(botigaRepository.findByIsDemo(true));

        insigniaRepository.deleteAll(insigniaRepository.findByIsDemo(true));
    }

    /**
     * Deletes all data
     */
    public void deleteAllData() {
        liniaComandaRepository.deleteAll();
        usuariInsigniaRepository.deleteAll();
        comandaRepository.deleteAll();
        producteRepository.deleteAll();

        List<Usuari> adminUsers = usuariRepository.findAll().stream()
                .filter(this::isAdmin)
                .toList();

        if (!adminUsers.isEmpty()) {
            adminUsers.forEach(adminUser -> {
                adminUser.setBotiga(null);
                adminUser.setNivell(null);
                adminUser.setPunts(null);
                adminUser.setIsDemo(false);
            });
            usuariRepository.saveAll(adminUsers);
        }

        List<Usuari> nonAdminUsers = usuariRepository.findAll().stream()
                .filter(usuari -> !isAdmin(usuari))
                .toList();

        usuariRepository.deleteAll(nonAdminUsers);

        insigniaRepository.deleteAll();
        botigaRepository.deleteAll();
    }

    private boolean isAdmin(Usuari usuari) {
        return usuari != null && usuari.getRol() != null && "admin".equalsIgnoreCase(usuari.getRol());
    }
}
