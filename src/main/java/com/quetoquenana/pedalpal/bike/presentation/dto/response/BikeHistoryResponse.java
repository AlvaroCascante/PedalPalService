package com.quetoquenana.pedalpal.bike.presentation.dto.response;

import com.quetoquenana.pedalpal.bike.domain.model.BikeHistoryEventType;

import java.time.LocalDateTime;
import java.util.UUID;

public record BikeHistoryResponse(
    UUID id,
    UUID bikeId,
    LocalDateTime occurredAt,
    UUID performedBy,
    BikeHistoryEventType type,
    String payload
) { }
