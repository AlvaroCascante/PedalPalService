package com.quetoquenana.pedalpal.strava.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Records Strava activity sync deltas to keep bike usage idempotent.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StravaActivitySync {
    private UUID id;
    private UUID userId;
    private UUID bikeId;
    private Long stravaActivityId;
    private Long stravaAthleteId;
    private Integer distanceKmDelta;
    private Integer movingTimeMinutesDelta;
    private Long originalDistanceMeters;
    private Long originalMovingTimeSeconds;
    private StravaSportType sportType;
    private StravaSyncStatus status;
    private Instant syncedAt;
}
