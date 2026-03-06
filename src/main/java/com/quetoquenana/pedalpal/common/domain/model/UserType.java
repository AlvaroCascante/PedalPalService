package com.quetoquenana.pedalpal.common.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum UserType {
        CUSTOMER,
        ADMIN,
        TECHNICIAN,
        UNKNOWN;

    public static UserType from(String value) {
        String normalized = value.toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
