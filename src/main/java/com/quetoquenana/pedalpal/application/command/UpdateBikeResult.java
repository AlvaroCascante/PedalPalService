package com.quetoquenana.pedalpal.application.command;

import java.util.UUID;

public record UpdateBikeResult(
        UUID id,
        String name,
        String type,
        boolean isPublic,
        boolean isExternalSync,
        String brand,
        String model,
        Integer year,
        String serialNumber,
        String notes,
        int odometerKm,
        int usageTimeMinutes
) { }
