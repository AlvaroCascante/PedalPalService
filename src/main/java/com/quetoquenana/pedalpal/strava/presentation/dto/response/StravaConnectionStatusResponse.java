package com.quetoquenana.pedalpal.strava.presentation.dto.response;

import com.quetoquenana.pedalpal.strava.domain.model.StravaConnectionStatus;

/**
 * Response payload for Strava connection status.
 */
public record StravaConnectionStatusResponse(
        boolean connected,
        StravaConnectionStatus status,
        Long athleteId,
        String scope
) {
}
