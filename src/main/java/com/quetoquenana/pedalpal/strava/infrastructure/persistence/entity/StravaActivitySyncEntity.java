package com.quetoquenana.pedalpal.strava.infrastructure.persistence.entity;

import com.quetoquenana.pedalpal.auditing.domain.model.AuditableEntity;
import com.quetoquenana.pedalpal.strava.domain.model.StravaSportType;
import com.quetoquenana.pedalpal.strava.domain.model.StravaSyncStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Persistence entity for Strava activity sync records.
 */
@Entity
@Table(name = "strava_activity_sync")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StravaActivitySyncEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "bike_id", nullable = false)
    private UUID bikeId;

    @Column(name = "strava_activity_id", nullable = false, unique = true)
    private Long stravaActivityId;

    @Column(name = "strava_athlete_id", nullable = false)
    private Long stravaAthleteId;

    @Column(name = "distance_km_delta", nullable = false)
    private Integer distanceKmDelta;

    @Column(name = "moving_time_minutes_delta", nullable = false)
    private Integer movingTimeMinutesDelta;

    @Column(name = "original_distance_meters")
    private Long originalDistanceMeters;

    @Column(name = "original_moving_time_seconds")
    private Long originalMovingTimeSeconds;

    @Enumerated(EnumType.STRING)
    @Column(name = "sport_type", length = 50)
    private StravaSportType sportType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private StravaSyncStatus status;

    @Column(name = "synced_at", nullable = false)
    private Instant syncedAt;
}
