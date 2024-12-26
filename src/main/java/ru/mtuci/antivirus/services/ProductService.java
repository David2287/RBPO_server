package ru.mtuci.antivirus.services;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mtuci.antivirus.entities.requests.ProductRequest;
import ru.mtuci.antivirus.entities.Product;
import ru.mtuci.antivirus.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    // Поиск продукта по ID
    public Product findById(Long id) {
        Optional<Product> product = repository.findById(id);
        return product.orElse(null); // Возвращает null, если продукт не найден
    }

    // CRUD Методы

    // Создание нового продукта
    public Product create(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setBlocked(request.isBlocked());
        return repository.save(product);
    }

    // Обновление существующего продукта
    public Product update(Long id, ProductRequest request) {
        Optional<Product> existingProductOpt = repository.findById(id);
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();
            existingProduct.setName(request.getName());
            existingProduct.setBlocked(request.isBlocked());
            return repository.save(existingProduct);
        }
        return null; // Продукт не найден, возвращаем null
    }

    // Удаление продукта
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // Получение списка всех продуктов
    public List<Product> findAll() {
        return repository.findAll();
    }

    // Метод для создания продукта
    public Product createProduct(@Valid ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setBlocked(productRequest.isBlocked());
        return repository.save(product);
    }

    // Поиск продукта по ID с использованием Optional
    public Product getProductById(Long productId) {
        Optional<Product> product = repository.findById(productId);
        return product.orElse(null); // Возвращает null, если продукт не найден
    }

    // Обновление продукта
    public Product updateProduct(Long id, @Valid ProductRequest productRequest) {
        Optional<Product> existingProduct = repository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(productRequest.getName());
            product.setBlocked(productRequest.isBlocked());
            return repository.save(product);
        } else {
            // Продукт не найден, можно выбросить исключение или вернуть null
            return null;
        }
    }

    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }
}
