package com.quetoquenana.pedalpal.model;

/**
 * Type of the bike.
 */
public enum BikeType {
    ROAD,
    MOUNTAIN,
    HYBRID,
    ELECTRIC,
    BMX,
    FOLDING,
    OTHER;

    /**
     * Parse a string into a BikeType. Returns OTHER if input is null or invalid.
     */
    public static BikeType fromString(String value) {
        if (value == null) return OTHER;
        try {
            String v = value.trim().toUpperCase().replace(' ', '_');
            return BikeType.valueOf(v);
        } catch (IllegalArgumentException ex) {
            return OTHER;
        }
    }
}
