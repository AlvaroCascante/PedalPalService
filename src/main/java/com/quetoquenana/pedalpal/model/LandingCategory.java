package com.quetoquenana.pedalpal.model;

public enum LandingCategory {
    EVENT,
    NEWS,
    ARTICLE,
    PROMO,
    OTHER;

    /**
     * Parse a string to a LandingCategory enum.
     * Accepts case-insensitive values and trims whitespace. Returns OTHER when the input is null or invalid.
     */
    public static LandingCategory fromString(String value) {
        if (value == null) return OTHER;
        try {
            String v = value.trim().toUpperCase().replace(' ', '_');
            return LandingCategory.valueOf(v);
        } catch (IllegalArgumentException ex) {
            return OTHER;
        }
    }
}
