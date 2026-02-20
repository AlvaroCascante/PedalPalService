package com.quetoquenana.pedalpal.bike.application.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UpdateBikeCommand(
        UUID bikeId,
        UUID authenticatedUserId,
        String name,
        String type,
        String brand,
        String model,
        Integer year,
        String serialNumber,
        String notes,
        Integer odometerKm,
        Integer usageTimeMinutes,
        Boolean isPublic,
        Boolean isExternalSync
) { }

