package com.quetoquenana.pedalpal.media.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum MediaReferenceType {
        ANNOUNCEMENT(true),
        APPOINTMENT(false),
        BIKE(false),
        COMPONENT(true),
        PROFILE(true),
        SERVICE_ORDER(true),
        OTHER(true);

    private final boolean isPublic;

    MediaReferenceType(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public static MediaReferenceType from(String value) {
        String normalized = value.toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized))
                .findFirst()
                .orElse(OTHER);
    }
}
