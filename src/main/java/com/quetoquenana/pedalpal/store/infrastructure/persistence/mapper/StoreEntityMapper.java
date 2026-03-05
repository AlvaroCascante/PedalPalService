package com.quetoquenana.pedalpal.store.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.store.infrastructure.persistence.entity.StoreEntity;
import com.quetoquenana.pedalpal.store.infrastructure.persistence.entity.StoreLocationEntity;
import com.quetoquenana.pedalpal.store.domain.model.Store;
import com.quetoquenana.pedalpal.store.domain.model.StoreLocation;

import java.util.HashSet;
import java.util.Set;

/**
 * Maps store persistence entities to domain models and back.
 */
public final class StoreEntityMapper {

    private StoreEntityMapper() {
    }

    /**
     * Converts a store entity into a domain model.
     */
    public static Store toModel(StoreEntity entity) {
        Store model = Store.builder()
                .id(entity.getId())
                .name(entity.getName())
                .locations(new HashSet<>())
                .build();
        model.setVersion(entity.getVersion());

        if (entity.getLocations() != null) {
            entity.getLocations()
                    .stream()
                    .map(StoreEntityMapper::toModel)
                    .forEach(model::addLocation);
        }
        return model;
    }

    /**
     * Converts a store location entity into a domain model.
     */
    public static StoreLocation toModel(StoreLocationEntity entity) {
        StoreLocation model = StoreLocation.builder()
                .id(entity.getId())
                .name(entity.getName())
                .storePrefix(entity.getStorePrefix())
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

    /**
     * Converts a store domain model into a persistence entity.
     */
    public static StoreEntity toEntity(Store model) {
        StoreEntity entity = StoreEntity.builder()
                .id(model.getId())
                .name(model.getName())
                .build();
        entity.setVersion(model.getVersion());
        Set<StoreLocation> locations = model.getLocations() == null ? Set.of() : model.getLocations();
        locations.stream()
                .map(StoreEntityMapper::toEntity)
                .forEach(entity::addLocation);
        return entity;
    }

    private static StoreLocationEntity toEntity(StoreLocation model) {
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
