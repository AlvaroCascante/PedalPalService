package com.quetoquenana.pedalpal.strava.infrastructure.persistence.entity;

import com.quetoquenana.pedalpal.auditing.domain.model.AuditableEntity;
import com.quetoquenana.pedalpal.strava.domain.model.StravaConnectionStatus;
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
 * Persistence entity for Strava OAuth connections.
 */
@Entity
@Table(name = "strava_connections")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StravaConnectionEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "strava_athlete_id", nullable = false)
    private Long stravaAthleteId;

    @Column(name = "access_token", nullable = false, length = 2048)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, length = 2048)
    private String refreshToken;

    @Column(name = "token_expires_at", nullable = false)
    private Instant tokenExpiresAt;

    @Column(name = "scope", length = 512)
    private String scope;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private StravaConnectionStatus status;
}
