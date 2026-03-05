package com.quetoquenana.pedalpal.store.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.store.domain.model.StoreLocation;
import com.quetoquenana.pedalpal.store.domain.repository.StoreLocationRepository;
import com.quetoquenana.pedalpal.store.infrastructure.persistence.mapper.StoreEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class StoreLocationRepositoryImpl implements StoreLocationRepository {

    private final StoreLocationJpaRepository repository;

    @Override
    public Optional<StoreLocation> getById(UUID id) {
        return repository.findById(id).map(StoreEntityMapper::toModel);
    }
}
