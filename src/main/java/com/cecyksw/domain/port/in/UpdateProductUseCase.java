package com.cecyksw.domain.port.in;

import com.cecyksw.domain.model.Product;

import java.util.Optional;

/**
 * Input port (interface) for updating a product.
 */
public interface UpdateProductUseCase {
    Optional<Product> updateProduct(Long id, Product product);
}
