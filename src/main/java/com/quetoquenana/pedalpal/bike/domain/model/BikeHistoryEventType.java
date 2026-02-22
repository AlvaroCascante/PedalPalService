package com.quetoquenana.pedalpal.bike.domain.model;

import java.util.Arrays;
import java.util.Locale;

public enum BikeHistoryEventType {
    CREATED("bike.history.created"),
    UPDATED("bike.history.updated"),
    STATUS_CHANGED("bike.history.status.changed"),
    COMPONENT_ADDED("bike.history.component.added"),
    COMPONENT_UPDATED("bike.history.component.updated"),
    COMPONENT_REPLACED("bike.history.component.replaced"),
    COMPONENT_STATUS_CHANGED("bike.history.component.status.changed"),
    UNKNOWN("bike.history.unknown");

    private final String key;

    BikeHistoryEventType(String key) {
        this.key = key;
    }

    public static BikeHistoryEventType from(String value) {
        String normalized = value.toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized) || s.key.equals(normalized))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
