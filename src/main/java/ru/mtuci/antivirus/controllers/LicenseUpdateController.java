package ru.mtuci.antivirus.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mtuci.antivirus.entities.Device;
import ru.mtuci.antivirus.entities.License;
import ru.mtuci.antivirus.entities.requests.LicenseUpdateRequest;
import ru.mtuci.antivirus.entities.Ticket;
import ru.mtuci.antivirus.services.LicenseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/license")
public class LicenseUpdateController {

    private static final Logger logger = LoggerFactory.getLogger(LicenseUpdateController.class);

    private final LicenseService licenseService;

    @Autowired
    public LicenseUpdateController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")  // Пример: проверка роли
    public ResponseEntity<?> updateLicense(@Valid @RequestBody LicenseUpdateRequest updateRequest) {
        try {
            // Обновление лицензии
            License updatedLicense = licenseService.updateExistentLicense(updateRequest.getLicenseKey(), updateRequest.getLogin());

            // Проверка на блокировку лицензии
            if (updatedLicense.getIsBlocked()) {
                logger.warn("License update unavailable for license key: {}", updateRequest.getLicenseKey());
                return ResponseEntity.status(400).body("License update unavailable: License is blocked.");
            }

            // Генерация билета (при необходимости передавайте device как аргумент)
            Ticket ticket = licenseService.generateTicket(updatedLicense, null);  // Замените `null` на объект Device, если он у вас есть

            // Возвращаем успешный ответ
            logger.info("License update successful for license key: {}", updateRequest.getLicenseKey());
            return ResponseEntity.ok("License update successful, Ticket:\n" + ticket.getBody());

        } catch (IllegalArgumentException e) {
            logger.error("Error updating license: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
