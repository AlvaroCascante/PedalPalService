package com.quetoquenana.pedalpal.strava.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.strava.infrastructure.persistence.entity.StravaActivitySyncEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for Strava activity sync records.
 */
public interface StravaActivitySyncJpaRepository extends JpaRepository<StravaActivitySyncEntity, UUID> {

    Optional<StravaActivitySyncEntity> findByStravaActivityId(Long activityId);
}
