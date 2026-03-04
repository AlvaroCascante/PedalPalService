package com.quetoquenana.pedalpal.bike.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record BikeHistoryResponse(
    UUID id,
    UUID bikeId,
    LocalDateTime occurredAt,
    UUID performedBy,
    String type,
    String payload
) { }
