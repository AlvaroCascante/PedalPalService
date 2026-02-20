package com.quetoquenana.pedalpal.bike.domain.repository;

import com.quetoquenana.pedalpal.bike.domain.model.StoreLocation;

import java.util.Optional;
import java.util.UUID;

public interface StoreLocationRepository {

    Optional<StoreLocation> getById(UUID storeLocationId);

    StoreLocation save(StoreLocation storeLocation);

    StoreLocation update(UUID storeLocationId, StoreLocation storeLocation);

    void deleteById(UUID storeLocationId);
}

