package com.quetoquenana.pedalpal.bike.application.result;

import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEventType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record BikeHistoryResult(
    UUID id,
    UUID bikeId,
    LocalDateTime occurredAt,
    UUID performedBy,
    BikeHistoryEventType type,
    String payload
) { }
