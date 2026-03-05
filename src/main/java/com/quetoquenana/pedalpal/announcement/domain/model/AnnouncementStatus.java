package com.quetoquenana.pedalpal.announcement.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum AnnouncementStatus {
    DRAFT("announcement.status.draft"),
    ACTIVE("announcement.status.active"),
    INACTIVE("announcement.status.inactive"),
    UNKNOWN("announcement.status.unknown");

    private final String key;

    AnnouncementStatus(String key) {
        this.key = key;
    }

    public static AnnouncementStatus from(String value) {
        String normalized = value.toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized) || s.key.equals(normalized))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
