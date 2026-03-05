package com.quetoquenana.pedalpal.product.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.product.domain.model.Product;
import com.quetoquenana.pedalpal.product.domain.model.ProductPackage;
import com.quetoquenana.pedalpal.product.infrastructure.persistence.entity.ProductEntity;
import com.quetoquenana.pedalpal.product.infrastructure.persistence.entity.ProductPackageEntity;

/**
 * Maps product persistence entities to domain models and back.
 * Prefer static utility if they are pure and dependency‑free.
 * If they need JPA helpers, converters, or other collaborators,
 * use DI and keep them package‑private when possible.
 */
public class ProductEntityMapper {

    private ProductEntityMapper() {}

    public static ProductPackage toModel(ProductPackageEntity entity) {
        ProductPackage model = ProductPackage.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .status(entity.getStatus())
                .build();
        model.setVersion(entity.getVersion());
        if (entity.getProducts() != null) {
            entity.getProducts()
                    .stream()
                    .map(ProductEntityMapper::toModel)
                    .forEach(model::addProduct);
        }
        return model;
    }

    public static Product toModel(ProductEntity entity) {
        Product model = Product.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .status(entity.getStatus())
                .build();
        model.setVersion(entity.getVersion());
        return model;
    }

    public static ProductPackageEntity toEntity(ProductPackage model) {
        ProductPackageEntity entity = ProductPackageEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .price(model.getPrice())
                .status(model.getStatus())
                .build();
        entity.setVersion(model.getVersion());
        model.getProducts()
                .stream()
                .map(ProductEntityMapper::toEntity)
                .forEach(entity::addProduct);
        return entity;
    }

    public static ProductEntity toEntity(Product model) {
        ProductEntity entity = ProductEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .status(model.getStatus())
                .description(model.getDescription())
                .price(model.getPrice())
                .build();
        entity.setVersion(model.getVersion());
        return entity;
    }
}
