package com.quetoquenana.pedalpal.strava.application.result;

import com.quetoquenana.pedalpal.strava.domain.model.StravaConnectionStatus;

/**
 * Result payload for the Strava connection status query.
 */
public record StravaConnectionStatusResult(
        boolean connected,
        StravaConnectionStatus status,
        Long athleteId,
        String scope
) {
}
