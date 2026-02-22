package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.infrastructure.persistence.entity.ProductEntity;
import com.quetoquenana.pedalpal.common.domain.model.Product;
import org.springframework.stereotype.Component;

@Component

public class ProductMapper {

    public Product toDomain(ProductEntity entity) {
        if (entity == null) return null;

        Product product = Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .status(SystemCodeMapper.toSystemCode(entity.getStatus()))
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
                .status(SystemCodeMapper.toSystemCodeEntity(domain.getStatus()))
                .build();

        return entity;
    }
}

