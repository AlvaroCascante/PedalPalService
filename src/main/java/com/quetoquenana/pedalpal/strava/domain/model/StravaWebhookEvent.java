package com.quetoquenana.pedalpal.strava.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

/**
 * Domain model for Strava webhook events.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StravaWebhookEvent {
    private String objectType;
    private String aspectType;
    private Long objectId;
    private Long ownerId;
    private Map<String, Object> updates;
    private Instant eventTime;

    /**
     * @return true when the event references an activity object.
     */
    public boolean isActivityEvent() {
        return "activity".equalsIgnoreCase(objectType);
    }
}
