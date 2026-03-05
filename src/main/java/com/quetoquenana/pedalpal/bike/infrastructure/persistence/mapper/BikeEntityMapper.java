package com.quetoquenana.pedalpal.bike.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.model.BikeComponent;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistory;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity.BikeComponentEntity;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity.BikeEntity;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity.BikeHistoryEntity;
import com.quetoquenana.pedalpal.systemCode.infrastructure.persistence.mapper.SystemCodeEntityMapper;

/**
 * Maps bike persistence entities to domain models and back.
 * Prefer static utility if they are pure and dependency‑free.
 * If they need JPA helpers, converters, or other collaborators,
 * use DI and keep them package‑private when possible.
 */
public class BikeEntityMapper {

    private BikeEntityMapper() {}

    public static Bike toModel(BikeEntity entity) {
        Bike model = Bike.builder()
                .id(entity.getId())
                .ownerId(entity.getOwnerId())
                .name(entity.getName())
                .type(entity.getType())
                .status(entity.getStatus())
                .isPublic(entity.isPublic())
                .isExternalSync(entity.isExternalSync())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .year(entity.getYear())
                .serialNumber(entity.getSerialNumber())
                .notes(entity.getNotes())
                .odometerKm(entity.getOdometerKm())
                .usageTimeMinutes(entity.getUsageTimeMinutes())
                .build();
        model.setVersion(entity.getVersion());
        if (entity.getComponents() != null) {
            entity.getComponents()
                    .stream()
                    .map(BikeEntityMapper::toModel)
                    .forEach(model::addComponent);
        }
        return model;
    }

    private static BikeComponent toModel(BikeComponentEntity entity) {
        BikeComponent model = BikeComponent.builder()
                .id(entity.getId())
                .componentType(SystemCodeEntityMapper.toModel(entity.getComponentType()))
                .name(entity.getName())
                .status(entity.getStatus())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .notes(entity.getNotes())
                .odometerKm(entity.getOdometerKm())
                .usageTimeMinutes(entity.getUsageTimeMinutes())
                .build();
        model.setVersion(entity.getVersion());
        return model;
    }

    public static BikeHistory toModel(BikeHistoryEntity entity) {
        return BikeHistory.builder()
                .id(entity.getId())
                .bikeId(entity.getBikeId())
                .occurredAt(entity.getOccurredAt())
                .performedBy(entity.getPerformedBy())
                .type(entity.getType())
                .payload(entity.getPayload())
                .build();
    }

    public static BikeEntity toEntity(Bike model) {
        BikeEntity entity = BikeEntity.builder()
                .id(model.getId())
                .ownerId(model.getOwnerId())
                .name(model.getName())
                .type(model.getType())
                .status(model.getStatus())
                .isPublic(model.isPublic())
                .isExternalSync(model.isExternalSync())
                .brand(model.getBrand())
                .model(model.getModel())
                .year(model.getYear())
                .serialNumber(model.getSerialNumber())
                .notes(model.getNotes())
                .odometerKm(model.getOdometerKm())
                .usageTimeMinutes(model.getUsageTimeMinutes())
                .build();
        entity.setVersion(model.getVersion());
        model.getComponents()
                .stream()
                .map(BikeEntityMapper::toEntity)
                .forEach(entity::addComponent);
        return entity;
    }

    private static BikeComponentEntity toEntity(BikeComponent model) {
        BikeComponentEntity entity = BikeComponentEntity.builder()
                .id(model.getId())
                .componentType(SystemCodeEntityMapper.toEntity(model.getComponentType()))
                .name(model.getName())
                .status(model.getStatus())
                .brand(model.getBrand())
                .model(model.getModel())
                .notes(model.getNotes())
                .odometerKm(model.getOdometerKm())
                .usageTimeMinutes(model.getUsageTimeMinutes())
                .build();
        entity.setVersion(model.getVersion());
        return entity;
    }

    public static BikeHistoryEntity toEntity(BikeHistory bikeHistory) {
        return BikeHistoryEntity.builder()
                .id(bikeHistory.getId())
                .bikeId(bikeHistory.getBikeId())
                .referenceId(bikeHistory.getReferenceId())
                .occurredAt(bikeHistory.getOccurredAt())
                .performedBy(bikeHistory.getPerformedBy())
                .type(bikeHistory.getType())
                .payload(bikeHistory.getPayload())
                .build();
    }
}
