package com.quetoquenana.pedalpal.store.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.store.domain.model.StoreLocation;
import com.quetoquenana.pedalpal.store.domain.repository.StoreLocationRepository;
import com.quetoquenana.pedalpal.store.mapper.StoreEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StoreLocationRepositoryImpl implements StoreLocationRepository {

    private final StoreLocationJpaRepository repository;
    private final StoreEntityMapper mapper;

    @Override
    public Optional<StoreLocation> getById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }
}

