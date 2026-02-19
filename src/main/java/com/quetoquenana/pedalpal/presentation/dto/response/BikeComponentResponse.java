package com.quetoquenana.pedalpal.presentation.dto.response;

import java.util.UUID;

public record BikeComponentResponse(
        UUID id,
        String type,
        String name,
        String status,
        String brand,
        String model,
        String notes,
        int odometerKm,
        int usageTimeMinutes
) { }

