package com.quetoquenana.pedalpal.bike.application.useCase;

import com.quetoquenana.pedalpal.bike.application.command.CreateBikeCommand;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.domain.model.Bike;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEvent;
import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEventType;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Transactional
@Slf4j
@RequiredArgsConstructor
public class CreateBikeUseCase {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuthenticatedUserPort authenticatedUserPort;
    private final BikeMapper mapper;
    private final BikeRepository repository;

    public BikeResult execute(CreateBikeCommand command) {
        AuthenticatedUser currentUser = authenticatedUserPort.getAuthenticatedUser().
                orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

        if (command.serialNumber() != null && repository.existsBySerialNumber(command.serialNumber())) {
            throw new BusinessException("bike.serial.number.already.exists", command.serialNumber());
        }

        Bike bike = mapper.toModel(currentUser.userId(), command);
        bike = repository.save(bike);

        publishHistoryEvent(bike.getId(), currentUser.userId());

        return mapper.toResult(bike);
    }

    private void publishHistoryEvent(UUID bikeId, UUID userId) {
        applicationEventPublisher.publishEvent(
            new BikeHistoryEvent(
                    bikeId,
                    userId,
                    bikeId,
                    BikeHistoryEventType.CREATED,
                    null,
                    Instant.now()
            )
        );
    }
}
