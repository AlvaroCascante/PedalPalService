package com.quetoquenana.pedalpal.bike.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.model.BikeComponent;
import com.quetoquenana.pedalpal.bike.domain.model.SystemCode;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity.BikeComponentEntity;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity.BikeEntity;
import com.quetoquenana.pedalpal.bike.infrastructure.persistence.entity.SystemCodeEntity;
import org.springframework.stereotype.Component;

@Component
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
                .componentType(toSystemCode(entity.getComponentType()))
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
                .componentType(toSystemCodeEntity(model.getComponentType()))
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

    public SystemCode toSystemCode(SystemCodeEntity entity) {
        return SystemCode.builder()
                .id(entity.getId())
                .category(entity.getCategory())
                .code(entity.getCode())
                .description(entity.getDescription())
                .codeKey(entity.getCodeKey())
                .label(entity.getLabel())
                .isActive(entity.getIsActive())
                .position(entity.getPosition())
                .build();
    }

    public SystemCodeEntity toSystemCodeEntity(SystemCode model) {
        return SystemCodeEntity.builder()
                .id(model.getId())
                .category(model.getCategory())
                .code(model.getCode())
                .description(model.getDescription())
                .codeKey(model.getCodeKey())
                .label(model.getLabel())
                .isActive(model.getIsActive())
                .position(model.getPosition())
                .build();
    }
}
