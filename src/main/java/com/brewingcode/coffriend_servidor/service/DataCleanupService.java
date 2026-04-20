package com.brewingcode.coffriend_servidor.service;

import com.brewingcode.coffriend_servidor.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        usuariRepository.findByIsDemo(true).forEach(usuario -> {
            usuario.getComandes().forEach(comanda -> {
                liniaComandaRepository.deleteAll(comanda.getLinies());
            });
        });

        usuariRepository.findByIsDemo(true).forEach(usuario -> {
            comandaRepository.deleteAll(usuario.getComandes());
        });

        usuariRepository.findByIsDemo(true).forEach(usuario -> {
            usuariInsigniaRepository.deleteAll(usuario.getInsignies());
        });

        usuariRepository.deleteAll(usuariRepository.findByIsDemo(true));

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
        usuariRepository.deleteAll();
        insigniaRepository.deleteAll();
        botigaRepository.deleteAll();
    }
}
