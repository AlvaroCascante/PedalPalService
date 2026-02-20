package com.quetoquenana.pedalpal.bike.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.bike.domain.model.MaintenanceSuggestionItem;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity.MaintenanceSuggestionItemEntity;

public class MaintenanceSuggestionItemMapper {

    private final ProductPackageMapper productPackageMapper = new ProductPackageMapper();
    private final ProductMapper productMapper = new ProductMapper();
    private final SystemCodeMapper systemCodeMapper = new SystemCodeMapper();

    public MaintenanceSuggestionItem toDomain(MaintenanceSuggestionItemEntity entity) {
        if (entity == null) return null;

        return MaintenanceSuggestionItem.builder()
                .id(entity.getId())
                .productsPackage(productPackageMapper.toDomain(entity.getProductsPackage()))
                .product(productMapper.toDomain(entity.getProduct()))
                .priority(systemCodeMapper.toDomain(entity.getPriority()))
                .urgency(systemCodeMapper.toDomain(entity.getUrgency()))
                .reason(entity.getReason())
                .build();
    }

    public MaintenanceSuggestionItemEntity toEntity(MaintenanceSuggestionItem domain) {
        if (domain == null) return null;

        return MaintenanceSuggestionItemEntity.builder()
                .id(domain.getId())
                .productsPackage(productPackageMapper.toEntity(domain.getProductsPackage()))
                .product(productMapper.toEntity(domain.getProduct()))
                .priority(systemCodeMapper.toEntity(domain.getPriority()))
                .urgency(systemCodeMapper.toEntity(domain.getUrgency()))
                .reason(domain.getReason())
                .build();
    }
}

