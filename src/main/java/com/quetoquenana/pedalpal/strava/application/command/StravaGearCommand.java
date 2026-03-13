package com.quetoquenana.pedalpal.strava.application.command;

public record StravaGearCommand (
        String id,
        boolean primary,
        String name,
        String nickname,
        int resourceState,
        boolean retired,
        double distance,
        Double convertedDistance,
        String brandName,
        String modelName,
        Integer frameType,
        String description
) {}