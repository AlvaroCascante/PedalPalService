package com.quetoquenana.pedalpal.model;

/**
 * Appointment lifecycle statuses.
 */
public enum AppointmentStatus {
    SCHEDULED,
    CONFIRMED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    NO_SHOW;

    /**
     * Parse a string into an AppointmentStatus. Returns SCHEDULED if input is null or invalid.
     */
    public static AppointmentStatus fromString(String value) {
        if (value == null) return SCHEDULED;
        try {
            String v = value.trim().toUpperCase().replace(' ', '_');
            return AppointmentStatus.valueOf(v);
        } catch (IllegalArgumentException ex) {
            return SCHEDULED;
        }
    }
}
