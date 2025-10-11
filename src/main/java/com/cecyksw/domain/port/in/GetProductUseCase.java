package com.cecyksw.domain.port.in;

import com.cecyksw.domain.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Input port (interface) for retrieving products.
 */
public interface GetProductUseCase {
    Optional<Product> getProductById(Long id);
    List<Product> getAllProducts();
}
