package com.quetoquenana.pedalpal.strava.application.result;

import com.quetoquenana.pedalpal.strava.domain.model.StravaSyncStatus;

/**
 * Result payload for Strava sync processing.
 */
public record StravaSyncResult(
        StravaSyncStatus status,
        String message,
        Long activityId
) {
}
