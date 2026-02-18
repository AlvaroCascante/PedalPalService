package com.quetoquenana.pedalpal.application.useCase;

import com.quetoquenana.pedalpal.application.command.BikeResult;
import com.quetoquenana.pedalpal.application.command.UpdateBikeStatusCommand;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
public class UpdateBikeStatusUseCase {

    private final BikeRepository bikeRepository;

    public BikeResult execute(UpdateBikeStatusCommand command) {
        Bike bike = bikeRepository.findByIdAndOwnerId(command.bikeId(), command.authenticatedUserId())
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));

        applyPatch(bike, command);

        Bike saved = bikeRepository.save(bike);

        return new BikeResult(
                saved.getId(),
                saved.getName(),
                saved.getType().name(),
                saved.getStatus().name(),
                saved.isPublic(),
                saved.isExternalSync(),
                saved.getBrand(),
                saved.getModel(),
                saved.getYear(),
                saved.getSerialNumber(),
                saved.getNotes(),
                saved.getOdometerKm() == null ? 0 : saved.getOdometerKm(),
                saved.getUsageTimeMinutes() == null ? 0 : saved.getUsageTimeMinutes()
        );
    }

    private void applyPatch(Bike bike, UpdateBikeStatusCommand command) {
        if (command.status() != null) {
            rejectBlank(command.status());
            try {
                bike.setStatus(BikeStatus.valueOf(command.status()));
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("bike.update.status.invalid", command.status());
            }
        }
    }

    private void rejectBlank(String value) {
        if (value != null && value.trim().isEmpty()) {
            throw new BadRequestException("bike.update.status.blank");
        }
    }
}
