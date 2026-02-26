package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.infrastructure.persistence.product.entity.ProductEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.product.entity.ProductPackageEntity;
import com.quetoquenana.pedalpal.product.domain.model.Product;
import com.quetoquenana.pedalpal.product.domain.model.ProductPackage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductEntityMapper {

    public ProductPackage toProductPackage(ProductPackageEntity entity) {
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
                    .map(this::toProduct)
                    .forEach(model::addProduct);
        }
        return model;
    }

    public Product toProduct(ProductEntity entity) {
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

    public ProductPackageEntity toProductPackageEntity(ProductPackage model) {
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
                .map(this::toProductEntity)
                .forEach(entity::addProduct);
        return entity;
    }

    public ProductEntity toProductEntity(Product model) {
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
