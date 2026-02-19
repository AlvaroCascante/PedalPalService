package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.model.BikeComponent;
import com.quetoquenana.pedalpal.domain.model.SystemCode;
import com.quetoquenana.pedalpal.infrastructure.persistence.entity.BikeComponentEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.entity.BikeEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.entity.SystemCodeEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BikeEntityMapper {

    public Bike toBike(BikeEntity entity) {
        Bike model = Bike.builder()
                .id(entity.getId())
                .ownerId(entity.getOwnerId())
                .name(entity.getName())
                .type(entity.getType())
                .status(BikeStatus.from(entity.getStatus()))
                .isPublic(entity.isPublic())
                .isExternalSync(entity.isExternalSync())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .year(entity.getYear())
                .serialNumber(entity.getSerialNumber())
                .notes(entity.getNotes())
                .odometerKm(entity.getOdometerKm())
                .usageTimeMinutes(entity.getUsageTimeMinutes())
                .components(entity.getComponents().stream().map(this::toBikeComponent).collect(Collectors.toSet()))
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
                .status(model.getStatus().name())
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
        return entity;
    }

    public BikeComponent toBikeComponent(BikeComponentEntity entity) {
        BikeComponent model = BikeComponent.builder()
                .id(entity.getId())
                .componentType(toSystemCode(entity.getComponentType()))
                .name(entity.getName())
                .brand(entity.getBrand())
                .model(entity.getModel())
                .notes(entity.getNotes())
                .odometerKm(entity.getOdometerKm())
                .usageTimeMinutes(entity.getUsageTimeMinutes())
                .build();
        model.setVersion(entity.getVersion());
        return model;
    }

    public BikeComponentEntity toBikeComponentEntity(BikeEntity bikeEntity, BikeComponent model) {
        return BikeComponentEntity.builder()
                .id(model.getId())
                .bike(bikeEntity)
                .componentType(toSystemCodeEntity(model.getComponentType()))
                .name(model.getName())
                .brand(model.getBrand())
                .model(model.getModel())
                .notes(model.getNotes())
                .odometerKm(model.getOdometerKm())
                .usageTimeMinutes(model.getUsageTimeMinutes())
                .build();
    }

    public SystemCode toSystemCode(SystemCodeEntity entity) {
        return SystemCode.builder()
                .id(entity.getId())
                .category(entity.getCategory())
                .code(entity.getCode())
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
                .label(model.getLabel())
                .isActive(model.getIsActive())
                .position(model.getPosition())
                .build();
    }
}
