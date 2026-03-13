package com.quetoquenana.pedalpal.strava.application.command;

/**
 * Command for handling Strava OAuth callback responses.
 */
public record HandleStravaOAuthCallbackCommand(
        String code,
        String scope,
        String state
) {
}
