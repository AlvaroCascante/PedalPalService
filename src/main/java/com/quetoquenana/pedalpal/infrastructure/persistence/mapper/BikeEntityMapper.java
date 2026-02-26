package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.bike.domain.model.*;
import com.quetoquenana.pedalpal.infrastructure.persistence.bike.entity.BikeComponentEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.bike.entity.BikeEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.bike.entity.BikeHistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BikeEntityMapper {

    public Bike toBike(BikeEntity entity) {
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
                    .map(this::toBikeComponent)
                    .forEach(model::addComponent);
        }
        return model;
    }

    public BikeComponent toBikeComponent(BikeComponentEntity entity) {
        BikeComponent model = BikeComponent.builder()
                .id(entity.getId())
                .componentType(SystemCodeEntityMapper.toSystemCode(entity.getComponentType()))
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

    public BikeHistory toBikeHistory(BikeHistoryEntity entity) {
        return BikeHistory.builder()
                .id(entity.getId())
                .bikeId(entity.getBikeId())
                .occurredAt(entity.getOccurredAt())
                .performedBy(entity.getPerformedBy())
                .type(entity.getType())
                .payload(entity.getPayload())
                .build();
    }

    public BikeEntity toBikeEntity(Bike model) {
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
                .map(this::toBikeComponentEntity)
                .forEach(entity::addComponent);
        return entity;
    }

    public BikeComponentEntity toBikeComponentEntity(BikeComponent model) {
        BikeComponentEntity entity = BikeComponentEntity.builder()
                .id(model.getId())
                .componentType(SystemCodeEntityMapper.toSystemCodeEntity(model.getComponentType()))
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

    public BikeHistoryEntity toBikeHistoryEntity(BikeHistory bikeHistory) {
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
