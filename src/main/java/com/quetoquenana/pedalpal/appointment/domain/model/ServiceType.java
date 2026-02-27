package com.quetoquenana.pedalpal.appointment.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum ServiceType {
    PRODUCT("service.type.product"),
    PACKAGE("service.type.package"),
    UNKNOWN("service.type.unknown");

    private final String key;

    ServiceType(String key) {
        this.key = key;
    }

    public static ServiceType from(String value) {
        String normalized = value.toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized) || s.key.equals(normalized))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
