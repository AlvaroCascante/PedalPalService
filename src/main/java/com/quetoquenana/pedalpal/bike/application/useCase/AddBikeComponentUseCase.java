package com.quetoquenana.pedalpal.bike.application.useCase;

import com.quetoquenana.pedalpal.bike.application.command.AddBikeComponentCommand;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.domain.model.*;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.systemCode.domain.model.SystemCode;
import com.quetoquenana.pedalpal.systemCode.domain.repository.SystemCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static com.quetoquenana.pedalpal.common.util.Constants.BikeComponents.COMPONENT_TYPE;

@Transactional
@Slf4j
@RequiredArgsConstructor
public class AddBikeComponentUseCase {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuthenticatedUserPort authenticatedUserPort;
    private final BikeMapper mapper;
    private final BikeRepository repository;
    private final SystemCodeRepository systemCodeRepository;

    public BikeResult execute(AddBikeComponentCommand command) {
        AuthenticatedUser currentUser = authenticatedUserPort.getAuthenticatedUser().
                orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

        Bike bike = validate(currentUser.userId(), command);

        SystemCode componentType = systemCodeRepository.findByCategoryAndCode(COMPONENT_TYPE, command.type())
                .orElseThrow(() -> new RecordNotFoundException("bike.component.type.not.found", command.type()));

        BikeComponent component = mapper.toModel(command, componentType);
        bike.addComponent(component);
        repository.save(bike);

        publishHistoryEvent(
                bike.getId(),
                currentUser.userId(),
                component.getId(),
                component.getName()
        );

        return mapper.toResult(bike);
    }

    private Bike validate(UUID authenticatedUserId, AddBikeComponentCommand command) {
        Bike bike = repository.findByIdAndOwnerId(command.bikeId(), authenticatedUserId)
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));

        if (bike.getStatus() == null || !bike.getStatus().equals(BikeStatus.ACTIVE)) {
            throw new BadRequestException("bike.update.no.active");
        }

        return bike;
    }

    private void publishHistoryEvent(
            UUID bikeId,
            UUID userId,
            UUID componentId,
            String componentName
    ) {
        applicationEventPublisher.publishEvent(
            new BikeHistoryEvent(
                    bikeId,
                    userId,
                    componentId,
                    BikeHistoryEventType.COMPONENT_ADDED,
                    List.of(BikeChangeItem.of(
                            BikeField.BIKE_COMPONENT,
                            "",
                            componentName
                    )),
                    Instant.now()
            )
        );
    }
}
