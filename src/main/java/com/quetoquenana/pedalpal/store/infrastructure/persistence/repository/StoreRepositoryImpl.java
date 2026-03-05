package com.quetoquenana.pedalpal.store.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.store.infrastructure.persistence.entity.StoreEntity;
import com.quetoquenana.pedalpal.store.infrastructure.persistence.mapper.StoreEntityMapper;
import com.quetoquenana.pedalpal.store.domain.model.Store;
import com.quetoquenana.pedalpal.store.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
class StoreRepositoryImpl implements StoreRepository {

    private final StoreJpaRepository repository;

    @Override
    public Optional<Store> getById(UUID id) {
        return repository.findById(id).map(StoreEntityMapper::toModel);
    }

    @Override
    public List<Store> getAll() {
        return repository.findAll().stream().map(StoreEntityMapper::toModel).toList();
    }

    @Override
    public Store save(Store store) {
        // Map the Bike domain model to a BikeEntity
        StoreEntity entity = StoreEntityMapper.toEntity(store);
        return StoreEntityMapper.toModel(repository.save(entity));
    }
}
