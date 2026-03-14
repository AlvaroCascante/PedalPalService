package com.quetoquenana.pedalpal.strava.domain.model;

import java.util.Arrays;
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
    public static StravaSportType from(String value) {
        String normalized = value.toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized))
                .findFirst()
                .orElse(OTHER);
    }
}
