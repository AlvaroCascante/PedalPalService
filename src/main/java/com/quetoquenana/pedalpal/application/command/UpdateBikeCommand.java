package com.quetoquenana.pedalpal.application.command;

import java.util.UUID;

/**
 * Application command for PATCH bike updates.
 * ownerId is intentionally not present (cannot be updated).
 * Nullable fields represent "not provided".
 */
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

