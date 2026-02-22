package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.infrastructure.persistence.entity.MaintenanceSuggestionItemEntity;
import com.quetoquenana.pedalpal.common.domain.model.MaintenanceSuggestionItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaintenanceSuggestionItemMapper {

    private final ProductPackageMapper productPackageMapper;
    private final ProductMapper productMapper;

    public MaintenanceSuggestionItem toDomain(MaintenanceSuggestionItemEntity entity) {
        if (entity == null) return null;

        return MaintenanceSuggestionItem.builder()
                .id(entity.getId())
                .productsPackage(productPackageMapper.toDomain(entity.getProductsPackage()))
                .product(productMapper.toDomain(entity.getProduct()))
                .priority(SystemCodeMapper.toSystemCode(entity.getPriority()))
                .urgency(SystemCodeMapper.toSystemCode(entity.getUrgency()))
                .reason(entity.getReason())
                .build();
    }

    public MaintenanceSuggestionItemEntity toEntity(MaintenanceSuggestionItem domain) {
        if (domain == null) return null;

        return MaintenanceSuggestionItemEntity.builder()
                .id(domain.getId())
                .productsPackage(productPackageMapper.toEntity(domain.getProductsPackage()))
                .product(productMapper.toEntity(domain.getProduct()))
                .priority(SystemCodeMapper.toSystemCodeEntity(domain.getPriority()))
                .urgency(SystemCodeMapper.toSystemCodeEntity(domain.getUrgency()))
                .reason(domain.getReason())
                .build();
    }
}

