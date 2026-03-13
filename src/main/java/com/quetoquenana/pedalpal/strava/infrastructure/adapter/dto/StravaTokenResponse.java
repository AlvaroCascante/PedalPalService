package com.quetoquenana.pedalpal.strava.infrastructure.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Strava OAuth token response payload.
 */
@Getter
@Setter
@NoArgsConstructor
public class StravaTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_at")
    private Long expiresAt;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("athlete")
    private Athlete athlete;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Athlete {
        @JsonProperty("id")
        private Long id;
    }
}
