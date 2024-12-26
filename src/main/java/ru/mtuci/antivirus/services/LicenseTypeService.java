package ru.mtuci.antivirus.services;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mtuci.antivirus.entities.requests.LicenseTypeRequest;
import ru.mtuci.antivirus.entities.LicenseType;
import ru.mtuci.antivirus.repositories.LicenseTypeRepository;

import java.util.List;

@Service
public class LicenseTypeService {

    private final LicenseTypeRepository repository;

    @Autowired
    public LicenseTypeService(LicenseTypeRepository repository) {
        this.repository = repository;
    }

    // Получение LicenseType по id
    public LicenseType getLicenseTypeById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("LicenseType with id " + id + " not found"));
    }

    // CRUD Методы

    // Создание нового LicenseType
    public LicenseType create(@Valid LicenseTypeRequest request) {
        LicenseType type = new LicenseType();
        type.setName(request.getName());
        type.setDefaultDuration(request.getDefaultDuration());
        type.setDescription(request.getDescription());
        return repository.save(type);
    }

    // Обновление существующего LicenseType
    public LicenseType update(Long id, @Valid LicenseTypeRequest request) {
        LicenseType existingType = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("LicenseType with id " + id + " not found"));

        existingType.setName(request.getName());
        existingType.setDefaultDuration(request.getDefaultDuration());
        existingType.setDescription(request.getDescription());

        return repository.save(existingType);
    }

    // Удаление LicenseType по id
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new IllegalArgumentException("LicenseType with id " + id + " does not exist");
        }
    }

    // Получение всех LicenseType
    public List<LicenseType> findAll() {
        return repository.findAll();
    }

    public LicenseType createLicenseType(@Valid LicenseTypeRequest licenseTypeRequest) {
        return create(licenseTypeRequest);
    }

    public LicenseType updateLicenseType(Long id, @Valid LicenseTypeRequest licenseTypeRequest) {
        return update(id, licenseTypeRequest);
    }

    public void deleteLicenseType(Long id) {
        repository.deleteById(id);
    }

    public List<LicenseType> getAllLicenseTypes() {
        return repository.findAll();
    }
}
