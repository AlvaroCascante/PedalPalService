package com.quetoquenana.pedalpal.strava.domain.model;

import java.util.Locale;

/**
 * Sport types returned by Strava that are relevant for bike activity filtering.
 */
public enum StravaSportType {
    RIDE,
    VIRTUAL_RIDE,
    E_BIKE_RIDE,
    OTHER;

    /**
     * Maps Strava sport type strings to enum values.
     */
    public static StravaSportType fromStravaValue(String value) {
        if (value == null) {
            return OTHER;
        }
        String normalized = value.trim().toUpperCase(Locale.US);
        return switch (normalized) {
            case "RIDE" -> RIDE;
            case "VIRTUALRIDE" -> VIRTUAL_RIDE;
            case "EBIKERIDE" -> E_BIKE_RIDE;
            default -> OTHER;
        };
    }
}
