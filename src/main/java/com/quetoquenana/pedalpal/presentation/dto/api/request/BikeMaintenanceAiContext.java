package com.quetoquenana.pedalpal.presentation.dto.api.request;

import java.util.List;

public record BikeMaintenanceAiContext(
        String bikeType,
        String brand,
        String model,
        Integer year,
        List<ComponentAiContext> components,
        String notes
) {}