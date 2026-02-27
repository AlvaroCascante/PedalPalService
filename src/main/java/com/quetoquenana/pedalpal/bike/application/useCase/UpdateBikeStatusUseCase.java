package com.quetoquenana.pedalpal.bike.application.useCase;

import com.quetoquenana.pedalpal.bike.application.command.UpdateBikeStatusCommand;
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
public class UpdateBikeStatusUseCase {

    private final BikeMapper bikeMapper;
    private final BikeRepository bikeRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BikeResult execute(UpdateBikeStatusCommand command) {
        Bike bike = bikeRepository.findByIdAndOwnerId(command.bikeId(), command.authenticatedUserId())
                .orElseThrow(() -> new RecordNotFoundException("bike.not.found"));
        try {
            List<BikeChangeItem> bikeChangeItems = applyPatch(bike, command);
            bikeRepository.save(bike);

            publishHistoryEvent(bike.getId(), command.authenticatedUserId(), bikeChangeItems);
            return bikeMapper.toResult(bike);
        } catch (BadRequestException ex) {
            log.error("BadRequestException on UpdateBikeComponentUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw ex;
        } catch (RuntimeException ex) {
            log.error("RuntimeException on UpdateBikeStatusUseCase -- Command: {}: Error: {}", command, ex.getMessage());
            throw new BusinessException("bike.update.status.failed");
        }
    }

    private void publishHistoryEvent(UUID bikeId, UUID userId, List<BikeChangeItem> bikeChangeItems) {
        if (!bikeChangeItems.isEmpty()) {
            eventPublisher.publishEvent(
                    new BikeHistoryEvent(
                            bikeId,
                            userId,
                            bikeId,
                            BikeHistoryEventType.STATUS_CHANGED,
                            bikeChangeItems,
                            LocalDateTime.now()
                    )
            );
        }
    }

    private List<BikeChangeItem>  applyPatch(Bike bike, UpdateBikeStatusCommand command) {
        List<BikeChangeItem> bikeChangeItems = new ArrayList<>();

        if (command.status() != null) {
            rejectBlank(command.status());
            bike.changeStatus(BikeStatus.from(command.status())).ifPresent(bikeChangeItems::add);
        }
        return bikeChangeItems;
    }

    private void rejectBlank(String value) {
        if (value != null && value.trim().isEmpty()) {
            throw new BadRequestException("bike.update.status.blank");
        }
    }
}
