package com.quetoquenana.pedalpal.infrastructure.persistence.store.repository;

import com.quetoquenana.pedalpal.infrastructure.persistence.store.entity.StoreEntity;
import com.quetoquenana.pedalpal.infrastructure.persistence.mapper.StoreEntityMapper;
import com.quetoquenana.pedalpal.store.domain.model.Store;
import com.quetoquenana.pedalpal.store.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {

    private final StoreJpaRepository repository;
    private final StoreEntityMapper mapper;

    @Override
    public Optional<Store> getById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<Store> getAll() {
        return repository.findAll().stream().map(mapper::toModel).toList();
    }

    @Override
    public Store save(Store store) {
        // Map the Bike domain model to a BikeEntity
        StoreEntity entity = mapper.toEntity(store);
        return mapper.toModel(repository.save(entity));
    }
}

