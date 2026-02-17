package com.quetoquenana.pedalpal.application.useCase;

import com.quetoquenana.pedalpal.application.command.CreateBikeCommand;
import com.quetoquenana.pedalpal.application.command.CreateBikeResult;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.domain.enums.BikeType;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
@RequiredArgsConstructor
public class CreateBikeUseCase {

    private final BikeRepository bikeRepository;

    public CreateBikeResult execute(CreateBikeCommand command) {

        if (command.serialNumber() != null &&
                bikeRepository.existsBySerialNumber(command.serialNumber())) {
            throw new BusinessException("bike.serial.number.already.exists", command.serialNumber());
        }

        Bike bike = Bike.builder()
                .id(UUID.randomUUID())
                .ownerId(command.ownerId())
                .name(command.name())
                .type(BikeType.valueOf(command.type()))
                .status(BikeStatus.ACTIVE)
                .isPublic(command.isPublic())
                .isExternalSync(command.isExternalSync())
                .brand(command.brand())
                .model(command.model())
                .year(command.year())
                .serialNumber(command.serialNumber())
                .notes(command.notes())
                .odometerKm(command.odometerKm())
                .usageTimeMinutes(command.usageTimeMinutes())
                .build();

        try {
            Bike savedBike = bikeRepository.save(bike);

            return CreateBikeResult.builder()
                    .id(savedBike.getId())
                    .ownerId(savedBike.getOwnerId())
                    .name(savedBike.getName())
                    .type(savedBike.getType().name())
                    .brand(savedBike.getBrand())
                    .model(savedBike.getModel())
                    .year(savedBike.getYear())
                    .serialNumber(savedBike.getSerialNumber())
                    .notes(savedBike.getNotes())
                    .odometerKm(savedBike.getOdometerKm())
                    .usageTimeMinutes(savedBike.getUsageTimeMinutes())
                    .isPublic(savedBike.isPublic())
                    .isExternalSync(savedBike.isExternalSync())
                    .build();
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException("bike.creation.failed", ex);
        }
    }
}
