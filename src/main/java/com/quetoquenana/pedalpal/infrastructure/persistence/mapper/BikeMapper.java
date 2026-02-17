package com.quetoquenana.pedalpal.infrastructure.persistence.mapper;

import com.quetoquenana.pedalpal.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.infrastructure.persistence.entity.BikeEntity;
import org.springframework.stereotype.Component;

@Component
public class BikeMapper {

    public Bike toDomain(BikeEntity entity) {
        return Bike.builder()
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
    }

    public BikeEntity toEntity(Bike entity) {
        return BikeEntity.builder()
                .ownerId(entity.getOwnerId())
                .name(entity.getName())
                .type(entity.getType())
                .status(entity.getStatus().name())
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
    }
}
