package com.quetoquenana.pedalpal.strava.domain.repository;

import com.quetoquenana.pedalpal.strava.domain.model.StravaActivitySync;

import java.util.Optional;

/**
 * Repository port for Strava activity sync records.
 */
public interface StravaActivitySyncRepository {

    /**
     * Finds a sync record by Strava activity id.
     */
    Optional<StravaActivitySync> findByStravaActivityId(Long activityId);

    /**
     * Saves a Strava activity sync record.
     */
    StravaActivitySync save(StravaActivitySync activitySync);
}
