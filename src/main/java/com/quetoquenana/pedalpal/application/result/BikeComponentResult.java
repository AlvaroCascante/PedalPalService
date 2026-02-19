package com.quetoquenana.pedalpal.application.result;

import com.quetoquenana.pedalpal.domain.model.SystemCode;
import lombok.Builder;

import java.util.UUID;

@Builder
public record BikeComponentResult(
        UUID id,
        SystemCode type,
        String name,
        String status,
        String brand,
        String model,
        String notes,
        int odometerKm,
        int usageTimeMinutes
) { }
