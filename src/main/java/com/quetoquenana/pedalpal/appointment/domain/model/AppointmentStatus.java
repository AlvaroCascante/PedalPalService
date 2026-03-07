package com.quetoquenana.pedalpal.appointment.domain.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Locale;

@Getter
public enum AppointmentStatus {
    REQUESTED("appointment.status.requested"),
    DEPOSIT_PAID("appointment.status.deposit_paid"),
    CONFIRMED("appointment.status.confirmed"),
    BIKE_RECEIVED("appointment.status.bike_received"),
    IN_PROGRESS("appointment.status.in_progress"),
    COMPLETED("appointment.status.completed"),
    BIKE_PICKED_UP("appointment.status.bike_returned"),
    CANCELED("appointment.status.canceled"),
    REJECTED("appointment.status.rejected"),
    NO_SHOW("appointment.status.no_show"),
    UNKNOWN("appointment.status.unknown");

    private final String key;

    AppointmentStatus(String key) {
        this.key = key;
    }

    public static AppointmentStatus from(String value) {
        if (value == null || value.isBlank()) {
            return UNKNOWN;
        }

        String normalized = value.trim().toUpperCase(Locale.ROOT);

        return Arrays.stream(values())
                .filter(s -> s.name().equals(normalized) || s.key.equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
