package com.quetoquenana.pedalpal.store.domain.repository;

import com.quetoquenana.pedalpal.store.domain.model.StoreLocation;

import java.util.Optional;
import java.util.UUID;

public interface StoreLocationRepository {

    Optional<StoreLocation> getById(UUID id);
}