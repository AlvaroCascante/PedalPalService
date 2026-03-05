package com.quetoquenana.pedalpal.bike.presentation.dto.response;

import java.time.Instant;
import java.util.UUID;

public record BikeHistoryResponse(
    UUID id,
    UUID bikeId,
    Instant occurredAt,
    UUID performedBy,
    String type,
    String payload
) { }
