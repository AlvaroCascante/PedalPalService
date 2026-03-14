package com.quetoquenana.pedalpal.bike.domain.model;

/**
 * Supported external sync providers for bikes.
 */
public enum ExternalSyncProvider {
    STRAVA,
    UNKNOWN;

    public static ExternalSyncProvider from(String provider) {
        if (provider == null) {
            return UNKNOWN;
        }
        try {
            return ExternalSyncProvider.valueOf(provider.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}

