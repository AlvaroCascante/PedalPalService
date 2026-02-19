package com.quetoquenana.pedalpal.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum BikeStatus {
        ACTIVE("bike.status.active"),
        INACTIVE("bike.status.inactive"),
        STOLEN("bike.status.stolen"),
        SOLD("bike.status.sold"),
        DELETED("bike.status.deleted"),
        UNKNOWN("bike.status.unknown");

    private final String key;

    BikeStatus(String key) {
        this.key = key;
    }

    public static BikeStatus from(String value) {
        String normalized = value.toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized) || s.key.equals(normalized))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
