package com.quetoquenana.pedalpal.strava.application.service;

import com.quetoquenana.pedalpal.strava.config.StravaProperties;
import com.quetoquenana.pedalpal.strava.domain.model.StravaConnection;
import com.quetoquenana.pedalpal.strava.domain.model.StravaToken;
import com.quetoquenana.pedalpal.strava.domain.port.StravaApiClient;
import com.quetoquenana.pedalpal.strava.domain.repository.StravaConnectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

/**
 * Application service for handling Strava token refresh logic.
 */
@Slf4j
@RequiredArgsConstructor
public class StravaTokenService {

    private final StravaApiClient stravaApiClient;
    private final StravaConnectionRepository connectionRepository;
    private final StravaProperties properties;

    /**
     * Ensures a valid access token exists and refreshes if needed.
     */
    public String getValidAccessToken(StravaConnection connection) {
        if (connection == null) {
            return null;
        }
        Instant now = Instant.now();
        Instant expiresAt = connection.getTokenExpiresAt();
        long skewSeconds = properties.getTokenRefreshSkewSeconds();

        if (expiresAt != null && expiresAt.isAfter(now.plusSeconds(skewSeconds))) {
            return connection.getAccessToken();
        }

        log.info("Refreshing Strava access token for user {}", connection.getUserId());
        StravaToken refreshed = stravaApiClient.refreshAccessToken(connection.getRefreshToken());
        connection.setAccessToken(refreshed.getAccessToken());
        connection.setRefreshToken(refreshed.getRefreshToken());
        connection.setTokenExpiresAt(refreshed.getExpiresAt());
        connection.setScope(refreshed.getScope());
        connectionRepository.save(connection);
        return refreshed.getAccessToken();
    }
}
