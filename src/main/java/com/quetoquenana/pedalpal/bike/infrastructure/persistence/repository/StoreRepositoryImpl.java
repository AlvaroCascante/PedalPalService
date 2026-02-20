package com.quetoquenana.pedalpal.bike.infrastructure.persistence.repository;

import com.quetoquenana.pedalpal.bike.domain.model.Store;
import com.quetoquenana.pedalpal.bike.domain.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepository {

    private final StoreJpaRepository repository;

    @Override
    public Optional<Store> getById(UUID storeId) {
        return Optional.empty();
    }

    @Override
    public Store save(Store store) {
        return null;
    }

    @Override
    public Store update(UUID storeId, Store store) {
        return null;
    }

    @Override
    public void deleteById(UUID storeId) {

    }
}

