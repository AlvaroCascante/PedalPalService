package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.infrastructure.persistence.entity.StoreLocationEntity;
import com.quetoquenana.pedalpal.common.domain.model.StoreLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StoreLocationMapper {

    private final StoreMapper storeMapper;

    public StoreLocation toDomain(StoreLocationEntity entity) {
        if (entity == null) return null;

        return StoreLocation.builder()
                .id(entity.getId())
                .store(storeMapper.toDomain(entity.getStore()))
                .name(entity.getName())
                .website(entity.getWebsite())
                .address(entity.getAddress())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .phone(entity.getPhone())
                .status(SystemCodeMapper.toSystemCode(entity.getStatus()))
                .build();
    }

    public StoreLocationEntity toEntity(StoreLocation domain) {
        if (domain == null) return null;

        return StoreLocationEntity.builder()
                .id(domain.getId())
                .store(storeMapper.toEntity(domain.getStore()))
                .name(domain.getName())
                .website(domain.getWebsite())
                .address(domain.getAddress())
                .latitude(domain.getLatitude())
                .longitude(domain.getLongitude())
                .phone(domain.getPhone())
                .status(SystemCodeMapper.toSystemCodeEntity(domain.getStatus()))
                .build();
    }
}

