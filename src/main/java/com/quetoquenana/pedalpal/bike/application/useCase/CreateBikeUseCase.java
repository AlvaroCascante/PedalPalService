package com.quetoquenana.pedalpal.bike.application.useCase;

import com.quetoquenana.pedalpal.bike.application.command.CreateBikeCommand;
import com.quetoquenana.pedalpal.bike.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEvent;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEventType;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Slf4j
public class CreateBikeUseCase {

    private final BikeMapper mapper;
    private final BikeRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    public BikeResult execute(CreateBikeCommand command) {
        if (command.serialNumber() != null && repository.existsBySerialNumber(command.serialNumber())) {
            throw new BusinessException("bike.serial.number.already.exists", command.serialNumber());
        }
        try {
            Bike bike = mapper.toModel(command);
            bike = repository.save(bike);

            publishHistoryEvent(bike.getId(), command.ownerId());

            return mapper.toResult(bike);
        } catch (RuntimeException ex) {
            log.error("RuntimeException on CreateBikeUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw new BusinessException("bike.creation.failed");
        }
    }

    private void publishHistoryEvent(UUID bikeId, UUID userId) {
        eventPublisher.publishEvent(
            new BikeHistoryEvent(
                    bikeId,
                    userId,
                    bikeId,
                    BikeHistoryEventType.CREATED,
                    null,
                    LocalDateTime.now()
            )
        );
    }
}
