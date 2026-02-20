package com.quetoquenana.pedalpal.bike.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.bike.domain.model.ProductPackage;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity.ProductEntity;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity.ProductPackageEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductPackageMapper {

    private final SystemCodeMapper systemCodeMapper = new SystemCodeMapper();
    private final ProductMapper productMapper = new ProductMapper();

    public ProductPackage toDomain(ProductPackageEntity entity) {
        if (entity == null) return null;

        ProductPackage domain = ProductPackage.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .status(systemCodeMapper.toDomain(entity.getStatus()))
                .products(entity.getProducts() == null ? new HashSet<>() :
                        entity.getProducts().stream().map(productMapper::toDomain).collect(Collectors.toSet()))
                .build();

        return domain;
    }

    public ProductPackageEntity toEntity(ProductPackage domain) {
        if (domain == null) return null;

        Set<ProductEntity> products = domain.getProducts() == null ? new HashSet<>() :
                domain.getProducts().stream().map(productMapper::toEntity).collect(Collectors.toSet());

        ProductPackageEntity entity = ProductPackageEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .price(domain.getPrice())
                .status(systemCodeMapper.toEntity(domain.getStatus()))
                .products(products)
                .build();
        return entity;
    }
}

