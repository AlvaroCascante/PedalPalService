package com.quetoquenana.pedalpal.bike.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.bike.domain.model.Product;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity.ProductEntity;

public class ProductMapper {

    private final SystemCodeMapper systemCodeMapper = new SystemCodeMapper();

    public Product toDomain(ProductEntity entity) {
        if (entity == null) return null;

        Product product = Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .status(systemCodeMapper.toDomain(entity.getStatus()))
                .build();

        return product;
    }

    public ProductEntity toEntity(Product domain) {
        if (domain == null) return null;

        ProductEntity entity = ProductEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .price(domain.getPrice())
                .status(systemCodeMapper.toEntity(domain.getStatus()))
                .build();

        return entity;
    }
}

