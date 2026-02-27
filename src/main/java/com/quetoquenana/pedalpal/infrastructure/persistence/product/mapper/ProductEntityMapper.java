package com.quetoquenana.pedalpal.infrastructure.persistence.product.mapper;

import com.quetoquenana.pedalpal.infrastructure.persistence.product.entity.ProductEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.product.entity.ProductPackageEntity;
import com.quetoquenana.pedalpal.product.domain.model.Product;
import com.quetoquenana.pedalpal.product.domain.model.ProductPackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEntityMapper {

    public ProductPackage toModel(ProductPackageEntity entity) {
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
                    .map(this::toModel)
                    .forEach(model::addProduct);
        }
        return model;
    }

    public Product toModel(ProductEntity entity) {
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

    public ProductPackageEntity toEntity(ProductPackage model) {
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
                .map(this::toEntity)
                .forEach(entity::addProduct);
        return entity;
    }

    public ProductEntity toEntity(Product model) {
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
