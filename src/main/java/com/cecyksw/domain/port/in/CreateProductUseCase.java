package com.cecyksw.domain.port.in;

import com.cecyksw.domain.model.Product;

/**
 * Input port (interface) for creating a product.
 * This defines what the application can do.
 */
public interface CreateProductUseCase {
    Product createProduct(Product product);
}
