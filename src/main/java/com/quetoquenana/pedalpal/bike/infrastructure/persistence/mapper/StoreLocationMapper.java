package com.quetoquenana.pedalpal.bike.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.bike.domain.model.StoreLocation;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity.StoreLocationEntity;

public class StoreLocationMapper {

    private final StoreMapper storeMapper = new StoreMapper();
    private final SystemCodeMapper systemCodeMapper = new SystemCodeMapper();

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
                .status(systemCodeMapper.toDomain(entity.getStatus()))
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
                .status(systemCodeMapper.toEntity(domain.getStatus()))
                .build();
    }
}

