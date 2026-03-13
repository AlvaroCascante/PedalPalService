package com.quetoquenana.pedalpal.strava.infrastructure.persistence.adapter;

import com.quetoquenana.pedalpal.strava.domain.model.StravaConnection;
import com.quetoquenana.pedalpal.strava.domain.repository.StravaConnectionRepository;
import com.quetoquenana.pedalpal.strava.infrastructure.persistence.mapper.StravaEntityMapper;
import com.quetoquenana.pedalpal.strava.infrastructure.persistence.repository.StravaConnectionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Persistence adapter for Strava connections.
 */
@Repository
@RequiredArgsConstructor
public class StravaConnectionPersistenceAdapter implements StravaConnectionRepository {

    private final StravaConnectionJpaRepository repository;

    @Override
    public Optional<StravaConnection> findByUserId(UUID userId) {
        return repository.findByUserId(userId).map(StravaEntityMapper::toModel);
    }

    @Override
    public Optional<StravaConnection> findByStravaAthleteId(Long athleteId) {
        return repository.findByStravaAthleteId(athleteId).map(StravaEntityMapper::toModel);
    }

    @Override
    public StravaConnection save(StravaConnection connection) {
        return StravaEntityMapper.toModel(repository.save(StravaEntityMapper.toEntity(connection)));
    }
}
