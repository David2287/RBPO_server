package ru.mtuci.antivirus.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.mtuci.antivirus.entities.DeviceLicense;
import ru.mtuci.antivirus.entities.License;
import ru.mtuci.antivirus.repositories.DeviceLicenseRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceLicenseService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceLicenseService.class);
    private final DeviceLicenseRepository deviceLicenseRepository;

    public DeviceLicenseService(DeviceLicenseRepository deviceLicenseRepository) {
        this.deviceLicenseRepository = deviceLicenseRepository;
    }

    public List<DeviceLicense> getDeviceLicensesByLicense(License license) {
        logger.info("Fetching device licenses for license ID: {}", license.getId());
        return deviceLicenseRepository.getDeviceLicensesByLicense(license);
    }

    public void save(DeviceLicense deviceLicense) {
        logger.info("Saving DeviceLicense for device ID: {} and license ID: {}",
                deviceLicense.getDevice().getId(),
                deviceLicense.getLicense().getId());
        if (deviceLicenseRepository.getDeviceLicenseByDeviceIdAndLicenseId(
                deviceLicense.getDevice().getId(),
                deviceLicense.getLicense().getId()) != null) {
            throw new IllegalArgumentException("DeviceLicense already exists for this device and license");
        }
        deviceLicenseRepository.save(deviceLicense);
    }

    public DeviceLicense getDeviceLicenseByDeviceIdAndLicenseId(Long deviceId, Long licenseId) {
        logger.info("Fetching DeviceLicense for device ID: {} and license ID: {}", deviceId, licenseId);
        DeviceLicense deviceLicense = Optional.ofNullable(deviceLicenseRepository.getDeviceLicenseByDeviceIdAndLicenseId(deviceId, licenseId))
                .orElseThrow(() -> new IllegalArgumentException("DeviceLicense not found for deviceId " + deviceId + " and licenseId " + licenseId));

        return deviceLicense;
    }
}
