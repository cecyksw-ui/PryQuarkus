package com.cecyksw.application.service;

import com.cecyksw.domain.model.Product;
import com.cecyksw.domain.port.in.CreateProductUseCase;
import com.cecyksw.domain.port.in.DeleteProductUseCase;
import com.cecyksw.domain.port.in.GetProductUseCase;
import com.cecyksw.domain.port.in.UpdateProductUseCase;
import com.cecyksw.domain.port.out.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

/**
 * Application service that implements all use cases.
 * This is the core business logic layer that orchestrates domain operations.
 */
@ApplicationScoped
public class ProductService implements 
        CreateProductUseCase, 
        GetProductUseCase, 
        UpdateProductUseCase, 
        DeleteProductUseCase {

    private final ProductRepository productRepository;

    @Inject
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> updateProduct(Long id, Product product) {
        return productRepository.update(id, product);
    }

    @Override
    public boolean deleteProduct(Long id) {
        return productRepository.deleteById(id);
    }
}
