package com.quetoquenana.pedalpal.bike.application.useCase;

import com.quetoquenana.pedalpal.bike.application.command.AddBikeComponentCommand;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.domain.model.*;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
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
@RequiredArgsConstructor
@Slf4j
public class AddBikeComponentUseCase {

    private final BikeMapper bikeMapper;
    private final BikeRepository bikeRepository;
    private final SystemCodeRepository systemCodeRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BikeResult execute(AddBikeComponentCommand command) {
        Bike bike = validate(command);

        SystemCode componentType = systemCodeRepository.findByCategoryAndCode(COMPONENT_TYPE, command.type())
                .orElseThrow(() -> new RecordNotFoundException("bike.component.type.not.found", command.type()));

        BikeComponent component = bikeMapper.toModel(command, componentType);
        bike.addComponent(component);
        bikeRepository.save(bike);

        publishHistoryEvent(
                bike.getId(),
                command.authenticatedUserId(),
                component.getId(),
                component.getName()
        );

        return bikeMapper.toResult(bike);
    }

    private Bike validate(AddBikeComponentCommand command) {
        Bike bike = bikeRepository.findByIdAndOwnerId(command.bikeId(), command.authenticatedUserId())
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
        eventPublisher.publishEvent(
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
