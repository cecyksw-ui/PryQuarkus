package com.cecyksw.domain.port.in;

/**
 * Input port (interface) for deleting a product.
 */
public interface DeleteProductUseCase {
    boolean deleteProduct(Long id);
}
