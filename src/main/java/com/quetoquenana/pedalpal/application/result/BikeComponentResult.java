package com.quetoquenana.pedalpal.application.result;

import lombok.Builder;

import java.util.UUID;

@Builder
public record BikeComponentResult(
        UUID id,
        String type,
        String name,
        String brand,
        String model,
        String notes,
        int odometerKm,
        int usageTimeMinutes
) { }
