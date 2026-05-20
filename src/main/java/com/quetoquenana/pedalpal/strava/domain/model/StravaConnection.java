package com.quetoquenana.pedalpal.strava.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.Auditable;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

/**
 * Domain model for a Strava OAuth connection tied to a user.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StravaConnection extends Auditable {
    private UUID id;
    private UUID userId;
    private Long stravaAthleteId;
    private String accessToken;
    private String refreshToken;
    private Instant tokenExpiresAt;
    private String scope;
    private StravaConnectionStatus status;
}
