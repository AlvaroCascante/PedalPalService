package com.quetoquenana.pedalpal.application.result;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record BikeResult(
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
        int usageTimeMinutes,
        Set<BikeComponentResult> components
) { }
