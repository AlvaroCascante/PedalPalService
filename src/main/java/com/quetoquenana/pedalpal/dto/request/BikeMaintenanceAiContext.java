package com.quetoquenana.pedalpal.dto.request;

import java.util.List;

public record BikeMaintenanceAiContext(
        String bikeType,
        String brand,
        String model,
        Integer year,
        List<ComponentAiContext> components,
        String notes
) {}