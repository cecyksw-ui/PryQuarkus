package com.cecyksw.infrastructure.adapter.out.persistence;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

/**
 * JPA Entity for product persistence.
 * This is part of the infrastructure layer and maps to the database.
 */
@Entity
@Table(name = "products")
public class ProductEntity extends PanacheEntity {

    @Column(nullable = false)
    public String name;

    @Column(length = 500)
    public String description;

    @Column(nullable = false, precision = 10, scale = 2)
    public BigDecimal price;

    @Column(nullable = false)
    public Integer stock;
}
