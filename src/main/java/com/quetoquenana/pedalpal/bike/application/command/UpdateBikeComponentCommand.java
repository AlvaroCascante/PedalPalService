package com.quetoquenana.pedalpal.bike.application.command;

import lombok.Builder;

import java.util.UUID;

@Builder
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
