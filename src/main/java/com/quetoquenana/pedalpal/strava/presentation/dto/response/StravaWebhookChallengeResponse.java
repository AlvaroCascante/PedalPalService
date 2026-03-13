package com.quetoquenana.pedalpal.strava.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response payload for Strava webhook verification.
 */
public record StravaWebhookChallengeResponse(
        @JsonProperty("hub.challenge") String challenge
) {
}
