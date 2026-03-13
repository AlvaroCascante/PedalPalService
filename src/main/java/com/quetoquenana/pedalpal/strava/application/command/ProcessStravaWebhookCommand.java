package com.quetoquenana.pedalpal.strava.application.command;

import com.quetoquenana.pedalpal.strava.domain.model.StravaWebhookEvent;

/**
 * Command for processing a Strava webhook event.
 */
public record ProcessStravaWebhookCommand(
        StravaWebhookEvent event
) {
}
