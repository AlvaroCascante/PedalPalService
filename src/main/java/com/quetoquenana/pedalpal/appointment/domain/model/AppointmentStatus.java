package com.quetoquenana.pedalpal.appointment.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum AppointmentStatus {
    REQUESTED("appointment.status.requested"),
    CONFIRMED("appointment.status.confirmed"),
    IN_PROGRESS("appointment.status.in_progress"),
    COMPLETED("appointment.status.completed"),
    CANCELLED("appointment.status.cancelled"),
    REJECTED("appointment.status.rejected"),
    NO_SHOW("appointment.status.no_show"),
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
