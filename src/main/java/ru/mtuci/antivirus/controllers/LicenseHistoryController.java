package ru.mtuci.antivirus.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.antivirus.entities.LicenseHistory;
import ru.mtuci.antivirus.services.LicenseHistoryService;

import java.util.List;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RestController
@RequestMapping("/license-history")
public class LicenseHistoryController {

    private final LicenseHistoryService licenseHistoryService;

    public LicenseHistoryController(LicenseHistoryService licenseHistoryService) {
        this.licenseHistoryService = licenseHistoryService;
    }

    @GetMapping
    public ResponseEntity<List<LicenseHistory>> getAllLicenseHistories() {
        List<LicenseHistory> licenseHistories = licenseHistoryService.getAllLicenseHistories();
        return ResponseEntity.ok(licenseHistories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLicenseHistoryById(@PathVariable Long id) {
        try {
            LicenseHistory licenseHistory = licenseHistoryService.getLicenseHistoryById(id);
            return ResponseEntity.ok(licenseHistory);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Record with this ID: " + id + " not founded");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLicenseHistoryById(@PathVariable Long id) {
        try {
            licenseHistoryService.deleteLicenseHistoryById(id);
            return ResponseEntity.ok("Record with this ID: " + id + " successful deleted");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Record with this ID: " + id + " not founded");
        }
    }
}
