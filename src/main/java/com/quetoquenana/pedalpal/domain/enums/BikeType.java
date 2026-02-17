package com.quetoquenana.pedalpal.domain.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum BikeType {
    MTB("bike.type.mtb"),
    ROAD("bike.type.road"),
    HYBRID("bike.type.hybrid"),
    GRAVEL("bike.type.gravel"),
    BMX("bike.type.bmx"),
    E_BIKE("bike.type.ebike"),
    OTHER("bike.type.other");

    private final String key;

    BikeType(String key) {
        this.key = key;
    }

    public static BikeType from(String value) {
        String normalized = value.toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized) || s.key.equals(normalized))
                .findFirst()
                .orElse(OTHER);
    }
}
