package com.quetoquenana.pedalpal.application.command;

import lombok.Builder;

import java.util.UUID;

@Builder
public record AddBikeComponentCommand(
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
