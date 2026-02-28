package com.quetoquenana.pedalpal.media.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum MediaType {
        IMAGE("media.type.image"),
        VIDEO("media.type.video"),
        DOCUMENT("media.type.document"),
        PDF("media.type.pdf"),
        OTHER("media.type.other");

    private final String key;

    MediaType(String key) {
        this.key = key;
    }

    public static MediaType from(String value) {
        String normalized = value.toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized) || s.key.equals(normalized))
                .findFirst()
                .orElse(OTHER);
    }
}
