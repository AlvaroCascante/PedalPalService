package com.quetoquenana.pedalpal.presentation.dto.api.response;

import java.util.UUID;

public record BikeResponse(
        UUID id,
        String name,
        String type,
        String status,
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
