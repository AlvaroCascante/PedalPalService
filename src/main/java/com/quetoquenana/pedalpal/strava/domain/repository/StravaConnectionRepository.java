package com.quetoquenana.pedalpal.strava.domain.repository;

import com.quetoquenana.pedalpal.strava.domain.model.StravaConnection;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for Strava connection persistence.
 */
public interface StravaConnectionRepository {

    /**
     * Finds a Strava connection by user id.
     */
    Optional<StravaConnection> findByUserId(UUID userId);

    /**
     * Finds a Strava connection by Strava athlete id.
     */
    Optional<StravaConnection> findByStravaAthleteId(Long athleteId);

    /**
     * Saves a Strava connection.
     */
    StravaConnection save(StravaConnection connection);
}
