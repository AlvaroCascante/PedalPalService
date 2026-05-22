package com.quetoquenana.pedalpal.common.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum MediaReferenceType {
    ANNOUNCEMENT(true, false),
    APPOINTMENT_DEPOSIT(false, true),
    APPOINTMENT_PAYMENT(false, false),
    BIKE(false, false),
    BIKE_PROFILE(false, true),
    COMPONENT(true, false),
    PROFILE(true, true),
    SERVICE_ORDER(true, false),
    OTHER(true, false);

    private final boolean isPublic;
    private final boolean isUnique;

    MediaReferenceType(boolean isPublic,  boolean isUnique) {
        this.isPublic = isPublic;
        this.isUnique = isUnique;
    }

    public static MediaReferenceType from(String value) {
        String normalized = value.toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized))
                .findFirst()
                .orElse(OTHER);
    }
}
