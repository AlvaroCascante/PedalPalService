package com.quetoquenana.pedalpal.strava.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Normalized Strava token response payload.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StravaToken {
    private String accessToken;
    private String refreshToken;
    private Instant expiresAt;
    private Long athleteId;
    private String scope;
}
