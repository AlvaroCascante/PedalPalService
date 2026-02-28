package com.quetoquenana.pedalpal.bike.application.command;

import java.util.UUID;

public record UpdateBikeComponentCommand(
        UUID bikeId,
        UUID componentId,
        UUID authenticatedUserId,
        String type,
        String name,
        String brand,
        String model,
        String notes,
        Integer odometerKm,
        Integer usageTimeMinutes
){ }
