package com.quetoquenana.pedalpal.bike.application.useCase;

import com.quetoquenana.pedalpal.bike.application.command.UpdateBikeComponentCommand;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.domain.model.*;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.systemCode.domain.model.SystemCode;
import com.quetoquenana.pedalpal.systemCode.domain.repository.SystemCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.quetoquenana.pedalpal.common.util.Constants.BikeComponents.COMPONENT_TYPE;

@Transactional
@Slf4j
@RequiredArgsConstructor
public class UpdateBikeComponentUseCase {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuthenticatedUserPort authenticatedUserPort;
    private final BikeMapper bikeMapper;
    private final BikeRepository bikeRepository;
    private final SystemCodeRepository systemCodeRepository;

    public BikeResult execute(UpdateBikeComponentCommand command) {
        AuthenticatedUser currentUser = authenticatedUserPort.getAuthenticatedUser().
                orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

        Bike bike = bikeRepository.findByIdAndOwnerId(command.bikeId(), currentUser.userId())
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));

        BikeComponent component = bike.getComponents().stream()
                .filter(c -> c.getId().equals(command.componentId()))
                .findFirst()
                .orElseThrow(() -> new RecordNotFoundException("bike.component.not.found"));

        SystemCode componentType = null;
        if (command.type() != null) {
            componentType = systemCodeRepository.findByCategoryAndCode(COMPONENT_TYPE, command.type())
                    .orElseThrow(() -> new RecordNotFoundException("bike.component.type.not.found", command.type()));
        }

        if (bike.getStatus() == null || !bike.getStatus().equals(BikeStatus.ACTIVE)) {
            throw new BadRequestException("bike.update.no.active");
        }

        if (component.getStatus() == null) {
            throw new BadRequestException("bike.component.update.status.required");
        }

        if (!component.getStatus().equals(BikeComponentStatus.ACTIVE)) {
            throw new BadRequestException("bike.component.update.no.active");
        }

        List<BikeChangeItem> bikeChangeItems = applyPatch(component, command, componentType);
        bikeRepository.save(bike);
        publishHistoryEvent(bike.getId(), currentUser.userId(), component.getId(), bikeChangeItems);
        return bikeMapper.toResult(bike);
    }

    private void publishHistoryEvent(UUID bikeId, UUID userId, UUID componentId, List<BikeChangeItem> bikeChangeItems) {
        if (!bikeChangeItems.isEmpty()) {
            applicationEventPublisher.publishEvent(
                new BikeHistoryEvent(
                        bikeId,
                        userId,
                        componentId,
                        BikeHistoryEventType.COMPONENT_UPDATED,
                        bikeChangeItems,
                        Instant.now()
                )
            );
        }
    }

    private List<BikeChangeItem> applyPatch(
            BikeComponent component,
            UpdateBikeComponentCommand command,
            SystemCode componentType
    ) {
        List<BikeChangeItem> bikeChangeItems = new ArrayList<>();

        if (command.name() != null) {
            rejectBlank(command.name(), "bike.component.update.name.blank");
            component.changeName(command.name()).ifPresent(bikeChangeItems::add);
        }
        if (command.type() != null) {
            rejectBlank(command.type(), "bike.component.update.type.blank");
            component.changeComponentType(componentType).ifPresent(bikeChangeItems::add);
        }
        if (command.brand() != null) {
            rejectBlank(command.brand(), "bike.component.update.brand.blank");
            component.changeBrand(command.brand()).ifPresent(bikeChangeItems::add);
        }
        if (command.model() != null) {
            rejectBlank(command.model(), "bike.component.update.model.blank");
            component.changeModel(command.model()).ifPresent(bikeChangeItems::add);
        }
        if (command.notes() != null) {
            rejectBlank(command.notes(), "bike.component.update.notes.blank");
            component.changeNotes(command.notes()).ifPresent(bikeChangeItems::add);
        }
        if (command.odometerKm() != null) {
            component.changeOdometerKm(command.odometerKm()).ifPresent(bikeChangeItems::add);
        }
        if (command.usageTimeMinutes() != null) {
            component.changeUsageTimeMinutes(command.usageTimeMinutes()).ifPresent(bikeChangeItems::add);
        }
        return bikeChangeItems;
    }

    private void rejectBlank(String value, String messageKey) {
        if (value != null && value.trim().isEmpty()) {
            throw new BadRequestException(messageKey);
        }
    }
}
