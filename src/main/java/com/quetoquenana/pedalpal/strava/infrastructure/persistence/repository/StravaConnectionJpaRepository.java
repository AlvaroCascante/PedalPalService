package com.quetoquenana.pedalpal.strava.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.strava.infrastructure.persistence.entity.StravaConnectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * JPA repository for Strava connections.
 */
public interface StravaConnectionJpaRepository extends JpaRepository<StravaConnectionEntity, UUID> {

    Optional<StravaConnectionEntity> findByUserId(UUID userId);

    Optional<StravaConnectionEntity> findByStravaAthleteId(Long athleteId);
}
