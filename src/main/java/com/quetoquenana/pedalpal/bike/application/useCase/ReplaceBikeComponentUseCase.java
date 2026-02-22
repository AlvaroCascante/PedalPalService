package com.quetoquenana.pedalpal.bike.application.useCase;

import com.quetoquenana.pedalpal.bike.application.command.AddBikeComponentCommand;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.domain.model.*;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.domain.model.SystemCode;
import com.quetoquenana.pedalpal.common.domain.repository.SystemCodeRepository;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.quetoquenana.pedalpal.common.util.Constants.BikeComponents.COMPONENT_TYPE;

@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReplaceBikeComponentUseCase {

    private final BikeMapper bikeMapper;
    private final BikeRepository bikeRepository;
    private final SystemCodeRepository systemCodeRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BikeResult execute(AddBikeComponentCommand command) {
        Bike bike = bikeRepository.findByIdAndOwnerId(command.bikeId(), command.authenticatedUserId())
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));

        BikeComponent component = bike.getComponents().stream()
                .filter(c -> c.getId().equals(command.componentId()))
                .findFirst()
                .orElseThrow(() -> new RecordNotFoundException("bike.component.not.found"));

        if (bike.getStatus() == null || !bike.getStatus().equals(BikeStatus.ACTIVE)) {
            throw new BadRequestException("bike.update.no.active");
        }

        if (!BikeComponentStatus.ACTIVE.equals(component.getStatus())) {
            throw new BadRequestException("bike.component.update.no.active");
        }

        SystemCode componentType = systemCodeRepository.findByCategoryAndCode(COMPONENT_TYPE, command.type())
                .orElseThrow(() -> new RecordNotFoundException("bike.component.type.not.found", command.type()));

        try {
            BikeComponent newComponent = bikeMapper.toBikeComponent(command, componentType);
            component.changeStatus(BikeComponentStatus.REPLACED);
            bike.addComponent(newComponent);
            bikeRepository.save(bike);

            publishHistoryEvent(bike.getId(), command.authenticatedUserId(), component, newComponent);
            return bikeMapper.toBikeResult(bike);
        } catch (RuntimeException ex) {
            log.error("RuntimeException on ReplaceBikeComponentUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw new BusinessException("bike.replace.component.failed");
        }
    }

    private void publishHistoryEvent(UUID bikeId, UUID userId, BikeComponent oldComponent, BikeComponent newComponent) {

        eventPublisher.publishEvent(
            new BikeHistoryEvent(
                    bikeId,
                    userId,
                    oldComponent.getId(),
                    BikeHistoryEventType.COMPONENT_REPLACED,
                null,
                    LocalDateTime.now()
            )
        );

        eventPublisher.publishEvent(
            new BikeHistoryEvent(
                    bikeId,
                    userId,
                    newComponent.getId(),
                    BikeHistoryEventType.COMPONENT_ADDED,
                    null,
                    LocalDateTime.now()
            )
        );
    }
}
