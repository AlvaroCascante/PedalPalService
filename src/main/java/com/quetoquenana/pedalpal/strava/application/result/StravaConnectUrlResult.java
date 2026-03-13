package com.quetoquenana.pedalpal.strava.application.result;

/**
 * Result payload for the Strava connect URL query.
 */
public record StravaConnectUrlResult(
        String url,
        String state
) {
}
