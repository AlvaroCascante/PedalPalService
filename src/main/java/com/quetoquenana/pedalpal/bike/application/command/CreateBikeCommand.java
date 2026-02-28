package com.quetoquenana.pedalpal.bike.application.command;

import java.util.UUID;

public record CreateBikeCommand(
        UUID ownerId,
        String name,
        String type,
        String brand,
        String model,
        Integer year,
        String serialNumber,
        String notes,
        Integer odometerKm,
        Integer usageTimeMinutes,
        boolean isPublic,
        boolean isExternalSync
){ }
