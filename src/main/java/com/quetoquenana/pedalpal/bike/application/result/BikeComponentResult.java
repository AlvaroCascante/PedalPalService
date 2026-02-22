package com.quetoquenana.pedalpal.bike.application.result;

import com.quetoquenana.pedalpal.bike.domain.model.BikeComponentStatus;
import com.quetoquenana.pedalpal.common.domain.model.SystemCode;
import lombok.Builder;

import java.util.UUID;

@Builder
public record BikeComponentResult(
        UUID id,
        SystemCode type,
        String name,
        BikeComponentStatus status,
        String brand,
        String model,
        String notes,
        int odometerKm,
        int usageTimeMinutes
) { }
