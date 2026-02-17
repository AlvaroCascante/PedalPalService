package com.quetoquenana.pedalpal.presentation.dto.api.response;

import java.util.UUID;

public record CreateBikeResponse(
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
