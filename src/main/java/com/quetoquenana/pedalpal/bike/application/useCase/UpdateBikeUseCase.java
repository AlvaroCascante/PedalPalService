package com.quetoquenana.pedalpal.bike.application.useCase;

import com.quetoquenana.pedalpal.bike.application.command.UpdateBikeCommand;
import com.quetoquenana.pedalpal.bike.application.mapper.BikeMapper;
import com.quetoquenana.pedalpal.bike.application.result.BikeResult;
import com.quetoquenana.pedalpal.bike.domain.model.*;
import com.quetoquenana.pedalpal.bike.domain.repository.BikeRepository;
import com.quetoquenana.pedalpal.common.application.port.AuthenticatedUserPort;
import com.quetoquenana.pedalpal.common.domain.model.AuthenticatedUser;
import com.quetoquenana.pedalpal.common.exception.BadRequestException;
import com.quetoquenana.pedalpal.common.exception.BusinessException;
import com.quetoquenana.pedalpal.common.exception.ForbiddenAccessException;
import com.quetoquenana.pedalpal.common.exception.RecordNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Transactional
@Slf4j
@RequiredArgsConstructor
public class UpdateBikeUseCase {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuthenticatedUserPort authenticatedUserPort;
    private final BikeMapper bikeMapper;
    private final BikeRepository bikeRepository;

    public BikeResult execute(UpdateBikeCommand command) {
        AuthenticatedUser currentUser = authenticatedUserPort.getAuthenticatedUser().
                orElseThrow(() -> new ForbiddenAccessException("authentication.required"));

        Bike bike = validate(currentUser.userId(), command);

        List<BikeChangeItem> bikeChangeItems = applyPatch(bike, command);
        bikeRepository.save(bike);

        publishHistoryEvent(bike.getId(), currentUser.userId(), bikeChangeItems);
        return bikeMapper.toResult(bike);
    }

    private Bike validate(UUID authenticatedUserId, UpdateBikeCommand command) {
        Bike bike = bikeRepository.findByIdAndOwnerId(command.bikeId(), authenticatedUserId)
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));

        if (!bike.getStatus().equals(BikeStatus.ACTIVE)) {
            throw new BadRequestException("bike.update.no.active");
        }

        String newSerial = command.serialNumber();
        if (!Objects.equals(newSerial, bike.getSerialNumber()) && bikeRepository.existsBySerialNumber(newSerial)) {
            throw new BusinessException("bike.serial.number.already.exists", newSerial);
        }

        return bike;
    }

    private void publishHistoryEvent(UUID bikeId, UUID userId, List<BikeChangeItem> bikeChangeItems) {
        if (!bikeChangeItems.isEmpty()) {
            applicationEventPublisher.publishEvent(
                new BikeHistoryEvent(
                        bikeId,
                        userId,
                        bikeId,
                        BikeHistoryEventType.UPDATED,
                        bikeChangeItems,
                        Instant.now()
                )
            );
        }
    }

    private List<BikeChangeItem> applyPatch(Bike bike, UpdateBikeCommand command) {
        List<BikeChangeItem> bikeChangeItems = new ArrayList<>();

        if (command.name() != null) {
            rejectBlank(command.name(), "bike.update.name.blank");
            bike.changeName(command.name()).ifPresent(bikeChangeItems::add);
        }

        if (command.type() != null) {
            rejectBlank(command.type(), "bike.update.type.blank");
            bike.changeType(BikeType.from(command.type())).ifPresent(bikeChangeItems::add);
        }

        if (command.brand() != null) {
            rejectBlank(command.brand(), "bike.update.brand.blank");
            bike.changeBrand(command.brand()).ifPresent(bikeChangeItems::add);
        }

        if (command.model() != null) {
            rejectBlank(command.model(), "bike.update.model.blank");
            bike.changeModel(command.model()).ifPresent(bikeChangeItems::add);
        }

        if (command.year() != null) {
            bike.changeYear(command.year()).ifPresent(bikeChangeItems::add);
        }

        if (command.serialNumber() != null) {
            rejectBlank(command.serialNumber(), "bike.update.serial.blank");
            bike.changeSerialNumber(command.serialNumber()).ifPresent(bikeChangeItems::add);
        }

        if (command.notes() != null) {
            rejectBlank(command.notes(), "bike.update.notes.blank");
            bike.changeNotes(command.notes()).ifPresent(bikeChangeItems::add);
        }

        if (command.odometerKm() != null) {
            bike.changeOdometerKm(command.odometerKm()).ifPresent(bikeChangeItems::add);
        }

        if (command.usageTimeMinutes() != null) {
            bike.changeUsageTimeMinutes(command.usageTimeMinutes()).ifPresent(bikeChangeItems::add);
        }

        if (command.isPublic() != null) {
            bike.changeIsPublic(command.isPublic()).ifPresent(bikeChangeItems::add);
        }

        if (command.isExternalSync() != null) {
            bike.changeIsExternalSync(command.isExternalSync()).ifPresent(bikeChangeItems::add);
        }

        if (command.externalGearId() != null) {
            bike.changeExternalGearId(command.externalGearId()).ifPresent(bikeChangeItems::add);
        }

        if (command.externalSyncProvider() != null) {
            bike.changeExternalSyncProvider(ExternalSyncProvider.from(command.externalSyncProvider())).ifPresent(bikeChangeItems::add);
        }
        return bikeChangeItems;
    }

    private void rejectBlank(String value, String messageKey) {
        if (value != null && value.trim().isEmpty()) {
            throw new BadRequestException(messageKey);
        }
    }
}
