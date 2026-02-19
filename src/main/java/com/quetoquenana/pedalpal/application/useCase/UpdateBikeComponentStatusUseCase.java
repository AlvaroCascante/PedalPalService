package com.quetoquenana.pedalpal.application.useCase;

import com.quetoquenana.pedalpal.application.command.UpdateBikeComponentStatusCommand;
import com.quetoquenana.pedalpal.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.application.result.BikeResult;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.domain.enums.BikeComponentStatus;
import com.quetoquenana.pedalpal.domain.model.Bike;
import com.quetoquenana.pedalpal.domain.model.BikeComponent;
import com.quetoquenana.pedalpal.domain.repository.BikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Slf4j
public class UpdateBikeComponentStatusUseCase {

    private final BikeRepository bikeRepository;

    public BikeResult execute(UpdateBikeComponentStatusCommand command) {
        try {
            Bike bike = bikeRepository.findByIdAndOwnerId(command.bikeId(), command.authenticatedUserId())
                    .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));

            BikeComponent component = bike.getComponents().stream()
                    .filter(c -> c.getId().equals(command.componentId()))
                    .findFirst()
                    .orElseThrow(() -> new RecordNotFoundException("bike.component.not.found"));

            applyPatch(component, command);

            return BikeMapper.toBikeResult(bikeRepository.save(bike));
        } catch (RuntimeException ex) {
            log.error("RuntimeException on UpdateBikeComponentStatusUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw new BusinessException("bike.component.update.status.failed");
        }
    }

    private void applyPatch(BikeComponent component, UpdateBikeComponentStatusCommand command) {
        if (command.status() != null) {
            rejectBlank(command.status());
            component.setStatus(BikeComponentStatus.from(command.status()));
        }
    }

    private void rejectBlank(String value) {
        if (value != null && value.trim().isEmpty()) {
            throw new BadRequestException("bike.component.update.status.blank");
        }
    }
}
