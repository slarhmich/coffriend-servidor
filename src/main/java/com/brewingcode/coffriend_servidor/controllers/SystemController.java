package com.brewingcode.coffriend_servidor.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.brewingcode.coffriend_servidor.service.DemoDataService;
import com.brewingcode.coffriend_servidor.service.DataCleanupService;

/** Controller for system related tasks 
 * - Adding demo data: seedDemoData()
 * - Deleting demo data: resetDemoData()
 * - Deleting all data: resetAllDatabase()
*/
@RestController
@RequestMapping("/api/system")
@CrossOrigin(origins = "*")
public class SystemController {
    @Autowired private DemoDataService demoDataService;
    @Autowired private DataCleanupService demoDataCleanupService;

    // delete all demo data
    @DeleteMapping("/resetDemo")
    public ResponseEntity<String> resetDemoData() {
        try {
            demoDataCleanupService.deleteDemoDataOnly();
            return ResponseEntity.ok("Demo data removed.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error resetting demo data: " + e.getMessage());
        }
    }

    // delete all data
    @DeleteMapping("/resetAll")
    public ResponseEntity<String> resetAllDatabase() {
        try {
            demoDataCleanupService.deleteAllData();
            return ResponseEntity.ok("All data removed.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error wiping database: " + e.getMessage());
        }
    }

    // generate demo data
    @PostMapping("/seed")
    public ResponseEntity<String> seedDemoData() {
        try {
            demoDataService.generateDemoData();
            return ResponseEntity.ok("Demo data generated successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating demo data: " + e.getMessage());
        }
    }
}
