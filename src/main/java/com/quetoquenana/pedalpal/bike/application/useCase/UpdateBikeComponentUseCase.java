package com.quetoquenana.pedalpal.bike.application.useCase;

import com.quetoquenana.pedalpal.bike.application.command.UpdateBikeComponentCommand;
import com.quetoquenana.pedalpal.bike.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.domain.model.*;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.systemCode.domain.model.SystemCode;
import com.quetoquenana.pedalpal.systemCode.domain.repository.SystemCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.quetoquenana.pedalpal.common.util.Constants.BikeComponents.COMPONENT_TYPE;

@Transactional
@RequiredArgsConstructor
@Slf4j
public class UpdateBikeComponentUseCase {

    private final BikeMapper bikeMapper;
    private final BikeRepository bikeRepository;
    private final SystemCodeRepository systemCodeRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BikeResult execute(UpdateBikeComponentCommand command) {
        Bike bike = bikeRepository.findByIdAndOwnerId(command.bikeId(), command.authenticatedUserId())
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

        try {
            List<BikeChangeItem> bikeChangeItems = applyPatch(component, command, componentType);
            bikeRepository.save(bike);
            publishHistoryEvent(bike.getId(), command.authenticatedUserId(), component.getId(), bikeChangeItems);
            return bikeMapper.toBikeResult(bike);
        } catch (RecordNotFoundException ex) {
            log.error("RecordNotFoundException on UpdateBikeComponentUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw ex;
        } catch (BadRequestException ex) {
            log.error("BadRequestException on UpdateBikeComponentUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw ex;
        } catch (RuntimeException ex) {
            log.error("RuntimeException on UpdateBikeComponentUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw new BusinessException("bike.component.update.failed");
        }
    }

    private void publishHistoryEvent(UUID bikeId, UUID userId, UUID componentId, List<BikeChangeItem> bikeChangeItems) {
        if (!bikeChangeItems.isEmpty()) {
            eventPublisher.publishEvent(
                new BikeHistoryEvent(
                        bikeId,
                        userId,
                        componentId,
                        BikeHistoryEventType.COMPONENT_UPDATED,
                        bikeChangeItems,
                        LocalDateTime.now()
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
