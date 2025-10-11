package com.cecyksw.domain.port.out;

import com.cecyksw.domain.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Output port (interface) for product persistence.
 * This defines what the application needs from external systems.
 */
public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    Optional<Product> update(Long id, Product product);
    boolean deleteById(Long id);
}
