package ru.mtuci.antivirus.services;

import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.mtuci.antivirus.entities.*;
import ru.mtuci.antivirus.entities.requests.LicenseRequest;
import ru.mtuci.antivirus.repositories.DeviceRepository;
import ru.mtuci.antivirus.repositories.LicenseRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Service
public class LicenseService {

    private final LicenseRepository licenseRepo;
    private final ProductService productService;
    private final UserService userService;
    private final LicenseTypeService licenseTypeService;
    private final LicenseHistoryService licenseHistoryService;
    private final DeviceLicenseService deviceLicenseService;
    private final DeviceRepository deviceRepo;
    private final PasswordEncoder passwordEncoder;
    private ChronoUnit request;

    @Autowired
    public LicenseService(
            LicenseRepository licenseRepo,
            ProductService productService,
            UserService userService,
            LicenseTypeService licenseTypeService,
            LicenseHistoryService licenseHistoryService,
            DeviceLicenseService deviceLicenseService,
            DeviceRepository deviceRepo,
            PasswordEncoder passwordEncoder
    ) {
        this.licenseRepo = licenseRepo;
        this.productService = productService;
        this.userService = userService;
        this.licenseTypeService = licenseTypeService;
        this.licenseHistoryService = licenseHistoryService;
        this.deviceLicenseService = deviceLicenseService;
        this.deviceRepo = deviceRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public License createLicense(LicenseRequest request) {
        Product product = productService.getProductById(request.getProductId());
        User user = userService.getUserById(request.getOwnerId());
        LicenseType licenseType = licenseTypeService.getLicenseTypeById(request.getLicenseTypeId());

        if (product == null || user == null || licenseType == null) {
            throw new IllegalArgumentException("Invalid license creation parameters.");
        }

        String licenseCode = generateLicenseCode(request);

        License license = new License();
        license.setCode(licenseCode);
        license.setProduct(product);
        license.setType(licenseType);
        license.setOwner(user);
        license.setDuration(request.getDuration());
        license.setDescription(request.getDescription());
        license.setDevicesCount(request.getDeviceCount());
        license.setIsBlocked(false);
        licenseRepo.save(license);

        LicenseHistory history = new LicenseHistory(license, user, "CREATED", new Date(), "License created");
        licenseHistoryService.saveLicenseHistory(history);

        return license;
    }

    public Ticket activateLicense(String code, Device device, String login) {
        License license = licenseRepo.getLicensesByCode(code);
        User user = (User) userService.findUserByLogin(login);

        if (license == null || user == null) {
            throw new IllegalArgumentException("Invalid license or user.");
        }

        validateActivation(license, device);
        linkDeviceToLicense(license, device);
        finalizeActivation(license, user);

        LicenseHistory history = new LicenseHistory(license, user, "ACTIVATED", new Date(), "License activated");
        licenseHistoryService.saveLicenseHistory(history);

        return generateTicket(license, device);
    }

    private void validateActivation(License license, Device device) {
        if (license.getIsBlocked() || license.getEndingDate() != null && license.getEndingDate().before(new Date())) {
            throw new IllegalArgumentException("License is not eligible for activation.");
        }

        if (license.getFirstActivationDate() != null) {
            throw new IllegalArgumentException("License is already activated.");
        }

        if (deviceLicenseService.getDeviceLicensesByLicense(license).size() >= license.getDevicesCount()) {
            throw new IllegalArgumentException("Device limit exceeded for this license.");
        }
    }

    private void linkDeviceToLicense(License license, Device device) {
        DeviceLicense deviceLicense = new DeviceLicense();
        deviceLicense.setDevice(device);
        deviceLicense.setLicense(license);
        deviceLicense.setActivationDate(new Date());
        deviceLicenseService.save(deviceLicense);
    }

    private void finalizeActivation(License license, User user) {
        license.setFirstActivationDate(new Date());
        license.setEndingDate(new Date(System.currentTimeMillis() + license.getDuration()));
        license.setOwner(user);
        licenseRepo.save(license);
    }

    public License getLicenseById(Long id) {
        return licenseRepo.getLicenseById(id);
    }

    private String generateLicenseCode(LicenseRequest request) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String data = request.getProductId() + request.getOwnerId() + request.getLicenseTypeId() +
                    request.getDeviceCount() + request.getDuration() + request.getDescription() + LocalDateTime.now();
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate license code.", e);
        }
    }

    public Ticket generateTicket(License license, Device device) {
        Ticket ticket = new Ticket();
        ticket.setCurrentDate(new Date());
        ticket.setLifetime(license.getDuration());
        ticket.setActivationDate(new Date(license.getFirstActivationDate().getTime()));
        ticket.setExpirationDate(new Date(license.getEndingDate().getTime()));
        ticket.setUserId(license.getOwner().getId());
        ticket.setDeviceId(device.getId());
        ticket.setIsBlocked(false);
        ticket.setSignature(generateSignature(ticket));
        return ticket;
    }

    private String generateSignature(Ticket ticket) {
        return passwordEncoder.encode(ticket.getBodyForSigning());
    }

    public License getActiveLicenseForDevice(Device device, User user, @NotBlank(message = "Code cannot be empty") String code) {
        return licenseRepo.getLicensesByCode(code);
    }

    // Доработка метода для обновления лицензии
    public License updateExistentLicense(@NotBlank(message = "License key cannot be empty") String licenseKey,
                                         @NotBlank(message = "Login cannot be empty") String login,
                                         LicenseRequest request) { // Убедитесь, что здесь тип LicenseRequest
        License license = licenseRepo.getLicensesByCode(licenseKey);
        User user = userService.getUserByLogin(login);

        if (license == null || user == null) {
            throw new IllegalArgumentException("Invalid license or user.");
        }

        // Обновляем лицензию с новыми данными из LicenseRequest
        license.setProduct(productService.getProductById(request.getProductId())); // Убедитесь, что request имеет правильный тип
        license.setType(licenseTypeService.getLicenseTypeById(request.getLicenseTypeId()));
        license.setDuration(request.getDuration());
        license.setDescription(request.getDescription());
        license.setDevicesCount(request.getDeviceCount());
        licenseRepo.save(license);

        // Записываем историю обновления
        LicenseHistory history = new LicenseHistory(license, user, "UPDATED", new Date(), "License updated");
        licenseHistoryService.saveLicenseHistory(history);

        return license;
    }


    public License updateExistentLicense(@NotBlank(message = "License key cannot be empty") String licenseKey, @NotBlank(message = "Login cannot be empty") String login) {
        return licenseRepo.getLicensesByCode(licenseKey);
    }
}
