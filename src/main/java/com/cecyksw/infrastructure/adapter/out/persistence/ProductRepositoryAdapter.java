package com.cecyksw.infrastructure.adapter.out.persistence;

import com.cecyksw.domain.model.Product;
import com.cecyksw.domain.port.out.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter that implements the ProductRepository port using Panache.
 * This adapts the external persistence mechanism to our domain needs.
 */
@ApplicationScoped
public class ProductRepositoryAdapter implements ProductRepository {

    @Override
    @Transactional
    public Product save(Product product) {
        ProductEntity entity = ProductMapper.toEntity(product);
        entity.persist();
        return ProductMapper.toDomain(entity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return ProductEntity.<ProductEntity>findByIdOptional(id)
                .map(entity -> ProductMapper.toDomain(entity));
    }

    @Override
    public List<Product> findAll() {
        return ProductEntity.<ProductEntity>listAll().stream()
                .map(ProductMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<Product> update(Long id, Product product) {
        return ProductEntity.<ProductEntity>findByIdOptional(id)
                .map(entity -> {
                    ProductMapper.updateEntity(entity, product);
                    entity.persist();
                    return ProductMapper.toDomain(entity);
                });
    }

    @Override
    @Transactional
    public boolean deleteById(Long id) {
        return ProductEntity.deleteById(id);
    }
}
