package com.quetoquenana.pedalpal.bike.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum BikeType {
    BMX("bike.type.bmx"),
    E_BIKE("bike.type.ebike"),
    GRAVEL("bike.type.gravel"),
    HYBRID("bike.type.hybrid"),
    MTB("bike.type.mtb"),
    OTHER("bike.type.other"),
    ROAD("bike.type.road");

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
