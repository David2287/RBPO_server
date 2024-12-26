package ru.mtuci.antivirus.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.mtuci.antivirus.entities.requests.ActivationRequest;
import ru.mtuci.antivirus.entities.requests.DeviceRequest;
import ru.mtuci.antivirus.entities.Device;
import ru.mtuci.antivirus.entities.User;
import ru.mtuci.antivirus.repositories.DeviceRepository;
import ru.mtuci.antivirus.repositories.UserRepository;

import java.util.List;

@Service
public class DeviceService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, UserRepository userRepository) {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
    }

    public Device registerOrUpdateDevice(ActivationRequest activationRequest, User user) {
        logger.info("Registering or updating device with MAC: {}", activationRequest.getMacAddress());

        Device device = deviceRepository.getDeviceByMacAddress(activationRequest.getMacAddress());
        if (device == null) {
            device = new Device();
            device.setMacAddress(activationRequest.getMacAddress());
            device.setUser(user);
        } else if (!device.getUser().equals(user)) {
            throw new IllegalArgumentException("Device already registered by another user");
        }

        device.setName(activationRequest.getDeviceName());
        return deviceRepository.save(device);
    }

    public Device getDeviceByInfo(String macAddress, User user) {
        logger.info("Fetching device by MAC: {} for user: {}", macAddress, user.getId());
        return deviceRepository.findDeviceByMacAddressAndUser(macAddress, user);
    }

    public Device createDevice(DeviceRequest deviceRequest) {
        logger.info("Creating device for user ID: {}", deviceRequest.getUserId());
        User user = userRepository.findById(deviceRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (deviceRepository.existsByMacAddress(deviceRequest.getMacAddress())) {
            throw new IllegalArgumentException("Device with this MAC address already exists");
        }
        Device device = new Device();
        device.setName(deviceRequest.getDeviceName());
        device.setMacAddress(deviceRequest.getMacAddress());
        device.setUser(user);
        return deviceRepository.save(device);
    }

    @Cacheable("devices")
    public Device getDeviceById(Long id) {
        logger.info("Fetching device with ID: {}", id);
        return deviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Device not found"));
    }

    public Device updateDevice(Long id, DeviceRequest deviceRequest) {
        logger.info("Updating device with ID: {}", id);
        Device device = getDeviceById(id);
        User user = userRepository.findById(deviceRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        device.setName(deviceRequest.getDeviceName());
        device.setMacAddress(deviceRequest.getMacAddress());
        device.setUser(user);
        return deviceRepository.save(device);
    }

    public void deleteDevice(Long id) {
        logger.info("Deleting device with ID: {}", id);
        deviceRepository.deleteById(id);
    }

    public List<Device> getAllDevices() {
        logger.info("Fetching all devices");
        return deviceRepository.findAll();
    }
}
