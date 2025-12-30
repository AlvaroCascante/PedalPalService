package com.quetoquenana.pedalpal.model;

public enum LandingStatus {
    ACTIVE,
    INACTIVE,
    DRAFT;

    /**
     * Parse a string to a LandingStatus enum.
     * Accepts case-insensitive values and trims whitespace. Returns ACTIVE when the input is null or invalid.
     */
    public static LandingStatus fromString(String value) {
        if (value == null) return ACTIVE;
        try {
            String v = value.trim().toUpperCase().replace(' ', '_');
            return LandingStatus.valueOf(v);
        } catch (IllegalArgumentException ex) {
            return ACTIVE;
        }
    }
}
