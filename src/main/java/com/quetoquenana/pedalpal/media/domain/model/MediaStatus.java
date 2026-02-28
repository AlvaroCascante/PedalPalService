package com.quetoquenana.pedalpal.media.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum MediaStatus {
        PENDING("media.status.pending"),
        ACTIVE("media.status.active"),
        INACTIVE("media.status.inactive"),
        FAILED("media.status.failed"),
        UNKNOWN("media.status.unknown");

    private final String key;

    MediaStatus(String key) {
        this.key = key;
    }

    public static MediaStatus from(String value) {
        String normalized = value.toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized) || s.key.equals(normalized))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
