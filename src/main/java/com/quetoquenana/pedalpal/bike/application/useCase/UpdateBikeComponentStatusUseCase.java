package com.quetoquenana.pedalpal.bike.application.useCase;

import com.quetoquenana.pedalpal.bike.application.command.UpdateBikeComponentStatusCommand;
import com.quetoquenana.pedalpal.bike.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.domain.model.*;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Slf4j
public class UpdateBikeComponentStatusUseCase {

    private final BikeMapper bikeMapper;
    private final BikeRepository bikeRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BikeResult execute(UpdateBikeComponentStatusCommand command) {
        Bike bike = bikeRepository.findByIdAndOwnerId(command.bikeId(), command.authenticatedUserId())
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));

        BikeComponent component = bike.getComponents().stream()
                .filter(c -> c.getId().equals(command.componentId()))
                .findFirst()
                .orElseThrow(() -> new RecordNotFoundException("bike.component.not.found"));

        try {
            List<BikeChangeItem> bikeChangeItems = applyPatch(component, command);
            bikeRepository.save(bike);
            publishHistoryEvent(bike.getId(), command.authenticatedUserId(), component.getId(), bikeChangeItems);
            return bikeMapper.toResult(bike);
        } catch (BadRequestException ex) {
            log.error("BadRequestException on UpdateBikeComponentUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw ex;
        } catch (RuntimeException ex) {
            log.error("RuntimeException on UpdateBikeComponentStatusUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw new BusinessException("bike.component.update.status.failed");
        }
    }

    private void publishHistoryEvent(UUID bikeId, UUID userId, UUID componentId, List<BikeChangeItem> bikeChangeItems) {
        if (!bikeChangeItems.isEmpty()) {
            eventPublisher.publishEvent(
                new BikeHistoryEvent(
                        bikeId,
                        userId,
                        componentId,
                        BikeHistoryEventType.COMPONENT_STATUS_CHANGED,
                        bikeChangeItems,
                        LocalDateTime.now()
                )
            );
        }
    }

    private List<BikeChangeItem>  applyPatch(BikeComponent component, UpdateBikeComponentStatusCommand command) {
        List<BikeChangeItem> bikeChangeItems = new ArrayList<>();

        if (command.status() != null) {
            rejectBlank(command.status());
            component.changeStatus(BikeComponentStatus.from(command.status())).ifPresent(bikeChangeItems::add);
        }
        return bikeChangeItems;
    }

    private void rejectBlank(String value) {
        if (value != null && value.trim().isEmpty()) {
            throw new BadRequestException("bike.component.update.status.blank");
        }
    }
}
