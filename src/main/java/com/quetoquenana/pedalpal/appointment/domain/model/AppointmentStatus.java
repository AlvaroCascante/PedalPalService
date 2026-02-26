package com.quetoquenana.pedalpal.appointment.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum AppointmentStatus {
    REQUESTED("appointment.status.requested"),
    CONFIRMED("appointment.status.confirmed"),
    CANCELLED("appointment.status.cancelled"),
    REJECTED("appointment.status.rejected"),
    UNKNOWN("appointment.status.unknown");

    private final String key;

    AppointmentStatus(String key) {
        this.key = key;
    }

    public static AppointmentStatus from(String value) {
        String normalized = value.toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized) || s.key.equals(normalized))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
