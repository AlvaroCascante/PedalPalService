package com.quetoquenana.pedalpal.strava.infrastructure.persistence.adapter;

import com.quetoquenana.pedalpal.strava.domain.model.StravaActivitySync;
import com.quetoquenana.pedalpal.strava.domain.repository.StravaActivitySyncRepository;
import com.quetoquenana.pedalpal.strava.infrastructure.persistence.mapper.StravaEntityMapper;
import com.quetoquenana.pedalpal.strava.infrastructure.persistence.repository.StravaActivitySyncJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Persistence adapter for Strava activity sync records.
 */
@Repository
@RequiredArgsConstructor
public class StravaActivitySyncPersistenceAdapter implements StravaActivitySyncRepository {

    private final StravaActivitySyncJpaRepository repository;

    @Override
    public Optional<StravaActivitySync> findByStravaActivityId(Long activityId) {
        return repository.findByStravaActivityId(activityId).map(StravaEntityMapper::toModel);
    }

    @Override
    public StravaActivitySync save(StravaActivitySync activitySync) {
        return StravaEntityMapper.toModel(repository.save(StravaEntityMapper.toEntity(activitySync)));
    }
}
