package com.cecyksw.infrastructure.adapter.out.persistence;

import com.cecyksw.domain.model.Product;

/**
 * Mapper to convert between domain model and JPA entity.
 * This keeps the domain model independent from infrastructure concerns.
 */
public class ProductMapper {

    public static Product toDomain(ProductEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Product(
                entity.id,
                entity.name,
                entity.description,
                entity.price,
                entity.stock
        );
    }

    public static ProductEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }
        ProductEntity entity = new ProductEntity();
        entity.id = product.getId();
        entity.name = product.getName();
        entity.description = product.getDescription();
        entity.price = product.getPrice();
        entity.stock = product.getStock();
        return entity;
    }

    public static void updateEntity(ProductEntity entity, Product product) {
        if (entity == null || product == null) {
            return;
        }
        entity.name = product.getName();
        entity.description = product.getDescription();
        entity.price = product.getPrice();
        entity.stock = product.getStock();
    }
}
