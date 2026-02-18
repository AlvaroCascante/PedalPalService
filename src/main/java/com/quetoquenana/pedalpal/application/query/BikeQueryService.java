package com.quetoquenana.pedalpal.application.query;

import com.quetoquenana.pedalpal.application.command.BikeResult;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class BikeQueryService {
    private final BikeRepository bikeRepository;

    public BikeResult getById(UUID id, UUID ownerId) {
        Bike bike = bikeRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(RecordNotFoundException::new);

        return BikeResult.builder()
                .id(bike.getId())
                .name(bike.getName())
                .type(bike.getType().name())
                .brand(bike.getBrand())
                .model(bike.getModel())
                .year(bike.getYear())
                .serialNumber(bike.getSerialNumber())
                .notes(bike.getNotes())
                .odometerKm(bike.getOdometerKm())
                .usageTimeMinutes(bike.getUsageTimeMinutes())
                .isPublic(bike.isPublic())
                .isExternalSync(bike.isExternalSync())
                .build();
    }

    public List<BikeResult> fetchActiveByOwnerId(UUID ownerId) {
        List<Bike> bikes = bikeRepository.findByOwnerIdAndStatus(ownerId, BikeStatus.ACTIVE.name());

        return bikes.stream()
                .map(bike -> BikeResult.builder()
                        .id(bike.getId())
                        .name(bike.getName())
                        .type(bike.getType().name())
                        .brand(bike.getBrand())
                        .model(bike.getModel())
                        .year(bike.getYear())
                        .serialNumber(bike.getSerialNumber())
                        .notes(bike.getNotes())
                        .odometerKm(bike.getOdometerKm())
                        .usageTimeMinutes(bike.getUsageTimeMinutes())
                        .isPublic(bike.isPublic())
                        .isExternalSync(bike.isExternalSync())
                        .build())
                .toList();
    }
}
