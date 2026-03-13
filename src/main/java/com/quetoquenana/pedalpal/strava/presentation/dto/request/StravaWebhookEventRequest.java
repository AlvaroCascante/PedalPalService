package com.quetoquenana.pedalpal.strava.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Request payload for Strava webhook events.
 */
@Getter
@Setter
@NoArgsConstructor
public class StravaWebhookEventRequest {

    @JsonProperty("object_type")
    private String objectType;

    @JsonProperty("aspect_type")
    private String aspectType;

    @JsonProperty("object_id")
    private Long objectId;

    @JsonProperty("owner_id")
    private Long ownerId;

    @JsonProperty("updates")
    private Map<String, Object> updates;

    @JsonProperty("event_time")
    private Long eventTime;
}
