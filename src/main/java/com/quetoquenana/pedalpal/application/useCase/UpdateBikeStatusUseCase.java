package com.quetoquenana.pedalpal.application.useCase;

import com.quetoquenana.pedalpal.application.command.UpdateBikeStatusCommand;
import com.quetoquenana.pedalpal.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.domain.enums.BikeStatus;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Slf4j
public class UpdateBikeStatusUseCase {

    private final BikeRepository bikeRepository;

    public BikeResult execute(UpdateBikeStatusCommand command) {
        try {
            Bike bike = bikeRepository.findByIdAndOwnerId(command.bikeId(), command.authenticatedUserId())
                    .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));

            applyPatch(bike, command);

            Bike saved = bikeRepository.save(bike);

            return BikeMapper.toBikeResult(saved);
        } catch (RuntimeException ex) {
            log.error("RuntimeException on UpdateBikeStatusUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw new BusinessException("bike.update.status.failed");
        }
    }

    private void applyPatch(Bike bike, UpdateBikeStatusCommand command) {
        if (command.status() != null) {
            rejectBlank(command.status());
            bike.setStatus(BikeStatus.from(command.status()));
        }
    }

    private void rejectBlank(String value) {
        if (value != null && value.trim().isEmpty()) {
            throw new BadRequestException("bike.update.status.blank");
        }
    }
}
