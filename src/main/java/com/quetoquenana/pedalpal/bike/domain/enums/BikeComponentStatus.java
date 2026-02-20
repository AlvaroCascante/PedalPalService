package com.quetoquenana.pedalpal.bike.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum BikeComponentStatus {
        ACTIVE("bike.component.status.active"),
        INACTIVE("bike.component.status.inactive"),
        REPLACED("bike.component.status.replaced"),
        DELETED("bike.component.status.deleted"),
        UNKNOWN("bike.component.status.unknown");

    private final String key;

    BikeComponentStatus(String key) {
        this.key = key;
    }

    public static BikeComponentStatus from(String value) {
        String normalized = value.toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized) || s.key.equals(normalized))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
