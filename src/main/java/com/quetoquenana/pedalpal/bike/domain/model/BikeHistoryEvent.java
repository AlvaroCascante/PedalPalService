package com.quetoquenana.pedalpal.bike.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BikeHistoryEvent(
        UUID bikeId,
        UUID performedBy,
        UUID referenceId,
        BikeHistoryEventType bikeHistoryEventType,
        List<BikeChangeItem> changes,
        LocalDateTime occurredAt
) {}