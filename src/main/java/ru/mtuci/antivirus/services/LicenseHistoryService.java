package ru.mtuci.antivirus.services;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import ru.mtuci.antivirus.entities.LicenseHistory;
import ru.mtuci.antivirus.repositories.LicenseHistoryRepository;

//import javax.validation.Valid;
import java.util.List;

@Service
public class LicenseHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(LicenseHistoryService.class);
    private final LicenseHistoryRepository licenseHistoryRepository;

    @Autowired
    public LicenseHistoryService(LicenseHistoryRepository licenseHistoryRepository) {
        this.licenseHistoryRepository = licenseHistoryRepository;
    }

    public void saveLicenseHistory(@Valid LicenseHistory licenseHistory) {
        logger.info("Saving license history: {}", licenseHistory);
        licenseHistoryRepository.save(licenseHistory);
    }

    public List<LicenseHistory> getAllLicenseHistories() {
        logger.info("Fetching all license histories");
        return licenseHistoryRepository.findAll();
    }

    @Cacheable("licenseHistories")
    public LicenseHistory getLicenseHistoryById(Long id) {
        logger.info("Fetching license history with id: {}", id);
        return licenseHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись с id " + id + " не найдена"));
    }

    public void deleteLicenseHistoryById(Long id) {
        LicenseHistory licenseHistory = getLicenseHistoryById(id);
        logger.info("Deleting license history: {}", licenseHistory);
        licenseHistoryRepository.delete(licenseHistory);
    }
}
