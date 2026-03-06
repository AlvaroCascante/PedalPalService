package com.quetoquenana.pedalpal.bike.application.command;

public record CreateBikeCommand(
        String name,
        String type,
        String brand,
        String model,
        Integer year,
        String serialNumber,
        String notes,
        Integer odometerKm,
        Integer usageTimeMinutes,
        boolean isPublic,
        boolean isExternalSync
){ }
