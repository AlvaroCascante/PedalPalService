package com.quetoquenana.pedalpal.application.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateBikeResult(
        UUID id,
        UUID ownerId,
        String name,
        String type,
        boolean isPublic,
        boolean isExternalSync,
        String brand,
        String model,
        Integer year,
        String serialNumber,
        String notes,
        Integer odometerKm,
        Integer usageTimeMinutes
){ }
