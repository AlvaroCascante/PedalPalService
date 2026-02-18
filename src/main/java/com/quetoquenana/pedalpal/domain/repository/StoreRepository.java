package com.quetoquenana.pedalpal.domain.repository;

import com.quetoquenana.pedalpal.domain.model.Store;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository {

    Optional<Store> getById(UUID storeId);

    Store save(Store store);

    Store update(UUID storeId, Store store);

    void deleteById(UUID storeId);
}

