package com.quetoquenana.pedalpal.model;

/**
 * Status of a bike.
 */
public enum BikeStatus {
    ACTIVE,
    INACTIVE,
    STOLEN,
    MAINTENANCE,
    SOLD;

    /**
     * Parse a string into a BikeStatus. Returns ACTIVE if input is null or invalid.
     */
    public static BikeStatus fromString(String value) {
        if (value == null) return ACTIVE;
        try {
            String v = value.trim().toUpperCase().replace(' ', '_');
            return BikeStatus.valueOf(v);
        } catch (IllegalArgumentException ex) {
            return ACTIVE;
        }
    }
}
