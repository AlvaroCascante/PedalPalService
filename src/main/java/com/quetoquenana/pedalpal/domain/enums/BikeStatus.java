package com.quetoquenana.pedalpal.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum BikeStatus {
        ACTIVE("bikeEntity.status.active"),
        INACTIVE("bikeEntity.status.inactive"),
        STOLEN("bikeEntity.status.stolen"),
        SOLD("bikeEntity.status.sold"),
        DELETED("bikeEntity.status.deleted"),
        UNKNOWN("bikeEntity.status.unknown");

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
