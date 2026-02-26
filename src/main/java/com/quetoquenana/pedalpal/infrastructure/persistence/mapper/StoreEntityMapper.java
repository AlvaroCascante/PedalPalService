package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.infrastructure.persistence.store.entity.StoreEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.store.entity.StoreLocationEntity;
import com.quetoquenana.pedalpal.store.domain.model.Store;
import com.quetoquenana.pedalpal.store.domain.model.StoreLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class StoreEntityMapper {

    public Store toStore(StoreEntity entity) {
        Store model = Store.builder()
                .id(entity.getId())
                .name(entity.getName())
                .locations(new HashSet<>())
                .build();
        model.setVersion(entity.getVersion());

        if (entity.getLocations() != null) {
            entity.getLocations()
                    .stream()
                    .map(this::toStoreLocation)
                    .forEach(model::addLocation);
        }
        return model;
    }

    public StoreLocation toStoreLocation(StoreLocationEntity entity) {
        StoreLocation model = StoreLocation.builder()
                .id(entity.getId())
                .name(entity.getName())
                .website(entity.getWebsite())
                .address(entity.getAddress())
                .latitude(entity.getLatitude())
                .longitude(entity.getLongitude())
                .phone(entity.getPhone())
                .status(entity.getStatus())
                .timezone(entity.getTimezone())
                .build();
        model.setVersion(entity.getVersion());
        return model;
    }

    public StoreEntity toStoreEntity(Store model) {
        StoreEntity entity = StoreEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
        entity.setVersion(model.getVersion());
        model.getLocations()
                .stream()
                .map(this::toStoreLocationEntity)
                .forEach(entity::addLocation);
        return entity;
    }

    public StoreLocationEntity toStoreLocationEntity(StoreLocation model) {
        StoreLocationEntity entity = StoreLocationEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .status(model.getStatus())
                .website(model.getWebsite())
                .address(model.getAddress())
                .latitude(model.getLatitude())
                .longitude(model.getLongitude())
                .phone(model.getPhone())
                .build();
        entity.setVersion(model.getVersion());
        return entity;
    }
}
