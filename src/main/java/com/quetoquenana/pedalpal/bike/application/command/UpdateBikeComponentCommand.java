package com.quetoquenana.pedalpal.bike.application.command;

import java.util.UUID;

public record UpdateBikeComponentCommand(
        UUID bikeId,
        UUID componentId,
        String type,
        String name,
        String brand,
        String model,
        String notes,
        Integer odometerKm,
        Integer usageTimeMinutes
){ }
