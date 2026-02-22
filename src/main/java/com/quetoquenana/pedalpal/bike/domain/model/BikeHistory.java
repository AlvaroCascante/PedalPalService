package com.quetoquenana.pedalpal.bike.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class BikeHistory {
    private UUID id;
    private UUID bikeId;
    private UUID referenceId;
    private LocalDateTime occurredAt;
    private UUID performedBy;
    private BikeHistoryEventType type;
    private String payload;
}
