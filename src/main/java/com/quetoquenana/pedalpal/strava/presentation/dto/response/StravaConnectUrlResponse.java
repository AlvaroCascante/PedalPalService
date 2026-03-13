package com.quetoquenana.pedalpal.strava.presentation.dto.response;

/**
 * Response payload for Strava connect URL.
 */
public record StravaConnectUrlResponse(
        String url,
        String state
) {
}
