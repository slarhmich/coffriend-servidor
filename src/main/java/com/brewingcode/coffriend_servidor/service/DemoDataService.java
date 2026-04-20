package com.brewingcode.coffriend_servidor.service;

import com.brewingcode.coffriend_servidor.entities.*;
import com.brewingcode.coffriend_servidor.repositories.*;
import com.brewingcode.coffriend_servidor.security.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Adds  demo data */
@Service
@PreAuthorize("hasRole('admin')")
public class DemoDataService {
    @Autowired private BotigaRepository botigaRepository;
    @Autowired private ProducteRepository producteRepository;
    @Autowired private UsuariRepository usuariRepository;
    @Autowired private InsigniaRepository insigniaRepository;
    @Autowired private UsuariInsigniaRepository usuariInsigniaRepository;
    @Autowired private BadgeTriggerRepository badgeTriggerRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private DataCleanupService demoDataCleanupService;

    public void generateDemoData() {
        // clear old demo data
        demoDataCleanupService.deleteDemoDataOnly();

        // create shop
        Botiga botiga = createBotiga(
          "Brewing Coffee", 
          "Carrer de l'Exemple, 1, Barcelona", 
          "08:00-20:00", 
          "image.jpg" 
        );

        // demo badges with their triggers
        Insignia earlyBird = createInsignia("Matiner", "Demana cafè entre les 6h i les 8h");
        createBadgeTriggerTimeOfDay(earlyBird, 6, 8);

        Insignia coffeeMaster = createInsignia("Cafè espress", "Compra cafes espresso");
        createBadgeTriggerProductCategory(coffeeMaster, "Café");

        Insignia bigSpender = createInsignia("Gran Despesa", "Gasta 50€ o més en una sola comanda");
        createBadgeTriggerSpendingAmount(bigSpender, new BigDecimal("50"));

        Insignia loyaltyRewards = createInsignia("Recompenses de Fidelitat", "Completa 10 comandes");
        createBadgeTriggerOrderCount(loyaltyRewards, 10);

        // create products
        createProduct("Espresso", "2.50", "Café", botiga);
        createProduct("Cappuccino", "3.50", "Café", botiga);
        createProduct("Croissant", "2.00", "Pastry", botiga);
        createProduct("Cortado", "2.75", "Café", botiga);
        createProduct("Americano", "2.75", "Café", botiga);
        createProduct("Latte", "4.00", "Café", botiga);
        createProduct("Croissant Almendra", "2.50", "Pastry", botiga);
        createProduct("Muffin Blueberry", "3.00", "Pastry", botiga);
        createProduct("Espresso Doble", "3.50", "Café", botiga);
        createProduct("Macchiato", "3.25", "Café", botiga);
        createProduct("Panettone", "4.00", "Pastry", botiga);
        createProduct("Tiramisu", "5.50", "Dessert", botiga);

        // create users
        createUsuari("admin", "admin@example.com", "1234", RoleEnum.ADMIN.getDbValue(), null);
        createUsuari("Juan", "juan@example.com", "1234", RoleEnum.STAFF.getDbValue(), botiga);
        createUsuari("Maria", "maria@example.com", "1234", RoleEnum.STAFF.getDbValue(), botiga);
        Usuari customer1 = createUsuari("Pedro", "pedro@example.com", "1234", RoleEnum.CLIENT.getDbValue(), null);
        Usuari customer2 = createUsuari("Anna", "anna@example.com", "1234", RoleEnum.CLIENT.getDbValue(), null);

        // set gamification data to demo users
        customer1.setNivell(2);
        customer1.setPunts(250);
        customer2.setNivell(1);
        customer2.setPunts(100);
        usuariRepository.save(customer1);
        usuariRepository.save(customer2);

        // assign badges to customers
        assignBadgeToUser(customer1, earlyBird);
        assignBadgeToUser(customer1, coffeeMaster);
        assignBadgeToUser(customer2, bigSpender);
    }

    // helper methods
    private Botiga createBotiga(String nom, String adreca, String horari, String imatgeURL) {
        Botiga botiga = new Botiga();
        botiga.setNom(nom);
        botiga.setAdreca(adreca);
        botiga.setHorari(horari);
        botiga.setImatgeURL(imatgeURL);
        botiga.setIsDemo(true);
        return botigaRepository.save(botiga);
    }

    private Insignia createInsignia(String nom, String descripcio) {
        Insignia insignia = new Insignia();
        insignia.setNom(nom);
        insignia.setDescripcio(descripcio);
        insignia.setIsDemo(true);
        return insigniaRepository.save(insignia);
    }

    private Producte createProduct(String nom, String preu, String categoria, Botiga botiga) {
        Producte producte = new Producte();
        producte.setNom(nom);
        producte.setPreu(new BigDecimal(preu));
        producte.setCategoria(categoria);
        producte.setBotiga(botiga);
        return producteRepository.save(producte);
    }

    private Usuari createUsuari(String nom, String email, String password, String rol, Botiga botiga) {
        Usuari usuari = new Usuari();
        usuari.setNom(nom);
        usuari.setEmail(email);
        usuari.setPassword(passwordEncoder.encode(password));
        usuari.setRol(rol);
        usuari.setBotiga(botiga);
        usuari.setNivell(1);
        usuari.setPunts(0);
        usuari.setIsDemo(true);
        return usuariRepository.save(usuari);
    }

    private void assignBadgeToUser(Usuari usuari, Insignia insignia) {
        UsuariInsignia ui = new UsuariInsignia();
        ui.setUsuari(usuari);
        ui.setInsignia(insignia);
        ui.setDataObtencio(LocalDate.now());
        usuariInsigniaRepository.save(ui);
    }

    private void createBadgeTriggerTimeOfDay(Insignia insignia, Integer hourStart, Integer hourEnd) {
        BadgeTrigger trigger = new BadgeTrigger();
        trigger.setInsignia(insignia);
        trigger.setTriggerType(BadgeTrigger.TriggerType.TIME_OF_DAY);
        trigger.setHourStart(hourStart);
        trigger.setHourEnd(hourEnd);
        trigger.setIsActive(true);
        trigger.setIsDemo(true);
        badgeTriggerRepository.save(trigger);
    }

    private void createBadgeTriggerProductCategory(Insignia insignia, String productCategory) {
        BadgeTrigger trigger = new BadgeTrigger();
        trigger.setInsignia(insignia);
        trigger.setTriggerType(BadgeTrigger.TriggerType.PRODUCT_CATEGORY);
        trigger.setProductCategory(productCategory);
        trigger.setIsActive(true);
        trigger.setIsDemo(true);
        badgeTriggerRepository.save(trigger);
    }

    private void createBadgeTriggerSpendingAmount(Insignia insignia, BigDecimal minSpendingAmount) {
        BadgeTrigger trigger = new BadgeTrigger();
        trigger.setInsignia(insignia);
        trigger.setTriggerType(BadgeTrigger.TriggerType.SPENDING_AMOUNT);
        trigger.setMinSpendingAmount(minSpendingAmount);
        trigger.setIsActive(true);
        trigger.setIsDemo(true);
        badgeTriggerRepository.save(trigger);
    }

    private void createBadgeTriggerOrderCount(Insignia insignia, Integer minOrderCount) {
        BadgeTrigger trigger = new BadgeTrigger();
        trigger.setInsignia(insignia);
        trigger.setTriggerType(BadgeTrigger.TriggerType.ORDER_COUNT);
        trigger.setMinOrderCount(minOrderCount);
        trigger.setIsActive(true);
        trigger.setIsDemo(true);
        badgeTriggerRepository.save(trigger);
    }
}
