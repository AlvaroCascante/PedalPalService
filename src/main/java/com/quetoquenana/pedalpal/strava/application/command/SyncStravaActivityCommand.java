package com.quetoquenana.pedalpal.strava.application.command;

import com.quetoquenana.pedalpal.strava.domain.model.StravaWebhookEvent;

/**
 * Command to sync a Strava activity referenced by a webhook event.
 */
public record SyncStravaActivityCommand(
        StravaWebhookEvent event
) {
}
