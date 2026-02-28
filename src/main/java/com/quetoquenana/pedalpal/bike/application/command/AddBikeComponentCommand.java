package com.quetoquenana.pedalpal.bike.application.command;

import java.util.UUID;

public record AddBikeComponentCommand(
        UUID componentId,
        UUID bikeId,
        UUID authenticatedUserId,
        String type,
        String name,
        String brand,
        String model,
        String notes,
        int odometerKm,
        int usageTimeMinutes
){ }
