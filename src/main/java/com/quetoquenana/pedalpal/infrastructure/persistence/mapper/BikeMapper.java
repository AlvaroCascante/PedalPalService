package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.infrastructure.persistence.entity.BikeEntity;
import org.springframework.stereotype.Component;

@Component
public class BikeMapper {

    public Bike toDomain(BikeEntity entity) {
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
                .build();
        model.setVersion(entity.getVersion());
        return model;
    }

    public BikeEntity toEntity(Bike model) {
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
}
