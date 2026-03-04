package com.quetoquenana.pedalpal.common.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum GeneralStatus {
        ACTIVE("general.status.active"),
        INACTIVE("general.status.inactive"),
        DELETED("general.status.deleted"),
        UNKNOWN("general.status.unknown");

    private final String key;

    GeneralStatus(String key) {
        this.key = key;
    }

    public static GeneralStatus from(String value) {
        String normalized = value.toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized) || s.key.equals(normalized))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
